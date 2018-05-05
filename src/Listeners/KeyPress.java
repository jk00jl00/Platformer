package Listeners;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

//The class used to record key presses in game to later be handled by the different components
public class KeyPress implements KeyListener {

    private boolean[] keysPressed = new boolean[256];
    private boolean[] controlMasked = new boolean[256];
    public boolean spaceReleased = true;

    public boolean[] getKeysPressed() {
        return keysPressed;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.isControlDown()) controlMasked[e.getKeyCode()] = true;
        else keysPressed[e.getKeyCode()] = true;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        keysPressed[e.getKeyCode()] = false;
        if(e.getKeyCode() == KeyEvent.VK_SPACE) spaceReleased = true;
    }

    public void setKey(int key, boolean b) {
        keysPressed[key] = b;
    }

    public boolean[] getControlMasked() {
        return controlMasked;
    }

    public void setControlMasked(int key, boolean b) {
        controlMasked[key] = b;
    }
}
