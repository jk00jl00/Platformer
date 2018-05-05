package Actors;

import Gfx.Camera;
import Objects.GameObject;
import States.PlayerStates.PlayerState;
import States.PlayerStates.PlayerStateStack;

import java.awt.*;

public class Player extends Creature{

    public double dy;
    public double dx;

    public double jumpSpeed = 12.5;

    private static final Color defaultColor = Color.LIGHT_GRAY;

    private boolean[] keys;
    private PlayerState currentState;
    private long invisTime = 0;
    private long lastShot;
    private int shotCooldown = 10;

    public Player(int x, int y, int health) {
        super(x, y, health);
        this.type = "Player";
        this.facing = Facing.Right;
        this.width = Creature.DEFAULT_CREATURE_WIDTH_;
        this.height = Creature.DEFAULT_CREATURE_HEIGHT_;
        this.hitBox = new Rectangle(this.x, this.y,this.width, this.height);
        this.color = Color.LIGHT_GRAY;
        getState();
    }

    public Player(int x, int y){
        super(x, y, 3);
        this.type = "Player";
        this.facing = Facing.Right;
        this.width = Creature.DEFAULT_CREATURE_WIDTH_;
        this.height = Creature.DEFAULT_CREATURE_HEIGHT_;
        this.hitBox = new Rectangle(this.x, this.y,this.width, this.height);
        this.color = Color.LIGHT_GRAY;
        getState();
    }

    @Override
    public void update(GameObject[] go, Creature[] creatures) {
        super.update(go, creatures);
        if(invisTime != 0 && (System.currentTimeMillis() - invisTime)/500 > 1){
            invisTime = 0;
            this.color = defaultColor;
        }
        getState();
        currentState.update();
        getState();
        currentState.handleKeys();
        getState();
        currentState.move();
    }

    public void draw(Graphics2D g, Camera camera){
        g.fillRect(this.x - camera.getX(), this.y + camera.getY(), this.width, this.height);
        g.drawString("Health: " + this.health, 0, (int)g.getFontMetrics().getStringBounds("Health: " + this.health, g).getHeight());
    }

    @Override
    public void damage(int dmg) {
        if (invisTime == 0) {
            this.health -= dmg;
            this.color = Color.BLUE;
            this.invisTime = System.currentTimeMillis();
        }
    }

    public void setKeys(boolean[] b) {
        this.keys = b;
    }

    private void getState() {
        currentState = PlayerStateStack.getCurrent();
    }

    public boolean[] getKeys() {
        return keys;
    }

    public Rectangle getHitBox() {
        return hitBox;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setHitbox(int x, int y) {
        this.hitBox.x = x;
        this.hitBox.y = y;
    }

    public int getHealth() {
        return this.health;
    }

    public static Color getDefaultColor(){
        return defaultColor;
    }
    public static int getDefaultWidth(){return DEFAULT_CREATURE_WIDTH_;}
    public static int getDefaultHeight(){return DEFAULT_CREATURE_HEIGHT_;}

    public boolean noState() {
        return currentState == null;
    }

    public boolean canShoot(boolean spaceReleased){
        return (System.currentTimeMillis() - lastShot > shotCooldown) && spaceReleased;
    }

    public void shot() {
        this.lastShot = System.currentTimeMillis();
    }
}
