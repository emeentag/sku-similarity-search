package utils;

import java.io.File;

/**
 * FileReader
 */
public class FileReader {

  private String pathOfFile;

  public FileReader() {

  }

  public String findFilePath(String name, File file) {
    File[] list = file.listFiles();

    if (pathOfFile == null) {
      for (File currentFile : list) {
        if (currentFile.isDirectory()) {
          findFilePath(name, currentFile);
        } else if (name.equalsIgnoreCase(currentFile.getName())) {
          pathOfFile = currentFile.getPath();
          break;
        }
      }
    }

    return pathOfFile;
  }
}