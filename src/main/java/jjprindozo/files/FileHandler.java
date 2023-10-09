package jjprindozo.files;

import java.io.File;

public class FileHandler {
  private static FileHandler instance;
  private File selectedFile;
  private FileChangeListener fileChangeListener;

  public void setFileChangeListener(FileChangeListener listener) {
    this.fileChangeListener = listener;
  }

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

    // listen to change
    if (fileChangeListener != null) {
      fileChangeListener.onFileChange(selectedFile != null ? selectedFile.getName() : "Untitled Text");
    }
  }
}
