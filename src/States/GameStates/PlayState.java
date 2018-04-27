package States.GameStates;

import Gfx.Camera;
import LevelManagment.Level;

import java.awt.*;
import java.awt.event.KeyEvent;

public class PlayState extends State {
    private boolean[] keys;

    @Override
    public void update() {
        keys = game.getkl().getKeysPressed();
        game.getLevel().getPlayer().setKeys(keys);
        if(keys[KeyEvent.VK_R]){
            game.getLevel().getPlayer().resetPos();
        } else if(keys[KeyEvent.VK_ESCAPE]){
            State.push(new MenuState());
            game.getkl().setKey(KeyEvent.VK_ESCAPE, false);
            State.currentState.init();
            State.currentState.update();
            return;
        } else if(keys[KeyEvent.VK_P]){
            State.push(new LevelEditState());
            game.getkl().setKey(KeyEvent.VK_P, false);
            State.currentState.init();
            State.currentState.update();
            return;
        }
        game.getLevel().update();
        game.getCamera().update();
    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor((game.getLevel().getDarker()) ? Color.DARK_GRAY.darker() : Color.DARK_GRAY);
        g.fillRect(0, 0, game.getWidth(), game.getHeight());

        game.getLevel().draw(g, game.getCamera());
    }

    @Override
    public void init() {
        game.setLevel(new Level());
        game.getLevel().instantiate();
        game.setCamera(new Camera(game.getLevel().getPlayer(), game.getWidth(), game.getHeight()));
    }
}
