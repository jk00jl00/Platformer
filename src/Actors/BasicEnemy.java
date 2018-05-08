package Actors;

import Gfx.Camera;
import Objects.GameObject;
import States.CreatureStates.BasicEnemyBehavior;

import java.awt.*;

public class BasicEnemy extends Creature{

    public double dy;
    public double dx;

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
        this.dmg = 1;
    }

    public BasicEnemy(int x, int y){
        super(x, y, 2);
        this.type = "BasicEnemy";
        this.width = Creature.DEFAULT_CREATURE_HEIGHT_;
        this.height = Creature.DEFAULT_CREATURE_WIDTH_;
        this.hitBox = new Rectangle(this.x, this.y,this.width, this.height);
        this.color = Color.RED;
        this.behavior = new BasicEnemyBehavior(this);
        this.dmg = 1;
    }
    //Getters
    public Rectangle getHitBox() {
        return hitBox;
    }

    public GameObject[] getGos() {
        return gos;
    }

    public static int getDefaultWidth(){return DEFAULT_CREATURE_HEIGHT_;}

    public static int getDefaultHeight(){return DEFAULT_CREATURE_WIDTH_;}

    public static Color getDefaultColor(){
        return defaultColor;
    }

    public int getHealth() {
        return this.health;
    }
    //Setters
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
    public void update(GameObject[] go, Creature[] creatures) {
        super.update(go, creatures);
        this.behavior.update();
    }

    @Override
    public void collide() {
        //Checks for collision with player.
        for(Creature c: cs){
            if(c.equals(this)) continue;
            if(this.hitBox.intersects(c.getHitBox())){
                if (c.getType().equals("Player")) {
                    c.damage(this.dmg);
                }
            }
        }
    }

    @Override
    public void damage(int dmg) {
        this.health -= dmg;
        this.color.darker();
    }

    public void draw(Graphics2D g, Camera camera){
        g.fillRect(this.x - camera.getX(), this.y + camera.getY(), this.width, this.height);
    }
    @Override
    public String toLevelSave() {
        String s = "";

        s += this.x + " "+ this.y + " "

        return s;
    }
}
