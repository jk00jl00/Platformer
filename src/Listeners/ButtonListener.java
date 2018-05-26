package Listeners;

import Actors.Creature;
import Gfx.Display;
import Gfx.LEIntAtr;
import Objects.GameObject;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ButtonListener implements ActionListener, ListSelectionListener{
    //Constants
    public static final int PLACING = -1;
    public static final int SELECT_TOOL_ = 0;
    public static final int SAVE = 1;
    public static final int LOAD = 2;
    public static final int COPY = 3;
    public static final int PASTE = 4;
    public static final int UNDO = 5;
    public static final int REDO = 6;
    public static final int SHOW_GRID_ = 7;
    public static final int SNAP_TO_GRID_ = 8;
    private static final String[] PLACEBLE_OBJECTS = GameObject.OBJECTS;
    private static final String[] PLACEBLE_CREATURES = Creature.CREATURES;
    private final boolean[] buttonsPressed = new boolean[9];
    public boolean intAtrChanged = false;
    private String selection = "objects";
    private String toPlace = "";
    private int newAtrValue;
    private String changedAtrName;
    private Display display;

    public ButtonListener(){
        super();
        for(int i = 0; i < buttonsPressed.length; i++){
            buttonsPressed[i] = false;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == display.getButtons()[SELECT_TOOL_]){
            buttonsPressed[SELECT_TOOL_] = true;
        }
        if(e.getSource() == display.getButtons()[UNDO]){
            buttonsPressed[UNDO] = true;
        }
        if(e.getSource() == display.getButtons()[REDO]){
            buttonsPressed[REDO] = true;
        }
        if(e.getSource() == display.getButtons()[SAVE]){
            buttonsPressed[SAVE] = true;
        }
        if(e.getSource() == display.getButtons()[COPY]){
            buttonsPressed[COPY] = true;
        }
        if(e.getSource() == display.getButtons()[PASTE]){
            buttonsPressed[PASTE] = true;
        }
        if(e.getSource() == display.getButtons()[LOAD]){
            buttonsPressed[LOAD] = true;
        }
        if(e.getSource() == display.getButtons()[SHOW_GRID_]){
            buttonsPressed[SHOW_GRID_] = true;
        }
        if(e.getSource() == display.getButtons()[SNAP_TO_GRID_]){
            buttonsPressed[SNAP_TO_GRID_] = true;
        }
        if(e.getSource() == display.getEditItemTypeSelector()){
            String selected = (String) display.getEditItemTypeSelector().getSelectedItem();
            switch(selected){
                case "Objects":
                    this.selection = "objects";
                    System.out.println("object");
                    break;
                case "Creatures":
                    this.selection = "creatures";
                    System.out.println("creature");
                    break;
            }
        }
        display.requestFocus();
    }

    public void addDisplay(Display display) {
        this.display = display;
    }

    public boolean[] getButtonsPressed() {
        return buttonsPressed;
    }

    public void disableButton(int i) {
        buttonsPressed[i] = false;
    }

    public String getSelection() {
        return selection;
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        JList l = (JList) e.getSource();
        int i = 0;

        if (l.isSelectionEmpty()) {
            toPlace = "";

        } else {
            // Find out what index is selected.
            int maxIndex = l.getMaxSelectionIndex();
            for (; i <= maxIndex; i++) {
                if (l.isSelectedIndex(i)) {
                    break;
                }
            }
            switch (selection){
                case "objects":
                    toPlace = PLACEBLE_OBJECTS[i];
                    break;
                case "creatures":
                    toPlace = PLACEBLE_CREATURES[i];
                    break;
            }
        }
        display.requestFocus();
    }

    public String getToPlace() {
        return toPlace;
    }

    public void setToPlace(String toPlace) {
        this.toPlace = toPlace;
    }

    public void attributeChange(LEIntAtr leIntAtr) {
        intAtrChanged = true;
        newAtrValue = leIntAtr.getCurrentValue();
        changedAtrName = leIntAtr.getAtrName();
    }

    public int getAtrChange() {
        return newAtrValue;
    }

    public String getAtrName() {
        return changedAtrName;
    }
}
