package LevelManagment;

import Actors.Player;
import GameController.Game;
import Objects.GameObject;
import Objects.Gate;
import States.PlayerStates.PlayerStateStack;

import javax.swing.*;
import java.util.ArrayList;

public class LevelManager {
    Level currentLevel;
    String firstLevel;
    Gate[] gates;
    Game game;

    public LevelManager(Game game){
        this.game = game;
    }

    public void tpToNewLevel(String levelName){
        Player tempPlayer = currentLevel.getPlayer();
        this.currentLevel = LevelLoader.loadLevel(levelName);
        tempPlayer.setX(currentLevel.getPlayer().getX());
        tempPlayer.setY(currentLevel.getPlayer().getY());
        currentLevel.setPlayer(tempPlayer);
    }

    public void loadLevel(String name){
        this.currentLevel = LevelLoader.loadLevel(name);
        this.firstLevel = name;
        ArrayList<Gate> gates = new ArrayList<>();
        for(GameObject o: currentLevel.getObjects()){
            if(o.getType().equals("Gate")){
                gates.add((Gate)o);
            }
        }
        this.gates = gates.toArray(new Gate[gates.size()]);
    }

    public boolean loadLevel(){
        //Shows a dialog box where the user can input a level name.
        String name = JOptionPane.showInputDialog("Enter level name");
        //Attempts to load the the level with the name.
        Level tempLevel = LevelLoader.loadLevel(name);
        //Checks if a level was loaded.
        if(tempLevel == null){
            JOptionPane.showMessageDialog(null, "No level found");
            return false;
        }
        firstLevel = tempLevel.getName();
        currentLevel = tempLevel;
        //Sets the current level to be the loaded one.
        return true;
    }

    public Level getCurrentLevel(){
        return currentLevel;
    }
    public Gate getGate(int id){
        for(Gate g: gates){
            if(g.getId() == id){
                return g;
            }
        }
        return null;
    }

    public String getFirstLevel() {
        return firstLevel;
    }

    public void setCurrentLevel(Level currentLevel) {
        this.currentLevel = currentLevel;
    }

    public void setToFirstLevel() {
        currentLevel = LevelLoader.loadLevel(firstLevel);
    }
}
