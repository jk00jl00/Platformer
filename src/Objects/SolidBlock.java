package Objects;

import Gfx.Camera;

import java.awt.*;

public class SolidBlock extends GameObject{

    private static Color defaultColor = Color.decode("#44240b");

    public SolidBlock(int x, int y, int width, int height) {
        super(x, y, width, height);
        this.type = "SolidBlock";
        this.color = Color.decode("#44240b");
        createHitBox();
    }

    private void createHitBox() {
        hitBox = new Rectangle(this.x, this.y, this.width, this.height);
    }

    @Override
    public boolean isSolid() {
        return true;
    }

    @Override
    public void draw(Graphics2D g, Camera camera) {
        g.fillRect(this.x -  camera.getX(),this.y + camera.getY(), this.width, this.height);
    }

    @Override
    public String toLevelSave() {
        StringBuilder sb = new StringBuilder();

        sb.append(this.x).append(" ").append(this.y).append(" ").append(this.width).append(" ").append(this.height).append(" ");

        return sb.toString();
    }

    public static Color getDefaultColor(){
        return defaultColor;
    }
}
