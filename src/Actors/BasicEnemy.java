package Actors;

import Gfx.Camera;
import Objects.GameObject;
import States.GameStates.CreatureStates.BasicEnemyBehavior;
import States.GameStates.CreatureStates.CreatureBehavior;
import States.PlayerStates.PlayerState;
import States.PlayerStates.PlayerStateStack;

import java.awt.*;

public class BasicEnemy extends Creature{

    public double dy;
    public double dx;

    public static final double grav = 0.5;

    private static final Color defaultColor = Color.RED;
    private BasicEnemyBehavior behavior;

    public BasicEnemy(int x, int y, int health) {
        super(x, y, health);
        this.type = "BasicEnemy";
        this.width = Creature.DEFAULT_CREATURE_HEIGHT_;
        this.height = Creature.DEFAULT_CREATURE_WIDTH_;
        this.hitBox = new Rectangle(this.x, this.y,this.width, this.height);
        this.color = Color.RED;
        this.behavior = new BasicEnemyBehavior(this);
    }

    public BasicEnemy(int x, int y){
        super(x, y, 2);
        this.type = "BasicEnemy";
        this.width = Creature.DEFAULT_CREATURE_HEIGHT_;
        this.height = Creature.DEFAULT_CREATURE_WIDTH_;
        this.hitBox = new Rectangle(this.x, this.y,this.width, this.height);
        this.color = Color.RED;
        this.behavior = new BasicEnemyBehavior(this);
    }

    @Override
    public void update(GameObject[] go) {
        super.update(go);
        this.behavior.update();
        //currentState.update();
        //currentState.handleKeys();
        //currentState.move();
    }

    public void draw(Graphics2D g, Camera camera){
        g.fillRect(this.x - camera.getX(), this.y + camera.getY(), this.width, this.height);
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

    @Override
    public String toLevelSave() {
        StringBuilder sb = new StringBuilder();

        sb.append(this.x).append(" ").append(this.y).append(" ");

        return sb.toString();
    }

    public int getHealth() {
        return this.health;
    }

    public static Color getDefaultColor(){
        return defaultColor;
    }

    public static int getDefaultWidth(){return DEFAULT_CREATURE_HEIGHT_;}
    public static int getDefaultHeight(){return DEFAULT_CREATURE_WIDTH_;}
}
