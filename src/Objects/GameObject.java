package Objects;

import Gfx.Camera;
import Utilities.Util;

import java.awt.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

//The superclass of all OBJECTS in game
public class GameObject{
    public static final String[] OBJECTS ={
            "Platform",
            "SolidBlock",
            "Gate"
    };
    protected final HashMap<String, Integer> intAttributes = new HashMap<>();
    protected final HashMap<String, String> stringAttributes = new HashMap<>();
    public Color color;
    public GameObject next;
    public boolean removed;
    protected int dx;
    protected int dy;
    int x;
    int y;
    int width;
    int height;
    Rectangle hitBox;
    String type;

    public GameObject(int x, int y, int width, int height){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.intAttributes.put("x", this.x);
        this.intAttributes.put("y", this.y);
        this.intAttributes.put("Width", this.width);
        this.intAttributes.put("Height", this.height);
        createHitBox();
    }

    public String getType() {
        return type;
    }

    public HashMap<String, Integer> getIntAttributes() {
        return intAttributes;
    }

    public void update(){

    }

    public void draw(Graphics2D g, Camera camera){
        g.fillRect((int)Math.round((this.x -  camera.getX()) * camera.getZoom()),(int)Math.round((this.y + camera.getY()) * camera.getZoom()),
                (int)Math.round(this.width * camera.getZoom()), (int)Math.round(this.height * camera.getZoom()));
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
        this.intAttributes.replace("x", this.x);
        this.intAttributes.replace("y", this.y);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void changeAttribute(String name, int change) {
        this.intAttributes.replace(name, change);

        this.x = this.intAttributes.get("x");
        this.y = this.intAttributes.get("y");
        this.height = this.intAttributes.get("Height");
        this.width = this.intAttributes.get("Width");
        this.hitBox.x = this.intAttributes.get("x");
        this.hitBox.y = this.intAttributes.get("y");
        this.hitBox.height = this.intAttributes.get("Height");
        this.hitBox.width = this.intAttributes.get("Width");
    }

    private void createHitBox() {
        hitBox = new Rectangle(this.x, this.y, this.width, this.height);
    }


    public GameObject copyOf(){
        GameObject o = new GameObject(0, 0, 0, 0);

        try {
            Class<?> c = this.getClass();
            Constructor<?> constr = c.getConstructor(int.class, int.class, int.class, int.class);
            o = (GameObject) constr.newInstance(new Object[]{
                    this.x,
                    this.y,
                    this.width,
                    this.height
            });
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
            e.printStackTrace();
        }

        return o;
    }

    public HashMap<String, String> getStringAttributes() {
        return stringAttributes;
    }

    public void changeStringAttribute(String name, String change) {
        this.stringAttributes.replace(name, change);
    }
}
