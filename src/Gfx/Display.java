package Gfx;

import GameController.Game;

import javax.swing.*;
import java.awt.*;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class Display extends Canvas{
    //Keeps the canvas variables;
    private JFrame frame;
    private int width;
    private int height;
    private String name;
    private JButton[] editorButtons = new JButton[4];
    private JPanel buttonPanel;

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


    //Called in LevelEditStat's init in order to change the ui to fit the editor;
    public void levelEditorUI(Game game){
        this.frame.remove(this);
        this.frame.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        //Making the left button panel and the buttons and then adding the buttons to the panel

        //The panel
        this.buttonPanel = new JPanel();
        this.buttonPanel.setPreferredSize(new Dimension(this.width/10, this.height));
        this.buttonPanel.setLayout(new GridBagLayout());
        this.buttonPanel.setBorder(BorderFactory.createMatteBorder(0,0,0,5, Color.BLACK));
        this.buttonPanel.setBackground(Color.lightGray);

        gbc.gridx = 0;
        gbc.gridy = 0;

        this.frame.add(this.buttonPanel, gbc);
        gbc = new GridBagConstraints();

        //Select tool button
        editorButtons[0] = new JButton("S");

        editorButtons[0].setToolTipText("Select tool");
        editorButtons[0].setBorder(BorderFactory.createBevelBorder(0));
        editorButtons[0].addActionListener(game.getbl());
        editorButtons[0].setPreferredSize(new Dimension(this.width/20 - 2, this.height/20));

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0,0,0,0);

        this.buttonPanel.add(editorButtons[0], gbc);
        gbc = new GridBagConstraints();

        //Delete tool button
        editorButtons[1] = new JButton("D");

        editorButtons[1].setToolTipText("Delete tool");
        editorButtons[1].setBorder(BorderFactory.createBevelBorder(0));
        editorButtons[1].addActionListener(game.getbl());
        editorButtons[1].setPreferredSize(new Dimension(this.width/20 - 3, this.height/20));

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.insets = new Insets(0,0,0,0);

        this.buttonPanel.add(editorButtons[1], gbc);
        gbc = new GridBagConstraints();

        //Readding the canvas to the frame
        gbc.gridy = 0;
        gbc.gridx = 1;

        this.setPreferredSize(new Dimension((this.width - this.buttonPanel.getWidth()), this.height));

        this.frame.add(this, gbc);
        this.createBufferStrategy(2);
        this.frame.pack();

    }

    public void exitLevelEditorUi() {
        this.frame.remove(this.buttonPanel);
        this.frame.remove(this);

        this.setPreferredSize(new Dimension(this.width, this.height));

        this.frame.add(this);
        this.createBufferStrategy(2);
        this.frame.pack();
    }

    public JButton[] getButtons() {
        return this.editorButtons;
    }
}
