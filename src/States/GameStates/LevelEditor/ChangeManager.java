package States.GameStates.LevelEditor;

import Actors.Creature;
import Objects.GameObject;

class ChangeManager {

    public static boolean isPushing = false;
    public static boolean firstObjectMove;
    public static boolean firstCreatureMove;
    static Change firstUndo;
    static Change firstRedo;

    /**
     * Adds a object to the first undo.
     * @param o The object to add.
     */
    public static void push(GameObject o){
        if(isPushing) firstUndo.object[firstUndo.oIndex++] = o;

    }

    /**
     * Adds a creature to the first undo.
     * @param c The creature to add.
     */
    public static void push(Creature c) {
        if (isPushing) firstUndo.creature[firstUndo.cIndex++] = c;
    }

    /**
     * Adds a object to the first redo.
     * @param o The object to add.
     */
    public static void pushRedo(GameObject o){
        if(isPushing) firstRedo.object[firstRedo.oIndex++] = o;

    }
    /**
     * Adds a creature to the first redo.
     * @param c The creature to add.
     */
    public static void pushRedo(Creature c) {
        if (isPushing) firstRedo.creature[firstRedo.cIndex++] = c;
    }

    /**
     * Creates an AttributeChange object as the new first undo.
     * @param o The amount of objects that were changed.
     * @param c The amount of creatures that were changed.
     * @param s The name of the changed attribute.
     * @param i The value of the attribute before the change.
     */
    public static void push(int o, int c, String s, int i) {
        if(firstUndo == null) {
            firstUndo = new AttributeChange(o, c);
            firstUndo.name = s;
            firstUndo.change = i;
            return;
        }
        Change temp = new AttributeChange(o, c);
        temp.prev = firstUndo;
        firstUndo = temp;

        firstUndo.name = s;
        firstUndo.change = i;
    }

    /**
     * Same as push(int,int, String, int) but with a StringAttribute
     * @param o The amount of changed objects
     * @param c The amount of changed creatures.
     * @param s The name of the changed attribute.
     * @param d The value of the attribute before the change.
     */
    public static void push(int o, int c, String s, String d) {
        if(firstUndo == null) {
            firstUndo = new AttributeChange(o, c);
            firstUndo.name = s;
            firstUndo.changeS = d;
            return;
        }
        Change temp = new AttributeChange(o, c);
        temp.prev = firstUndo;
        firstUndo = temp;

        firstUndo.name = s;
        firstUndo.changeS = d;
    }

    /**
     * Creates an AttributeChange as the firstRedo.
     * @param o The amount of objects changed.
     * @param c The amount of creatures changed.
     * @param s The name of the changed attribute.
     * @param i The value of the attribute before the change.
     */
    public static void pushRedo(int o, int c, String s, int i) {
        if(firstRedo == null) {
            firstRedo = new AttributeChange(o, c);
            firstRedo.name = s;
            firstRedo.change = i;
            return;
        }
        Change temp = new AttributeChange(o, c);
        temp.prev = firstRedo;
        firstRedo = temp;

        firstRedo.name = s;
        firstRedo.change = i;
    }
    /**
     * Same as pushRedo(int,int, String, int) but with a StringAttribute
     * @param o The amount of changed objects
     * @param c The amount of changed creatures.
     * @param s The name of the changed attribute.
     * @param d The value of the attribute before the change.
     */
    public static void pushRedo(int o, int c, String s, String d) {
        if(firstRedo == null) {
            firstRedo = new AttributeChange(o, c);
            firstRedo.name = s;
            firstRedo.changeS = d;
            return;
        }
        Change temp = new AttributeChange(o, c);
        temp.prev = firstRedo;
        firstRedo = temp;

        firstRedo.name = s;
        firstRedo.changeS = d;
    }

    /**
     * Creates a movement change as the firstUndo.
     * @param o The amount of moved objects.
     * @param c The amount of moved creatures.
     */
    public static void push(int o, int c) {
        if(firstUndo == null) {
            firstUndo = new MovementChange(o, c);
            return;
        }
        Change temp = new MovementChange(o, c);
        temp.prev = firstUndo;
        firstUndo = temp;
    }
    /**
     * Creates a movement change as the firstRedo.
     * @param o The amount of moved objects.
     * @param c The amount of moved creatures.
     */
    public static void pushRedo(int o, int c) {
        if(firstRedo == null) {
            firstRedo = new MovementChange(o, c);
            return;
        }
        Change temp = new MovementChange(o, c);
        temp.prev = firstRedo;
        firstRedo = temp;
    }

