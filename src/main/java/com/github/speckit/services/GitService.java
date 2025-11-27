package com.github.speckit.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Service for Git repository operations
 */
public class GitService {
    
    public boolean isGitRepo(Path path) {
        return Files.exists(path.resolve(".git"));
    }
    
    public void initRepo(Path path) throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder("git", "init");
        pb.directory(path.toFile());
        pb.redirectOutput(ProcessBuilder.Redirect.INHERIT);
        pb.redirectError(ProcessBuilder.Redirect.INHERIT);
        Process process = pb.start();
        int exitCode = process.waitFor();
        
        if (exitCode != 0) {
            throw new IOException("Git init failed with exit code: " + exitCode);
        }
    }
}
