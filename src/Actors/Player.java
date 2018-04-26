package Actors;

import Objects.GameObject;


import java.awt.*;
import java.awt.event.KeyEvent;

public class Player extends Creature{
    private double dy;
    private double dx;

    private double xAccceleration = 0.2;
    private double jumpSpeed = 15;

    private static final double grav = 0.5;

    private GameObject[] gos;
    private boolean[] keys;
    private boolean jumping = false;

    public Player(int x, int y, int health) {
        super(x, y, health);
        this.width = Creature.DEFAULT_CREATURE_WIDTH_;
        this.height = Creature.DEFAULT_CREATURE_HEIGHT_;
        this.hitBox = new Rectangle(this.x, this.y,this.width, this.height);
    }

    @Override
    public void update(GameObject[] go){
        gos = go;
        move();
        updatePosition();
        collide();
    }

    private void move() {
        if(keys[KeyEvent.VK_W]){
            jump();
        }
        if(!keys[KeyEvent.VK_D] && !keys[KeyEvent.VK_A]){
            dx = 0;
        } else {
            if (keys[KeyEvent.VK_D]) {
                if(dx < 0){
                    dx = 0;
                }
                dx += xAccceleration;
                Math.max(dx, 1);

            }
            if (keys[KeyEvent.VK_A]) {
                if(dx > 0){
                    dx = 0;
                }
                dx -= xAccceleration;
                Math.min(dx, -1);
            }
        }
    }

    private void jump() {
        if (!jumping) {
            dy -= jumpSpeed;
            jumping = true;
        }
    }

    private void collide() {

    }

    private boolean collide(Rectangle r, Rectangle p){
       return r.intersects(p);
    }

    private void updatePosition() {
        dy += grav;

        Rectangle currentPosition = this.hitBox;
        Rectangle nextPosition = new Rectangle(this.x, this.y, this.width, this.height);

        nextPosition.x += Math.round(dx);

        for(GameObject o: gos)
            if (collide(o.getHitBox()[0], nextPosition)) {
                nextPosition.x = currentPosition.x;
                nextPosition.x = o.getHitBox()[0].x - this.width;
                dx = 0;
            }
        nextPosition.y += Math.round(dy);

        for(GameObject o: gos)
            if(collide(o.getHitBox()[0], nextPosition)){
                nextPosition.y = currentPosition.y;
                dy = 0;
                jumping = false;
            }

        if(!nextPosition.equals(currentPosition)){
            this.x = nextPosition.x;
            this.y = nextPosition.y;
            this.hitBox.x = this.x;
            this.hitBox.y = this.y;
        }
    }

    public void draw(Graphics2D g){
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(this.x, this.y, this.width, this.height);
    }

    public void setKeys(boolean[] b) {
        this.keys = b;
    }
}
