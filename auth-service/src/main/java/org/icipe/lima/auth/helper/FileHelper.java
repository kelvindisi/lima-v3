package org.icipe.lima.auth.helper;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileHelper {
  public static boolean fileExists(Path path) {
    return Files.exists(path);
  }

  public static void createFoldersIfNotExists(Path path) {
    try {
      Files.createDirectories(path);
    } catch (IOException ex) {
      throw new RuntimeException(ex);
    }
  }

  public static void createFile(Path path) {
    try {
      if (!fileExists(path)) {
        createFoldersIfNotExists(path);
      }
      Files.createFile(path);
    } catch (IOException ex) {
      throw new RuntimeException(ex);
    }
  }

  public static void writeFile(Path path, byte[] content) {
    try (var out = new FileOutputStream(path.toString())) {
      out.write(content);
    } catch (IOException ex) {
      throw new RuntimeException(ex);
    }
  }

  public static byte[] readFile(Path path) {
    try {
      if (fileExists(path)) {
        return Files.readAllBytes(path);
      }
      return null;
    } catch (IOException ex) {
      throw new RuntimeException(ex);
    }
  }
}
