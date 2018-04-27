package States.GameStates;

import Listeners.MouseListener;
import Objects.Platform;

import java.awt.*;
import java.awt.event.KeyEvent;

public class LevelEditState extends State {

    private MouseListener ml;
    private Rectangle tempRect;


    @Override
    public void update() {
        if(game.getkl().getKeysPressed()[KeyEvent.VK_ESCAPE]){
            game.getkl().setKey(KeyEvent.VK_ESCAPE ,false);
            pop();
        }

        if(ml.dragTangle.width != 0){
            tempRect = ml.dragTangle;
        }
        if(ml.rectReady && tempRect != null){
            game.getLevel().addPlatform(new Platform(tempRect.x, tempRect.y, tempRect.width, tempRect.height, false));
            tempRect = null;
            ml.dragTangle.width = 0;
            ml.rectReady = false;
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
    }

    @Override
    public void init() {
        ml = game.getml();
        ml.dragTangle.width = 0;
        ml.rectReady = false;
    }
}
