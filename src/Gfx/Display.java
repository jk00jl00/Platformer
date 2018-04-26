package Gfx;

import javax.swing.*;
import java.awt.*;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class Display extends Canvas{
    //Keeps the canvas variables;
    private JFrame frame;
    private int width;
    private int height;
    private String name;

    //Constructor for the game display
    public Display(int width, int height, String name){
        super();
        this.width = width;
        this.height = height;
        this.name = name;
        this.setBackground(Color.DARK_GRAY);

        makeComponents();
        requestFocus();
    }

    //Creates the frame and sets necessary variables before adding the Canvas, packing, and showing the window
    private void makeComponents() {
        this.frame = new JFrame(this.name);
        this.frame.setDefaultCloseOperation(EXIT_ON_CLOSE);

        this.setPreferredSize(new Dimension(width, height));

        this.frame.add(this);
        this.frame.pack();
        this.frame.setLocationRelativeTo(null);
        this.frame.setResizable(false);
        this.frame.setVisible(true);
    }
}
