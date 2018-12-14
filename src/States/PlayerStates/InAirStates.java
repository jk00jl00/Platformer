package States.PlayerStates;

import Actors.Creature;
import Actors.Player;
import GameController.Game;
import Objects.GameObject;
import Projectiles.BasicShot;
import Utilities.Util;

import java.awt.*;
import java.awt.event.KeyEvent;

import static Utilities.Util.collide;

public class InAirStates extends PlayerState {
    private boolean[] keys;

    private final double inAirXAcc= 0.2;
    private final double xMaxSpeed = 7.5;
    protected double jumpSpeed = 12.5;
    private final double maxAirSpeed = 20;


    InAirStates(Player creature, Game game) {
        super(creature, game);
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
        if(keys[KeyEvent.VK_SPACE]){
            shoot();
            game.getkl().setKey(KeyEvent.VK_SPACE, false);
        }
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
        player.dy += Player.grav;
        player.dy = Util.clamp(player.dy, -100, maxAirSpeed);

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
        nextPosition.y += Math.round(player.dy);

        for(GameObject o: player.getGos())
            if (o.isSolid()) {
                if(collide(o.getHitBox(), nextPosition)){
                    if (currentPosition.y + player.getHeight() <= o.getHitBox().y) {
                        nextPosition.y = o.getHitBox().y - player.getHeight();
                    } else{
                        nextPosition.y = o.getHitBox().y + o.getHitBox().height;
                    }
                    player.dy = 0;
                    if(player.getY() + player.getWidth() <= o.getHitBox().y){
                        exit();
                    }
                }
            }

        if(!nextPosition.equals(currentPosition)){
            player.setX(nextPosition.x);
            player.setY(nextPosition.y);
            player.setHitbox(nextPosition.x, nextPosition.y);
        }

        if(player.dx > 0) player.setFacing(Creature.Facing.Right);
        else if(player.dx < 0)player.setFacing(Creature.Facing.Left);
    }

    @Override
    public void enter() {

    }

    @Override
    protected void exit() {
        PlayerStateStack.pop();

    }
}
