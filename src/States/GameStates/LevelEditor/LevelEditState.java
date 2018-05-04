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

//TODO - Add a attribute display when having an item selected.
//TODO - Add movement with mouse.

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
            handleActionWithTool();
        }

        if(!this.selectionEmpty() && ml.draggingSelection){
            drag();
        }

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
    //TODO - major cleanup of cluttered if statements into wider statements.
    private void checkArrowKeys() {
        if(selectionEmpty()){
            //If the selection if empty it moves the camera by half the grid height.
            if(game.getkl().getKeysPressed()[KeyEvent.VK_UP]){
                game.getCamera().move(0, GRID_HEIGHT/2);
                game.getkl().setKey(KeyEvent.VK_UP, false);
            }
            if(game.getkl().getKeysPressed()[KeyEvent.VK_RIGHT]){
                game.getCamera().move(GRID_WIDTH/2, 0);
                game.getkl().setKey(KeyEvent.VK_RIGHT, false);
            }
            if(game.getkl().getKeysPressed()[KeyEvent.VK_DOWN]){
                game.getCamera().move(0, -GRID_HEIGHT/2);
                game.getkl().setKey(KeyEvent.VK_DOWN, false);
            }
            if(game.getkl().getKeysPressed()[KeyEvent.VK_LEFT]){
                game.getCamera().move(-GRID_WIDTH/2, 0);
                game.getkl().setKey(KeyEvent.VK_LEFT, false);
            }
        }
        if (!snapTo) {
            //Checks the direction the of the check and sets up a Change object to store the movement if no Change object exists.
            if(game.getkl().getKeysPressed()[KeyEvent.VK_UP]){
                if(!ChangeManager.isPushing){
                    ChangeManager.isPushing = true;
                    ChangeManager.push(selectedGameObjects.size(), selectedCreatures.size());
                }
                ChangeManager.firstObjectMove = true;
                ChangeManager.firstCreatureMove = true;
                for(GameObject o :this.selectedGameObjects){
                    //Moves every GameObject one pixel on the screen
                    o.move(0, -1);
                    //Adds the movement to the current change.
                    ChangeManager.moveStep(o,0, 1);
                }
                for(Creature c: this.selectedCreatures){
                    //Moves every creature one pixel on the screen.
                    c.move(0, -1);
                    //Adds the movement to the current change.
                    ChangeManager.moveStep(c, 0, 1);
                }
                game.getkl().setKey(KeyEvent.VK_UP, false);
            }
            if(game.getkl().getKeysPressed()[KeyEvent.VK_RIGHT]){
                if(!ChangeManager.isPushing){
                    ChangeManager.isPushing = true;
                    ChangeManager.push(selectedGameObjects.size(), selectedCreatures.size());
                }
                ChangeManager.firstObjectMove = true;
                ChangeManager.firstCreatureMove = true;
                for(GameObject o :this.selectedGameObjects){
                    o.move(1, 0);
                    ChangeManager.moveStep(o,-1, 0);
                }
                for(Creature c: this.selectedCreatures){
                    c.move(1, 0);
                    ChangeManager.moveStep(c, -1, 0);
                }
                game.getkl().setKey(KeyEvent.VK_RIGHT, false);
            }
            if(game.getkl().getKeysPressed()[KeyEvent.VK_DOWN]){
                if(!ChangeManager.isPushing){
                    ChangeManager.isPushing = true;
                    ChangeManager.push(selectedGameObjects.size(), selectedCreatures.size());
                }
                ChangeManager.firstObjectMove = true;
                ChangeManager.firstCreatureMove = true;
                for(GameObject o :this.selectedGameObjects){
                    o.move(0, 1);
                    ChangeManager.moveStep(o,0, -1);
                }
                for(Creature c: this.selectedCreatures){
                    c.move(0, 1);
                    ChangeManager.moveStep(c, 0, -1);
                }
                game.getkl().setKey(KeyEvent.VK_DOWN, false);
            }
            if(game.getkl().getKeysPressed()[KeyEvent.VK_LEFT]){
                if(!ChangeManager.isPushing){
                    ChangeManager.isPushing = true;
                    ChangeManager.push(selectedGameObjects.size(), selectedCreatures.size());
                }
                ChangeManager.firstObjectMove = true;
                ChangeManager.firstCreatureMove = true;
                for(GameObject o :this.selectedGameObjects){
                    o.move(-1,0);
                    ChangeManager.moveStep(o,1, 0);
                }
                for(Creature c: this.selectedCreatures){
                    c.move(-1, 0);
                    ChangeManager.moveStep(c, 1, 0);
                }
                game.getkl().setKey(KeyEvent.VK_LEFT, false);
            }
        } else{
            if(game.getkl().getKeysPressed()[KeyEvent.VK_UP]){
                if(!ChangeManager.isPushing){
                    ChangeManager.isPushing = true;
                    ChangeManager.push(selectedGameObjects.size(), selectedCreatures.size());
                }
                ChangeManager.firstObjectMove = true;
                ChangeManager.firstCreatureMove = true;
                for(GameObject o :this.selectedGameObjects){
                    int y = snapToGridY(o.getY(), -1);
                    o.move(0, y);
                    ChangeManager.moveStep(o, 0, -y);
                }
                for(Creature c :this.selectedCreatures){
                    int y = snapToGridY(c.getY(), -1);
                    c.move(0, y);
                    ChangeManager.moveStep(c, 0, -y);
                }
                game.getkl().setKey(KeyEvent.VK_UP, false);
            }
            if(game.getkl().getKeysPressed()[KeyEvent.VK_RIGHT]){
                if(!ChangeManager.isPushing){
                    ChangeManager.isPushing = true;
                    ChangeManager.push(selectedGameObjects.size(), selectedCreatures.size());
                }
                ChangeManager.firstObjectMove = true;
                ChangeManager.firstCreatureMove = true;
                for(GameObject o :this.selectedGameObjects){
                    int x = snapToGridX(o.getX(), 1);
                    o.move(x, 0);
                    ChangeManager.moveStep(o, -x, 0);
                }
                for(Creature c :this.selectedCreatures){
                    int x = snapToGridX(c.getX(), 1);
                    c.move(x, 0);
                    ChangeManager.moveStep(c, -x, 0);
                }
                game.getkl().setKey(KeyEvent.VK_RIGHT, false);
            }
            if(game.getkl().getKeysPressed()[KeyEvent.VK_DOWN]){
                if(!ChangeManager.isPushing){
                    ChangeManager.isPushing = true;
                    ChangeManager.push(selectedGameObjects.size(), selectedCreatures.size());
                }
                ChangeManager.firstObjectMove = true;
                ChangeManager.firstCreatureMove = true;
                for(GameObject o :this.selectedGameObjects){
                    int y = snapToGridY(o.getY(), 1);
                    o.move(0, y);
                    ChangeManager.moveStep(o, 0, -y);
                }
                for(Creature c :this.selectedCreatures){
                    int y = snapToGridY(c.getY(), 1);
                    c.move(0, y);
                    ChangeManager.moveStep(c, 0, -y);
                }
                game.getkl().setKey(KeyEvent.VK_DOWN, false);
            }
            if(game.getkl().getKeysPressed()[KeyEvent.VK_LEFT]){
                if(!ChangeManager.isPushing){
                    ChangeManager.isPushing = true;
                    ChangeManager.push(selectedGameObjects.size(), selectedCreatures.size());
                }
                ChangeManager.firstObjectMove = true;
                ChangeManager.firstCreatureMove = true;
                for(GameObject o :this.selectedGameObjects) {
                    int x = snapToGridX(o.getX(), -1);
                    o.move(x, 0);
                    ChangeManager.moveStep(o, -x, 0);
                }
                for(Creature c:this.selectedCreatures) {
                    int x = snapToGridX(c.getX(), -1);
                    c.move(x, 0);
                    ChangeManager.moveStep(c, -x, 0);
                }
                game.getkl().setKey(KeyEvent.VK_LEFT, false);
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
    //TODO - remove the delete tool section of handleRightClick.
    private void handleRightClick() {
        switch (this.tool){
            //Clears the current selection if the right click does not intersect with a selected object.
            case SELECT_TOOL_:
                if(!Util.rectIntersectsWithObjectArray(ml.getRClick(), this.selectedGameObjects) &&
                        !Util.rectIntersectsWithCreatureArray(ml.getRClick(), this.selectedCreatures)){
                    clearSelection();
                }
                break;
            //Cancels the deletion (No longer used);
            case DELETE_TOOL_:
                ml.dragging = false;
                ml.dragTangle.width = 0;
                break;
            //Cancels the placement.
            case PLACING:
                ml.dragging = false;
                ml.dragTangle.width = 0;
                break;
        }
    }
    /**
     * Is called after a location has been specified using the Place tool.
     * Places the currently selected creature at the specified location.
     */
    //TODO - add a error message when a Creature was not added.
    private void placeSelectedCreature() {
        //If it's the player character it replaces the player instead of placing a new one.
        if(toPlace.equals("Player")) {
            Player player = new Player(tempRect.x + game.getCamera().getX(), tempRect.y + game.getCamera().getY());
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
                        tempRect.x + game.getCamera().getX(),
                        tempRect.y - game.getCamera().getY(),
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
                //Resets all rectangles.
                tempRect = null;
                ml.dragTangle.width = 0;
                ml.rectReady = false;
            }
        }
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
               tempRect.x + game.getCamera().getX(),
               tempRect.y - game.getCamera().getY(),
               tempRect.width,
               tempRect.height
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
            //If it's placing a creature it needs to to increase the dimensions of the rectangle but instead show a rectangle with the creatures dimensions.
            if(!game.getbl().getSelection().equals("creatures")){
                ml.placingCreature = false;
            } else{
                ml.placingCreature = true;
            }
        }
    }

    /**
     * This method is called when the player has clicked a creature in the tool panel and sets up the placement.
     */
    private void selectCreatureToPlace() {
        switch (game.getbl().getToPlace()){
            case "Player":
                this.toPlace = game.getbl().getToPlace();
                this.toPlaceColor = Player.getDefaultColor();
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
        if(game.getkl().getControlMasked()[KeyEvent.VK_S]){
            saveLevel();
        }
        if(game.getkl().getControlMasked()[KeyEvent.VK_L]){
            if (loadLevel()) return true;
        }
        if(game.getkl().getControlMasked()[KeyEvent.VK_Z]){
            undo();
        }
        if(game.getkl().getKeysPressed()[KeyEvent.VK_DELETE]){
            handleDeletion();
        }
        return false;
    }

    /**
     * Is called in the update method in order to make sure the correct tool is selected.
     */
    private void checkToolSelection() {
        //Checks if the select tool has been selected and switches to it.
        if(game.getbl().getButtonsPressed()[SELECT_TOOL_]){
            game.getbl().disableButton(SELECT_TOOL_);
            changeTool(SELECT_TOOL_);
            System.out.println("SelectTool");
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
    }
    /**
     * Called when the user is dragging a selection and moves the objects.
     */
    private void drag() {
        for(GameObject o: selectedGameObjects){
            o.move(ml.getXDrag(), ml.getYDrag());
        }
        for(Creature c: selectedCreatures){
            c.move(ml.getXDrag(), ml.getYDrag());
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
        tempRect.x += game.getCamera().getX();
        tempRect.y -= game.getCamera().getY();
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
                //Sets up the mouseListener to know that a selection has been made in order to check if the user is dragging the selection.
                ml.setHasSelection(true);
                ml.setSelectedObjects(this.selectedGameObjects.toArray(new GameObject[this.selectedGameObjects.size()]));
                ml.setSelectedCreatures(this.selectedCreatures.toArray(new Creature[this.selectedCreatures.size()]));
            } else {
                //Same as above but will only select the first object found.
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

    /**
     * This method is called when the user presses the undo button combination (Default ctrl z).
     * Undoes the latest change made by the user.
     */
    private void undo() {
        ChangeManager.isPushing = false;
        //Makes sure that a change exists to be undone.
        if (ChangeManager.firstChange != null) {
            //Checks if the Change was a movement.
            if (!ChangeManager.wasMoved()) {
                //Checks if the Change was a deletion or addition.
                if(ChangeManager.wasDeleted()){
                    //Restores the removed objects and pops the Change of the stack.
                    for (GameObject o: ChangeManager.getFirst().object) game.getLevel().addGameObject(o);
                    for(Creature c: ChangeManager.getFirst().creature)game.getLevel().addCreature(c);
                    ChangeManager.pop();
                } else{
                    //Removes the added objects and pops the Change of the stack.
                    for(GameObject o: ChangeManager.getFirst().object) {
                        if(selectedGameObjects.contains(o)) selectedGameObjects.remove(o);
                        game.getLevel().removeObject(o);
                    }
                    for(Creature c: ChangeManager.getFirst().creature){
                        if(selectedCreatures.contains(c)) selectedCreatures.remove(c);
                        game.getLevel().removeCreature(c);
                    }
                    ChangeManager.pop();
                }
            } else{
                //Moves the objects back into their original position and pops the Change of the stack.
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
                game.getbl().setToPlace("");
                ml.placingCreature = false;
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
        g.fillRect(0, 0, game.getWidth() + 100, game.getHeight() +100);
        //Draws the level.
        game.getLevel().draw(g, game.getCamera());
        //Draws the grid if it is displayed.
        if(gridDisplayed){
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
                g.drawRect(tempRect.x, tempRect.y, tempRect.width, tempRect.height);
            } else if(tool == PLACING){
                g.setColor(toPlaceColor);
                g.fillRect(tempRect.x, tempRect.y, tempRect.width, tempRect.height);
            }
        }
        //Draws out highlights on selected objects.
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
