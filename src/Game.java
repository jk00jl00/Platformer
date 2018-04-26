import Gfx.Camera;
import Gfx.Display;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;

public class Game implements Runnable{
    //GameLoop variables
    private Thread thread;
    private boolean running = false;

    private long fps;
    private boolean drawQueued;

    //Screen variables
    private Display display;
    private KeyPress kl = new KeyPress();
    private int width = 1280;
    private int height = 720;
    private String title;
    private Camera camera;
    private BufferStrategy bs;
    private Graphics2D g;

    //Level variables
    Level level;


    Game(){
        this.display = new Display(width, height, "Game");
        this.display.addKeyListener(this.kl);
    }

    //Initializes variables before starting the gameLoop
    private void initialize(){
        drawQueued = true;
        level = new Level();
        level.instantiate();
    }

    //Holds the gameLoop and keeps it running
    @Override
    public void run() {
        try {
            thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        initialize();
        fps = 60;
        double timePerFrame = 1000000000/60;
        long start;
        long now;
        double dt = 0;
        start = System.nanoTime();
        long timer = 0;
        int ticks = 0;

        //GameLoop
        while (running){
            //While updates are needed update;
            now = System.nanoTime();
            dt += (now - start)/ timePerFrame;
            timer += now - start;
            start = now;
            while (dt >= 1) {
                update();
                draw();
                dt --;
                ticks++;
            }

            if(timer >= 1000000000){
                System.out.println("Frames: " + ticks);
                ticks = 0;
                timer = 0;
            }


        }


    }

    //Draws the game to the screen;
    private void draw() {
        bs = display.getBufferStrategy();
        if(bs == null){
            display.createBufferStrategy(2);
            return;
        }

        g = (Graphics2D) bs.getDrawGraphics();

        //Start of drawing
        g.setColor(Color.DARK_GRAY);
        g.fillRect(0, 0,width, height);

        level.draw(g);



        //End of Drawing

        bs.show();
        g.dispose();
        drawQueued = false;
    }

    //Updates all gameObjects
    private void update() {
        level.getPlayer().setKeys(this.kl.keysPressed);
        level.update();
    }

    //Starts the game by initializing a thread and starting the gameLoop
    public synchronized void start(){
        if(!running){
            thread = new Thread(this);
            running = true;
            thread.start();
        }
    }

    public static void main(String[] args) {
        new Game().start();
    }
}
