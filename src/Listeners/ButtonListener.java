package Listeners;

import Gfx.Display;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ButtonListener implements ActionListener{
    private boolean[] buttonsPressed = new boolean[]{false, false, false, false};
    public static final int SELECT_TOOL_ = 0;
    public static final int DELETE_TOOL_ = 1;
    public static final int SHOW_GRID_ = 2;
    public static final int SNAP_TO_GRID_ = 3;

    private Display display;
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == display.getButtons()[SELECT_TOOL_]){
            buttonsPressed[SELECT_TOOL_] = true;
        }
        if(e.getSource() == display.getButtons()[DELETE_TOOL_]){
            buttonsPressed[DELETE_TOOL_] = true;
        }
        if(e.getSource() == display.getButtons()[SHOW_GRID_]){

        }
        if(e.getSource() == display.getButtons()[SNAP_TO_GRID_]){

        }

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
}
