package States.GameStates.LevelEditor;

import Actors.Creature;
import Actors.Player;
import Gfx.Camera;
import LevelManagment.Level;
import LevelManagment.LevelLoader;
import Listeners.MouseListener;
import Objects.GameObject;
import Objects.Platform;
import Objects.SolidBlock;
import States.GameStates.PauseMenuState;
import States.GameStates.State;
import Utilities.Util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import static Listeners.ButtonListener.*;

public class LevelEditState extends State {

    private static final int GRID_HEIGHT = 32;
    private static final int GRID_WIDTH = 32;
    private MouseListener ml;
    private Rectangle tempRect;
    private Level workingLevel;

    private int[] beforeMoveObjects;
    private int[] beforeMoveCreatures;
    private ArrayList<GameObject> selectedGameObjects = new ArrayList<>();
    private ArrayList<Creature> selectedCreatures = new ArrayList<>();

    private String toPlace = "";
    private Color toPlaceColor;

    private boolean gridDisplayed;
    private boolean snapTo;

    private int tool = SELECT_TOOL_;
    private String editItemSelection = " ";

    @Override
    public void update() {
        if (checkKeyButtons()) return;
        checkToolSelection();
        checkEditItemChange();
        checkPlaceble();
        checkArrowKeys();

        if(ml.getRClick() != null){
            handleRightClick();
            ml.setRClick(null);
        }

        if(ml.dragTangle.width == 0){
            return;
        }

        tempRect = ml.getDragTangle();
        if (ml.rectReady) {
            switch (tool){
                case SELECT_TOOL_:
                    handleSelection();
                    break;
                case PLACING:
                    if(editItemSelection.equals("objects"))
                        placeSelectedObject();
                    else if(editItemSelection.equals("creatures"))
                        placeSelectedCreature();

                    System.out.println(toPlace);
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

    }

    private void checkArrowKeys() {
        if(selectionEmpty()){
            if(game.getkl().getKeysPressed()[KeyEvent.VK_UP]){
                game.getCamera().move(0, 20);
                game.getkl().setKey(KeyEvent.VK_UP, false);
            }
            if(game.getkl().getKeysPressed()[KeyEvent.VK_RIGHT]){
                game.getCamera().move(20, 0);                game.getkl().setKey(KeyEvent.VK_RIGHT, false);
            }
            if(game.getkl().getKeysPressed()[KeyEvent.VK_DOWN]){
                game.getCamera().move(0, -20);                game.getkl().setKey(KeyEvent.VK_DOWN, false);
            }
            if(game.getkl().getKeysPressed()[KeyEvent.VK_LEFT]){
                game.getCamera().move(-20, 0);                game.getkl().setKey(KeyEvent.VK_LEFT, false);
            }
        }
        if (!snapTo) {
            if(game.getkl().getKeysPressed()[KeyEvent.VK_UP]){
                for(GameObject o :this.selectedGameObjects) o.move(0, -1);
                game.getkl().setKey(KeyEvent.VK_UP, false);
            }
            if(game.getkl().getKeysPressed()[KeyEvent.VK_RIGHT]){
                for(GameObject o :this.selectedGameObjects) o.move(1, 0);
                game.getkl().setKey(KeyEvent.VK_RIGHT, false);
            }
            if(game.getkl().getKeysPressed()[KeyEvent.VK_DOWN]){
                for(GameObject o :this.selectedGameObjects) o.move(0, 1);
                game.getkl().setKey(KeyEvent.VK_DOWN, false);
            }
            if(game.getkl().getKeysPressed()[KeyEvent.VK_LEFT]){
                for(GameObject o :this.selectedGameObjects) o.move(-1,0);
                game.getkl().setKey(KeyEvent.VK_LEFT, false);
            }
        } else{
            if(game.getkl().getKeysPressed()[KeyEvent.VK_UP]){
                for(GameObject o :this.selectedGameObjects) o.move(0, snapToGridY(o.getY(), -1));
                game.getkl().setKey(KeyEvent.VK_UP, false);
            }
            if(game.getkl().getKeysPressed()[KeyEvent.VK_RIGHT]){
                for(GameObject o :this.selectedGameObjects) o.move(snapToGridX(o.getX(), 1), 0);
                game.getkl().setKey(KeyEvent.VK_RIGHT, false);
            }
            if(game.getkl().getKeysPressed()[KeyEvent.VK_DOWN]){
                for(GameObject o :this.selectedGameObjects) o.move(0, snapToGridY(o.getY(), 1));
                game.getkl().setKey(KeyEvent.VK_DOWN, false);
            }
            if(game.getkl().getKeysPressed()[KeyEvent.VK_LEFT]){
                for(GameObject o :this.selectedGameObjects) o.move(snapToGridX(o.getX(), -1),0);
                game.getkl().setKey(KeyEvent.VK_LEFT, false);
            }

        }
    }

    private int snapToGridY(int y, int i) {
        return (y%GRID_HEIGHT == 0) ? i*GRID_HEIGHT : (i > 0) ? GRID_HEIGHT - y%GRID_HEIGHT : -(y%GRID_HEIGHT);
    }
    private int snapToGridX(int x, int i) {
        return (x%GRID_WIDTH == 0) ? i*GRID_WIDTH : (i > 0) ? GRID_WIDTH - x%GRID_WIDTH : -(x%GRID_WIDTH);
    }

    private void handleRightClick() {
        switch (this.tool){
            case SELECT_TOOL_:
                if(!Util.rectIntersectsWithObjectArray(ml.getRClick(), this.selectedGameObjects) &&
                        !Util.rectIntersectsWithCreatureArray(ml.getRClick(), this.selectedCreatures)){
                    clearSelection();
                }
                break;
            case DELETE_TOOL_:
                ml.dragging = false;
                ml.dragTangle.width = 0;
                break;
            case PLACING:
                ml.dragging = false;
                ml.dragTangle.width = 0;
                break;
        }
    }

    private void placeSelectedCreature() {
        switch (this.toPlace){
            case "Player":
                Player player = new Player(tempRect.x + game.getCamera().getX(), tempRect.y + game.getCamera().getY());
                game.getLevel().setPlayer(player);
        }
    }

    private void placeSelectedObject() {
        switch (this.toPlace){
            case "Platform":
                Platform tempPlatform = new Platform(tempRect.x + game.getCamera().getX(), tempRect.y - game.getCamera().getY(), tempRect.width, tempRect.height);
                System.out.println(game.getCamera().getY());
                ChangeManager.push(1, 0, false);
                ChangeManager.push(tempPlatform);
                game.getLevel().addGameObject(tempPlatform);
                tempRect = null;
                ml.dragTangle.width = 0;
                ml.rectReady = false;
                break;
            case "SolidBlock":
                SolidBlock tempBlock = new SolidBlock(tempRect.x + game.getCamera().getX(), tempRect.y - game.getCamera().getY(), tempRect.width, tempRect.height);
                ChangeManager.push(1, 0, false);
                ChangeManager.push(tempBlock);
                game.getLevel().addGameObject(tempBlock);
                tempRect = null;
                ml.dragTangle.width = 0;
                ml.rectReady = false;
        }
    }

    private void checkPlaceble() {
        if(!game.getbl().getToPlace().equals("") && !this.toPlace.equals(game.getbl().getToPlace())){
            System.out.println(this.toPlace + " " + game.getbl().getToPlace());
            switch (this.editItemSelection){
                case "objects":
                    selectObjectToPlace();
                    break;
                case "creatures":
                    selectCreatureToPlace();
                    break;
            }
            if (this.tool != PLACING) {
                this.changeTool(PLACING);
            }
            if(!this.toPlace.equals("Player")){
                ml.placingPlayer = false;
            } else{
                ml.placingPlayer = true;
            }
        }
    }

    private void selectCreatureToPlace() {
        switch (game.getbl().getToPlace()){
            case "Player":
                this.toPlace = game.getbl().getToPlace();
                this.toPlaceColor = Player.getDefaultColor();
        }
    }

    private void selectObjectToPlace() {
        switch (game.getbl().getToPlace()){
            case "Platform":
                this.toPlace = game.getbl().getToPlace();
                this.toPlaceColor = Platform.getColor();
                System.out.println(toPlace);
                break;
            case "SolidBlock":
                this.toPlace = game.getbl().getToPlace();
                this.toPlaceColor = SolidBlock.getColor();
                break;

        }
    }

    private void checkEditItemChange() {
        if(!this.editItemSelection.equals(game.getbl().getSelection())){
            this.editItemSelection = game.getbl().getSelection();

            switch (this.editItemSelection){
                case "objects":
                    game.getDisplay().displayEditItems(game);
                    break;
                case "creatures":
                    game.getDisplay().displayEditItems(game);
                    break;
            }
        }
    }

    private void handleDeletion() {
        int a = this.selectedGameObjects.size();
        int b = this.selectedCreatures.size();
        ChangeManager.isPushing = true;
        ChangeManager.push(a, b, true);

        for(GameObject o: selectedGameObjects){
            ChangeManager.push(o);
            game.getLevel().removeObject(o);
        }
        for(Creature c: selectedCreatures){
            ChangeManager.push(c);
            game.getLevel().removeCreature(c);
        }
        clearSelection();
        game.getkl().setKey(KeyEvent.VK_DELETE, false);
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
        if(game.getkl().getKeysPressed()[KeyEvent.VK_DELETE]){
            handleDeletion();
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
            System.out.println("DeleteTool");
        }
        if(game.getbl().getButtonsPressed()[SHOW_GRID_]){
            if(gridDisplayed) gridDisplayed = false;
            else gridDisplayed = true;
            game.getbl().disableButton(SHOW_GRID_);
        }
        if(game.getbl().getButtonsPressed()[SNAP_TO_GRID_]){
            if(snapTo) snapTo = false;
            else snapTo = true;
            game.getbl().disableButton(SNAP_TO_GRID_);
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
        tempRect = ml.getDragTangle();
        System.out.println("rxb: " + tempRect.x + ", ryb: " + tempRect.y);
        tempRect.x += game.getCamera().getX();
        tempRect.y -= game.getCamera().getY();
        System.out.println("rxe: " + tempRect.x + ", rye: " + tempRect.y);
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
            clearSelection();
            this.handleSelection();
        }

        //if single selection open attributes on the left and allow movement when dragging;
    }

    private boolean selectionEmpty() {
        return selectedCreatures.isEmpty() && selectedGameObjects.isEmpty();
    }

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
                clearSelection();
                break;
            case PLACING:
                game.getDisplay().removeItemAreaSelection();
                this.toPlace = "";
                game.getbl().setToPlace("");
                ml.placingPlayer = false;
                clearSelection();
                break;
        }

        this.tool = tool;
    }

