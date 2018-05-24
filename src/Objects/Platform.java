package Objects;

import Gfx.Camera;

import java.awt.*;

public class Platform extends GameObject{
    private static Color defaultColor = Color.BLACK;

    public Platform(int x, int y, int width, int height) {
        //Makes sure the platforms are at least 5 pixels wide
        super(x, y, (width < 5) ? 5 : width, (height < 5) ? 5 : height);
        this.type = "Platform";
        this.color = Color.BLACK;
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
    public String toLevelSave() {
        StringBuilder sb = new StringBuilder();

        sb.append(this.x).append(" ").append(this.y).append(" ").append(this.width).append(" ").append(this.height).append(" ");

        return sb.toString();
    }

    public static Color getDefaultColor(){
        return defaultColor;
    }
}
