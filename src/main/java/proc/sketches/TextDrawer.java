package proc.sketches;

import javax.swing.*;
import java.awt.*;

public class TextDrawer extends JFrame {

    private String text;

    public TextDrawer(String text) {
        this.text = text;

        // Set up the JFrame
        setTitle("Text Drawer");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Add a JPanel to the JFrame
        add(new TextPanel());
    }

    private class TextPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            // Draw the text in the center of the panel
            FontMetrics fm = g.getFontMetrics();
            int textWidth = fm.stringWidth(text);
            int textHeight = fm.getHeight();

            int x = (getWidth() - textWidth) / 2;
            int y = (getHeight() - textHeight) / 2 + fm.getAscent();

            drawString2(g,text, x+250, y-50);
        }
        void drawString2(Graphics g, String text, int x, int y) {
            for (String line : text.split("\n"))
                g.drawString(line, x, y += g.getFontMetrics().getHeight());
        }
    }


}