package Objects;

import java.awt.*;

//The superclass of all objects in game
public class GameObject {
    protected int x;
    protected int y;
    protected Rectangle[] hitBox;

    public GameObject(int x, int y){
        this.x = x;
        this.y = y;
    }

    public void update(){

    }

    public void draw(Graphics2D g){

    }

    public boolean isSolid(){
        return false;
    }

    public Rectangle[] getHitBox() {
        return hitBox;
    }
}
