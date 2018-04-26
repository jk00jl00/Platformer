package States;

import Actors.Player;

public class JumpingState extends InAirStates{
    JumpingState(Player creature) {
        super(creature);
    }

    @Override
    public void update() {
        super.update();
        if(player.dy < 0){
            exit();
            PlayerState.push(new FallingState(player));
            PlayerState.getCurrent().update();
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
        PlayerState.pop();
    }
}
