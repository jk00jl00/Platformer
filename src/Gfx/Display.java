package Gfx;

import Actors.Creature;
import GameController.Game;
import Objects.GameObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.HashMap;

import static Listeners.ButtonListener.*;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class Display extends Canvas{
    private static final String[] PLACEBLE_OBJECTS = GameObject.OBJECTS;
    private static final String[] PLACEBLE_CREATURES = Creature.CREATURES;
    private final int width;
    private final int height;
    private final String name;
    private final JMenuItem[] editorButtons = new JMenuItem[9];
    private final String[] editItems = new String[]{
            "Objects",
            "Creatures"
    };
    //Keeps the canvas variables;
    private JFrame frame;
    private JPanel buttonPanel;
    private JPanel attriutes;
    private JList itemArea;
    private JComboBox editItemTypeSelector;

    private HashMap<String , LEIntAtr> intAtr = new HashMap<>();

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

        JMenuBar menuBar = new JMenuBar();

        JMenu editMenu = new JMenu("Edit");
        editMenu.setMnemonic(KeyEvent.VK_E);

        menuBar.add(editMenu);
        frame.setJMenuBar(menuBar);

        //Select tool button
        editorButtons[SELECT_TOOL_] = new JMenuItem("Select");

        editorButtons[SELECT_TOOL_].setToolTipText("Select tool");
        editorButtons[SELECT_TOOL_].addActionListener(game.getbl());


        editMenu.add(editorButtons[SELECT_TOOL_]);

        //Save button.
        editorButtons[SAVE] = new JMenuItem("Save");

        editorButtons[SAVE].setToolTipText("Save current level");
        editorButtons[SAVE].addActionListener(game.getbl());
        editorButtons[SAVE].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK));

        editMenu.add(editorButtons[SAVE]);

        //Load button.
        editorButtons[LOAD] = new JMenuItem("Load");

        editorButtons[LOAD].setToolTipText("Load a selected level");
        editorButtons[LOAD].addActionListener(game.getbl());
        editorButtons[LOAD].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, KeyEvent.CTRL_DOWN_MASK));

        editMenu.add(editorButtons[LOAD]);
        editMenu.addSeparator();

        //Copy button.
        editorButtons[COPY] = new JMenuItem("Copy");

        editorButtons[COPY].setToolTipText("Copy selection");
        editorButtons[COPY].addActionListener(game.getbl());
        editorButtons[COPY].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.CTRL_DOWN_MASK));

        editMenu.add(editorButtons[COPY]);

        //Paste button.
        editorButtons[PASTE] = new JMenuItem("Paste");

        editorButtons[PASTE].setToolTipText("Paste a copy from clipboard");
        editorButtons[PASTE].addActionListener(game.getbl());
        editorButtons[PASTE].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, KeyEvent.CTRL_DOWN_MASK));

        editMenu.add(editorButtons[PASTE]);

        //Undo button.
        editorButtons[UNDO] = new JMenuItem("Undo");

        editorButtons[UNDO].setToolTipText("Undo the latest change");
        editorButtons[UNDO].addActionListener(game.getbl());
        editorButtons[UNDO].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, KeyEvent.CTRL_DOWN_MASK));

        editMenu.add(editorButtons[UNDO]);

        //Redo button.
        editorButtons[REDO] = new JMenuItem("Redo");

        editorButtons[REDO].setToolTipText("Redoes the the latest undo");
        editorButtons[REDO].addActionListener(game.getbl());
        editorButtons[REDO].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.SHIFT_DOWN_MASK|InputEvent.CTRL_DOWN_MASK));

        editMenu.add(editorButtons[REDO]);

        //Show grid button
        editorButtons[SHOW_GRID_] = new JMenuItem("Show Grid");

        editorButtons[SHOW_GRID_].setToolTipText("Shows grid");
        editorButtons[SHOW_GRID_].addActionListener(game.getbl());


        editMenu.add(editorButtons[SHOW_GRID_]);

        //Snap to grid button
        editorButtons[SNAP_TO_GRID_] = new JMenuItem("Snap to grid");

        editorButtons[SNAP_TO_GRID_].setToolTipText("Toggles snapping to grid");
        editorButtons[SNAP_TO_GRID_].addActionListener(game.getbl());


        editMenu.add(editorButtons[SNAP_TO_GRID_]);
        gbc = new GridBagConstraints();

        //Edit item type selector menu
        editItemTypeSelector = new JComboBox(editItems);

        editItemTypeSelector.setSelectedIndex(0);
        editItemTypeSelector.addActionListener(game.getbl());

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weighty = 0;
        gbc.weightx = 1;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.fill =  GridBagConstraints.HORIZONTAL;

        this.buttonPanel.add(editItemTypeSelector, gbc);
        gbc = new GridBagConstraints();

        //The JList for selecting what to place;

        this.itemArea = new JList(editItems);

        this.itemArea.setPreferredSize(new Dimension(this.width/10, this.height - ((this.height * 9) / 40)));
        this.itemArea.setLayoutOrientation(JList.VERTICAL);
        this.itemArea.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        this.itemArea.setCellRenderer(new LEditItemDisplay());
        this.itemArea.addListSelectionListener(game.getbl());

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1;
        gbc.gridwidth = 2;

        this.buttonPanel.add(this.itemArea, gbc);
        gbc = new GridBagConstraints();

        //Readding the canvas to the frame
        gbc.gridy = 0;
        gbc.gridx = 1;

        this.setPreferredSize(new Dimension((this.width - this.width/10), this.height));

        this.frame.add(this, gbc);
        this.createBufferStrategy(2);
        this.frame.pack();
        this.requestFocus();

    }

    public void exitLevelEditorUi() {
        this.frame.remove(this.buttonPanel);
        this.frame.remove(this);

        this.setPreferredSize(new Dimension(this.width, this.height));

        this.frame.add(this);
        this.createBufferStrategy(2);
        this.frame.pack();
        this.requestFocus();
    }

    public JMenuItem[] getButtons() {
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
                this.itemArea = new JList(PLACEBLE_OBJECTS);
                break;
            case"Creatures":
                this.itemArea = new JList(PLACEBLE_CREATURES);
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
        gbc.gridy = 1;
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

    /**
     * Called when a single object has been selected in the editor.
     * Opens a display below the the itemArea with the objects attributes
     * @param o The object which attributes will be displayed.
     */
    public void displayAttributes(GameObject o, Game game) {
        this.attriutes = new JPanel();
        this.intAtr = new HashMap<>();
        this.attriutes.setLayout(new GridBagLayout());
        this.attriutes.setBorder(BorderFactory.createMatteBorder(5,0,5,0, Color.BLACK));
        GridBagConstraints gbc;


        int i = 0;
        for(String s : o.getAttributes().keySet()){
            gbc = new GridBagConstraints();
            gbc.anchor = GridBagConstraints.EAST;
            gbc.fill = GridBagConstraints.NONE;
            JLabel l = new JLabel();
            l.setText(s + ": ");
            gbc.gridx = 0;
            gbc.gridy = i;
            attriutes.add(l, gbc);
            LEIntAtr f = new LEIntAtr(o.getAttributes().get(s), s ,game.getbl());
            intAtr.put(s, f);
            gbc.fill = GridBagConstraints.BOTH;
            gbc.anchor = GridBagConstraints.CENTER;
            gbc.weightx = 0.5;
            gbc.gridx = 1;

            attriutes.add(f, gbc);
            f.setColumns(5);
            i++;
        }

        gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        this.buttonPanel.add(attriutes, gbc);
        this.frame.pack();
    }
    public void displayAttributes(Creature c, Game game) {
        this.attriutes = new JPanel();
        this.intAtr = new HashMap<>();
        this.attriutes.setLayout(new GridBagLayout());
        this.attriutes.setBorder(BorderFactory.createMatteBorder(5,0,5,0, Color.BLACK));
        GridBagConstraints gbc;


        int i = 0;
        for(String s : c.getAttributes().keySet()){
            gbc = new GridBagConstraints();
            gbc.anchor = GridBagConstraints.EAST;
            gbc.fill = GridBagConstraints.NONE;
            JLabel l = new JLabel();
            l.setText(s + ": ");
            gbc.gridx = 0;
            gbc.gridy = i;
            attriutes.add(l, gbc);
            LEIntAtr f = new LEIntAtr(c.getAttributes().get(s), s ,game.getbl());
            intAtr.put(s, f);
            gbc.fill = GridBagConstraints.BOTH;
            gbc.anchor = GridBagConstraints.CENTER;
            gbc.weightx = 0.5;
            gbc.gridx = 1;

            attriutes.add(f, gbc);
            f.setColumns(5);
            i++;
        }

        gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        this.buttonPanel.add(attriutes, gbc);
        this.frame.pack();
    }

    public void removeAtrDisplay() {
        if(attriutes != null) {
            this.buttonPanel.remove(this.attriutes);
            this.frame.pack();
            this.attriutes = null;
        }
    }

    public void updateAtrDisplay(GameObject o) {
        for(String s: intAtr.keySet()){
            if(intAtr.get(s).getCurrentValue() != o.getAttributes().get(s)){
                intAtr.get(s).setValue(o.getAttributes().get(s));
            }
        }
    }
    public void updateAtrDisplay(Creature c) {
        for(String s: intAtr.keySet()){
            if(intAtr.get(s).getCurrentValue() != c.getAttributes().get(s)){
                intAtr.get(s).setValue(c.getAttributes().get(s));
            }
        }
    }
}
