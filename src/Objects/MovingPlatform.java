package Objects;

import java.awt.*;

public class MovingPlatform extends GameObject {
    private static final Color defaultColor = Color.BLACK;

    private int maxMove = 200;
    private int dir = 1;
    private int startX;
    private int startY;


    public MovingPlatform(int x, int y, int width, int height) {
        //Makes sure the platforms are at least 5 pixels wide
        super(x, y, (width < 5) ? 5 : width, (height < 5) ? 5 : height);
        this.startX = x;
        this.startY = y;
        this.type = "MovingPlatform";
        this.color = Color.BLACK;
    }

    public static Color getDefaultColor(){
        return defaultColor;
    }

    @Override
    public boolean isSolid() {
        return true;
    }

    @Override
    public void update() {
        if(Math.abs(this.x - this.startX) > maxMove) dir = -dir;
        this.move(dir, 0);
    }

    public int getDir(){
        return dir;
    }

    @Override
    public String toLevelSave() {
        StringBuilder sb = new StringBuilder();

        sb.append(this.x).append(" ").append(this.y).append(" ").append(this.width).append(" ").append(this.height).append(" ");

        return sb.toString();
    }
}
