package States.GameStates.LevelEditor;

import Actors.Creature;
import Objects.GameObject;

public class ChangeManager {

    static Change firstUndo;
    static Change firstRedo;
    public static boolean isPushing = false;
    public static boolean firstObjectMove;
    public static boolean firstCreatureMove;


    public static void push(GameObject o){
        if(isPushing) firstUndo.object[firstUndo.oIndex++] = o;

    }
    public static void push(Creature c) {
        if (isPushing) firstUndo.creature[firstUndo.cIndex++] = c;
    }

    public static void pushRedo(GameObject o){
        if(isPushing) firstRedo.object[firstRedo.oIndex++] = o;

    }
    public static void pushRedo(Creature c) {
        if (isPushing) firstRedo.creature[firstRedo.cIndex++] = c;
    }

    public static void push(int o, int c, String s, int i) {
        if(firstUndo == null) {
            firstUndo = new AttributeChange(o, c);
            return;
        }
        Change temp = new AttributeChange(o, c);
        temp.prev = firstUndo;
        firstUndo = temp;

        firstUndo.name = s;
        firstUndo.change = i;
    }

    public static void pushRedo(int o, int c, String s, int i) {
        if(firstRedo == null) {
            firstRedo = new AttributeChange(o, c);
            return;
        }
        Change temp = new AttributeChange(o, c);
        temp.prev = firstRedo;
        firstRedo = temp;

        firstRedo.name = s;
        firstRedo.change = i;
    }

    public static void push(int o, int c) {
        if(firstUndo == null) {
            firstUndo = new MovementChange(o, c);
            return;
        }
        Change temp = new MovementChange(o, c);
        temp.prev = firstUndo;
        firstUndo = temp;
    }

    public static void pushRedo(int o, int c) {
        if(firstRedo == null) {
            firstRedo = new MovementChange(o, c);
            return;
        }
        Change temp = new MovementChange(o, c);
        temp.prev = firstRedo;
        firstRedo = temp;
    }

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

    public static void push(GameObject o, int x, int y){
        if(isPushing){
            firstUndo.object[firstUndo.oIndex] = o;
            firstUndo.dox[firstUndo.oIndex] = x;
            firstUndo.doy[firstUndo.oIndex++] = y;
        }
    }

    public static void pushRedo(GameObject o, int x, int y){
        if(isPushing){
            firstRedo.object[firstRedo.oIndex] = o;
            firstRedo.dox[firstRedo.oIndex] = x;
            firstRedo.doy[firstRedo.oIndex++] = y;
        }
    }

    public static void push(Creature c, int x, int y) {
        if(isPushing){
            firstUndo.creature[firstUndo.cIndex] = c;
            firstUndo.dcx[firstUndo.cIndex] = x;
            firstUndo.dcy[firstUndo.cIndex++] = y;
        }
    }

    public static void pushRedo(Creature c, int x, int y) {
        if(isPushing){
            firstRedo.creature[firstRedo.cIndex] = c;
            firstRedo.dcx[firstRedo.cIndex] = x;
            firstRedo.dcy[firstRedo.cIndex++] = y;
        }
    }

    public static boolean isObject(){
        return firstUndo.object != null;
    }

    public static Change getFirst(){
        Change temp = firstUndo;
        return temp;
    }

    public static void pop() {
        firstUndo = firstUndo.prev;
    }

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
}
