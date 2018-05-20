package Objects;

import Gfx.Camera;
import Utilities.Util;

import java.awt.*;
import java.util.HashMap;

//The superclass of all OBJECTS in game
public class GameObject{
    protected int x;
    protected int y;
    protected int width;
    protected int height;
    protected int dx;
    protected int dy;
    protected Rectangle hitBox;
    public Color color;
    public GameObject next;
    public boolean removed;
    protected String type;

    protected HashMap<String, Integer> attributes = new HashMap<>();

    public static final String[] OBJECTS ={
            "Platform",
            "SolidBlock"
    };

    public String getType() {
        return type;
    }

    public HashMap<String, Integer> getAttributes() {
        return attributes;
    }

    public GameObject(int x, int y, int width, int height){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.attributes.put("x", this.x);
        this.attributes.put("y", this.y);
        this.attributes.put("Width", this.width);
        this.attributes.put("Height", this.height);
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
