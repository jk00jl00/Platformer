import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

//The class used to record key presses in game to later be handled by the different components
public class KeyPress implements KeyListener {

    boolean[] keysPressed = new boolean[256];

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        keysPressed[e.getKeyCode()] = true;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        keysPressed[e.getKeyCode()] = false;
    }
}
