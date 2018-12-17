package States.PlayerStates;

import Actors.Creature;
import Actors.Player;
import GameController.Game;
import Objects.GameObject;
import Objects.Gate;
import Objects.MovingPlatform;
import Objects.Platform;
import Projectiles.BasicShot;
import States.GameStates.PlayState;
import States.GameStates.State;
import Utilities.Util;
import com.sun.corba.se.impl.oa.poa.ActiveObjectMap;

import java.awt.*;
import java.awt.event.KeyEvent;

public class OnGroundStates extends PlayerState {
    boolean[] keys;

    final double xAcceleration = 0.5;
    final double xMaxSpeed = 7.5;
    protected double jumpSpeed = 12.5;
    protected double onPlatformSpeed;

    public OnGroundStates(Player player, Game game) {
        super(player, game);
    }

    @Override
    public void update() {
        this.keys = player.getKeys();
        onPlatformSpeed = 0;
        checkIfFalling();
    }

    private void checkIfFalling() {
        player.dy += Player.grav;

        Rectangle nextPosition = new Rectangle(player.getX(), player.getY(), player.getWidth(), player.getHeight());

        nextPosition.y += Math.round(player.dy);
        for(GameObject o: player.getGos())
            if (o.isSolid()) {
                if(collide(o.getHitBox(), nextPosition)) {
                    player.dy = 0;
                    if(o.getType().equals("MovingPlatform")){
                        onPlatformSpeed = (double) ((MovingPlatform)o).getDir();
                    }
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
        if(keys[KeyEvent.VK_F]){
            Gate g = (Gate)Util.collide(player.getHitBox(), game.getLevelManager().getCurrentLevel().getObjects(), "Gate");
            if(g != null){
                activateGate(g);
                keys[KeyEvent.VK_W] = false;
                game.getkl().setKey(KeyEvent.VK_F, false);
            }
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
        if(game.getkl().arrowKey()){
            shoot();
        }
    }

    private void activateGate(Gate g) {
        game.getLevelManager().tpToNewLevel(g.getEndLeveL());
    }

    private void shoot() {
        if(player.canShoot()) {
            double dx = (keys[KeyEvent.VK_RIGHT]) ? 1 : (keys[KeyEvent.VK_LEFT]) ? -1 : 0;
            double dy = (keys[KeyEvent.VK_DOWN]) ? 1 : (keys[KeyEvent.VK_UP]) ? -1 : 0;

            if(dx != 0&& dy != 0){
                dx /= 2;
                dy /=2;
            }

            game.getLevelManager().getCurrentLevel().addProjectile(new BasicShot(this.player, dx, dy, game));
            player.shot();
        }
    }

    @Override
    public void move() {
        Rectangle currentPosition = player.getHitBox();
        Rectangle nextPosition = new Rectangle(player.getX(), player.getY(), player.getWidth(), player.getHeight());

        nextPosition.x += Math.round(player.dx);
        if(onPlatformSpeed != 0) nextPosition.x += onPlatformSpeed;

        for(GameObject  o: player.getGos())
            if (o.isSolid()) {
                if (collide(o.getHitBox(), nextPosition)) {
                    if (currentPosition.x <= o.getHitBox().x) {
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
