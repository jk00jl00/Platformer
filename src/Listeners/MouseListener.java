package Listeners;

import Actors.Creature;
import Gfx.Camera;
import Objects.GameObject;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class MouseListener implements java.awt.event.MouseListener, MouseMotionListener{
    //Positions
    public Rectangle lClick;
    private Rectangle rClick;
    public Rectangle cPos = new Rectangle(0, 0, 2, 3);
    public Rectangle dragTangle = new Rectangle(0,0,0,0);

    //Checks for dragging in different ways.
    public boolean dragging = false;
    public boolean rectReady;
    public boolean draggingSelection = false;
    private boolean hasSelection;

    //Checks to make sure the user is in the editor.
    private boolean inEdit;

    //Arrays to check if user is trying to drag objects.
    private GameObject[] selectedObjects;
    private Creature[] selectedCreatures;

    //Variables for placing creatures.
    private int creatureWidth = 0;
    private int creatureHeight = 0;
    public boolean placingCreature;

    //How much the user has dragged the mouse.
    private int xDrag = 0;
    private int yDrag = 0;

    private Camera camera;
    public boolean rDrag;
    public boolean rClicked;

    public int getXDrag() {
        return xDrag;
    }

    public int getYDrag() {
        return yDrag;
    }

    public Rectangle getRClick() {
        return rClick;
    }

    public Rectangle getDragTangle() {
        return new Rectangle(dragTangle.x, dragTangle.y, dragTangle.width, dragTangle.height);
    }

    public void setLClick(Rectangle lClick) {
        this.lClick = lClick;
    }

    public void setInEdit(boolean b) {
        this.inEdit = b;
    }

    public void setHasSelection(boolean hasSelection) {
        this.hasSelection = hasSelection;
    }

    public void setSelectedObjects(GameObject[] selectedObjects) {
        this.selectedObjects = selectedObjects;
    }

    public void setXDrag(int XDrag) {
        this.xDrag = XDrag;
    }

    public void setYDrag(int YDrag) {
        this.yDrag = YDrag;
    }

    public void setSelectedCreatures(Creature[] selectedCreatures) {
        this.selectedCreatures = selectedCreatures;
    }

    public void setRClick(Rectangle r) {
        this.rClick = r;
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    public void setCreatureDims(int width, int height){
        this.creatureWidth = width;
        this.creatureHeight = height;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        //Checks if the user pressed left mousebutton.
        if (e.getButton() == MouseEvent.BUTTON1) {
            lClick = new Rectangle(e.getX(), e.getY(), 2, 3);
            dragTangle.x = lClick.x;
            dragTangle.y = lClick.y;
            dragTangle.width = 1;
            dragTangle.height = 1;
            if (hasSelection) {
                //Checks if trying to drag.
                Rectangle tempBox = new Rectangle((int)Math.ceil((dragTangle.x * camera.getInvertedZoom()) + camera.getX()),
                        (int)Math.ceil((dragTangle.y * camera.getInvertedZoom()) - camera.getY()),(int)Math.ceil(dragTangle.width * camera.getInvertedZoom()),
                        (int)Math.ceil(dragTangle.height * camera.getInvertedZoom()));
                for (GameObject o : selectedObjects) {
                    if (o.getHitBox().intersects(tempBox)) {
                        draggingSelection = true;
                        xDrag = 0;
                        yDrag = 0;
                        break;
                    }
                }
                if (!draggingSelection  ) {
                    for (Creature c : selectedCreatures) {
                        if (c.getHitBox().intersects(tempBox)) {
                            draggingSelection = true;
                            xDrag = 0;
                            yDrag = 0;
                            break;
                        }
                    }
                }

            }
            if(placingCreature){
                //If the user is placing a creature the top left corner will be by the mouse and the width and height will match the creature.
                dragTangle.x = e.getX();
                dragTangle.y = e.getY();
                dragTangle.width = creatureWidth;
                dragTangle.height = creatureHeight;
            }
            dragging = true;

        }
        if(e.getButton() == MouseEvent.BUTTON3){
            this.rClick = new Rectangle(e.getX(), e.getY(), 2, 3);
            xDrag = 0;
            yDrag = 0;
            this.rDrag = true;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getButton() ==  MouseEvent.BUTTON1 && dragging) {
            rectReady = true;
        } else if(e.getButton() == MouseEvent.BUTTON3){
            rDrag = false;
            rClicked = true;
        }
        dragging = false;
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (inEdit) {
            if(placingCreature){
                dragTangle.x = e.getX();
                dragTangle.y = e.getY();
                dragTangle.width = creatureWidth;
                dragTangle.height = creatureHeight;
                return;
            }

            if(draggingSelection){
                xDrag += e.getX() - lClick.x;
                yDrag += e.getY() - lClick.y;
                lClick.x += xDrag;
                lClick.y += yDrag;
                xDrag *= camera.getInvertedZoom();
                yDrag *= camera.getInvertedZoom();
                return;
            }
            if(rDrag){
                xDrag += e.getX() - rClick.x;
                yDrag += e.getY() - rClick.y;
                rClick.x += xDrag;
                rClick.y += yDrag;
                xDrag *= camera.getInvertedZoom();
                yDrag *= camera.getInvertedZoom();
                return;
            }

            if (dragging) {
                if(lClick.x < e.getX()){
                    dragTangle.x = lClick.x;
                    dragTangle.width = e.getX() - lClick.x;
                } else if(lClick.x > e.getX()){
                    dragTangle.x = e.getX();
                    dragTangle.width = lClick.x - e.getX();
                }

                if(lClick.y < e.getY()){
                    dragTangle.y = lClick.y;
                    dragTangle.height = e.getY() - lClick.y;
                } else if (lClick.y > e.getY()){
                    dragTangle.y = e.getY();
                    dragTangle.height = lClick.y - e.getY();
                }
            }
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        cPos.x = e.getX();
        cPos.y = e.getY();
    }
}
