package Objects;

import GameController.Game;
import Gfx.Camera;

import java.awt.*;

//The superclass of all objects in game
public class GameObject {
    protected int x;
    protected int y;
    protected Rectangle[] hitBox;
    public Color color;
    public GameObject next;

    public GameObject(int x, int y){
        this.x = x;
        this.y = y;
    }

    public void update(){

    }

    public void draw(Graphics2D g, Camera camera){

    }

    public boolean isSolid(){
        return false;
    }

    public Rectangle[] getHitBox() {
        return hitBox;
    }
}
