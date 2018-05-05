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

    public static void saveLevel(Level level, String name){
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(new File("levels/" + name + ".lvl")))){
            StringBuilder sb = new StringBuilder();

            if(level.getPlayer().getHealth() !=  3){
                sb.append("true ");
                sb.append(level.getPlayer().getHealth()).append(" ");
            } else sb.append("false ");

            sb.append(level.getPlayer().getX()).append(" ").append(level.getPlayer().getY()).append("\n");

            sb.append(level.getObjects().length);
            sb.append("\n");

            for(GameObject o: level.getObjects()){
                sb.append(o.getType()).append(" ");
                sb.append(o.toLevelSave());
                sb.append("\n");
            }

            sb.append(level.getCreatures().length - 1).append("\n");

            for(Creature c: level.getCreatures()){
                if(c.getType().equals("Player")) continue;

                sb.append(c.getType()).append(" ");
                sb.append(c.toLevelSave());
                sb.append("\n");
            }

            bw.write(sb.toString());

        } catch (IOException e){
            e.printStackTrace();
        }
        //Will the player include hp?
        //Write player start coords if includes hp write hp;

        //How many gameObjects are there?
        //For every game Object
        //Write Type
        //gameObject.write(writer)

        //How may Creatures are there?
        //For every Creature
        //write type
        //gameObject.write(writer)

    }
}
