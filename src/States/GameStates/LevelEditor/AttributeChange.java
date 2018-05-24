package States.GameStates.LevelEditor;

import Actors.Creature;
import Objects.GameObject;

public class AttributeChange extends Change{

    public AttributeChange(int o, int c){
        this.object = new GameObject[o];
        this.creature = new Creature[c];
    }
}
