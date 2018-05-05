package States.PlayerStates;

import Actors.Player;

public abstract class PlayerState {
    protected Player player;

    PlayerState(Player player){
        this.player = player;
    }


    public abstract void update();
    public abstract void handleKeys();
    public abstract void move();
    public abstract void enter();
    protected abstract void exit();

}
