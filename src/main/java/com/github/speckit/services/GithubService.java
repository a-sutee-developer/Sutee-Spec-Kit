package com.github.speckit.services;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

/**
 * Service for interacting with GitHub API and downloading templates
 */
public class GithubService {
    
    private static final String REPO_OWNER = "github";
    private static final String REPO_NAME = "spec-kit";
    private final OkHttpClient client;
    
    public GithubService() {
        this.client = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .build();
    }
    
    public Path downloadTemplate(String agent, String scriptType) throws IOException {
        // 1. Get latest release information
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
        
        // 2. Find matching asset
        String pattern = String.format("spec-kit-template-%s-%s", agent, scriptType);
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
        
        // 3. Download zip file
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
}
