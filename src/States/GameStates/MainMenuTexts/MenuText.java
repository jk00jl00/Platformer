package States.GameStates.MainMenuTexts;

import Gfx.Display;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class MenuText {
    public final int y;
    public final Font font;
    public final String text;
    public Rectangle clickBox;
    public boolean highlighted = false;

    public MenuText(int y, Font font, String text){
        this.font = font;
        this.text = text;
        this.y = y;
    }
    public void init(Display d, Graphics2D g, boolean edit){
        g.setFont(font);
        Rectangle2D r = g.getFontMetrics().getStringBounds(text , g);
        this.clickBox = new Rectangle( (edit) ? (d.getWidth() - d.getWidth()/10)/2 - (int)r.getWidth()/2 : d.getWidth()/2 - (int)r.getWidth()/2,
                y - (int) r.getHeight(), (int)r.getWidth(), (int)r.getHeight() + 10);
    }
}
