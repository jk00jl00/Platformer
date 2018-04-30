package Utilities;

import Actors.Creature;
import Objects.GameObject;

import java.awt.*;
import java.util.ArrayList;

public class Util {
    public static int clamp(int value, int min, int max) {
        return (value < min) ? min : (value > max) ? max : value;
    }
    public static double clamp(double value, double min, double max) {
        return (value < min) ? min : (value > max) ? max : value;
    }
    public static boolean collide(Rectangle r, Rectangle p){
        return r.intersects(p);
    }

    public static boolean rectIntersectsWithObjectArray(Rectangle r, ArrayList<GameObject> selection) {
        for(GameObject o: selection){
            if(r.intersects(o.getHitBox())) return true;
        }

        return false;
    }

    public static boolean rectIntersectsWithCreatureArray(Rectangle r, ArrayList<Creature> selection){
        for(Creature c: selection){
            if(r.intersects(c.getHitBox())) return true;
        }
        return false;
    }
}
