package edu.ccrm.util;

import java.io.IOException;
import java.nio.file.*;

/**
 * Demonstrates recursion: compute total size of directory recursively.
 */
public class RecursionUtil {

    public static long totalSize(Path p) {
        if (!Files.exists(p)) return 0L;
        if (Files.isRegularFile(p)) {
            try { return Files.size(p); } catch (IOException e) { return 0L; }
        }
        try {
            long sum = 0L;
            try (var stream = Files.newDirectoryStream(p)) {
                for (Path child : stream) {
                    sum += totalSize(child);
                }
            }
            return sum;
        } catch (IOException ex) { return 0L; }
    }
}
