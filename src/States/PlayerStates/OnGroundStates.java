package States.PlayerStates;

import Actors.Creature;
import Actors.Player;
import GameController.Game;
import Objects.GameObject;
import Projectiles.BasicShot;

import java.awt.*;
import java.awt.event.KeyEvent;

public class OnGroundStates extends PlayerState {
    protected boolean[] keys;

    protected double xAcceleration = 0.5;
    protected double xMaxSpeed = 7.5;
    protected double jumpSpeed = 12.5;

    public OnGroundStates(Player player, Game game) {
        super(player, game);
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


        PlayerStateStack.push(new FallingState(player, game));
        PlayerStateStack.getCurrent().enter();
        PlayerStateStack.getCurrent().update();
    }

    private boolean collide(Rectangle r, Rectangle n) {
        return r.intersects(n);
    }

    public void handleKeys(){
        if(keys[KeyEvent.VK_D] || keys[KeyEvent.VK_A]){
            PlayerStateStack.push(new WalkingState(player, game));
            PlayerStateStack.getCurrent().enter();
            PlayerStateStack.getCurrent().update();
        }
        if(keys[KeyEvent.VK_W]){
            PlayerStateStack.push(new InAirStates(player, game));
            PlayerStateStack.push(new JumpingState(player, game));
            PlayerStateStack.getCurrent().enter();
            PlayerStateStack.getCurrent().update();
        }
        if(!(keys[KeyEvent.VK_D] || keys[KeyEvent.VK_A])) {
            player.dx = 0;
        }
        if(keys[KeyEvent.VK_SPACE]){
            shoot();
        }
    }

    private void shoot() {
        if(player.canShoot()) {
            System.out.println(player.getFacing());
            game.getLevel().addProjectile(new BasicShot(this.player, (this.player.getFacing() == Creature.Facing.Right) ? 1 : -1, 0, game));
            player.shot();
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
        if(player.dx > 0) player.setFacing(Creature.Facing.Right);
        else if(player.dx < 0)player.setFacing(Creature.Facing.Left);
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
