package Gfx;

import Actors.Creature;
import GameController.Game;
import Objects.GameObject;

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
    private static final String[] PLACEBLE_OBJECTS = new String[]{
            "Platform"
    };
    private static final String[] PLACEBLE_CREATURES = new String[]{
            "Nothing"
    };
    private JList itemArea;
    private String[] editItems = new String[]{
            "Objects",
            "Creatures"
    };
    private JComboBox editItemTypeSelector;

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
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weighty = 0;
        gbc.weightx = 0.5;

        this.frame.add(this.buttonPanel, gbc);

        //Select tool button
        editorButtons[0] = new JButton("S");

        editorButtons[0].setToolTipText("Select tool");
        editorButtons[0].setBorder(BorderFactory.createBevelBorder(0));
        editorButtons[0].addActionListener(game.getbl());

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0,0,0,0);
        gbc.ipady = this.height/80;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;

        this.buttonPanel.add(editorButtons[0], gbc);

        //Delete tool button
        editorButtons[1] = new JButton("D");

        editorButtons[1].setToolTipText("Delete tool");
        editorButtons[1].setBorder(BorderFactory.createBevelBorder(0));
        editorButtons[1].addActionListener(game.getbl());

        gbc.gridx = 1;
        gbc.gridy = 0;

        this.buttonPanel.add(editorButtons[1], gbc);

        //Show grid button
        editorButtons[2] = new JButton("G");

        editorButtons[2].setToolTipText("Show Grid");
        editorButtons[2].setBorder(BorderFactory.createBevelBorder(0));
        editorButtons[2].addActionListener(game.getbl());

        gbc.gridx = 0;
        gbc.gridy = 1;

        this.buttonPanel.add(editorButtons[2], gbc);

        //Snap to grid button
        editorButtons[3] = new JButton("SG");

        editorButtons[3].setToolTipText("Snap to Grid");
        editorButtons[3].setBorder(BorderFactory.createBevelBorder(0));
        editorButtons[3].addActionListener(game.getbl());

        gbc.gridx = 1;
        gbc.gridy = 1;

        this.buttonPanel.add(editorButtons[3], gbc);
        gbc = new GridBagConstraints();

        //Edit item type selector menu
        editItemTypeSelector = new JComboBox(editItems);

        editItemTypeSelector.setSelectedIndex(0);
        editItemTypeSelector.addActionListener(game.getbl());

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weighty = 0;
        gbc.weightx = 1;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.fill =  GridBagConstraints.HORIZONTAL;

        this.buttonPanel.add(editItemTypeSelector, gbc);
        gbc = new GridBagConstraints();

        //The scrollPanel for selecting what to place;

        this.itemArea = new JList(editItems);

        this.itemArea.setPreferredSize(new Dimension(this.width/10, this.height - ((this.height * 9) / 40)));
        this.itemArea.setLayoutOrientation(JList.VERTICAL);
        this.itemArea.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        this.itemArea.setCellRenderer(new LEditItemDisplay());
        this.itemArea.addListSelectionListener(game.getbl());

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1;
        gbc.gridwidth = 2;

        this.buttonPanel.add(this.itemArea, gbc);
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

        this.setPreferredSize(new Dimension(this.width - this.width/10, this.height));

        this.frame.add(this);
        this.createBufferStrategy(2);
        this.frame.pack();
    }

    public JButton[] getButtons() {
        return this.editorButtons;
    }

    public JComboBox getEditItemTypeSelector() {
        return editItemTypeSelector;
    }

    public void displayEditItems(Game game) {
        String selection = (String)this.editItemTypeSelector.getSelectedItem();
        int a = 0;


        this.buttonPanel.remove(this.itemArea);
        this.itemArea = new JList();

        switch (selection){
            case "Objects":
                this.itemArea = new JList(this.PLACEBLE_OBJECTS);
                break;
            case"Creatures":
                this.itemArea = new JList(this.PLACEBLE_CREATURES);
                break;
        }
        GridBagConstraints gbc = new GridBagConstraints();

        //The scrollPanel for selecting what to place;

        this.itemArea.setPreferredSize(new Dimension(this.width/10, this.height - ((this.height * 9) / 40)));
        this.itemArea.setLayoutOrientation(JList.VERTICAL);
        this.itemArea.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        this.itemArea.setCellRenderer(new LEditItemDisplay());
        this.itemArea.addListSelectionListener(game.getbl());

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1;
        gbc.gridwidth = 2;

        this.buttonPanel.add(this.itemArea, gbc);
        this.buttonPanel.revalidate();
        this.frame.pack();
    }

    public void removeItemAreaSelection() {
        this.itemArea.clearSelection();
    }
}
