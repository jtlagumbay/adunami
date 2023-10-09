package jjprindozo.main;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import jjprindozo.common.GlobalVar;

public class CustomFileChooser extends JFileChooser {
  public CustomFileChooser() {
    FileNameExtensionFilter filter = new FileNameExtensionFilter("Text Files", GlobalVar.FILE_EXTENSION); // To change when we have our programming language already

    setFileFilter(filter);  

  }
}
