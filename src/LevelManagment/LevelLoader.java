package LevelManagment;

import Actors.Player;
import Objects.Platform;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class LevelLoader {
    public static Level loadLevel(String name){
        Level l =  new Level(name);
        int i;

        try(BufferedReader br = new BufferedReader(new FileReader(new File("levels/" + name + ".lvl")))){
            String currentRead = br.readLine();
            String[] currentReadSplit = currentRead.split(" ");

            if(Boolean.parseBoolean(currentReadSplit[0]))
                l.setPlayer(new Player(Integer.parseInt(currentReadSplit[1]), Integer.parseInt(currentReadSplit[2]), Integer.parseInt(currentReadSplit[2])));
            else l.setPlayer(new Player(Integer.parseInt(currentReadSplit[1]), Integer.parseInt(currentReadSplit[2])));

            currentRead = br.readLine();
            i = Integer.parseInt(currentRead);

            for(int a = 0; a < i; a++){
                currentRead = br.readLine();
                currentReadSplit = currentRead.split(" ");
                switch (currentReadSplit[0]){
                    case "platform":
                        l.addPlatform(new Platform(Integer.parseInt(currentReadSplit[1]), Integer.parseInt(currentReadSplit[2]) , Integer.parseInt(currentReadSplit[3]),
                                Integer.parseInt(currentReadSplit[4]), Boolean.parseBoolean(currentReadSplit[5])));
                        break;
                }
            }

            currentRead = br.readLine();
            i = Integer.parseInt(currentRead);

        } catch (IOException e){
            l = null;
            e.printStackTrace();
        }

        return l;
    }
}
