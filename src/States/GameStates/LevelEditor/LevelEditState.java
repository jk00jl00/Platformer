package States.GameStates.LevelEditor;

import Actors.Creature;
import Gfx.Camera;
import LevelManagment.Level;
import LevelManagment.LevelLoader;
import Listeners.MouseListener;
import Objects.GameObject;
import Objects.Platform;
import States.GameStates.PauseMenuState;
import States.GameStates.State;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import static Listeners.ButtonListener.DELETE_TOOL_;
import static Listeners.ButtonListener.SELECT_TOOL_;

public class LevelEditState extends State {

    private MouseListener ml;
    private Rectangle tempRect;
    private Platform tempPlatform;
    private Level workingLevel;

    private int[] beforeMoveObjects;
    private int[] beforeMoveCreatures;
    private ArrayList<GameObject> selectedGameObjects = new ArrayList<>();
    private ArrayList<Creature> selectedCreatures = new ArrayList<>();

    private static GameObject justChanged;

    private int tool = SELECT_TOOL_;

    @Override
    public void update() {
        if (checkKeyButtons()) return;
        checkToolSelection();

        if(ml.dragTangle.width == 0){
            return;
        }

        tempRect = ml.dragTangle;
        if (ml.rectReady) {
            switch (tool){
                case SELECT_TOOL_:
                    handleSelection();
                    break;
                case DELETE_TOOL_:
                    handleDeletion();
                    break;


            }
            System.out.println(ml.dragTangle.width + " " +ml.dragTangle.height);
            ml.dragTangle.width = 0;
            ml.rectReady = false;
            this.tempRect = null;
        }

        if(!this.selectionEmpty() && ml.draggingSelection){
            drag();
        }
        /*

        if(ml.rectReady && tempRect != null){
            createPlatform();
        }*/

    }

    private void handleDeletion() {
        tempRect.x += game.getCamera().getX();
        tempRect.y += game.getCamera().getY();
        int a = 0;
        int b = 0;
        ChangeManager.isPushing = true;
        for(GameObject o: game.getLevel().getObjects()){
            if(tempRect.intersects(o.getHitBox())){
                a++;
            }
        }
        for(Creature c: game.getLevel().getCreatures()){
            if(!c.getType().equals("player") && tempRect.intersects(c.getHitBox())){
                b++;
            }
        }
        ChangeManager.push(a, b, true);

        for(GameObject o: game.getLevel().getObjects()){
            if(tempRect.intersects(o.getHitBox())){
                ChangeManager.push(o);
                game.getLevel().removeObject(o);
            }
        }
        for(Creature c: game.getLevel().getCreatures()){
            if(!c.getType().equals("player") && tempRect.intersects(c.getHitBox())){
                ChangeManager.push(c);
                game.getLevel().removeCreature(c);
            }
        }
    }

    private boolean checkKeyButtons() {
        if(game.getkl().getKeysPressed()[KeyEvent.VK_ESCAPE]){
            enterMenu();
            return true;
        }
        if(game.getkl().getControlMasked()[KeyEvent.VK_S]){
            saveLevel();
        }
        if(game.getkl().getControlMasked()[KeyEvent.VK_L]){
            if (leadLevel()) return true;
        }
        if(game.getkl().getControlMasked()[KeyEvent.VK_Z]){
            undo();
        }
        return false;
    }

    private void checkToolSelection() {
        if(game.getbl().getButtonsPressed()[SELECT_TOOL_]){
            game.getbl().disableButton(SELECT_TOOL_);
            changeTool(SELECT_TOOL_);
            System.out.println("SelectTool");
        }
        if(game.getbl().getButtonsPressed()[DELETE_TOOL_]){
            game.getbl().disableButton(DELETE_TOOL_);
            changeTool(DELETE_TOOL_);
            System.out.println(DELETE_TOOL_);
        }
    }

