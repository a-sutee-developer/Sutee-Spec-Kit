package com.github.speckit.services;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

/**
 * Service for locating/downloading Spec Kit templates
 */
public class GithubService {
    
    private static final String REPO_OWNER = "a-sutee-developer";
    private static final String REPO_NAME = "Sutee-Spec-Kit";
    private static final String EMBEDDED_DIR = "META-INF/spec-kit/templates/";
    private final OkHttpClient client;
    
    public GithubService() {
        this.client = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .build();
    }
    
    public Path downloadTemplate(String agent, String scriptType) throws IOException {
        String pattern = String.format("spec-kit-template-%s-%s", agent, scriptType);
        
        // 0. Try embedded resources inside the running JAR first
        Path embedded = findEmbeddedTemplate(pattern);
        if (embedded != null && Files.exists(embedded)) {
            return embedded;
        }
        
        // 1. Try local .genreleases directory first
        Path projectRoot = Paths.get("").toAbsolutePath().normalize();
        Path localDir = projectRoot.resolve(".genreleases");
        if (Files.isDirectory(localDir)) {
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(localDir, pattern + "-*.zip")) {
                for (Path candidate : stream) {
                    if (Files.isRegularFile(candidate)) {
                        return candidate.toAbsolutePath();
                    }
                }
            }
        }
        
        // 2. Fallback to GitHub latest release
        String apiUrl = String.format(
            "https://api.github.com/repos/%s/%s/releases/latest",
            REPO_OWNER, REPO_NAME
        );
        
        Request request = new Request.Builder()
            .url(apiUrl)
            .header("Accept", "application/vnd.github.v3+json")
            .build();
        
        JsonObject release;
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Failed to fetch release: HTTP " + response.code());
            }
            String body = response.body().string();
            release = JsonParser.parseString(body).getAsJsonObject();
        }
        
        // 3. Find matching asset
        JsonObject matchedAsset = null;
        
        JsonArray assets = release.getAsJsonArray("assets");
        for (JsonElement assetElement : assets) {
            JsonObject asset = assetElement.getAsJsonObject();
            String name = asset.get("name").getAsString();
            if (name.contains(pattern) && name.endsWith(".zip")) {
                matchedAsset = asset;
                break;
            }
        }
        
        if (matchedAsset == null) {
            throw new IOException("No matching template found for pattern: " + pattern);
        }
        
        // 4. Download zip file
        String downloadUrl = matchedAsset.get("browser_download_url").getAsString();
        String filename = matchedAsset.get("name").getAsString();
        
        Request downloadRequest = new Request.Builder()
            .url(downloadUrl)
            .build();
        
        Path tmpDir = Paths.get(System.getProperty("java.io.tmpdir"));
        Path zipPath = tmpDir.resolve(filename);
        
        try (Response response = client.newCall(downloadRequest).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Download failed: HTTP " + response.code());
            }
            Files.write(zipPath, response.body().bytes());
        }
        
        return zipPath;
    }
    
    private Path findEmbeddedTemplate(String pattern) throws IOException {
        ClassLoader cl = GithubService.class.getClassLoader();
        
        // List known template variants to search (since we can't list resources in Native)
        String[] agents = {"claude", "gemini", "copilot", "cursor-agent", "qwen", "opencode", 
                          "windsurf", "codex", "kilocode", "auggie", "roo", "codebuddy", "amp", "q"};
        String[] scripts = {"sh", "ps"};
        String[] versions = {"v0.0.103", "v0.0.102", "v0.0.101", "v0.0.100"};
        
        for (String version : versions) {
            for (String agent : agents) {
                for (String script : scripts) {
                    String resourceName = String.format("%sspec-kit-template-%s-%s-%s.zip", 
                                                       EMBEDDED_DIR, agent, script, version);
                    if (resourceName.contains(pattern)) {
                        InputStream is = cl.getResourceAsStream(resourceName);
                        if (is != null) {
                            try {
                                String filename = resourceName.substring(resourceName.lastIndexOf('/') + 1);
                                Path tmpDir = Paths.get(System.getProperty("java.io.tmpdir"));
                                Path zipPath = tmpDir.resolve(filename);
                                Files.copy(is, zipPath, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                                return zipPath;
                            } finally {
                                is.close();
                            }
                        }
                    }
                }
            }
        }
        
        return null;
    }
}
