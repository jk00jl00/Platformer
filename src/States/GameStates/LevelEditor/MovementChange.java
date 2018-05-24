package States.GameStates.LevelEditor;

import Actors.Creature;
import Objects.GameObject;

public class MovementChange extends Change{

    public MovementChange(int o, int c) {
        this.object = new GameObject[o];
        this.dox = new int[o];
        this.doy = new int[o];
        this.creature = new Creature[c];
        this.dcx = new int[c];
        this.dcy = new int[c];
    }
}
