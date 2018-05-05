package States.PlayerStates;

import Actors.Player;
import GameController.Game;

public class FallingState extends InAirStates {


    FallingState(Player creature, Game game) {
        super(creature, game);
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
        PlayerStateStack.pop();
    }
}
