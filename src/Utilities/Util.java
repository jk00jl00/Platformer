package Utilities;

public class Util {
    public static int clamp(int value, int min, int max) {
        return (value < min) ? min : (value > max) ? max : value;
    }
    public static double clamp(double value, double min, double max) {
        return (value < min) ? min : (value > max) ? max : value;
    }
}
