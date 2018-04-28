package Gfx;

import Actors.Creature;

//Keeps the offset in order to make centering the camera easier
public class Camera {
    private int width;
    private int height;
    private int x;
    private int y = 0;
    Creature centerOn;

    public Camera(Creature centerOn, int width, int height){
        this.centerOn = centerOn;
        this.width = width;
        this.height = height;
    }

    public void update(){
        if (centerOn != null) {
            x = centerOn.getX() - width/2  + centerOn.getWidth()/2;
            y = -(centerOn.getY() - height/2 + centerOn.getHeight()/2);
            if(x < 0) x = 0;
            if(y > 0) y = 0;
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
