package States.PlayerStates;

import Actors.Player;
import GameController.Game;

public abstract class PlayerState {
    protected Player player;
    protected Game game;

    PlayerState(Player player, Game game){
        this.player = player;
        this.game = game;
    }


    public abstract void update();
    public abstract void handleKeys();
    public abstract void move();
    public abstract void enter();
    protected abstract void exit();

}
