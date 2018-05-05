package States.PlayerStates;

import Actors.Player;
import GameController.Game;

public class IdleState extends OnGroundStates{

    public IdleState(Player player, Game game){
        super(player, game);
    }

    @Override
    public void handleKeys() {
        this.exit();
        super.handleKeys();
    }

    @Override
    public void update() {
        super.update();
    }

    @Override
    public void exit() {
        PlayerStateStack.pop();
    }
}
