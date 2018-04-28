package States.PlayerStates;

import Actors.Player;
import Objects.GameObject;

import java.awt.*;
import java.awt.event.KeyEvent;

public class OnGroundStates extends CreatureState{
    protected boolean[] keys;

    protected double xAcceleration = 0.5;
    protected double xMaxSpeed = 7.5;
    protected double jumpSpeed = 12.5;

    public OnGroundStates(Player player) {
        super(player);
    }

    @Override
    public void update() {
        this.keys = player.getKeys();
        checkIfFalling();
    }

    private void checkIfFalling() {
        player.dy += Player.grav;

        Rectangle currentPosition = player.getHitBox();
        Rectangle nextPosition = new Rectangle(player.getX(), player.getY(), player.getWidth(), player.getHeight());

        nextPosition.y += Math.round(player.dy);
        for(GameObject o: player.getGos())
            if (o.isSolid()) {
                if(collide(o.getHitBox(), nextPosition)) {
                    player.dy = 0;
                    return;
                }
            }


        PlayerState.push(new FallingState(player));
        PlayerState.getCurrent().enter();
        PlayerState.getCurrent().update();
    }

    private boolean collide(Rectangle r, Rectangle n) {
        return r.intersects(n);
    }

    public void handleKeys(){
        if(keys[KeyEvent.VK_D] || keys[KeyEvent.VK_A]){
            PlayerState.push(new WalkingState(player));
            PlayerState.getCurrent().enter();
            PlayerState.getCurrent().update();
        }
        if(keys[KeyEvent.VK_W]){
            PlayerState.push(new InAirStates(player));
            PlayerState.push(new JumpingState(player));
            PlayerState.getCurrent().enter();
            PlayerState.getCurrent().update();
        }
        if(!(keys[KeyEvent.VK_D] || keys[KeyEvent.VK_A])) {
            player.dx = 0;
        }
    }

    @Override
    public void move() {
        Rectangle currentPosition = player.getHitBox();
        Rectangle nextPosition = new Rectangle(player.getX(), player.getY(), player.getWidth(), player.getHeight());

        nextPosition.x += Math.round(player.dx);

        for(GameObject o: player.getGos())
            if (o.isSolid()) {
                if (collide(o.getHitBox(), nextPosition)) {
                    if (currentPosition.x + player.getWidth() <= o.getHitBox().x) {
                        nextPosition.x = o.getHitBox().x - player.getWidth();
                    } else{
                        nextPosition.x = o.getHitBox().x +o.getHitBox().width;
                    }
                    player.dx = 0;
                }
            }

        if(!nextPosition.equals(currentPosition)){
            player.setX(nextPosition.x);
            player.setHitbox(nextPosition.x, nextPosition.y);
        }
    }

    protected void collide(){

    }

    @Override
    public void enter() {
        //Landing anim
        //startTimer before Idle
    }

    @Override
    public void exit() {
        //blend to next

    }
}
