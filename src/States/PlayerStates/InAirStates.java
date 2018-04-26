package States.PlayerStates;

import Actors.Player;
import Objects.GameObject;
import Utilities.Util;

import java.awt.*;
import java.awt.event.KeyEvent;

import static Utilities.Util.collide;

public class InAirStates extends CreatureState{
    protected boolean[] keys;

    protected double inAirXAcc= 0.2;
    protected double xMaxSpeed = 7.5;
    protected double jumpSpeed = 12.5;
    protected double maxAirSpeed = 20;


    InAirStates(Player creature) {
        super(creature);
    }

    @Override
    public void update() {
        this.keys = player.getKeys();
    }

    @Override
    public void handleKeys() {
        if(keys == null) keys = player.getKeys();
        if (keys[KeyEvent.VK_D]) {
            if(player.dx < 0){
                player.dx = 0;
            }
            player.dx += inAirXAcc;
            player.dx = Util.clamp(player.dx, 0, xMaxSpeed);

        }
        if (keys[KeyEvent.VK_A]) {
            if(player.dx > 0){
                player.dx = 0;
            }
            player.dx -= inAirXAcc;
            player.dx = Util.clamp(player.dx, -xMaxSpeed, 0);
        }
        if(!keys[KeyEvent.VK_D] && !keys[KeyEvent.VK_A]){
            if(player.dx > 0.5){
                player.dx -= 0.5;
            } else if(player.dx < -0.5){
                player.dx += 0.5;
            }
        }
    }

    @Override
    public void move() {
        player.dy += Player.grav;
        player.dy = Util.clamp(player.dy, -100, maxAirSpeed);

        Rectangle currentPosition = player.getHitBox();
        Rectangle nextPosition = new Rectangle(player.getX(), player.getY(), player.getwidth(), player.getHeight());

        nextPosition.x += Math.round(player.dx);

        for(GameObject o: player.getGos())
            if (o.isSolid()) {
                if (collide(o.getHitBox()[0], nextPosition)) {
                    if (currentPosition.x + player.getwidth() <= o.getHitBox()[0].x) {
                        nextPosition.x = o.getHitBox()[0].x - player.getwidth();
                    } else{
                        nextPosition.x = o.getHitBox()[0].x +o.getHitBox()[0].width;
                    }
                    player.dx = 0;
                }
            }
        nextPosition.y += Math.round(player.dy);

        for(GameObject o: player.getGos())
            if (o.isSolid()) {
                if(collide(o.getHitBox()[0], nextPosition)){
                    if (currentPosition.y + player.getHeight() <= o.getHitBox()[0].y) {
                        nextPosition.y = o.getHitBox()[0].y - player.getHeight();
                    } else{
                        nextPosition.y = o.getHitBox()[0].y + o.getHitBox()[0].height;
                    }
                    player.dy = 0;
                    if(player.getY() + player.getwidth() <= o.getHitBox()[0].y){
                        exit();
                    }
                }
            }

        if(!nextPosition.equals(currentPosition)){
            player.setX(nextPosition.x);
            player.setY(nextPosition.y);
            player.setHitbox(nextPosition.x, nextPosition.y);
        }
    }

    @Override
    public void enter() {

    }

    @Override
    public void exit() {
        PlayerState.pop();

    }
}