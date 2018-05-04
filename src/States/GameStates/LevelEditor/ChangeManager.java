package States.GameStates.LevelEditor;

import Actors.Creature;
import Objects.GameObject;

public class ChangeManager {

    static Change firstChange;
    public static boolean isPushing = false;
    public static boolean firstObjectMove;
    public static boolean firstCreatureMove;


    public static void push(GameObject o){
        if(isPushing) firstChange.object[firstChange.oIndex++] = o;

    }
    public static void push(Creature c) {
        if (isPushing) firstChange.creature[firstChange.cIndex++] = c;
    }

    public static void push(int o, int c) {
        if(firstChange == null) {
            firstChange = new Change(o, c);
            return;
        }
        Change temp = new Change(o, c);
        temp.prev = firstChange;
        firstChange = temp;
    }

    public static void push(int o, int c, boolean deleted) {
        if(firstChange == null) {
            firstChange = new Change(o, c, deleted);
            return;
        }
        Change temp = new Change(o, c, deleted);
        temp.prev = firstChange;
        firstChange = temp;
    }

    public static void push(GameObject o, int x, int y){
        if(isPushing){
            firstChange.object[firstChange.oIndex] = o;
            firstChange.dox[firstChange.oIndex] = x;
            firstChange.doy[firstChange.oIndex++] = y;
        }
    }

    public static void push(Creature c, int x, int y) {
        if(isPushing){
            firstChange.creature[firstChange.cIndex] = c;
            firstChange.dcx[firstChange.cIndex] = x;
            firstChange.dcy[firstChange.cIndex++] = y;
        }
    }

    public static boolean isObject(){
        return firstChange.object != null;
    }

    public static Change getFirst(){
        Change temp = firstChange;
        return temp;
    }

    public static boolean wasDeleted() {
        return firstChange.deleted;
    }

    public static boolean wasMoved() {
        return firstChange.moved;
    }

    public static void pop() {
        firstChange = firstChange.prev;
    }

    public static void moveStep(GameObject o, int x, int y) {
        if(isPushing && !firstObjectMove) {
            if(firstChange.dox[firstChange.oIndex] == 0 && firstChange.doy[firstChange.oIndex] == 0) push(o,x,y);
            else{
                firstChange.dox[firstChange.oIndex] += x;
                firstChange.doy[firstChange.oIndex++] += y;
            }
        }
        else if(isPushing){
            firstChange.oIndex = 0;
            if(firstChange.dox[firstChange.oIndex] == 0 && firstChange.doy[firstChange.oIndex] == 0) push(o,x,y);
            else{
                firstChange.dox[firstChange.oIndex] += x;
                firstChange.doy[firstChange.oIndex++] += y;
            }

            firstObjectMove = false;
        }
    }
    public static void moveStep(Creature c, int x, int y) {
        if(isPushing && !firstCreatureMove) {
            if(firstChange.dcx[firstChange.cIndex] == 0 && firstChange.dcy[firstChange.cIndex] == 0) push(c,x,y);
            else{
                firstChange.dcx[firstChange.cIndex] += x;
                firstChange.dcy[firstChange.cIndex++] += y;
            }
        }
        else if(isPushing){
            firstChange.cIndex = 0;
            if(firstChange.dcx[firstChange.cIndex] == 0 && firstChange.dcy[firstChange.cIndex] == 0) push(c,x,y);
            else{
                firstChange.dcx[firstChange.cIndex] += x;
                firstChange.dcy[firstChange.cIndex++] += y;
            }

            firstCreatureMove = false;
        }
    }
}
