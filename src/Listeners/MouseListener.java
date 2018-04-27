package Listeners;

import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class MouseListener implements java.awt.event.MouseListener, MouseMotionListener {
    public Rectangle lClick;
    public Rectangle cPos = new Rectangle(0, 0, 2, 3);
    public Rectangle dragTangle = new Rectangle(0,0,0,0);
    public boolean dragging = false;
    public boolean rectReady;

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if(e.getButton() == MouseEvent.BUTTON1){
            lClick = new Rectangle(e.getX(), e.getY(), 2, 3);
            dragTangle.x = 0;
            dragTangle.y = 0;
            dragTangle.width = 0;
            dragTangle.height = 0;
        }
        dragging = true;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        dragging = false;
        rectReady = true;
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
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

    @Override
    public void mouseMoved(MouseEvent e) {
        cPos.x = e.getX();
        cPos.y = e.getY();
    }

    public void setLClick(Rectangle lClick) {
        this.lClick = lClick;
    }
}
