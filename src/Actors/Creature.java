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
    public static final double grav = 0.5;

    public Facing getFacing() {
        return facing;
    }

    public int getHealth() {
        return health;
    }

    public void setFacing(Facing facing) {
        this.facing = facing;
    }

    public enum Facing{
        Right, Left
    }

    protected GameObject[] gos;
    protected Creature[] cs;

    protected int dmg;
    protected Facing facing;

    public static final String[] CREATURES = new String[]{
            "Player",
            "BasicEnemy"
    };

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

    public Creature(int x, int y){
        this.x = x;
        this.y = y;
        this.health = 1;
    }

    public Creature(int x, int y, int health){
        this.x = x;
        this.y = y;
        this.health = health;
    }

    public void update(GameObject[] go, Creature[] creatures){
        this.gos = go;
        this.cs = creatures;
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
        if(xDrag > 0) facing = Facing.Right;
        else if(xDrag < 0)facing = Facing.Left;
        this.x = Util.clamp(x + xDrag, 0, 10000 - this.width);
        this.y = Util.clamp(y + yDrag, 0, 10000 - this.height);
        this.hitBox.x  = this.x;
        this.hitBox.y = this.y;
    }

    public GameObject[] getGos() {
        return gos;
    }

    public Creature[] getcs() {
        return cs;
    }

    public void damage() {

    }
    public void damage(int dmg){

    }

    public void collide() {

    }
}
