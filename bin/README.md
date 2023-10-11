# **CMSC 124 IDE**

In partial fulfillment of CMSC 124 A.Y. 2023-2024.

Group members:

> Princess Ethel Atillo

> Jade Zahyen Cataques

> Jhoanna Rica Lagumbay

> Zoe Molina

> Donna Mae Mozo

## Important Links

[Google Docs for Task Checklist](https://docs.google.com/document/d/1K4OFlEwd8bidJGa218W5pyPbHMxs76Of8L8putbWvQo/edit?usp=sharing)

[PDF IDE Project Requirements from UVEC](https://uvec.upcebu.edu.ph/pluginfile.php/72161/mod_resource/content/2/Integrated%20Development%20Environment.pdf)

[Canva for Images Needed](https://www.canva.com/design/DAFvaX6CoH8/EUn4byC6XGrWyc5wMzJERg/edit?utm_content=DAFvaX6CoH8&utm_campaign=designshare&utm_medium=link2&utm_source=sharebutton)

[Figma for IDE Design](https://www.figma.com/file/ibBGeQiizNjCklskpr56s9/Adunami-IDE?type=design&node-id=0:1&mode=design&t=HuysQX8UZngm80ns-1)

[FlatLaf for Look and Feal Documentation](https://www.formdev.com/flatlaf/)

## Directory Guide

**src** - contains all code for the IDE

**src \ assets \ images** - all images needed

**src \ assets \ fonts** - all custom fonts needed

**src \ main \ java \ jjprindozo** - contains all java classes for ide

**src \ main \ java \ jjprindozo \ buttons** - contains all buttons needed in IDE

**src \ main \ java \ jjprindozo \ common** - contains all classes that are reusable all throughout the classes

**src \ main \ java \ jjprindozo \ main** - contains main components of the IDE

## NavbarButtonTheme

The purpose of creating this class is to unify style, and to reuse code for Adding Tooltip and adding Key Bindings and Action Listeners.

This is how the constructor of NavbarButtonTheme looks like.

    public  NavbarButtonTheme(

    String  iconPath,

    String  tootlTipText,

    KeyStroke  buttonKeyStroke,

    Action  buttonAction,

    String  actionObject

    ) { /* Insert Code Here */ }

**IconPath:** String of the path of the image that will be used for the icons.

**toolTipText:** String that will be displayed for the tool tip.

**buttonKeyStroke:** Keystroke of the keys that would be pressed to call this button
This is the general look of a keystroke. For more complex keystroke, please refer to the internet or chatGPT hahaha.

    private  static  KeyStroke  ctrlSKeyStroke  =  KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK);

**buttonAction:** Action that will be called by the AddActionListener and the KeyStroke.
This is the format of the buttonAction:

     new  AbstractAction() {
    			@Override
    	        public  void  actionPerformed(ActionEvent  e) {
    		    /*Insert Code that you would normally put inside an add action listener */
    	        }
            }

**_Example taken from SaveButton.java:_**

    public  SaveButton(JTextArea  textArea) {
    super(
        GlobalVar.IMAGE_PATH  +  "save_file_icon.png",
        "Save",
        ctrlSKeyStroke,
        new  AbstractAction() {
    		@Override
            public  void  actionPerformed(ActionEvent  e) {
    	    selectedFile  =  fileHandler.getSelectedFile();
            saveFile(textArea);
            }
        },
        "saveAction"
        );
       }

If you encounter error in the KeyEvent, InputEvent, and ActionEvent, your IDE must have used a wrong import file. These are the correct import file:

    import  java.awt.event.InputEvent;

    import  java.awt.event.KeyEvent;

    import  java.awt.event.ActionEvent;

## KEY BINDINGS

**New File** - CTRL + N

**Open** - CTRL + O

**Save** - CTRL + S

**Undo** - CTRL + Z

**Redo** - CTRL + SHIFT + Z

**Compile** - CTRL + B

**Run** - CTRL + R