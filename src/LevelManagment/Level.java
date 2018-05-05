package LevelManagment;

import Actors.Creature;
import Actors.Player;
import Gfx.Camera;
import Objects.GameObject;

import java.awt.*;
import java.util.ArrayList;

public class Level{

    private Player player;
    private ArrayList<GameObject> gameObjects = new ArrayList<>();
    private ArrayList<Creature> gameCreatures = new ArrayList<>();
    private String name;

    private boolean darker = false;

    private int playerStartX;
    private int playerStartY;

    public void setDarker(boolean b){
        this.darker = b;
    }

    public Level(){
        player = new Player(640,360);
        gameCreatures.add(player);
    }

    public Level(String name){
        this.name = name;
        player = new Player(0,0);
        gameCreatures.add(player);
    }

    public void update(){
        for(GameObject o: gameObjects){
            o.update();
        }
        for(Creature c: gameCreatures){
            c.update(gameObjects.toArray(new GameObject[gameObjects.size()]), gameCreatures.toArray(new Creature[gameCreatures.size()]));
        }
    }

    public void draw(Graphics2D g, Camera camera) {
        for(GameObject o: gameObjects){
            g.setColor((darker) ? o.color.darker() : o.color);
            o.draw(g, camera);
        }
        for(Creature c: gameCreatures){
            g.setColor((darker) ? c.color.darker() : c.color);
            c.draw(g, camera);
        }
    }

    public Player getPlayer() {
        return player;
    }

    public boolean getDarker() {
        return darker;
    }

    public void addGameObject(GameObject o) {
        this.gameObjects.add(o);
    }

    public void removeObject(GameObject object) {
        gameObjects.remove(object);
    }

    public GameObject[] getObjects() {
        return gameObjects.toArray(new GameObject[gameObjects.size()]);
    }

    public void saveLevel(){
        LevelSaver.saveLevel(this, this.name);
    }

    public Creature[] getCreatures() {
        return gameCreatures.toArray(new Creature[gameCreatures.size()])    ;
    }

    public void setPlayer(Player player) {
        this.player = player;
        this.playerStartX = player.getX();
        this.playerStartY = player.getY();
        gameCreatures.set(0, player);
    }

    public void resetPlayer(){
        this.player.setX(this.playerStartX);
        this.player.setY(this.playerStartY);
        this.player.dx = 0;
        this.player.dy = 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String tempName) {
        this.name = tempName;
    }

    public void removeCreature(Creature c) {
        gameCreatures.remove(c);
    }

    public void addCreature(Creature c) {
        this.gameCreatures.add(c);
    }
}
