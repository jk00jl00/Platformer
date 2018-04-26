import Actors.Creature;
import Actors.Player;
import Gfx.Camera;
import Objects.GameObject;
import Objects.Platform;

import java.awt.*;
import java.util.ArrayList;

public class Level {

    private Player player;
    private ArrayList<GameObject> gameObjects = new ArrayList<>();
    private ArrayList<Creature> gameCreatures = new ArrayList<>();

    public Level(){

    }

    public void instantiate(){
        gameObjects.add(new Platform(0, 700, 2560, 20, false));
        gameObjects.add(new Platform(100, 600, 50, 10, false));
        gameObjects.add(new Platform(150, 500, 50, 10, false));
        gameObjects.add(new Platform(200, 400, 50, 10, false));
        gameObjects.add(new Platform(400, 400, 5, 200, false));
        gameCreatures.add(new Player(50, 600, 1));
        player = (Player) gameCreatures.get(0);
    }


    public void update(){
        for(GameObject o: gameObjects){
            o.update();
        }
        for(Creature c: gameCreatures){
            c.update(gameObjects.toArray(new GameObject[gameObjects.size()]));
        }
    }

    public void draw(Graphics2D g, Camera camera) {
        for(GameObject o: gameObjects){
            o.draw(g, camera);
        }
        for(Creature c: gameCreatures){
            c.draw(g, camera);
        }
    }

    public Player getPlayer() {
        return player;
    }
}
