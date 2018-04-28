package Listeners;

import Actors.Creature;
import Objects.GameObject;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class MouseListener implements java.awt.event.MouseListener, MouseMotionListener {
    public Rectangle lClick;
    public Rectangle cPos = new Rectangle(0, 0, 2, 3);
    public Rectangle dragTangle = new Rectangle(0,0,0,0);
    public boolean dragging = false;
    public boolean rectReady;
    public boolean draggingSelection = false;
    private boolean hasSelection;
    private boolean inEdit;
    private GameObject[] selectedObjects;

    private int xDrag;
    private int yDrag;
    private Creature[] selectedCreatures;

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            lClick = new Rectangle(e.getX(), e.getY(), 2, 3);
            dragTangle.x = lClick.x;
            dragTangle.y = lClick.y;
            dragTangle.width = 1;
            dragTangle.height = 1;
            if (hasSelection) {
                for (GameObject o : selectedObjects) {
                    if (o.getHitBox().intersects(dragTangle)) {
                        draggingSelection = true;
                        xDrag = 0;
                        yDrag = 0;
                        break;
                    }
                }
                if (!draggingSelection  ) {
                    for (Creature c : selectedCreatures) {
                        if (c.getHitBox().intersects(dragTangle)) {
                            draggingSelection = true;
                            xDrag = 0;
                            yDrag = 0;
                            break;
                        }
                    }
                }
            }
            dragging = true;

        }
    }

    @Override
public void mouseReleased(MouseEvent e) {
        dragging = false;
        if (e.getButton() ==  MouseEvent.BUTTON1) {
            rectReady = true;
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if(draggingSelection){
            xDrag = e.getX() - lClick.x;
            yDrag = e.getY() - lClick.y;
            lClick.x += xDrag;
            lClick.y += yDrag;
            return;
        }

        if (dragging && inEdit) {
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

    @Override
    public void mouseMoved(MouseEvent e) {
        cPos.x = e.getX();
        cPos.y = e.getY();
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

    public int getXDrag() {
        return xDrag;
    }

    public int getYDrag() {
        return yDrag;
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
}
