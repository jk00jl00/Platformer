package States;

import Actors.Player;

public abstract class CreatureState {
    protected Player player;

    CreatureState(Player player){
        this.player = player;
    }


    public abstract void update();
    public abstract void handleKeys();
    public abstract void move();
    public abstract void enter();
    protected abstract void exit();

}