    private void drag() {
        for(GameObject o: selectedGameObjects){
            o.move(ml.getXDrag(), ml.getYDrag());
        }
        for(Creature c: selectedCreatures){
            c.move(ml.getXDrag(), ml.getYDrag());
        }

        ml.setXDrag(0);
        ml.setYDrag(0);
    }

    private void handleSelection() {
        //if selection is dragged be prepared to handle multiple selections
        tempRect.x += game.getCamera().getX();
        tempRect.y += game.getCamera().getY();
        if (selectionEmpty()) {
            if(tempRect.width > 1){
                for(GameObject o: game.getLevel().getObjects()){
                    if(o.getHitBox().intersects(tempRect)){
                        selectedGameObjects.add(o);
                    }
                }
                for(Creature c: game.getLevel().getCreatures()){
                    if(c.getHitBox().intersects(tempRect)){
                        selectedCreatures.add(c);
                    }
                }
                ml.setHasSelection(true);
                ml.setSelectedObjects(this.selectedGameObjects.toArray(new GameObject[this.selectedGameObjects.size()]));
                ml.setSelectedCreatures(this.selectedCreatures.toArray(new Creature[this.selectedCreatures.size()]));
            } else {
                for (GameObject o : game.getLevel().getObjects()) {
                    if (o.getHitBox().intersects(tempRect)) {
                        selectedGameObjects.add(o);
                        break;
                    }
                }

                for (Creature c : game.getLevel().getCreatures()) {
                    if (c.getHitBox().intersects(tempRect)) {
                        selectedCreatures.add(c);
                        break;
                    }
                }
                ml.setHasSelection(true);
                ml.setSelectedObjects(this.selectedGameObjects.toArray(new GameObject[this.selectedGameObjects.size()]));
                ml.setSelectedCreatures(this.selectedCreatures.toArray(new Creature[this.selectedCreatures.size()]));

                //Open the object on the left hand side;
            }
            if(!selectionEmpty()){
                beforeMoveCreatures = new int[selectedCreatures.size() * 2];
                beforeMoveObjects = new int[selectedGameObjects.size() * 2];
                for (int i = 0; i < selectedCreatures.size(); i++) {
                    beforeMoveCreatures[i * 2] = selectedCreatures.get(i).getX();
                    beforeMoveCreatures[(i * 2 + 1)] = selectedCreatures.get(i).getY();
                }
                for (int i = 0; i < selectedGameObjects.size(); i++) {
                    beforeMoveObjects[i * 2] = selectedGameObjects.get(i).getX();
                    beforeMoveObjects[(i * 2 + 1)] = selectedGameObjects.get(i).getY();
                }
            }
        } else if(ml.draggingSelection){
            ChangeManager.isPushing = true;
            ChangeManager.push(selectedGameObjects.size() , selectedCreatures.size());
            for(int i = 0; i < selectedGameObjects.size(); i++){
                ChangeManager.push(selectedGameObjects.get(i), (beforeMoveObjects[i * 2] - selectedGameObjects.get(i).getX()),
                        beforeMoveObjects[i * 2 + 1] - selectedGameObjects.get(i).getY());
                System.out.println((beforeMoveObjects[i * 2] - selectedGameObjects.get(i).getX()));
            }
            for(int i = 0; i < selectedCreatures.size(); i++){
                ChangeManager.push(selectedCreatures.get(i), beforeMoveCreatures[i * 2] - selectedCreatures.get(i).getX(),
                        beforeMoveCreatures[i * 2 + 1] - selectedCreatures.get(i).getY());
            }
            ml.draggingSelection = false;
        } else{
            selectedGameObjects.removeAll(selectedGameObjects);
            selectedCreatures.removeAll(selectedCreatures);
            ml.setSelectedObjects(selectedGameObjects.toArray(new GameObject[selectedGameObjects.size()]));
            ml.setSelectedCreatures(selectedGameObjects.toArray(new Creature[selectedGameObjects.size()]));
            ml.setHasSelection(false);
            this.handleSelection();
        }

        //if single selection open attributes on the left and allow movement when dragging;
    }

