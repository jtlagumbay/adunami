package jjprindozo.common;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.File;
import java.io.IOException;

public class Fonts {
    private Fonts() {

    }

    public static Font getRegular() {
        Font jost = new Font("sans-serif", Font.PLAIN, 14);

        try {
            jost = Font.createFont(Font.TRUETYPE_FONT, new File("src/main/resources/fonts/jost.ttf")).deriveFont(14f);
        } catch(IOException e) {
            e.printStackTrace();
        } catch(FontFormatException e) {
            e.printStackTrace();
        }

        return jost;
    }

    public static Font getRegular(float fontSize) {
        Font jost = new Font("sans-serif", Font.PLAIN, 14);

        try {
            jost = Font.createFont(Font.TRUETYPE_FONT, new File("src/main/resources/fonts/jost.ttf")).deriveFont(fontSize);
        } catch(IOException e) {
            e.printStackTrace();
        } catch(FontFormatException e) {
            e.printStackTrace();
        }

        return jost;
    }

    public static Font getBold() {
        Font jostBold = new Font("sans-serif", Font.PLAIN, 14);

        try {
            jostBold = Font.createFont(Font.TRUETYPE_FONT, new File("src/main/resources/fonts/jost-bold.ttf")).deriveFont(14f);
        } catch(IOException e) {
            e.printStackTrace();
        } catch(FontFormatException e) {
            e.printStackTrace();
        }

        return jostBold;
    }

    public static Font getBold(float fontSize) {
        Font jostBold = new Font("sans-serif", Font.PLAIN, 14);

        try {
            jostBold = Font.createFont(Font.TRUETYPE_FONT, new File("src/main/resources/fonts/jost-bold.ttf")).deriveFont(fontSize);
        } catch(IOException e) {
            e.printStackTrace();
        } catch(FontFormatException e) {
            e.printStackTrace();
        }

        return jostBold;
    }
}