    private void clearSelection() {
        ml.setSelectedCreatures(new Creature[0]);
        ml.setSelectedObjects(new GameObject[0]);
        this.selectedGameObjects.removeAll(selectedGameObjects);
        this.selectedCreatures.removeAll(selectedCreatures);
        ml.setHasSelection(false);
        ml.draggingSelection = false;
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
            JOptionPane.showMessageDialog(game.getDisplay(), "Level Saved");
        } else {
            workingLevel.saveLevel();
            JOptionPane.showMessageDialog(game.getDisplay(), "Level Saved");
        }
    }

    private void enterMenu()    {
        game.getkl().setKey(KeyEvent.VK_ESCAPE ,false);

        State.push(new PauseMenuState(true));
        State.currentState.init();
        State.currentState.update();
        return;
    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor((game.getLevel().getDarker()) ? Color.DARK_GRAY.darker() : Color.DARK_GRAY);
        g.fillRect(0, 0, game.getWidth() + 100, game.getHeight() +100);
        game.getLevel().draw(g, game.getCamera());

        if(gridDisplayed){
            g.setColor(Color.BLACK);
            for(int y = 0; y < game.getDisplay().getHeight(); y += GRID_HEIGHT){
                for(int x = 0; x < game.getDisplay().getWidth(); x += GRID_WIDTH){
                    g.drawRect(x, y, GRID_WIDTH, GRID_HEIGHT);
                }
            }
        }
        if (tempRect != null) {
            if(tool == SELECT_TOOL_){
                g.setColor(Color.RED);
                g.drawRect(tempRect.x, tempRect.y, tempRect.width, tempRect.height);
            }else if (tool == DELETE_TOOL_) {
                g.fillRect(tempRect.x, tempRect.y, tempRect.width, tempRect.height);
            } else if(tool == PLACING){
                g.setColor(toPlaceColor);
                g.fillRect(tempRect.x, tempRect.y, tempRect.width, tempRect.height);
            }
        }

        if(!selectionEmpty()){
            g.setColor(Color.BLUE.brighter());

            for(GameObject o: selectedGameObjects){
                Rectangle r = o.getHitBox();
                g.drawRect(r.x - game.getCamera().getX(), r.y + game.getCamera().getY(), r.width, r.height);
            }
            for(Creature c: selectedCreatures){
                Rectangle r = c.getHitBox();
                g.drawRect(r.x - game.getCamera().getX(), r.y + game.getCamera().getX(), r.width, r.height);
            }
        }

        g.setColor(Color.WHITE);
        g.drawString("Edit mode - Undo: ctrl z  ||  Place platform: click and drag", 0,
                (int) g.getFontMetrics().getStringBounds("Edit mode - Undo: ctrl z  ||  Place platform: click and drag", g).getHeight());

    }

    @Override
    public void init() {
        game.getDisplay().levelEditorUI(game);
        game.getDisplay().displayEditItems(game);
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
