package States.GameStates.CreatureStates;

import Actors.Creature;

public class CreatureBehavior {
    protected Creature creature;
    protected double dy = 0;
    protected double dx = 0;

    protected CreatureBehavior(Creature creature){
        this.creature = creature;
    }

    public void update(){

    }
    public void move(int x, int y){
        this.creature.move(x, y);
    }
}
