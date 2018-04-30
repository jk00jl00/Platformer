package Actors;

import Gfx.Camera;
import Objects.GameObject;
import States.PlayerStates.CreatureState;
import States.PlayerStates.OnGroundStates;
import States.PlayerStates.PlayerState;

import java.awt.*;

public class Player extends Creature{

    public double dy;
    public double dx;

    public double jumpSpeed = 12.5;

    public static final double grav = 0.5;

    private GameObject[] gos;
    private boolean[] keys;
    private CreatureState currentState;

    public Player(int x, int y, int health) {
        super(x, y, health);
        this.type = "player";
        this.width = Creature.DEFAULT_CREATURE_WIDTH_;
        this.height = Creature.DEFAULT_CREATURE_HEIGHT_;
        this.hitBox = new Rectangle(this.x, this.y,this.width, this.height);
        this.color = Color.LIGHT_GRAY;
        PlayerState.push(new OnGroundStates(this));
        getState();
    }

    public Player(int x, int y){
        super(x, y, 3);
        this.type = "player";
        this.width = Creature.DEFAULT_CREATURE_WIDTH_;
        this.height = Creature.DEFAULT_CREATURE_HEIGHT_;
        this.hitBox = new Rectangle(this.x, this.y,this.width, this.height);
        this.color = Color.LIGHT_GRAY;
        PlayerState.push(new OnGroundStates(this));
        getState();
    }

    @Override
    public void update(GameObject[] go) {
        gos = go;
        currentState.update();
        getState();
        currentState.handleKeys();
        getState();
        currentState.move();
    }

    public void draw(Graphics2D g, Camera camera){
        g.fillRect(this.x - camera.getX(), this.y + camera.getY(), this.width, this.height);
    }

    public void setKeys(boolean[] b) {
        this.keys = b;
    }

    private void getState() {
        currentState = PlayerState.getCurrent();
    }

    public boolean[] getKeys() {
        return keys;
    }

    public Rectangle getHitBox() {
        return hitBox;
    }

    public GameObject[] getGos() {
        return gos;
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
}
