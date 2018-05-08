package Actors;

import Gfx.Camera;
import Objects.GameObject;
import Utilities.Util;

import java.awt.*;

public class Creature{
    //The position and health of the player, used by all subclasses of player
    protected int x;
    protected int y;
    protected int width;
    protected int height;
    int health;
    Rectangle hitBox;
    Facing facing;
    public enum Facing{
        Right, Left


    }

    GameObject[] gos;
    Creature[] cs;

    int dmg;
    String type;
    public Color color;


    //Static constants
    //Creatures that can be placed.
    public static final String[] CREATURES = new String[]{
            "Player",
            "BasicEnemy"
    };
    //The acceleration per frame towards the bottom of the screen.
    public static final double grav = 0.5;
    static final int DEFAULT_CREATURE_WIDTH_ = 16;
    static final int DEFAULT_CREATURE_HEIGHT_ = 32;

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
    //Getters
    public static int getDefaultCreatureWidth() {
        return DEFAULT_CREATURE_WIDTH_;
    }

    public static int getDefaultCreatureHeight() {
        return DEFAULT_CREATURE_HEIGHT_;
    }

    public String getType() {
        return this.type;
    }

    public Rectangle getHitBox() {
        return this.hitBox;
    }

    public Facing getFacing() {
        return facing;
    }

    public int getHealth() {
        return health;
    }

    public GameObject[] getGos() {
        return gos;
    }

    public Creature[] getcs() {
        return cs;
    }

    public int getHeight() {
        return height;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    //Setters
    public void setFacing(Facing facing) {
        this.facing = facing;
    }

    /**
     * Called by the level when updating.
     * Sets the GameObjects and Creatures to be used in collision.
     * @param go The GameObjects the creature will collide with.
     * @param creatures The Creatures the creature will collide with.
     */
    public void update(GameObject[] go, Creature[] creatures){
        this.gos = go;
        this.cs = creatures;
    }

    public void draw(Graphics2D g, Camera camera){

    }

    public String toLevelSave() {
        return null;
    }

    /**
     * Called when moving the creature in the level editor.
     * @param x The movement on the x axis
     * @param y The movement on the y axis.
     */
    public void move(int x, int y) {
        if(x > 0) facing = Facing.Right;
        else if(x < 0)facing = Facing.Left;
        //The clamping ensures the creature stays within the game bounds.
        this.x = Util.clamp(this.x + x, 0, 10000 - this.width);
        this.y = Util.clamp(this.y + y, 0, 10000 - this.height);
        this.hitBox.x  = this.x;
        this.hitBox.y = this.y;
    }

    /**Called when the creature should be damaged.
     * Is expanded upon in subclasses.
     */
    public void damage(int dmg){

    }

    public void collide() {

    }
}
