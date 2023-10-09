package jjprindozo.main;

import java.io.File;

public class FileHandler {
  private static FileHandler instance;
  private File selectedFile;

  private FileHandler() {
    // Private constructor to prevent instantiation from outside the class
  }
  
  public static synchronized FileHandler getInstance() {
    if (instance == null) {
      instance = new FileHandler();
    }
    return instance;
  }

  public File getSelectedFile() {
    if (selectedFile != null && selectedFile.exists())
      return selectedFile;
    else
      return null;
  }
  
  public void setSelectedFile(File newSelectedFile) {
    selectedFile = newSelectedFile;
    }
}
