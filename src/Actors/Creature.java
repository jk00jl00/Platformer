package Actors;

import Gfx.Camera;
import Objects.GameObject;

import java.awt.*;
import java.io.Serializable;

public class Creature{
    //The position and health of the player, used by all subclasses of player
    protected int x;
    protected int y;
    protected int health;
    protected int width;
    protected int height;
    protected Rectangle hitBox;

    //Used for drawing
    public Color color;

    public static int getDefaultCreatureWidth() {
        return DEFAULT_CREATURE_WIDTH_;
    }

    public static int getDefaultCreatureHeight() {
        return DEFAULT_CREATURE_HEIGHT_;
    }

    //Static constants
    protected static final int DEFAULT_CREATURE_WIDTH_ = 15;
    protected static final int DEFAULT_CREATURE_HEIGHT_ = 35;
    protected String type;

    public String getType() {
        return this.type;
    }

    public Creature(){

    }

    public Creature(int x, int y, int health){
        this.x = x;
        this.y = y;
        this.health = health;
    }

    public void update(GameObject[] go){

    }

    public void draw(Graphics2D g, Camera camera){

    }

    public int getwidth() {
        return width;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getHeight() {
        return height;
    }

    public String toLevelSave() {
        return null;
    }
}
