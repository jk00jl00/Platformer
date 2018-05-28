package GameController;

import Gfx.Camera;
import Gfx.Display;
import LevelManagment.Level;
import LevelManagment.LevelManager;
import Listeners.ButtonListener;
import Listeners.KeyPress;
import Listeners.MouseListener;
import States.GameStates.MainMenu;
import States.GameStates.PlayState;
import States.GameStates.State;
import States.PlayerStates.PlayerStateStack;

import java.awt.*;
import java.awt.image.BufferStrategy;

public class Game implements Runnable{
    //Screen variables
    private final Display display;
    private final KeyPress kl = new KeyPress();
    private final MouseListener ml = new MouseListener();
    private final ButtonListener bl = new ButtonListener();
    private final int width = 1280;
    private final int height = 720;
    //GameLoop variables
    private Thread thread;
    private boolean running = false;
    //Used when the level loads so that updates don't fall behind.
    private boolean justLoaded = false;
    private String title;
    private Camera camera;

    //Level variables
    private LevelManager lm;


    public Game(){
        this.display = new Display(width, height, "Game");
        this.display.addKeyListener(this.kl);
        this.display.addMouseListener(this.ml);
        this.display.addMouseMotionListener(this.ml);
        bl.addDisplay(display);
    }

    //Initializes variables before starting the gameLoop
    private void initialize(){
        State.setGame(this);
        State.currentState = new MainMenu();
        lm = new LevelManager(this);
    }

    //Holds the gameLoop and keeps it running
    @Override
    public void run() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        initialize();
        int fps = 60;
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
            //Checks if the game just loaded a level and skips extra frames.
            if(justLoaded){
                dt = 0;
                justLoaded = false;
            }
            now = System.nanoTime();
            dt += (now - start)/ timePerFrame;
            timer += now - start;
            start = now;
            if (dt >= 1) {
                State.currentState.update();
                draw();
                dt --;
                ticks++;
            }
            if(timer >= 250000000){
                //System.out.println("Frames: " + ticks * 4);
                //if(PlayerStateStack.getCurrent() != null)System.out.println("State: " + PlayerStateStack.getCurrent());
                //if(level != null)System.out.println("dx: " + level.getPlayer().dx + "  ||  dy: " + level.getPlayer().dy);
                //if(level != null) System.out.println("x: " + this.camera.getX() + ", y: " + this.camera.getY());
                ticks = 0;
                timer = 0;
            }
        }
    }

    //Draws the game to the screen;
    private void draw() {
        BufferStrategy bs = display.getBufferStrategy();
        if(bs == null){
            display.createBufferStrategy(2);
            return;
        }

        Graphics2D g = (Graphics2D) bs.getDrawGraphics();

        //Start of drawing

        State.currentState.draw(g);

        //End of Drawing

        bs.show();
        g.dispose();
    }

    //Starts the game by initializing a thread and starting the gameLoop
    public synchronized void start(){
        if(!running){
            thread = new Thread(this);
            running = true;
            thread.start();
        }
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

    public LevelManager getLevelManager() {
        return lm;
    }

    public KeyPress getkl() {
        return kl;
    }

    public Camera getCamera() {
        return camera;
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
        this.ml.setCamera(camera);
    }

    public void setJustLoaded(boolean justLoaded) {
        this.justLoaded = justLoaded;
    }

    public ButtonListener getbl() {
        return bl;
    }

    public void resetLevel() {
        while(PlayerStateStack.getCurrent() != null) PlayerStateStack.pop();
        lm.setFirstLevel();
    }
}
