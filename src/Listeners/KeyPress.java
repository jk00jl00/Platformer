package Listeners;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

//The class used to record key presses in game to later be handled by the different components
public class KeyPress implements KeyListener {

    private final boolean[] keysPressed = new boolean[524];
    private final boolean[] controlMasked = new boolean[524];
    public boolean wReleased;
    public boolean sReleased;

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
        if(e.getKeyCode() == KeyEvent.VK_W) wReleased = true;
        if(e.getKeyCode() == KeyEvent.VK_S) sReleased = true;
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

    public boolean arrowKey() {
        return keysPressed[KeyEvent.VK_LEFT] || keysPressed[KeyEvent.VK_UP] || keysPressed[KeyEvent.VK_RIGHT] || keysPressed[KeyEvent.VK_DOWN];
    }
}
