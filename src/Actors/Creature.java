package Actors;

import Gfx.Camera;
import Objects.GameObject;
import Utilities.Util;

import java.awt.*;

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
    protected static final int DEFAULT_CREATURE_WIDTH_ = 16;
    protected static final int DEFAULT_CREATURE_HEIGHT_ = 32;
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

    public int getWidth() {
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

    public Rectangle getHitBox() {
        return this.hitBox;
    }

    public void move(int xDrag, int yDrag) {
        this.x = Util.clamp(x + xDrag, 0, 10000 - this.width);
        this.y = Util.clamp(y + yDrag, 0, 10000 - this.height);
        this.hitBox.x  = this.x;
        this.hitBox.y = this.y;
    }
}
