package com.github.speckit.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.PosixFilePermission;
import java.util.HashSet;
import java.util.Set;

/**
 * Service for managing script permissions
 */
public class ScriptService {
    
    public void setExecutablePermissions(Path projectPath) throws IOException {
        // Only set permissions on Unix-like systems
        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            return;
        }
        
        Path scriptsPath = projectPath.resolve(".specify/scripts");
        
        if (!Files.exists(scriptsPath)) {
            return;
        }
        
        Files.walk(scriptsPath)
            .filter(p -> p.toString().endsWith(".sh"))
            .forEach(script -> {
                try {
                    Set<PosixFilePermission> perms = new HashSet<>(Files.getPosixFilePermissions(script));
                    perms.add(PosixFilePermission.OWNER_EXECUTE);
                    perms.add(PosixFilePermission.GROUP_EXECUTE);
                    perms.add(PosixFilePermission.OTHERS_EXECUTE);
                    Files.setPosixFilePermissions(script, perms);
                } catch (IOException e) {
                    System.err.println("Failed to set permissions for: " + script);
                }
            });
    }
}
