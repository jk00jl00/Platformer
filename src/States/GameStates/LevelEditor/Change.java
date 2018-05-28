package States.GameStates.LevelEditor;

import Actors.Creature;
import Objects.GameObject;

public class Change {
    GameObject[] object;
    Creature[] creature;

    int[] dox;
    int[] dcx;

    int[] doy;
    int[] dcy;

    int oIndex;
    int cIndex;

    boolean deleted;
    boolean moved = false;

    String name;
    int change;

    Change prev;
    public String changeS;

    Change(){

    }

}
