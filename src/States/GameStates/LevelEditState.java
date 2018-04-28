package States.GameStates;

import Gfx.Camera;
import LevelManagment.Level;
import LevelManagment.LevelLoader;
import Listeners.MouseListener;
import Objects.GameObject;
import Objects.Platform;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class LevelEditState extends State {

    private MouseListener ml;
    private Rectangle tempRect;
    private Platform tempPlatform;
    private Level workingLevel;

    private static GameObject justChanged;

    @Override
    public void update() {
        if(game.getkl().getKeysPressed()[KeyEvent.VK_ESCAPE]){
            enterMenu();
            return;
        }
        if(game.getkl().getControlMasked()[KeyEvent.VK_S]){
            saveLevel();
        }
        if(game.getkl().getControlMasked()[KeyEvent.VK_L]){
            if (leadLevel()) return;
        }
        if(ml.dragTangle.width != 0){
            tempRect = ml.dragTangle;
        }
        if(ml.rectReady && tempRect != null){
            createPlatform();
        }

        if(game.getkl().getControlMasked()[KeyEvent.VK_Z]){
            undo();
        }

        if(ml.delQueued){
            delete();
        }
    }

    private void delete() {
        if(ml.delTangle.width == 0) ml.delTangle = ml.rClick;
        ml.delTangle.x += game.getCamera().getX();
        ml.delTangle.y += game.getCamera().getY();
        for(GameObject o: game.getLevel().getObjects()){
            if(ml.delTangle.intersects(o.getHitBox()[0])){
                o.next = justChanged;
                justChanged = o;
                o.removed = true;
                game.getLevel().removePlatform((Platform) o);
            }
        }
        ml.delQueued = false;
        ml.delTangle.width = 0;
    }

    private void undo() {
        if (justChanged != null) {
            if(justChanged.removed){
                game.getLevel().addPlatform((Platform) justChanged);
                justChanged.removed = false;
                justChanged = justChanged.next;
            } else{
                game.getLevel().removePlatform((Platform) justChanged);
                justChanged = justChanged.next;
            }
        }
        game.getkl().setControlMasked(KeyEvent.VK_Z, false);
    }

    private void createPlatform() {
        tempPlatform = new Platform(tempRect.x + game.getCamera().getX(), tempRect.y, tempRect.width, tempRect.height, false);
        if(justChanged == null) justChanged = tempPlatform;
        else {
            tempPlatform.next = justChanged;
            justChanged = tempPlatform;
        }
        game.getLevel().addPlatform((Platform) justChanged);
        tempRect = null;
        ml.dragTangle.width = 0;
        ml.rectReady = false;
    }

    private boolean leadLevel() {
        game.getkl().setControlMasked(KeyEvent.VK_L, false);

        String name = JOptionPane.showInputDialog("Enter level name");
        Level tempLevel = LevelLoader.loadLevel(name);

        if(tempLevel == null){
            JOptionPane.showMessageDialog(null, "No level found");
            return true;
        }

        workingLevel = LevelLoader.loadLevel(name);
        game.setLevel(this.workingLevel);
        return false;
    }

    private void saveLevel() {
        game.getkl().setControlMasked(KeyEvent.VK_S, false);

        if(workingLevel.getName() == null){
            String tempName;
            do{
                tempName = JOptionPane.showInputDialog("Enter level name");
            }while (tempName.isEmpty());

            workingLevel.setName(tempName);
            workingLevel.saveLevel();
            JOptionPane.showMessageDialog(null, "Level Saved");
        }
    }

    private void enterMenu() {
        game.getkl().setKey(KeyEvent.VK_ESCAPE ,false);

        State.push(new PauseMenuState());
        State.currentState.init();
        State.currentState.update();
        return;
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

        if(ml.delTangle != null && ml.rDragging){
            g.setColor(Color.RED);
            g.drawRect(ml.delTangle.x, ml.delTangle.y, ml.delTangle.width, ml.delTangle.height);
        }
    }

    @Override
    public void init() {
        this.workingLevel = new Level();
        game.setLevel(workingLevel);
        game.setCamera(new Camera(workingLevel.getPlayer(), game.getWidth(), game.getHeight()));
        ml = game.getml();
        ml.dragTangle.width = 0;
        ml.rectReady = false;
    }
}
