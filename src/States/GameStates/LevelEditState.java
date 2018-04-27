package States.GameStates;

import Listeners.MouseListener;
import Objects.GameObject;
import Objects.Platform;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.security.Key;

public class LevelEditState extends State {

    private MouseListener ml;
    private Rectangle tempRect;
    private Platform tempPlatform;

    private static GameObject justAdded;


    @Override
    public void update() {
        if(game.getkl().getKeysPressed()[KeyEvent.VK_ESCAPE] || game.getkl().getKeysPressed()[KeyEvent.VK_P]){
            game.getkl().setKey(KeyEvent.VK_ESCAPE ,false);
            game.getkl().setKey(KeyEvent.VK_P ,false);
            pop();
        }

        if(ml.dragTangle.width != 0){
            tempRect = ml.dragTangle;
        }
        if(ml.rectReady && tempRect != null){
            tempPlatform = new Platform(tempRect.x + game.getCamera().getX(), tempRect.y, tempRect.width, tempRect.height, false);
            if(justAdded == null) justAdded = tempPlatform;
            else {
                tempPlatform.next = justAdded;
                justAdded = tempPlatform;
            }
            game.getLevel().addPlatform((Platform) justAdded);
            tempRect = null;
            ml.dragTangle.width = 0;
            ml.rectReady = false;
        }

        if(game.getkl().getControlMasked()[KeyEvent.VK_Z]){
            if (justAdded != null) {
                game.getLevel().removePlatform((Platform) justAdded);
                justAdded = justAdded.next;
            }
            game.getkl().seControlMasked(KeyEvent.VK_Z, false);
        }
    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor((game.getLevel().getDarker()) ? Color.DARK_GRAY.darker() : Color.DARK_GRAY);
        g.fillRect(0, 0, game.getWidth(), game.getHeight());

        game.getLevel().draw(g, game.getCamera());
        if (tempRect != null) {
            g.fillRect(tempRect.x, tempRect.y, tempRect.width, tempRect.height);
        }
        g.setColor(Color.WHITE);
        g.drawString("Edit mode - Undo: ctrl z  ||  Place platform: click and drag", 0,
                (int) g.getFontMetrics().getStringBounds("Edit mode - Undo: ctrl z  ||  Place platform: click and drag", g).getHeight());
    }

    @Override
    public void init() {
        ml = game.getml();
        ml.dragTangle.width = 0;
        ml.rectReady = false;
    }
}
