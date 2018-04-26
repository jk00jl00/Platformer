package GameController;

import Gfx.Camera;
import Gfx.Display;
import LevelManagment.Level;
import Listeners.KeyPress;
import Listeners.MouseListener;
import States.GameStates.MainMenu;
import States.GameStates.State;
import States.PlayerStates.PlayerState;

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
    private MouseListener ml = new MouseListener();
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
        this.display.addMouseListener(this.ml);
        this.display.addMouseMotionListener(this.ml);
    }

    //Initializes variables before starting the gameLoop
    private void initialize(){
        drawQueued = true;
        level = new Level();
        level.instantiate();
        camera = new Camera(level.getPlayer(), width, height);
        State.setGame(this);
        State.currentState = new MainMenu();
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
                State.currentState.update();
                draw();
                dt --;
                ticks++;
            }

            if(timer >= 1000000000){
                System.out.println("Frames: " + ticks);
                System.out.println("State: " + PlayerState.getCurrent());
                System.out.println("dx: " + level.getPlayer().dx + "  ||  dy: " + level.getPlayer().dy);
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
        /*g.setColor(Color.DARK_GRAY);
        g.fillRect(0, 0,width, height);

        level.draw(g, camera);*/
        State.currentState.draw(g);



        //End of Drawing

        bs.show();
        g.dispose();
        drawQueued = false;
    }

    //Updates all gameObjects
    private void update() {
        level.getPlayer().setKeys(this.kl.getKeysPressed());
        if(kl.getKeysPressed()[KeyEvent.VK_R]){
            level.getPlayer().resetPos();
        }
        level.update();
        camera.update();
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

    public int getWidth() {
        return width;
    }

    public MouseListener getml() {
        return ml;
    }

    public Display getDisplay() {
        return display;
    }

    public int getHeight() {
        return height;
    }

    public Level getLevel() {
        return level;
    }

    public KeyPress getkl() {
        return kl;
    }

    public Camera getCamera() {
        return camera;
    }
}
