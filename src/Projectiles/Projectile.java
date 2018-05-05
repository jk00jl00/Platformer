package Projectiles;

import Actors.Creature;
import GameController.Game;
import Gfx.Camera;
import Objects.GameObject;
import Utilities.Util;

import java.awt.*;

public class Projectile {
    private final Game game;
    protected int x;
    protected int y;
    protected double dx;
    protected double dy;
    protected int width;
    protected int height;

    protected Creature shooter;
    protected Rectangle hitBox;

    protected int dmg;
    protected int range;

    protected int[] shotPos = new int[2];

    protected GameObject[] gameObjects;
    protected Creature[] creatures;

    public Projectile(Creature c, double dx, double dy, Game game) {
        this.game = game;
        this.shooter = c;
        this.dx = dx;
        this.dy = dy;
        generatePos();
    }

    private void generatePos() {
        if(dx > 0){
            this.x = shooter.getX() + shooter.getWidth();
        } else{
            this.x = shooter.getX() - this.width;
        }
        this.y = shooter.getY() + shooter.getHeight()/ 2 - this.height/2;
        shotPos[0] = this.x;
        shotPos[1] = this.y;
    }

    public void update(GameObject[] g, Creature[] c){
        this.gameObjects = g;
        this.creatures = c;
        move();
        collide();
    }

    private void collide() {
        for(GameObject o: gameObjects){
            if(Util.collide(o.getHitBox(), this.hitBox)){
                game.getLevel().removeProjectile(this);
            }
        }
        for(Creature c: creatures){
            if(c.equals(shooter)) continue;
            if(Util.collide(c.getHitBox(), hitBox)){
                c.damage(this.dmg);
                game.getLevel().removeProjectile(this);
            }
        }
    }

    private void move() {
        this.x += this.dx;
        this.y += this.dy;
        this.hitBox.x = x;
        this.hitBox.y = y;
        int traveled = (this.shotPos[0] > this.x) ? this.shotPos[0] - this.x : this.x - this.shotPos[0];
        if(traveled > this.range);
    }

    public void draw(Graphics2D g, Camera camera) {
        g.fillRect(this.x - camera.getX(), this.y + camera.getY(), this.width, this.height);
    }
}
