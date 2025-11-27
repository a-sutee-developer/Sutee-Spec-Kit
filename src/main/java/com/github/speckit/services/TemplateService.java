package com.github.speckit.services;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Service for extracting and managing template files
 */
public class TemplateService {
    
    public void extractTemplate(Path zipPath, Path targetPath, boolean mergeMode) throws IOException {
        if (!mergeMode) {
            Files.createDirectories(targetPath);
        }
        
        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(zipPath.toFile()))) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                Path destPath = targetPath.resolve(entry.getName());
                
                if (entry.isDirectory()) {
                    Files.createDirectories(destPath);
                } else {
                    Files.createDirectories(destPath.getParent());
                    Files.copy(zis, destPath, StandardCopyOption.REPLACE_EXISTING);
                }
                zis.closeEntry();
            }
        }
        
        // Clean up zip file
        Files.deleteIfExists(zipPath);
    }
}
