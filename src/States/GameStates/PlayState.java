package States.GameStates;

import java.awt.*;
import java.awt.event.KeyEvent;

public class PlayState extends State {

    @Override
    public void update() {
        game.getLevel().getPlayer().setKeys(game.getkl().getKeysPressed());
        if(game.getkl().getKeysPressed()[KeyEvent.VK_R]){
            game.getLevel().getPlayer().resetPos();
        }
        game.getLevel().update();
        game.getCamera().update();
    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(Color.DARK_GRAY);
        g.fillRect(0, 0, game.getWidth(), game.getHeight());

        game.getLevel().draw(g, game.getCamera());
    }
}
