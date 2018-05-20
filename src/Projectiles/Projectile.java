package Projectiles;

import Actors.Creature;
import GameController.Game;
import Gfx.Camera;
import Objects.GameObject;
import Utilities.Util;

import java.awt.*;

public class Projectile {
    //For removing itself.
    private final Game game;
    //Positions and speeds.
    protected int x;
    protected int y;
    protected double dx;
    protected double dy;
    protected int width;
    protected int height;

    //The shooter and projectile hitbox.
    Creature shooter;
    Rectangle hitBox;

    int dmg;
    int range;

    //Were it was shot for checking how far it has traveled.
    int[] shotPos = new int[2];

    //The objects it can hit.
    private GameObject[] gameObjects;
    private Creature[] creatures;

    Projectile(Creature c, double dx, double dy, Game game) {
        this.game = game;
        this.shooter = c;
        this.dx = dx;
        this.dy = dy;
        generatePos();
    }

    /**
     * Called in the constructor in order to get were to spawn relative to the Creature.
     */
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

    /**
     * The default projectile update function.
     * @param g Objects to collide with.
     * @param c Creatures to collide with.
     */
    public void update(GameObject[] g, Creature[] c){
        this.gameObjects = g;
        this.creatures = c;
        move();
        checkRange();
    }

    protected void checkRange() {
        if(this.hitBox != null){
            if (Math.abs(this.x - this.shotPos[0]) > this.range)
                game.getLevel().removeProjectile(this);

        }
    }

    /**
     * Called last in the update function.
     * Moves the projectile pixel by pixel until either to move distance/frame is achieved or the projectile collides with a object.
     */
    protected void move() {
        int traveledx = 0;
        int traveledy = 0;
        while (traveledx < ((dx > 0) ? dx : -dx) || traveledy < ((dy > 0) ? dy : -dy)) {
            if (dx > 0 && traveledx < ((dx > 0) ? dx : -dx)) {
                traveledx++;
                this.x++;
                if (hitBox == null) return;
                this.hitBox.x++;
                collide();
            } else if (traveledx < ((dx > 0) ? dx : -dx)) {
                traveledx++;
                this.x--;
                if (hitBox == null) return;
                this.hitBox.x--;
                collide();
            }
            if (dy > 0 && traveledy < ((dy > 0) ? dy : -dy)) {
                traveledy++;
                this.y++;
                if (hitBox == null) return;
                this.hitBox.y++;
                collide();
            } else if (traveledy < ((dy > 0) ? dy : -dy)) {
                traveledy++;
                this.y--;
                if (hitBox == null) return;
                this.hitBox.y--;
                collide();
            }
        }
    }

    /**
     * Attempts to collide with the objects specified in the update function.
     */
    private void collide() {
        if(hitBox == null) return;
        for(GameObject o: gameObjects){
            if(Util.collide(o.getHitBox(), this.hitBox)){
                game.getLevel().removeProjectile(this);
                this.hitBox = null;
                return;
            }
        }
        for(Creature c: creatures){
            if(c.equals(shooter)) continue;
            if(Util.collide(c.getHitBox(), hitBox)){
                c.damage(this.dmg);
                game.getLevel().removeProjectile(this);
                this.hitBox = null;
                return;
            }
        }
    }

    public void draw(Graphics2D g, Camera camera) {
        g.fillRect(this.x - camera.getX(), this.y + camera.getY(), this.width, this.height);
    }
}
