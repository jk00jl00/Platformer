package States.GameStates;

import Gfx.Camera;
import LevelManagment.LevelLoader;
import States.PlayerStates.OnGroundStates;
import States.PlayerStates.PlayerStateStack;

import java.awt.*;
import java.awt.event.KeyEvent;
//TODO - Fix reset level.
public class PlayState extends State {

    @Override
    public void update() {
        boolean[] keys = game.getkl().getKeysPressed();
        game.getLevelManager().getCurrentLevel().getPlayer().setKeys(keys);
        if(keys[KeyEvent.VK_R]){
            game.getLevelManager().getCurrentLevel().resetPlayer();
        } else if(keys[KeyEvent.VK_ESCAPE]){
            enterMenu();
            return;
        } else if(game.getkl().getControlMasked()[KeyEvent.VK_R]){
            game.resetLevel();
            game.getkl().setControlMasked(KeyEvent.VK_R, false);
            game.getCamera().setFocus(game.getLevelManager().getCurrentLevel().getPlayer());
            return;
        }
        if(game.getLevelManager().getCurrentLevel().getPlayer().noState()) PlayerStateStack.push(new OnGroundStates(game.getLevelManager().getCurrentLevel().getPlayer(), game));
        game.getLevelManager().getCurrentLevel().update();
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

        game.getLevelManager().getCurrentLevel().draw(g, game.getCamera());
    }

    @Override
    public void init() {
        //game.getLevelManager().instantiate();
        if(!game.getLevelManager().loadLevel()){
            pop();
            return;
        }
        game.setJustLoaded(true);
        game.setCamera(new Camera(game.getLevelManager().getCurrentLevel().getPlayer(), game.getWidth(), game.getHeight()));
    }
    @Override
    public void init(String name){
        game.getLevelManager().setCurrentLevel(LevelLoader.loadLevel(name));
        game.setJustLoaded(true);
        game.setCamera(new Camera(game.getLevelManager().getCurrentLevel().getPlayer(), game.getWidth(), game.getHeight()));
    }
}
