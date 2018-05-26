package States.GameStates.LevelEditor;

import Actors.Creature;
import Objects.GameObject;

class RemovalChange extends Change{
    public RemovalChange(int o, int c) {
        this.object = new GameObject[o];
        this.creature = new Creature[c];
        this.deleted = true;
    }
}
