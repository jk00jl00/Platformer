package States.PlayerStates;

import Actors.Player;
import GameController.Game;

public abstract class PlayerState {
    final Player player;
    final Game game;

    PlayerState(Player player, Game game){
        this.player = player;
        this.game = game;
    }


    public abstract void update();
    public abstract void handleKeys();
    public abstract void move();
    protected abstract void enter();
    protected abstract void exit();

}
