package States.PlayerStates;

import Actors.Player;

public class IdleState extends OnGroundStates{

    public IdleState(Player player){
        super(player);
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
        PlayerState.pop();
    }
}