    /**
     * Creates a Change object depending on a deletion or addition.
     * @param o The amount of objects created or deleted.
     * @param c The amount of creatures created or deleted.
     * @param deleted If it was a deletion or addition.
     */
    public static void push(int o, int c, boolean deleted) {
        if(firstUndo == null) {
            if(deleted)
                firstUndo = new RemovalChange(o, c);
            else
                firstUndo = new AdditionChange(o, c);
            return;
        }
        Change temp;
        if(deleted)
            temp = new RemovalChange(o, c);
        else
            temp = new AdditionChange(o, c);

        temp.prev = firstUndo;
        firstUndo = temp;
    }
    /**
     * Creates a Change object depending on a deletion or addition.
     * @param o The amount of objects created or deleted.
     * @param c The amount of creatures created or deleted.
     * @param deleted If it was a deletion or addition.
     */
    public static void pushRedo(int o, int c, boolean deleted) {
        if(firstRedo == null) {
            if(deleted)
                firstRedo = new RemovalChange(o, c);
            else
                firstRedo = new AdditionChange(o, c);
            return;
        }
        Change temp;
        if(deleted)
            temp = new RemovalChange(o, c);
        else
            temp = new AdditionChange(o, c);

        temp.prev = firstRedo;
        firstRedo = temp;
    }

    /**
     * Adds a object and the amount of pixels it was moved to the firstUndo.
     * @param o The object moved.
     * @param x The amount of pixels the object was moved on the x-axis.
     * @param y The amount of pixels the object was moved on the y-axis.
     */
    public static void push(GameObject o, int x, int y){
        if(isPushing){
            firstUndo.object[firstUndo.oIndex] = o;
            firstUndo.dox[firstUndo.oIndex] = x;
            firstUndo.doy[firstUndo.oIndex++] = y;
        }
    }
    /**
     * Adds a object and the amount of pixels it was moved to the firstRedo.
     * @param o The object moved.
     * @param x The amount of pixels the object was moved on the x-axis.
     * @param y The amount of pixels the object was moved on the y-axis.
     */
    public static void pushRedo(GameObject o, int x, int y){
        if(isPushing){
            firstRedo.object[firstRedo.oIndex] = o;
            firstRedo.dox[firstRedo.oIndex] = x;
            firstRedo.doy[firstRedo.oIndex++] = y;
        }
    }
    /**
     * Adds a creature and the amount of pixels it was moved to the firstUndo.
     * @param c The creature moved.
     * @param x The amount of pixels the creature was moved on the x-axis.
     * @param y The amount of pixels the creature was moved on the y-axis.
     */
    public static void push(Creature c, int x, int y) {
        if(isPushing){
            firstUndo.creature[firstUndo.cIndex] = c;
            firstUndo.dcx[firstUndo.cIndex] = x;
            firstUndo.dcy[firstUndo.cIndex++] = y;
        }
    }
    /**
     * Adds a creature and the amount of pixels it was moved to the firstRedo.
     * @param c The creature moved.
     * @param x The amount of pixels the creature was moved on the x-axis.
     * @param y The amount of pixels the creature was moved on the y-axis.
     */
    public static void pushRedo(Creature c, int x, int y) {
        if(isPushing){
            firstRedo.creature[firstRedo.cIndex] = c;
            firstRedo.dcx[firstRedo.cIndex] = x;
            firstRedo.dcy[firstRedo.cIndex++] = y;
        }
    }

    public static void pop() {
        firstUndo = firstUndo.prev;
    }

    public static boolean isObject(){
        return firstUndo.object != null;
    }

    public static Change getFirst(){
        return firstUndo;
    }

    /**
     * Adds movement to the current change when moving with arrow keys.
     * @param o The moved object.
     * @param x The amount moved on the x-axis.
     * @param y The amount moved on the y-axis.
     */
    public static void moveStep(GameObject o, int x, int y) {
        if(isPushing && !firstObjectMove) {
            if(firstUndo.dox[firstUndo.oIndex] == 0 && firstUndo.doy[firstUndo.oIndex] == 0) push(o,x,y);
            else{
                firstUndo.dox[firstUndo.oIndex] += x;
                firstUndo.doy[firstUndo.oIndex++] += y;
            }
        }
        else if(isPushing){
            firstUndo.oIndex = 0;
            if(firstUndo.dox[firstUndo.oIndex] == 0 && firstUndo.doy[firstUndo.oIndex] == 0) push(o,x,y);
            else{
                firstUndo.dox[firstUndo.oIndex] += x;
                firstUndo.doy[firstUndo.oIndex++] += y;
            }

            firstObjectMove = false;
        }
    }
    public static void moveStep(Creature c, int x, int y) {
        if(isPushing && !firstCreatureMove) {
            if(firstUndo.dcx[firstUndo.cIndex] == 0 && firstUndo.dcy[firstUndo.cIndex] == 0) push(c,x,y);
            else{
                firstUndo.dcx[firstUndo.cIndex] += x;
                firstUndo.dcy[firstUndo.cIndex++] += y;
            }
        }
        else if(isPushing){
            firstUndo.cIndex = 0;
            if(firstUndo.dcx[firstUndo.cIndex] == 0 && firstUndo.dcy[firstUndo.cIndex] == 0) push(c,x,y);
            else{
                firstUndo.dcx[firstUndo.cIndex] += x;
                firstUndo.dcy[firstUndo.cIndex++] += y;
            }

            firstCreatureMove = false;
        }
    }

    public static void popRedo() {
        firstRedo = firstRedo.prev;
    }
}
