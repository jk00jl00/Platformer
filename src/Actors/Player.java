package Actors;

import Objects.GameObject;
import Utilities.Util;


import java.awt.*;
import java.awt.event.KeyEvent;

public class Player extends Creature{

    private enum state{
        JUMPING, WALKING, RUNNING, STANDING
    }

    private double dy;
    private double dx;

    state currentState = state.STANDING;

    private double xAccceleration = 0.5;
    private double xMaxSpeed = 10;
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
        //System.out.println("Player statistics:\n" + "dx: " + dx + "  ||  dy: " + dy);
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
                dx = Util.clamp(dx, 0, xMaxSpeed);

            }
            if (keys[KeyEvent.VK_A]) {
                if(dx > 0){
                    dx = 0;
                }
                dx -= xAccceleration;
                dx = Util.clamp(dx, -xMaxSpeed, 0);
            }
        }
    }

    private void jump() {
        if (!currentState.equals(state.JUMPING)) {
            dy -= jumpSpeed;
            currentState = state.JUMPING;
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
            if (o.isSolid()) {
                if (collide(o.getHitBox()[0], nextPosition)) {
                    if (currentPosition.x + this. width <= o.getHitBox()[0].x) {
                        nextPosition.x = o.getHitBox()[0].x - this.width;
                    } else{
                        nextPosition.x = o.getHitBox()[0].x +o.getHitBox()[0].width;
                    }
                    dx = 0;
                }
            }
        nextPosition.y += Math.round(dy);

        for(GameObject o: gos)
            if (o.isSolid()) {
                if(collide(o.getHitBox()[0], nextPosition)){
                    if (currentPosition.y + this.height <= o.getHitBox()[0].y) {
                        nextPosition.y = o.getHitBox()[0].y - this.height;
                    } else{
                        nextPosition.y = o.getHitBox()[0].y + o.getHitBox()[0].height;
                    }
                    dy = 0;
                    if(this.y + this.height <= o.getHitBox()[0].y){
                        currentState = state.WALKING;
                    }
                }
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

    public void resetPos() {
        this.x = 500;
        this.y = 0;
        this.hitBox.x = this.x;
        this.hitBox.y = this.y;
    }
}
