package edu.ccrm.config;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;

/**
 * Singleton AppConfig demonstrating the Singleton pattern.
 */
public final class AppConfig {
    private static AppConfig instance;
    private final Path dataFolder;
    private final Instant startedAt;
    private final int maxCreditsPerSemester;

    private AppConfig() {
        this.dataFolder = Paths.get(System.getProperty("user.home"), "ccrm-data");
        this.startedAt = Instant.now();
        this.maxCreditsPerSemester = 24; // business rule
    }

    public static synchronized AppConfig getInstance() {
        if (instance == null) {
            instance = new AppConfig();
        }
        return instance;
    }

    public Path getDataFolder() {
        return dataFolder;
    }

    public Instant getStartedAt() {
        return startedAt;
    }

    public int getMaxCreditsPerSemester() {
        return maxCreditsPerSemester;
    }

    @Override
    public String toString() {
        return "AppConfig[dataFolder=" + dataFolder + ", startedAt=" + startedAt + ", maxCredits=" + maxCreditsPerSemester + "]";
    }
}
