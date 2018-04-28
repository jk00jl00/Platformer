package States.GameStates;

import States.GameStates.MainMenuTexts.MenuText;

import java.awt.*;
import java.awt.event.KeyEvent;

public class MenuState extends State{
    private boolean[] keys;
    private MenuText[] menuTexts = new MenuText[]{
            new MenuText(200, new Font(Font.SANS_SERIF, Font.BOLD, 15), "Continue"),
            new MenuText(250, new Font(Font.SANS_SERIF, Font.BOLD, 15), "Exit to Menu"),
            new MenuText(300, new Font(Font.SANS_SERIF, Font.BOLD, 15), "Exit")
    };

    @Override
    public void update() {
        keys = game.getkl().getKeysPressed();
        game.getLevel().getPlayer().setKeys(keys);

        if(keys[KeyEvent.VK_ESCAPE]){
            exitMenu();
        }
        if(menuTexts[0].clickBox == null || game.getml().lClick == null) return;

        if (checkClicks()) return;

        for(MenuText m: menuTexts){
            if(m.clickBox.intersects(game.getml().cPos)){
                m.highlighted = true;
            } else if(m.highlighted) m.highlighted = false;
        }
    }

    private void exitMenu() {
        game.getkl().setKey(KeyEvent.VK_ESCAPE, false);
        game.getLevel().setDarker(false);
        State.pop();
    }

    private boolean checkClicks() {
        for (MenuText m : menuTexts) {
            if(m.clickBox.intersects(game.getml().lClick)){
                switch (m.text){
                    case "Exit":
                        System.exit(0);
                        break;
                    case "Exit to Menu":
                        game.getml().setLClick(null);
                        State.currentState = new MainMenu();
                        game.getLevel().setDarker(false);
                        return true;
                    case "Continue":
                        game.getml().setLClick(null);
                        State.pop();
                        game.getLevel().setDarker(false);
                        return true;

                }
            }
        }
        return false;
    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor((game.getLevel().getDarker()) ? Color.DARK_GRAY.darker() : Color.DARK_GRAY);
        g.fillRect(0, 0, game.getWidth(), game.getHeight());

        game.getLevel().draw(g, game.getCamera());
        for (MenuText m: menuTexts) {
            if(m.clickBox == null) m.init(game.getDisplay(), g);
            g.setFont(m.font);

            g.setColor(Color.RED);

            if(m.highlighted)
                g.setColor(Color.BLUE);
            g.drawString(m.text, game.getWidth()/2 - (int)g.getFontMetrics().getStringBounds(m.text, g).getWidth()/2, m.y);

            g.setColor(Color.BLUE);
            g.drawRect(m.clickBox.x, m.clickBox.y, m.clickBox.width, m.clickBox.height);
        }
    }

    @Override
    public void init() {
        game.getLevel().setDarker(true);
    }
}
