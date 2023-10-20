package jjprindozo.main;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import jjprindozo.common.Fonts;

public class Quotes extends JPanel {
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private List<JSONObject> jsonObjectList;
    private String randomQuote[] = {"Be motivated. Click generate.", "adunami Team"};
    private JTextArea msg;
    private JTextArea auth;

    public Quotes() {
        setLayout(new BorderLayout());
        JPanel top = new JPanel(new BorderLayout());
        top.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        // read JSON file
        readJSON();

        JLabel title = new JLabel("Quotes to Motivate You");
        title.setFont(Fonts.getBold());
        top.add(title, BorderLayout.WEST);
        
        JButton generate = new JButton("Generate New Quote", new ImageIcon(new ImageIcon("src/main/resources/images/generate-icon.png").getImage().getScaledInstance(10, 10, Image.SCALE_SMOOTH)));
        generate.setFont(Fonts.getRegular());
        generate.setIconTextGap(15);
        generate.setMargin(new Insets(0, 5, 0, 5));
        generate.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        generate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getNewQuote();
            }
        });

        top.add(generate, BorderLayout.EAST);

        add(top, BorderLayout.NORTH);

        JPanel pane = generateQuote();
        add(pane);
    }

    private JPanel generateQuote() {
        JPanel quoteContainer = new JPanel(new BorderLayout());
        
        msg = new JTextArea();
        msg.setFont(Fonts.getRegular(22f));
        msg.setEditable(false);
        msg.setWrapStyleWord(true);
        msg.setLineWrap(true);
        msg.setBorder(BorderFactory.createEmptyBorder(40, 20, 0, 20));
        quoteContainer.add(msg, BorderLayout.CENTER);
        
        auth = new JTextArea();
        auth.setFont(Fonts.getRegular(20f));
        auth.setEditable(false);
        auth.setWrapStyleWord(true);
        auth.setLineWrap(true);
        auth.setBorder(BorderFactory.createEmptyBorder(0, 20, 60, 20));
        quoteContainer.add(auth, BorderLayout.SOUTH);

        updateQuoteText();

        return quoteContainer;
    }

    private void updateQuoteText() {
        String message = "'" + randomQuote[0] + "'";
        String author = "- " + randomQuote[1];
    
        msg.setText(message);
        auth.setText(author);
    }

    private void readJSON() {
        try {
            String filePath = "src/main/resources/Quotes.json";
            FileReader fileReader = new FileReader(filePath);
            JSONTokener jsonTokener = new JSONTokener(fileReader);
            JSONArray jsonArray = new JSONArray(jsonTokener);
            jsonObjectList = new ArrayList<>();

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                jsonObjectList.add(jsonObject);
            }

            fileReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getNewQuote() {
        int randomIndex = new Random().nextInt(jsonObjectList.size());
        JSONObject randomJsonObject = jsonObjectList.get(randomIndex);

        randomQuote[0] = randomJsonObject.getString("text");
        randomQuote[1] = randomJsonObject.getString("author");

        updateQuoteText();
    }
}
