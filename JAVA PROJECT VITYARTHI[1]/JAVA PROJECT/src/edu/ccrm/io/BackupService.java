package edu.ccrm.io;

import edu.ccrm.config.AppConfig;
import edu.ccrm.util.RecursionUtil;

import java.io.IOException;
import java.nio.file.*;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

/**
 * Backup utilities using NIO.2. Demonstrates Path, Files.walk, copy, move, exists.
 */
public class BackupService {
    private final AppConfig cfg = AppConfig.getInstance();

    public Path createBackup(Path sourceFolder) throws IOException {
        String ts = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        Path backupRoot = cfg.getDataFolder().resolve("backup_" + ts);
        Files.createDirectories(backupRoot);
        if (!Files.exists(sourceFolder)) throw new IOException("source not exists: " + sourceFolder);
        try (var stream = Files.walk(sourceFolder)) {
            stream.forEach(p -> {
                try {
                    Path target = backupRoot.resolve(sourceFolder.relativize(p));
                    if (Files.isDirectory(p)) {
                        Files.createDirectories(target);
                    } else {
                        Files.copy(p, target, StandardCopyOption.REPLACE_EXISTING);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
        return backupRoot;
    }

    public long computeBackupSize(Path folder) {
        return RecursionUtil.totalSize(folder);
    }
}
