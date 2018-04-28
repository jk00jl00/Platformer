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

    Change prev;

    public Change(int o, int c) {
        this.moved = true;
        this.object = new GameObject[o];
        this.dox = new int[o];
        this.doy = new int[o];
        this.creature = new Creature[c];
        this.dcx = new int[c];
        this.dcy = new int[c];
    }

    public Change(int o, int c, boolean deleted) {
        this.object = new GameObject[o];
        this.creature = new Creature[c];
        this.deleted = deleted;
    }

}