    private boolean selectionEmpty() {
        return selectedCreatures.isEmpty() && selectedGameObjects.isEmpty();
    }

    /*private void delete() {
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
    }*/

    private void undo() {
        if (ChangeManager.firstChange != null) {
            if (!ChangeManager.wasMoved()) {
                if(ChangeManager.wasDeleted()){
                    for (GameObject o: ChangeManager.getFirst().object) game.getLevel().addGameObject(o);
                    for(Creature c: ChangeManager.getFirst().creature)game.getLevel().addCreature(c);
                    ChangeManager.pop();
                } else{
                    for(GameObject o: ChangeManager.getFirst().object) game.getLevel().removeObject(o);
                    for(Creature c: ChangeManager.getFirst().creature)game.getLevel().removeCreature(c);
                    ChangeManager.pop();
                }
            } else{
                GameObject[] objects = ChangeManager.getFirst().object;
                Creature[] creatures = ChangeManager.getFirst().creature;
                for(int i = 0; i < objects.length; i++){
                    objects[i].move(ChangeManager.getFirst().dox[i], ChangeManager.getFirst().doy[i]);
                }
                for(int i = 0; i < creatures.length; i++){
                    creatures[i].move(ChangeManager.getFirst().dcx[i], ChangeManager.getFirst().dcy[i]);
                }
                ChangeManager.pop();
            }
        }
        game.getkl().setControlMasked(KeyEvent.VK_Z, false);
    }

    public void changeTool(int tool){
        switch (this.tool){
            case SELECT_TOOL_:
                ml.setSelectedCreatures(new Creature[0]);
                ml.setSelectedObjects(new GameObject[0]);
                ml.setHasSelection(false);
                ml.draggingSelection = false;
                break;
        }
        this.tool = tool;
    }

    private void createPlatform() {
        tempPlatform = new Platform(tempRect.x + game.getCamera().getX(), tempRect.y, tempRect.width, tempRect.height, false);
        if(justChanged == null) justChanged = tempPlatform;
        else {
            tempPlatform.next = justChanged;
            justChanged = tempPlatform;
        }
        game.getLevel().addGameObject((Platform) justChanged);
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
        if(tempRect != null && tool == SELECT_TOOL_){
            g.setColor(Color.RED);
            g.drawRect(tempRect.x, tempRect.y, tempRect.width, tempRect.height);
        }

        if(!selectionEmpty()){
            g.setColor(Color.BLUE.brighter());

            for(GameObject o: selectedGameObjects){
                Rectangle r = o.getHitBox();
                g.drawRect(r.x, r.y, r.width, r.height);
            }
            for(Creature c: selectedCreatures){
                Rectangle r = c.getHitBox();
                g.drawRect(r.x, r.y, r.width, r.height);
            }
        }

        if (tempRect != null && tool != SELECT_TOOL_) {
            g.fillRect(tempRect.x, tempRect.y, tempRect.width, tempRect.height);
        }
        g.setColor(Color.WHITE);
        g.drawString("Edit mode - Undo: ctrl z  ||  Place platform: click and drag", 0,
                (int) g.getFontMetrics().getStringBounds("Edit mode - Undo: ctrl z  ||  Place platform: click and drag", g).getHeight());

    }

    @Override
    public void init() {
        game.getDisplay().levelEditorUI(game);
        game.getml().setInEdit(true);
        this.workingLevel = new Level();
        game.setLevel(workingLevel);
        game.setCamera(new Camera(workingLevel.getPlayer(), game.getWidth(), game.getHeight()));
        ml = game.getml();
        ml.dragTangle.width = 0;
        ml.rectReady = false;
    }

    @Override
    public void exit() {
        game.getml().setInEdit(false);
        game.getDisplay().exitLevelEditorUi();
    }
}
