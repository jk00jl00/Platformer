package States.CreatureStates;

import Actors.Creature;

class CreatureBehavior {
    final Creature creature;
    double dy = 0;
    double dx = 0;

    CreatureBehavior(Creature creature){
        this.creature = creature;
    }

    void update(){

    }
    void move(int x, int y){
        this.creature.move(x, y);
    }
}
