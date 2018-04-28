package Objects;

import Gfx.Camera;
import Utilities.Util;

import java.awt.*;

//The superclass of all objects in game
public class GameObject{
    protected int x;
    protected int y;
    protected Rectangle hitBox;
    public Color color;
    public GameObject next;
    public boolean removed;
    protected String type;
    private int width;
    private int height;

    public String getType() {
        return type;
    }

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

    public Rectangle getHitBox() {
        return hitBox;
    }

    public String toLevelSave() {
        return null;
    }

    public void move(int x, int y) {
        this.x = Util.clamp(this.x + x, 0, 10000 - this.width);
        this.y = Util.clamp(this.y + y, 0, 10000 - this.height);
        this.hitBox.x = this.x;
        this.hitBox.y = this.y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
