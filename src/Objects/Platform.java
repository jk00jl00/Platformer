package Objects;

import Gfx.Camera;

import java.awt.*;

public class Platform extends GameObject{
    private int width;
    private int height;
    private boolean both;

    public Platform(int x, int y, int width, int height, boolean both) {
        super(x, y);
        this.both = both;
        this.width = width;
        this.height = height;
        createHitBox();
    }

    private void createHitBox() {
        if(both){
            hitBox = new Rectangle[4];
            hitBox[0] = new Rectangle(this.x, this.y, this.width, this.height);
            hitBox[1] = new Rectangle(this.x, this.y, 1, this.height);
            hitBox[2] = new Rectangle(this.x, this.y + this.height - 1, this.width, 1);
            hitBox[3] = new Rectangle(this.x + this.width - 1, this.y, 1, this.height);
        } else{
            hitBox = new Rectangle[1];
            hitBox[0] = new Rectangle(this.x, this.y, this.width, this.height);
        }
    }

    @Override
    public boolean isSolid() {
        return true;
    }

    @Override
    public void draw(Graphics2D g, Camera camera) {
        g.setColor(Color.BLACK);
        g.fillRect(this.x -  camera.getX(),this.y + camera.getY(), this.width, this.height);
    }
}
