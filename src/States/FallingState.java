package States;

import Actors.Player;
import Objects.GameObject;
import Utilities.Util;

import java.awt.*;

import static Utilities.Util.collide;

public class FallingState extends InAirStates {


    FallingState(Player creature) {
        super(creature);
    }


    @Override
    public void handleKeys() {
        super.handleKeys();
    }

    @Override
    public void move() {
            super.move();
    }

    @Override
    public void exit() {
        PlayerState.pop();
        super.exit();
    }
}
