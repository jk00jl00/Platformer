package LevelManagment;

import Actors.Creature;
import Actors.Player;
import Gfx.Camera;
import Objects.GameObject;
import Projectiles.Projectile;

import java.awt.*;
import java.util.ArrayList;
//TODO - Only give the player the objects on screen in update.
public class Level{
    private String name;

    private final ArrayList<GameObject> gameObjects = new ArrayList<>();
    private final ArrayList<Creature> gameCreatures = new ArrayList<>();
    private final ArrayList<Projectile> projectiles = new ArrayList<>();

    //If the game is paused darker will be true and the screen will darken.
    private boolean darker = false;

    private Player player;
    private int playerStartX;
    private int playerStartY;

    public Level(){
        player = new Player(640,360);
        gameCreatures.add(player);
    }

    Level(String name){
        this.name = name;
        player = new Player(0,0);
        gameCreatures.add(player);
    }

    //Getters
    public Player getPlayer() {
        return player;
    }

    public boolean getDarker() {
        return darker;
    }

    public Creature[] getCreatures() {
        return gameCreatures.toArray(new Creature[gameCreatures.size()])    ;
    }

    public String getName() {
        return name;
    }

    public GameObject[] getObjects() {
        return gameObjects.toArray(new GameObject[gameObjects.size()]);
    }
    //Setters

    public void setDarker(boolean b){
        this.darker = b;
    }

    public void setPlayer(Player player) {
        this.player = player;
        this.playerStartX = player.getX();
        this.playerStartY = player.getY();
        if (gameCreatures.size() > 0) {
            gameCreatures.set(0, player);
        } else{
            gameCreatures.add(player);
        }
    }

    public void setName(String tempName) {
        this.name = tempName;
    }

    public void update(){
        for(GameObject o: gameObjects){
            o.update();
        }
        for(int i = 0; i < gameCreatures.size(); i++){
            gameCreatures.get(i).update(gameObjects.toArray(new GameObject[gameObjects.size()]), gameCreatures.toArray(new Creature[gameCreatures.size()]));
            //Removes the creature if it has no hp
            if(!gameCreatures.get(i).getType().equals("Player") && gameCreatures.get(i).getHealth() <= 0) this.removeCreature(gameCreatures.get(i));
        }
        for(int i = 0; i < projectiles.size(); i++){
            projectiles.get(i).update(gameObjects.toArray(new GameObject[gameObjects.size()]), gameCreatures.toArray(new Creature[gameCreatures.size()]));
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
        for(Projectile p: projectiles){
            g.setColor(Color.BLACK);
            p.draw(g, camera);
        }
    }

    public void resetPlayer(){
        this.player.setX(this.playerStartX);
        this.player.setY(this.playerStartY);
        this.player.dx = 0;
        this.player.dy = 0;
    }

    //Used to add objects from the editor.
    public void addGameObject(GameObject o) {
        this.gameObjects.add(o);
    }

    public void removeObject(GameObject object) {
        gameObjects.remove(object);
    }

    public void addCreature(Creature c) {
        this.gameCreatures.add(c);
    }

    public void removeCreature(Creature c) {
        gameCreatures.remove(c);
    }

    public void addProjectile(Projectile p) {
        this.projectiles.add(p);
    }

    public void removeProjectile(Projectile p) {
        this.projectiles.remove(p);
    }

    public void saveLevel(){
        LevelSaver.saveLevel(this, this.name);
    }
}
