package States.GameStates;


import States.GameStates.MainMenuTexts.MenuText;

import java.awt.*;

public class MainMenu extends State{

    MenuText[] menuTexts = new MenuText[4];

    public MainMenu(){
        menuTexts[0] = new MenuText(175, new Font(Font.SANS_SERIF, Font.BOLD, 25), "Main Menu");
        menuTexts[1] = new MenuText(275, new Font(Font.SANS_SERIF, Font.BOLD, 15), "Start");
        menuTexts[2] = new MenuText(325, new Font(Font.SANS_SERIF, Font.BOLD, 15), "Options");
        menuTexts[3] = new MenuText(375, new Font(Font.SANS_SERIF, Font.BOLD, 15), "Exit");

    }



    @Override
    public void draw(Graphics2D g) {
        g.setColor(Color.lightGray);
        g.clearRect(0, 0, game.getWidth(), game.getHeight());
        for (MenuText m: menuTexts) {
            if(m.clickBox == null) m.init(game.getDisplay(), g);
            g.setFont(m.font);
            if(!m.text.equals("Main Menu"))
                g.setColor(Color.RED);

            if(m.highlighted)
                g.setColor(Color.BLUE);
            g.drawString(m.text, game.getWidth()/2 - (int)g.getFontMetrics().getStringBounds(m.text, g).getWidth()/2, m.y);

            g.setColor(Color.BLUE);
            g.drawRect(m.clickBox.x, m.clickBox.y, m.clickBox.width, m.clickBox.height);
        }
    }

    @Override
    public void update() {
        if(menuTexts[0].clickBox == null || game.getml().lClick == null) return;
        for (MenuText m : menuTexts) {
            if(m.clickBox.intersects(game.getml().lClick)){
                switch (m.text){
                    case "Exit":
                        System.exit(0);
                        break;
                    case "Start":
                        State.currentState = new PlayState();
                        State.currentState.init();
                }
            }
        }
        for(MenuText m: menuTexts){
            if(m.clickBox.intersects(game.getml().cPos) && !m.text.equals("Main Menu")){
                m.highlighted = true;
            } else if(m.highlighted) m.highlighted = false;
        }
    }
}
