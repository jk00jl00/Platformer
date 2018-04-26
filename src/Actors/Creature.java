package Actors;

import Objects.GameObject;

import java.awt.*;

public class Creature {
    //The position and health of the creature, used by all subclasses of creature
    protected int x;
    protected int y;
    protected int health;
    protected int width;
    protected int height;
    protected Rectangle hitBox;

    //Static constants
    protected static final int DEFAULT_CREATURE_WIDTH_ = 20;
    protected static final int DEFAULT_CREATURE_HEIGHT_ = 50;

    public Creature(int x, int y, int health){
        this.x = x;
        this.y = y;
        this.health = health;
    }

    public void update(GameObject[] go){

    }

    public void draw(Graphics2D g){

    }

}
