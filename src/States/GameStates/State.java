package States.GameStates;

import GameController.Game;

import java.awt.*;

public class State {
    public static State currentState;
    protected static Game game;

    public static void setGame(Game game) {
        State.game = game;
    }

    public void update(){

    }

    public void draw(Graphics2D g){

    }
}
