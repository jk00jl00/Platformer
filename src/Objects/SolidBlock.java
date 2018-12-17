package Objects;

import java.awt.*;

public class SolidBlock extends GameObject{

    private static final Color defaultColor = Color.decode("#44240b");

    public SolidBlock(int x, int y, int width, int height) {
        super(x, y, width, height);
        this.type = "SolidBlock";
        this.color = Color.decode("#44240b");
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
