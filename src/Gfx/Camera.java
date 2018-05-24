package Gfx;

import Actors.Creature;
import Utilities.Util;

//Keeps the offset in order to make centering the camera easier
public class Camera {
    private int width;
    private int height;
    private int x;
    private int y = 0;
    private int zoomlevel = 1;
    Creature centerOn;
    private int invertedZoom;

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

    public void move(int x, int y) {
        this.x = Util.clamp(this.x + x, 0, 10000);
        this.y = Util.clamp(this.y + y, -10000, 0);
    }

    public void zoomOut(int i) {
        this.zoomlevel -= i;
        if(zoomlevel == 0) zoomlevel = -2;
    }
    public void zoomIn(int i){
        this.zoomlevel += i;
        if(zoomlevel == -1) zoomlevel = 1;
    }

    public double getZoom() {
        return (zoomlevel > 0) ? zoomlevel : 1d / -zoomlevel;
    }

    public double getInvertedZoom() {
        return (zoomlevel < 0) ? -zoomlevel : 1d / zoomlevel;
    }
}
