package States.GameStates;

import Gfx.Camera;
import LevelManagment.LevelLoader;

import javax.swing.*;
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
            enterMenu();
            return;
        }
        game.getLevel().update();
        game.getCamera().update();
    }

    private void enterMenu() {
        State.push(new PauseMenuState());
        game.getkl().setKey(KeyEvent.VK_ESCAPE, false);
        State.currentState.init();
        State.currentState.update();
        return;
    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(Color.DARK_GRAY);
        g.fillRect(0, 0, game.getWidth(), game.getHeight());

        game.getLevel().draw(g, game.getCamera());
    }

    @Override
    public void init() {
        //game.getLevel().instantiate();
        String levelName = JOptionPane.showInputDialog("Enter level name");
        if(levelName == null){
            State.pop();
            return;
        }
        game.setLevel(LevelLoader.loadLevel(levelName));

        if(game.getLevel() == null){
            JOptionPane.showMessageDialog(null, "No level with that name");
            State.pop();
            return;
        }

        game.setCamera(new Camera(game.getLevel().getPlayer(), game.getWidth(), game.getHeight()));
    }
}
