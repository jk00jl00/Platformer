package States.GameStates;

import States.GameStates.MainMenuTexts.MenuText;
import States.PlayerStates.PlayerStateStack;

import java.awt.*;
import java.awt.event.KeyEvent;

public class PauseMenuState extends State{
    private final boolean levelEdit;
    private final MenuText[] menuTexts = new MenuText[]{
            new MenuText(200, new Font(Font.SANS_SERIF, Font.BOLD, 15), "Continue"),
            new MenuText(250, new Font(Font.SANS_SERIF, Font.BOLD, 15), "Exit to Menu"),
            new MenuText(300, new Font(Font.SANS_SERIF, Font.BOLD, 15), "Exit")
    };

    public PauseMenuState(boolean b) {
        this.levelEdit = b;
    }

    @Override
    public void update() {
        boolean[] keys = game.getkl().getKeysPressed();
        game.getLevelManager().getCurrentLevel().getPlayer().setKeys(keys);

        if(keys[KeyEvent.VK_ESCAPE]){
            exitMenu();
        }
        //Makes sure the menu is drawn before actions can be made and also returns if the user has not pressed left click.
        if(menuTexts[0].clickBox == null || game.getml().lClick == null) return;
        //Returns if a menutext was clicked.
        if (checkClicks()) return;

        for(MenuText m: menuTexts){
            if(m.clickBox.intersects(game.getml().cPos)){
                m.highlighted = true;
            } else if(m.highlighted) m.highlighted = false;
        }
    }

    /**
     * Called when the user presses the escape key.
     * Exits this state and returns to whatever state can before it.
     */
    private void exitMenu() {
        game.getkl().setKey(KeyEvent.VK_ESCAPE, false);
        game.getLevelManager().getCurrentLevel().setDarker(false);
        State.pop();
    }

    /**
     * Checks if the user has clicked on one of the menu texts.
     * @return true if a text has been pressed.
     */
    private boolean checkClicks() {
        for (MenuText m : menuTexts) {
            if(m.clickBox.intersects(game.getml().lClick)){
                switch (m.text){
                    case "Exit":
                        System.exit(0);
                        break;
                    case "Exit to Menu":
                        game.getml().setLClick(null);
                        State.pop();
                        State.currentState.exit();
                        State.currentState = new MainMenu();
                        State.currentState.init();
                        //Makes sure the player does not have a state in the stack.
                        while(PlayerStateStack.getCurrent() != null) PlayerStateStack.pop();
                        game.getLevelManager().getCurrentLevel().setDarker(false);
                        return true;
                    case "Continue":
                        game.getml().setLClick(null);
                        State.pop();
                        game.getLevelManager().getCurrentLevel().setDarker(false);
                        return true;

                }
            }
        }
        return false;
    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor((game.getLevelManager().getCurrentLevel().getDarker()) ? Color.DARK_GRAY.darker() : Color.DARK_GRAY);
        g.fillRect(0, 0, game.getWidth() + 100, game.getHeight() + 100);

        game.getLevelManager().getCurrentLevel().draw(g, game.getCamera());
        for (MenuText m: menuTexts) {
            if(m.clickBox == null) m.init(game.getDisplay(), g, this.levelEdit);
            g.setFont(m.font);

            g.setColor(Color.RED);

            if(m.highlighted)
                g.setColor(Color.BLUE);
            g.drawString(m.text, m.clickBox.x, m.y);

            g.setColor(Color.BLUE);
            g.drawRect(m.clickBox.x, m.clickBox.y, m.clickBox.width, m.clickBox.height);
        }
    }

    @Override
    public void init() {
        game.getLevelManager().getCurrentLevel().setDarker(true);
    }
}
