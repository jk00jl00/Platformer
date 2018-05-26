package States.GameStates;

import Gfx.Camera;
import LevelManagment.LevelLoader;
import States.PlayerStates.OnGroundStates;
import States.PlayerStates.PlayerStateStack;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class PlayState extends State {

    @Override
    public void update() {
        boolean[] keys = game.getkl().getKeysPressed();
        game.getLevel().getPlayer().setKeys(keys);
        if(keys[KeyEvent.VK_R]){
            game.getLevel().resetPlayer();
        } else if(keys[KeyEvent.VK_ESCAPE]){
            enterMenu();
            return;
        } else if(game.getkl().getControlMasked()[KeyEvent.VK_R]){
            game.resetLevel();
            game.getkl().setControlMasked(KeyEvent.VK_R, false);
            return;
        }
        if(game.getLevel().getPlayer().noState()) PlayerStateStack.push(new OnGroundStates(game.getLevel().getPlayer(), game));
        game.getLevel().update();
        game.getCamera().update();
    }

    private void enterMenu() {
        State.push(new PauseMenuState(false));
        game.getkl().setKey(KeyEvent.VK_ESCAPE, false);
        State.currentState.init();
        State.currentState.update();
    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(Color.DARK_GRAY);
        g.fillRect(0, 0, game.getWidth() + 100, game.getHeight() + 100);

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
        game.setJustLoaded(true);
        game.setCamera(new Camera(game.getLevel().getPlayer(), game.getWidth(), game.getHeight()));
    }
    @Override
    public void init(String name){
        game.setLevel(LevelLoader.loadLevel(name));
        game.setJustLoaded(true);
        game.setCamera(new Camera(game.getLevel().getPlayer(), game.getWidth(), game.getHeight()));
    }
}
