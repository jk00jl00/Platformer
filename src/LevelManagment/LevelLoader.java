package LevelManagment;

import Actors.Creature;
import Actors.Player;
import Objects.GameObject;
import Objects.Gate;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class LevelLoader {
    /**
     * Loads a level.
     * @param name The name of the level to be loaded.
     * @return The loaded level.
     */
    public static Level loadLevel(String name){
        Level l =  new Level(name);
        int i;
        //Tries to read the level file.
        try(BufferedReader br = new BufferedReader(new FileReader(new File("levels/" + name + ".lvl")))){
            String currentRead = br.readLine();
            String[] currentReadSplit = currentRead.split(" ");
            //Loads the player.
            if(Boolean.parseBoolean(currentReadSplit[0]))
                l.setPlayer(new Player(Integer.parseInt(currentReadSplit[1]), Integer.parseInt(currentReadSplit[2]), Integer.parseInt(currentReadSplit[2])));
            else l.setPlayer(new Player(Integer.parseInt(currentReadSplit[1]), Integer.parseInt(currentReadSplit[2])));
            //Checks how many (i) objects to load.
            currentRead = br.readLine();
            i = Integer.parseInt(currentRead);
            //Loads i objects.
            for(int a = 0; a < i; a++){
                currentRead = br.readLine();
                currentReadSplit = currentRead.split(" ");
                //Tries to make a object of the type specified at the beginning of the line.
                try {
                    Class<?> c = Class.forName("Objects." + currentReadSplit[0]);
                    Constructor<?> constr = c.getConstructor(int.class, int.class, int.class, int.class);
                    GameObject o = (GameObject) constr.newInstance(new Object[]{
                            Integer.parseInt(currentReadSplit[1]),
                            Integer.parseInt(currentReadSplit[2]),
                            Integer.parseInt(currentReadSplit[3]),
                            Integer.parseInt(currentReadSplit[4])
                    });

                    if(currentReadSplit[0].equals("Gate")){
                        Gate g = (Gate) o;
                        g.setStartlevel(currentReadSplit[5]);
                        g.setEndlevel(currentReadSplit[6]);
                        o = g;
                    }

                    l.addGameObject(o);
                } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
                    e.printStackTrace();
                }
            }
            //Checks how many (i) creatures to load.
            currentRead = br.readLine();
            i = Integer.parseInt(currentRead);
            //Loads i creatures.
            for(int a = 0; a < i; a++){
                currentRead = br.readLine();
                currentReadSplit = currentRead.split(" ");
                //Tries to load a creature of the type specified at the beginning of the line.
                try {
                    Class<?> c = Class.forName("Actors." + currentReadSplit[0]);
                    Constructor<?> constr = c.getConstructor(int.class, int.class);
                    l.addCreature((Creature) constr.newInstance(new Object[]{
                            Integer.parseInt(currentReadSplit[1]),
                            Integer.parseInt(currentReadSplit[2])
                    }));
                } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
                    e.printStackTrace();
                }
            }


        } catch (IOException e){
            l = null;
            e.printStackTrace();
        }

        return l;
    }
}
