package States.PlayerStates;

import Actors.Player;
import Utilities.Util;

import java.awt.event.KeyEvent;

public class WalkingState extends OnGroundStates {

    WalkingState(Player player) {
        super(player);
    }

    @Override
    public void update() {
        super.update();
    }

    @Override
    public void handleKeys() {
        if (keys[KeyEvent.VK_D]) {
            if(player.dx < 0){
                player.dx = 0;
            }
            player.dx += xAcceleration;
            player.dx = Util.clamp(player.dx, 0, xMaxSpeed);

        }
        if (keys[KeyEvent.VK_A]) {
            if(player.dx > 0){
                player.dx = 0;
            }
            player.dx -= xAcceleration;
            player.dx = Util.clamp(player.dx, -xMaxSpeed, 0);
        } else if(!keys[KeyEvent.VK_D]){
            player.dx = 0;
            exit();
        }

        super.handleKeys();

    }

    @Override
    public void move() {
        super.move();
    }

    @Override
    public void exit() {
        //stop
        PlayerStateStack.pop();
    }
}
