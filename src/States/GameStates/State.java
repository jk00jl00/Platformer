package States.GameStates;

import GameController.Game;

import java.awt.*;

public class State{
    public static State currentState;
    protected static Game game;
    protected State next;

    public static void push(State state){
        state.next = currentState;
        currentState = state;
    }

    public static void pop(){
        currentState = currentState.next;
        currentState.next = null;
    }


    public static void setGame(Game game) {
        State.game = game;
    }

    public void update(){

    }

    public void draw(Graphics2D g){

    }

    public void init(){

    }
}
