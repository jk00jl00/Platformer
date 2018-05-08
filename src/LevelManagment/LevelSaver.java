package LevelManagment;

import Actors.Creature;
import Objects.GameObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class LevelSaver {
    public static void saveGameState(Level level, String name){

    }

    /**
     * Called when saving a level in level editor.
     * Saves the individual objects in a level to a .lvl document to be read later by the level loader.
     * @param level The level to be saved.
     * @param name The desired name of the .lvl file.
     */
    static void saveLevel(Level level, String name){
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(new File("levels/" + name + ".lvl")))){
            //StringBuilder in order to build strings more efficiently.
            StringBuilder sb = new StringBuilder();

            //Appends the first line which will specifies weather or not the players hp will be the default if not it appends the hp.
            if(level.getPlayer().getHealth() !=  3){
                sb.append("true ");
                sb.append(level.getPlayer().getHealth()).append(" ");
            } else sb.append("false ");

            //Appends the players position.
            sb.append(level.getPlayer().getX()).append(" ").append(level.getPlayer().getY()).append("\n");

            //Appends how many GameObjects there are.
            sb.append(level.getObjects().length);
            sb.append("\n");

            //Loops through the GameObjects and appends them.
            for(GameObject o: level.getObjects()){
                sb.append(o.getType()).append(" ");
                sb.append(o.toLevelSave());
                sb.append("\n");
            }

            //Repeats for Creatures - player.
            sb.append(level.getCreatures().length - 1).append("\n");

            for(Creature c: level.getCreatures()){
                if(c.getType().equals("Player")) continue;

                sb.append(c.getType()).append(" ");
                sb.append(c.toLevelSave());
                sb.append("\n");
            }

            //Writes the level to the document.
            bw.write(sb.toString());

        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
