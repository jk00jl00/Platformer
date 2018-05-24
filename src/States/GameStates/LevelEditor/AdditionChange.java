package States.GameStates.LevelEditor;

import Actors.Creature;
import Objects.GameObject;

public class AdditionChange extends Change {
    public AdditionChange(int o, int c) {
        this.object = new GameObject[o];
        this.creature = new Creature[c];
        this.deleted = false;
    }
}
