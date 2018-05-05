package States.PlayerStates;

import Actors.Player;
import GameController.Game;

public class JumpingState extends InAirStates{
    JumpingState(Player creature, Game game) {
        super(creature, game);
    }

    @Override
    public void update() {
        super.update();
        if(player.dy > 0){
            exit();
            PlayerStateStack.push(new FallingState(player, game));
            PlayerStateStack.getCurrent().update();
        }
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
    public void enter() {
        player.dy -= player.jumpSpeed;
    }

    @Override
    public void exit() {
        PlayerStateStack.pop();
    }
}
