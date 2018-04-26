package Actors;

import Gfx.Camera;
import Objects.GameObject;

import java.awt.*;

public class Creature {
    //The position and health of the player, used by all subclasses of player
    protected int x;
    protected int y;
    protected int health;
    protected int width;
    protected int height;
    protected Rectangle hitBox;

    //Static constants
    protected static final int DEFAULT_CREATURE_WIDTH_ = 15;
    protected static final int DEFAULT_CREATURE_HEIGHT_ = 35;

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
}
