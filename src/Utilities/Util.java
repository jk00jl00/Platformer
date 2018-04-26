package Utilities;

import java.awt.*;

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
}
