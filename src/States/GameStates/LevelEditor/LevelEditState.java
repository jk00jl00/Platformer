package States.GameStates.LevelEditor;

import Actors.Creature;
import Actors.Player;
import Gfx.Camera;
import LevelManagment.Level;
import LevelManagment.LevelLoader;
import Listeners.MouseListener;
import Objects.GameObject;
import States.GameStates.PauseMenuState;
import States.GameStates.State;
import Utilities.Util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
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
    private GameObject[] copiedObjects = new GameObject[0];
    private Creature[] copiedCreatures = new Creature[0];
    private ArrayList<GameObject> selectedGameObjects = new ArrayList<>();
    private ArrayList<Creature> selectedCreatures = new ArrayList<>();

    private String toPlace = "";
    private Color toPlaceColor;

    private boolean gridDisplayed;
    private boolean snapTo;

    private boolean atrDisplayed = false;

    private int tool = SELECT_TOOL_;
    private String editItemSelection = " ";

    @Override
    public void update() {
        if (checkKeyButtons()) return;
        checkButtonsPressed();
        checkEditItemChange();
        checkPlaceble();
        checkArrowKeys();

        if(ml.rClicked){
            handleRightClick();
            ml.rClicked = false;
        }
        if(selectionEmpty() && this.tool == SELECT_TOOL_ && ml.rDrag){
            cameraDrag();
        }

        if(ml.dragTangle.width == 0){
            return;
        }

        tempRect = ml.getDragTangle();
        if (ml.rectReady) {
            handleActionWithTool();
        }

        if(!this.selectionEmpty() && ml.draggingSelection){
            drag();
        }


    }

    private void cameraDrag() {
        game.getCamera().move(-ml.getXDrag(), ml.getYDrag());
        ml.setXDrag(0);
        ml.setYDrag(0);
    }

    /**
     * Is called after a user releases left click.
     * Handles the area depending on what tool is selected.
     */
    private void handleActionWithTool() {
        switch (tool){
            case SELECT_TOOL_:
                handleSelection();
                break;
            case PLACING:
                System.out.println(editItemSelection);
                if(editItemSelection.equals("objects"))
                    placeSelectedObject();
                else if(editItemSelection.equals("creatures"))
                    placeSelectedCreature();

                System.out.println(toPlace);
                break;
        }
        System.out.println(ml.dragTangle.width + " " +ml.dragTangle.height);
        //Resets the rectangles.
        ml.dragTangle.width = 0;
        ml.rectReady = false;
        this.tempRect = null;
    }


    /**
     * This method is called early in the Update method.
     * It checks whether arrow keys have benn pressed and then either moves a selection or moves the camera.
     */
    private void checkArrowKeys() {
        int x, y;
        if(selectionEmpty()){
            x = y = 0;
            //If the selection if empty it moves the camera by half the grid height.
            if(game.getkl().getKeysPressed()[KeyEvent.VK_UP]){
                y++;
                game.getkl().setKey(KeyEvent.VK_UP, false);
            }
            if(game.getkl().getKeysPressed()[KeyEvent.VK_RIGHT]){
                x++;
                game.getkl().setKey(KeyEvent.VK_RIGHT, false);
            }
            if(game.getkl().getKeysPressed()[KeyEvent.VK_DOWN]){
                y--;
                game.getkl().setKey(KeyEvent.VK_DOWN, false);
            }
            if(game.getkl().getKeysPressed()[KeyEvent.VK_LEFT]){
                x--;
                game.getkl().setKey(KeyEvent.VK_LEFT, false);
            }
            game.getCamera().move((int)Math.ceil((x * GRID_WIDTH/2) * game.getCamera().getInvertedZoom()) ,
                    (int)Math.ceil((y * GRID_HEIGHT/2) * game.getCamera().getInvertedZoom()));
        }
        if (!snapTo) {
            x = y = 0;
            //Checks the direction the of the check and sets up a Change object to store the movement if no Change object exists.
            if(game.getkl().getKeysPressed()[KeyEvent.VK_UP]){
                y--;
                game.getkl().setKey(KeyEvent.VK_UP, false);
            }
            if(game.getkl().getKeysPressed()[KeyEvent.VK_RIGHT]){
                x++;
                game.getkl().setKey(KeyEvent.VK_RIGHT, false);
            }
            if(game.getkl().getKeysPressed()[KeyEvent.VK_DOWN]){
                y++;
                game.getkl().setKey(KeyEvent.VK_DOWN, false);
            }
            if(game.getkl().getKeysPressed()[KeyEvent.VK_LEFT]){
                x--;
                game.getkl().setKey(KeyEvent.VK_LEFT, false);
            }
            if(x != 0 || y != 0){
                if(!ChangeManager.isPushing){
                    ChangeManager.isPushing = true;
                    ChangeManager.push(selectedGameObjects.size(), selectedCreatures.size());
                }
                ChangeManager.firstObjectMove = true;
                ChangeManager.firstCreatureMove = true;
                for(GameObject o :this.selectedGameObjects){
                    //Moves every GameObject one pixel on the screen
                    o.move((int)Math.ceil(x * game.getCamera().getInvertedZoom()),(int)Math.ceil(y * game.getCamera().getInvertedZoom()));
                    //Adds the movement to the current change.
                    ChangeManager.moveStep(o,(int)Math.ceil(-x * game.getCamera().getInvertedZoom()),(int)Math.ceil(-y * game.getCamera().getInvertedZoom()));
                }
                for(Creature c: this.selectedCreatures){
                    //Moves every creature one pixel on the screen.
                    c.move((int)Math.ceil(x * game.getCamera().getInvertedZoom()),(int)Math.ceil(y * game.getCamera().getInvertedZoom()));
                    //Adds the movement to the current change.
                    ChangeManager.moveStep(c,(int)Math.ceil(-x * game.getCamera().getInvertedZoom()),(int)Math.ceil(-y * game.getCamera().getInvertedZoom()));
                }
                if(selectedCreatures.size() + selectedGameObjects.size() == 1){
                    if(selectedGameObjects.size() > 0) game.getDisplay().updateAtrDisplay(selectedGameObjects.get(0));
                    else game.getDisplay().updateAtrDisplay(selectedCreatures.get(0));
                }
            }
        } else{
            int dx = 0, dy = 0;
            x = y = 0;
            if(game.getkl().getKeysPressed()[KeyEvent.VK_UP]){
                dy = -1;
                game.getkl().setKey(KeyEvent.VK_UP, false);
            }
            if(game.getkl().getKeysPressed()[KeyEvent.VK_RIGHT]){
                dx = 1;
                game.getkl().setKey(KeyEvent.VK_RIGHT, false);
            }
            if(game.getkl().getKeysPressed()[KeyEvent.VK_DOWN]){
                dy = 1;
                game.getkl().setKey(KeyEvent.VK_DOWN, false);
            }
            if(game.getkl().getKeysPressed()[KeyEvent.VK_LEFT]){
                dx = -1;
                game.getkl().setKey(KeyEvent.VK_LEFT, false);
            }
            if(dx != 0|| dy != 0){
                if(!ChangeManager.isPushing){
                    ChangeManager.isPushing = true;
                    ChangeManager.push(selectedGameObjects.size(), selectedCreatures.size());
                }
                ChangeManager.firstObjectMove = true;
                ChangeManager.firstCreatureMove = true;
                for(GameObject o :this.selectedGameObjects){
                    if(dy != 0) y = snapToGridY(o.getY(), dy);
                    if(dx != 0) x = snapToGridX(o.getX(), dx);
                    o.move(x, y);
                    ChangeManager.moveStep(o, -x, -y);
                }
                for(Creature c :this.selectedCreatures){

                    if(dy != 0) y = snapToGridY(c.getY(), dy);
                    if(dx != 0) x = snapToGridX(c.getX(), dx);
                    c.move(x, y);
                    ChangeManager.moveStep(c, -x, -y);
                }
                if(selectedCreatures.size() + selectedGameObjects.size() == 1){
                    if(selectedGameObjects.size() > 0) game.getDisplay().updateAtrDisplay(selectedGameObjects.get(0));
                    else game.getDisplay().updateAtrDisplay(selectedCreatures.get(0));
                }
            }
        }
    }
    /**
     * A method used to get the required pixels an object need to move in a specified direction in order to land on the grid's y axis.
     * @param y Specifies where the object to be moved lies on the y axis.
     * @param i Specifies whether to move the object up or down, negative being up.
     * @return Returns the value in pixels for the object to be moved in order to land on the grid's y axis.
     */
    private int snapToGridY(int y, int i) {
        return (y%GRID_HEIGHT == 0) ? i*GRID_HEIGHT : (i > 0) ? GRID_HEIGHT - y%GRID_HEIGHT : -(y%GRID_HEIGHT);
    }

    /**
     * A method used to get the required pixels an object need to move in a specified direction in order to land on the grid's x axis.
     * @param x Specifies where the object to be moved lies on the x axis
     * @param i Specifies whether to move the object left or right, negative being left.
     * @return Returns the value in pixels for the object to be moved in order to land on the grid's x axis.
     */
    private int snapToGridX(int x, int i) {
        //Returns gridWith times the direction if the object lies on the grid, otherwise it returns the distance to the grid.
        return (x%GRID_WIDTH == 0) ? i*GRID_WIDTH : (i > 0) ? GRID_WIDTH - x%GRID_WIDTH : -(x%GRID_WIDTH);
    }

    /**
     * Is called when the user has right clicked.
     * The click is handled dependent on which tool is currently selected.
     */
    private void handleRightClick() {
        switch (this.tool){
            //Clears the current selection if the right click does not intersect with a selected object.
            case SELECT_TOOL_:
                if(!Util.rectIntersectsWithObjectArray(ml.getRClick(), this.selectedGameObjects) &&
                        !Util.rectIntersectsWithCreatureArray(ml.getRClick(), this.selectedCreatures)){
                    clearSelection();
                }
                break;
            //Cancels the placement.
            case PLACING:
                ml.dragging = false;
                ml.dragTangle.width = 0;
                changeTool(SELECT_TOOL_);
                break;
        }
    }
    /**
     * Is called after a location has been specified using the Place tool.
     * Places the currently selected creature at the specified location.
     */
    //TODO - add a error message when a Creature was not added.
    private void placeSelectedCreature() {
        System.out.println(1);
        //If it's the player character it replaces the player instead of placing a new one.
        if(toPlace.equals("Player")) {
            Player player = new Player((int)Math.ceil((tempRect.x* game.getCamera().getInvertedZoom()) + game.getCamera().getX()),
                    (int)Math.ceil((tempRect.y + game.getCamera().getY()) * game.getCamera().getInvertedZoom()));
            game.getLevel().setPlayer(player);
        } else{
            //Stores a generic Creature to replace with the placement.
            Creature c = new Creature(0, 0);
            try {
                //Gets the class of the selected object from the Actors package.
                Class<?> clazz = Class.forName("Actors." + toPlace);
                //Gets the constructor from the class.
                Constructor<?> constr = clazz.getConstructor(int.class, int.class);
                //Creates a new Creature using the gathered constructor.
                c = (Creature) constr.newInstance(new Object[]{
                        (int)Math.ceil((tempRect.x* game.getCamera().getInvertedZoom()) + game.getCamera().getX()),
                        (int)Math.ceil((tempRect.y* game.getCamera().getInvertedZoom()) - game.getCamera().getY()),
                });
            } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
                e.printStackTrace();
            }
            //Makes sure a Creature was created and the generic Creature was replaced.
            if(c.getClass() != Creature.class){
                //Creates a Change object that stores a Creature addition wo that it can be undone at a later date.
                ChangeManager.push(0, 1, false);
                ChangeManager.push(c);
                //Add the creature to the level.
                game.getLevel().addCreature(c);
            }
        }
        //Resets all rectangles.
        tempRect = null;
        ml.dragTangle.width = 0;
        ml.rectReady = false;
    }
    /**
     * Is called after a location has been specified using the Place tool.
     * Places the currently selected gameObject at the specified location.
     */
    private void placeSelectedObject() {
        //Functions the same way as the placeSelectedCreature method.
        GameObject o = new GameObject(0, 0, 0, 0);
        try {
            Class<?> clazz = Class.forName("Objects." + toPlace);
            Constructor<?> constr = clazz.getConstructor(int.class, int.class, int.class, int.class);
            o = (GameObject) constr.newInstance(new Object[]{
                    (int)Math.ceil((tempRect.x* game.getCamera().getInvertedZoom()) + game.getCamera().getX()),
                    (int)Math.ceil((tempRect.y* game.getCamera().getInvertedZoom()) - game.getCamera().getY()),
                    (int)Math.ceil(tempRect.width * game.getCamera().getInvertedZoom()),
                    (int)Math.ceil(tempRect.height * game.getCamera().getInvertedZoom())
            });
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
            e.printStackTrace();
        }
        if(o.getClass() != GameObject.class){
            ChangeManager.isPushing = true;
            ChangeManager.push(1, 0, false);
            ChangeManager.push(o);
            game.getLevel().addGameObject(o);
            tempRect = null;
            ml.dragTangle.width = 0;
            ml.rectReady = false;
            ChangeManager.isPushing = false;
        } else System.out.println("Object is null");
    }
    /**
     * This method is called in the update method.
     * Checks if the user has clicked a placeble object within in the editors tool panel and then switches to the desired object.
     */
    private void checkPlaceble() {
        if(!game.getbl().getToPlace().equals("") && !this.toPlace.equals(game.getbl().getToPlace())){
            switch (this.editItemSelection){
                case "objects":
                    selectObjectToPlace();
                    break;
                case "creatures":
                    selectCreatureToPlace();
                    break;
            }
            //Makes sure the tool is correct.
            if (this.tool != PLACING) {
                this.changeTool(PLACING);
            }
            if(this.editItemSelection.equals("creatures") && this.tool == PLACING) ml.placingCreature = true;
            else ml.placingCreature = false;
        }
    }

    /**
     * This method is called when the player has clicked a creature in the tool panel and sets up the placement.
     */
    private void selectCreatureToPlace() {
        this.toPlace = game.getbl().getToPlace();
        try {
            //Gets the color of the object by getting the class and method and then invoking it.
            //This is done in order to be able to easily add new objects without adding to this code.
            Class<?> clazz = Class.forName("Actors." + toPlace);
            this.toPlaceColor =(Color) clazz.getMethod("getDefaultColor").invoke(new Object[0]);
            //Gets the dimensions of the selected creatures class
            ml.setCreatureDims((int)clazz.getMethod("getDefaultWidth").invoke(new Object[0]),(int)clazz.getMethod("getDefaultHeight").invoke(new Object[0]));
        } catch (IllegalAccessException | ClassNotFoundException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
    /**
     * This method is called when the player has clicked a gameObject in the tool panel and sets up the placement.
     */
    private void selectObjectToPlace() {
        this.toPlace = game.getbl().getToPlace();
        try {
            //Gets the color of the object by getting the class and method and then invoking it.
            //This is done in order to be able to easily add new objects without adding to this code.
            this.toPlaceColor =(Color) Class.forName("Objects." + toPlace).getMethod("getDefaultColor").invoke(new Object[0]);
        } catch (IllegalAccessException | ClassNotFoundException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method is called early in the update method in order to check if the user and changed their selection.
     */
    private void checkEditItemChange() {
        if(!this.editItemSelection.equals(game.getbl().getSelection())){
            this.editItemSelection = game.getbl().getSelection();
            switch (this.editItemSelection){
                case "objects":
                    //Changes the display to display creatures or objects in the itemArea.
                    game.getDisplay().displayEditItems(game);
                    break;
                case "creatures":
                    game.getDisplay().displayEditItems(game);
                    break;
            }
        }
    }

    /**
     * Is called when the user has pressed the delete button.
     * Deletes the objects in the selection.
     */
    private void handleDeletion() {
        int a = this.selectedGameObjects.size();
        int b = this.selectedCreatures.size();
        ChangeManager.isPushing = true;
        //Creates a Change objects with the required array sizes for the selected GameObjects and Creatures and flags the Change object as a deletion.
        ChangeManager.push(a, b, true);

        for(GameObject o: selectedGameObjects){
            //Removes the GameObjects from the level and adds them to the Change object.
            ChangeManager.push(o);
            game.getLevel().removeObject(o);
        }
        for(Creature c: selectedCreatures){
            //Removes the Creatures from the level and adds them to the Change object.
            ChangeManager.push(c);
            game.getLevel().removeCreature(c);
        }
        ChangeManager.isPushing = false;
        clearSelection();
        game.getkl().setKey(KeyEvent.VK_DELETE, false);
    }

    /**
     * Id called first in the update method.
     * Checks if an important button has been pressed invokes the correct action.
     * @return if a button had been pressed.
     */
    private boolean checkKeyButtons() {
        if(game.getkl().getKeysPressed()[KeyEvent.VK_ESCAPE]){
            enterMenu();
            return true;
        }
        if(game.getkl().getKeysPressed()[KeyEvent.VK_DELETE]){
            handleDeletion();
        }
        if(game.getkl().getControlMasked()[KeyEvent.VK_MINUS]){
            game.getkl().setControlMasked(KeyEvent.VK_MINUS, false);
            game.getCamera().zoomOut(1);
        }
        if(game.getkl().getControlMasked()[KeyEvent.VK_PLUS]){
            game.getkl().setControlMasked(KeyEvent.VK_PLUS, false);
            game.getCamera().zoomIn(1);
        }
        return false;
    }

    /**
     * Is called in the update method in order to check which buttons have been pressed in the editMenu of the editor.
     */
    private void checkButtonsPressed() {
        //Checks if the select tool has been selected and switches to it.
        if(game.getbl().getButtonsPressed()[SELECT_TOOL_]){
            game.getbl().disableButton(SELECT_TOOL_);
            changeTool(SELECT_TOOL_);
            System.out.println("SelectTool");
        }
        //Undoes the latest change.
        if(game.getbl().getButtonsPressed()[UNDO]){
            game.getbl().disableButton(UNDO);
            undo();
            System.out.println("Undone");
        }
        if(game.getbl().getButtonsPressed()[REDO]){
            game.getbl().disableButton(REDO);
            redo();
        }
        if(game.getbl().getButtonsPressed()[SAVE]){
            game.getbl().disableButton(SAVE);
            saveLevel();
            System.out.println("Save");
        }
        if(game.getbl().getButtonsPressed()[LOAD]){
            game.getbl().disableButton(LOAD);
            loadLevel();
            System.out.println("Load");
        }
        if(game.getbl().getButtonsPressed()[COPY]){
            game.getbl().disableButton(COPY);
            copy();
            System.out.println("Copy");
        }
        if(game.getbl().getButtonsPressed()[PASTE]){
            game.getbl().disableButton(PASTE);
            paste();
            System.out.println("Paste");
        }
        //Toggles the grid.
        if(game.getbl().getButtonsPressed()[SHOW_GRID_]){
            if(gridDisplayed) gridDisplayed = false;
            else gridDisplayed = true;
            game.getbl().disableButton(SHOW_GRID_);
        }
        //Toggles snapping to the grid.
        if(game.getbl().getButtonsPressed()[SNAP_TO_GRID_]){
            if(snapTo) snapTo = false;
            else snapTo = true;
            game.getbl().disableButton(SNAP_TO_GRID_);
        }
        //Changes an attribute on a selected object
        if(game.getbl().intAtrChanged){
            if(selectionEmpty() || this.selectedCreatures.size() + this.selectedGameObjects.size() > 1) {
                game.getbl().intAtrChanged = false;
                return;
            }
            if(selectedGameObjects.size() > 0){
                ChangeManager.isPushing = true;
                ChangeManager.push(1, 0, game.getbl().getAtrName(), selectedGameObjects.get(0).getAttributes().get(game.getbl().getAtrName()));
                selectedGameObjects.get(0).changeAttribute(game.getbl().getAtrName(), game.getbl().getAtrChange());
                ChangeManager.push(selectedGameObjects.get(0));
                ChangeManager.isPushing = false;
                game.getbl().intAtrChanged = false;
            } else{
                ChangeManager.isPushing = true;
                ChangeManager.push(0, 1, game.getbl().getAtrName(), selectedGameObjects.get(0).getAttributes().get(game.getbl().getAtrName()));
                selectedCreatures.get(0).changeAttribute(game.getbl().getAtrName(), game.getbl().getAtrChange());
                ChangeManager.push(selectedCreatures.get(0));
                ChangeManager.isPushing = false;
                game.getbl().intAtrChanged = false;
            }
        }
    }

    private void paste() {
        if(copiedObjects.length + copiedCreatures.length < 1) return;
        clearSelection();
        ChangeManager.push(copiedObjects.length, copiedCreatures.length, false);
        ChangeManager.isPushing = true;
        for(GameObject o: copiedObjects){
            workingLevel.addGameObject(o);
            selectedGameObjects.add(o);
            ChangeManager.push(o);
        }
        for(Creature c: copiedCreatures){
            workingLevel.addCreature(c);
            selectedCreatures.add(c);
            ChangeManager.push(c);
        }
        ml.setSelectedObjects(selectedGameObjects.toArray(new GameObject[selectedGameObjects.size()]));
        ml.setSelectedCreatures(selectedCreatures.toArray(new Creature[selectedCreatures.size()]));
        ml.setHasSelection(true);
        ChangeManager.isPushing = false;
        copy();
    }

    private void copy() {
        if(selectionEmpty()) return;
        copiedObjects = new GameObject[selectedGameObjects.size()];
        copiedCreatures = new Creature[selectedCreatures.size()];

        for(int i = 0; i < selectedGameObjects.size(); i++){
            copiedObjects[i] = selectedGameObjects.get(i).copyOf();
            copiedObjects[i].move(15, 15);
        }
        for(int i = 0; i < selectedCreatures.size(); i++){
            if(selectedCreatures.get(i).getType().equals("Player")) continue;
            copiedCreatures[i] = selectedCreatures.get(i).copyOf();
            copiedCreatures[i].move(15, 15);
        }
    }

    /**
     * Called when the user is dragging a selection and moves the objects.
     */
    private void drag() {
        for(GameObject o: selectedGameObjects){
            o.move(ml.getXDrag(), ml.getYDrag());
            if(atrDisplayed) game.getDisplay().updateAtrDisplay(o);
        }
        for(Creature c: selectedCreatures){
            c.move(ml.getXDrag(), ml.getYDrag());
            if(atrDisplayed) game.getDisplay().updateAtrDisplay(c);
        }
        //Resets the drag so that the selection doesn't "slide".
        ml.setXDrag(0);
        ml.setYDrag(0);
    }

    /**
     * This method is called when the user has released left click whilst using the selection tool.
     */
    private void handleSelection() {
        //Gets the rectangle and adds the camera offsets.
        tempRect = ml.getDragTangle();
        tempRect.x = (int)Math.ceil((tempRect.x* game.getCamera().getInvertedZoom()) + game.getCamera().getX());
        tempRect.y = (int)Math.ceil((tempRect.y* game.getCamera().getInvertedZoom()) - game.getCamera().getY());
        tempRect.width = (int)Math.ceil(tempRect.width * game.getCamera().getInvertedZoom());
        tempRect.height = (int)Math.ceil(tempRect.height * game.getCamera().getInvertedZoom());
        //Checks if the selection is empty.
        if (selectionEmpty()) {
            ChangeManager.isPushing = false;
            //Checks if you clicked once or dragged
            if(tempRect.width > 1){
                //Adds any objects that intersect with the selection rectangle.
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
                if(!selectionEmpty() && selectedGameObjects.size() + selectedCreatures.size() == 1){
                    if(selectedGameObjects.size() > 0){
                        game.getDisplay().displayAttributes(selectedGameObjects.get(0), game);
                        atrDisplayed = true;
                    } else {
                        game.getDisplay().displayAttributes(selectedCreatures.get(0), game);
                        atrDisplayed = true;
                    }
                }

                //Sets up the mouseListener to know that a selection has been made in order to check if the user is dragging the selection.
                ml.setHasSelection(true);
                ml.setSelectedObjects(this.selectedGameObjects.toArray(new GameObject[this.selectedGameObjects.size()]));
                ml.setSelectedCreatures(this.selectedCreatures.toArray(new Creature[this.selectedCreatures.size()]));
            } else {
                //Same as above but will only select the first object found.
                boolean object = false;
                for (GameObject o : game.getLevel().getObjects()) {
                    if (o.getHitBox().intersects(tempRect)) {
                        selectedGameObjects.add(o);
                        object = true;
                        break;
                    }
                }
                if(selectionEmpty())
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

                if (!selectionEmpty()) {
                    if (object) {
                        game.getDisplay().displayAttributes(selectedGameObjects.get(0), game); //Checks if the selection is an object else it gets the creature.
                        atrDisplayed = true;
                    }
                    else {
                        game.getDisplay().displayAttributes(selectedCreatures.get(0), game);
                        atrDisplayed = true;
                    }
                }
            }
            if(!selectionEmpty()){
                //Ones the selection is made a copy of the selected creature's coordinates are made in order to add a undo to movement.
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
            //If the selection has been dragged the objects will get added to the ChangeManager.
            ChangeManager.isPushing = true;
            ChangeManager.push(selectedGameObjects.size() , selectedCreatures.size());
            for(int i = 0; i < selectedGameObjects.size(); i++){
                //The objects are pushed along with the difference (old - new) between the old and new positions.
                ChangeManager.push(selectedGameObjects.get(i), (beforeMoveObjects[i * 2] - selectedGameObjects.get(i).getX()),
                        beforeMoveObjects[i * 2 + 1] - selectedGameObjects.get(i).getY());
                System.out.println((beforeMoveObjects[i * 2] - selectedGameObjects.get(i).getX()));
            }
            for(int i = 0; i < selectedCreatures.size(); i++){
                ChangeManager.push(selectedCreatures.get(i), beforeMoveCreatures[i * 2] - selectedCreatures.get(i).getX(),
                        beforeMoveCreatures[i * 2 + 1] - selectedCreatures.get(i).getY());
            }
            ChangeManager.isPushing = false;
            ml.draggingSelection = false;
        } else{
            clearSelection();
            this.handleSelection();
        }

        //if single selection open attributes on the left and allow movement when dragging;
    }

    /**
     * Is called to check if the selection is empty.
     * @return true if both selectedCreatures and selectedGameObjects are empty.
     */
    private boolean selectionEmpty() {
        return selectedCreatures.isEmpty() && selectedGameObjects.isEmpty();
    }

    private void redo() {
        ChangeManager.isPushing = false;

        if(ChangeManager.firstRedo != null){
            switch (ChangeManager.firstRedo.getClass().getName().replaceAll("States.GameStates.LevelEditor.", "")){
                case "AdditionChange":
                    ChangeManager.push(ChangeManager.firstRedo.object.length, ChangeManager.firstRedo.creature.length, true);
                    ChangeManager.isPushing = true;
                    for(GameObject o: ChangeManager.firstRedo.object) {
                        if(selectedGameObjects.contains(o)) selectedGameObjects.remove(o);
                        game.getLevel().removeObject(o);
                        ChangeManager.push(o);
                    }
                    for(Creature c: ChangeManager.firstRedo.creature){
                        if(selectedCreatures.contains(c)) selectedCreatures.remove(c);
                        ChangeManager.push(c);
                        game.getLevel().removeCreature(c);
                    }
                    ChangeManager.isPushing = false;
                    ChangeManager.popRedo();
                    break;
                case "RemovalChange":
                    ChangeManager.push(ChangeManager.firstRedo.object.length, ChangeManager.firstRedo.creature.length, false);
                    ChangeManager.isPushing = true;
                    for (GameObject o: ChangeManager.firstRedo.object){
                        game.getLevel().addGameObject(o);
                        ChangeManager.push(o);
                    }
                    for(Creature c: ChangeManager.firstRedo.creature){
                        game.getLevel().addCreature(c);
                        ChangeManager.push(c);
                    }
                    ChangeManager.isPushing = false;
                    ChangeManager.popRedo();
                    break;
                case "MovementChange":
                    GameObject[] objects = ChangeManager.firstRedo.object;
                    Creature[] creatures = ChangeManager.firstRedo.creature;

                    ChangeManager.push(objects.length, creatures.length);
                    ChangeManager.isPushing = true;

                    for(int i = 0; i < objects.length; i++){
                        objects[i].move(ChangeManager.firstRedo.dox[i], ChangeManager.firstRedo.doy[i]);
                        ChangeManager.push(objects[i], -ChangeManager.firstRedo.dox[i], -ChangeManager.firstRedo.doy[i]);
                    }
                    for(int i = 0; i < creatures.length; i++){
                        creatures[i].move(ChangeManager.firstRedo.dcx[i], ChangeManager.firstRedo.dcy[i]);
                        ChangeManager.push(creatures[i], -ChangeManager.firstRedo.dcx[i], -ChangeManager.firstRedo.dcy[i]);
                    }
                    ChangeManager.isPushing = false;
                    ChangeManager.popRedo();
                    break;
                case "AttributeChange":
                    objects = ChangeManager.firstRedo.object;
                    creatures = ChangeManager.firstRedo.creature;

                    for(int i = 0; i < objects.length; i++){
                        ChangeManager.isPushing = true;
                        ChangeManager.push(1, 0, ChangeManager.firstRedo.name, objects[i].getAttributes().get(ChangeManager.firstRedo.name));
                        ChangeManager.push(objects[i]);
                        objects[i].changeAttribute(ChangeManager.firstRedo.name, ChangeManager.firstRedo.change);
                        game.getDisplay().updateAtrDisplay(objects[i]);
                    }
                    for(int i = 0; i < creatures.length; i++){
                        ChangeManager.isPushing = true;
                        ChangeManager.push(1, 0, ChangeManager.firstRedo.name, objects[i].getAttributes().get(ChangeManager.firstRedo.name));
                        ChangeManager.push(objects[i]);
                        creatures[i].changeAttribute(ChangeManager.firstRedo.name, ChangeManager.firstRedo.change);
                        game.getDisplay().updateAtrDisplay(creatures[i]);
                    }
                    ChangeManager.isPushing = false;
                    ChangeManager.popRedo();
                    break;
            }
        }
    }
    /**
     * This method is called when the user presses the undo button combination (Default ctrl z).
     * Undoes the latest change made by the user.
     */
    private void undo() {
        ChangeManager.isPushing = false;
        //Makes sure that a change exists to be undone.
        if (ChangeManager.firstUndo != null) {
            switch (ChangeManager.firstUndo.getClass().getName().replaceAll("States.GameStates.LevelEditor.", "")){
                case "AdditionChange":
                    //Removes the added objects and pops the Change of the stack.
                    ChangeManager.pushRedo(ChangeManager.getFirst().object.length, ChangeManager.getFirst().creature.length, true);
                    ChangeManager.isPushing = true;
                    for(GameObject o: ChangeManager.getFirst().object) {
                        if(selectedGameObjects.contains(o)) selectedGameObjects.remove(o);
                        game.getLevel().removeObject(o);
                        ChangeManager.pushRedo(o);
                    }
                    for(Creature c: ChangeManager.getFirst().creature){
                        if(selectedCreatures.contains(c)) selectedCreatures.remove(c);
                        game.getLevel().removeCreature(c);
                        ChangeManager.pushRedo(c);
                    }
                    ChangeManager.isPushing = false;
                    ChangeManager.pop();
                    break;
                case "RemovalChange":
                    //Restores the removed objects and pops the Change of the stack.
                    ChangeManager.pushRedo(ChangeManager.getFirst().object.length, ChangeManager.getFirst().creature.length, false);
                    ChangeManager.isPushing = true;
                    for (GameObject o: ChangeManager.getFirst().object){
                        game.getLevel().addGameObject(o);
                        ChangeManager.pushRedo(o);
                    }
                    for(Creature c: ChangeManager.getFirst().creature){
                        game.getLevel().addCreature(c);
                        ChangeManager.pushRedo(c);
                    }
                    ChangeManager.isPushing = false;
                    ChangeManager.pop();
                    break;
                case "MovementChange":
                    //Moves the objects back into their original position and pops the Change of the stack.
                    GameObject[] objects = ChangeManager.getFirst().object;
                    Creature[] creatures = ChangeManager.getFirst().creature;
                    ChangeManager.pushRedo(objects.length, creatures.length);
                    ChangeManager.isPushing = true;
                    for(int i = 0; i < objects.length; i++){
                        objects[i].move(ChangeManager.getFirst().dox[i], ChangeManager.getFirst().doy[i]);
                        ChangeManager.pushRedo(objects[i] , -ChangeManager.getFirst().dox[i], -ChangeManager.getFirst().doy[i]);
                    }
                    for(int i = 0; i < creatures.length; i++){
                        creatures[i].move(ChangeManager.getFirst().dcx[i], ChangeManager.getFirst().dcy[i]);
                        ChangeManager.pushRedo(creatures[i] , -ChangeManager.getFirst().dcx[i], -ChangeManager.getFirst().dcy[i]);
                    }
                    ChangeManager.isPushing = false;
                    ChangeManager.pop();
                    break;
                case "AttributeChange":
                    objects = ChangeManager.getFirst().object;
                    creatures = ChangeManager.getFirst().creature;
                    for(int i = 0; i < objects.length; i++){
                        ChangeManager.isPushing = true;
                        ChangeManager.pushRedo(objects.length, creatures.length, ChangeManager.getFirst().name,
                                objects[i].getAttributes().get(ChangeManager.getFirst().name));
                        ChangeManager.pushRedo(objects[i]);
                        objects[i].changeAttribute(ChangeManager.getFirst().name, ChangeManager.getFirst().change);
                        game.getDisplay().updateAtrDisplay(objects[i]);
                    }
                    for(int i = 0; i < creatures.length; i++){
                        ChangeManager.isPushing = true;
                        ChangeManager.pushRedo(objects.length, creatures.length, ChangeManager.getFirst().name,
                                creatures[i].getAttributes().get(ChangeManager.getFirst().name));
                        creatures[i].changeAttribute(ChangeManager.getFirst().name, ChangeManager.getFirst().change);
                        game.getDisplay().updateAtrDisplay(creatures[i]);
                        ChangeManager.pushRedo(creatures[i]);
                    }
                    ChangeManager.isPushing = false;
                    ChangeManager.pop();
                    break;
            }
        }
        game.getkl().setControlMasked(KeyEvent.VK_Z, false);
    }

    /**
     * This method is called when the user changes the tool selection.
     * Changes the tool and makes sure the old tool is disabled.
     * @param tool The tool to be selected.
     */
    public void changeTool(int tool){
        switch (this.tool){
            case SELECT_TOOL_:
                clearSelection();
                break;
            case PLACING:
                game.getDisplay().removeItemAreaSelection();
                this.toPlace = "";
                ml.placingCreature = false;
                game.getbl().setToPlace("");
                clearSelection();
                break;
        }
        this.tool = tool;
    }

    /**
     * This method is called when the selection needs to be cleared.
     * Clears the current selection.
     */
    private void clearSelection() {
        ml.setSelectedCreatures(new Creature[0]);
        ml.setSelectedObjects(new GameObject[0]);
        this.selectedGameObjects.removeAll(selectedGameObjects);
        this.selectedCreatures.removeAll(selectedCreatures);
        ml.setHasSelection(false);
        ml.draggingSelection = false;
        game.getDisplay().removeAtrDisplay();
        atrDisplayed = false;
    }

    /**
     * This method is called when the user attempts to load a level with ctrl L.
     * Loads a level based on a name from user input.
     * @return true if a level is not found.
     */
    private boolean loadLevel() {
        game.getkl().setControlMasked(KeyEvent.VK_L, false);
        //Shows a dialog box where the user can input a level name.
        String name = JOptionPane.showInputDialog("Enter level name");
        //Attempts to load the the level with the name.
        Level tempLevel = LevelLoader.loadLevel(name);
        //Checks if a level was loaded.
        if(tempLevel == null){
            JOptionPane.showMessageDialog(null, "No level found");
            return true;
        }
        //Sets the current level to be the loaded one.
        workingLevel = LevelLoader.loadLevel(name);
        game.setLevel(this.workingLevel);
        return false;
    }

    /**
     * This method is called when the user attempts to save the level with ctrl S.
     * Save the level to a .lvl file in the levels folder.
     */
    private void saveLevel() {
        game.getkl().setControlMasked(KeyEvent.VK_S, false);
        //Checks if the current working level has a name.
        if(workingLevel.getName() == null){
            String tempName;
            //Attempts to get a name for the level.
            do{
                tempName = JOptionPane.showInputDialog("Enter level name");
            }while (tempName.isEmpty());
            //Sets the name of the level and tries to save it.
            workingLevel.setName(tempName);
            workingLevel.saveLevel();
            JOptionPane.showMessageDialog(game.getDisplay(), "Level Saved");
        } else {
            workingLevel.saveLevel();
            JOptionPane.showMessageDialog(game.getDisplay(), "Level Saved");
        }
    }

    /**
     * This method is called when the user presses the menu key (Default esc).
     * Enters the menu.
     */
    private void enterMenu()    {
        game.getkl().setKey(KeyEvent.VK_ESCAPE ,false);
        //Pushes a new PauseMenuState to the State stack and then updates it.
        State.push(new PauseMenuState(true));
        State.currentState.init();
        State.currentState.update();
        return;
    }

    /**
     * The states draw method called by the drag method in the gameLoop.
     * Draws out the level to the screen along with any selections made.
     * @param g The Graphics2D Object from the Game.draw method.
     */
    @Override
    public void draw(Graphics2D g) {
        //Clears the screen.
        g.setColor((game.getLevel().getDarker()) ? Color.DARK_GRAY.darker() : Color.DARK_GRAY);
        g.fillRect(0, 0, (int)Math.ceil((game.getWidth() + 100) * game.getCamera().getInvertedZoom(true)),
                (int)Math.ceil((game.getHeight() +100) * game.getCamera().getInvertedZoom(true)));
        //Draws the level.
        game.getLevel().draw(g, game.getCamera());
        //Draws the grid if it is displayed.
        if(gridDisplayed && game.getCamera().getZoom() == 1){
            g.setColor(Color.BLACK);
            for(int y = game.getCamera().getY()%GRID_HEIGHT; y < game.getDisplay().getHeight(); y += GRID_HEIGHT){
                for(int x = -(game.getCamera().getX()%GRID_WIDTH); x < game.getDisplay().getWidth(); x += GRID_WIDTH){
                    g.drawRect(x, y, GRID_WIDTH, GRID_HEIGHT);
                }
            }
        }
        //Draws the tempRectangle in different form depending on tool.
        if (tempRect != null) {
            if(tool == SELECT_TOOL_){
                g.setColor(Color.RED);
                g.drawRect(tempRect.x ,tempRect.y,
                        tempRect.width ,tempRect.height);
            } else if(tool == PLACING){
                g.setColor(toPlaceColor);
                g.fillRect(tempRect.x,tempRect.y,
                        tempRect.width,tempRect.height);
            }
        }
        //Draws out highlights on selected objects.
        if(!selectionEmpty()){
            g.setColor(Color.BLUE.brighter());

            for(GameObject o: selectedGameObjects){
                Rectangle r = o.getHitBox();
                g.drawRect((int)Math.ceil((r.x - game.getCamera().getX()) * game.getCamera().getZoom()),
                        (int)Math.ceil((r.y + game.getCamera().getY())* game.getCamera().getZoom()),
                        (int)Math.ceil(r.width * game.getCamera().getZoom()), (int)Math.ceil(r.height * game.getCamera().getZoom()));
            }
            for(Creature c: selectedCreatures){
                Rectangle r = c.getHitBox();
                g.drawRect((int) Math.ceil((r.x - game.getCamera().getX()) * game.getCamera().getZoom()),
                        (int)Math.ceil((r.y + game.getCamera().getX()) * game.getCamera().getZoom()),
                        (int)Math.ceil(r.width * game.getCamera().getZoom()),(int)Math.ceil( r.height * game.getCamera().getZoom()));
            }
        }
        g.setColor(Color.BLUE.brighter().brighter().brighter());
        g.drawString("Zoom: " + game.getCamera().getZoomLevel() + ", x: " + game.getCamera().getX() + ", y: " + -game.getCamera().getY(),
                2, game.getHeight() - 2);
    }

    /**
     * Initializes the state.
     */
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

    /**
     * Ensures a clean exit.
     */
    @Override
    public void exit() {
        game.getml().setInEdit(false);
        game.getDisplay().exitLevelEditorUi();
    }
}
