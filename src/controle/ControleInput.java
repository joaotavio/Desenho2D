package controle;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

public class ControleInput implements MouseListener, MouseMotionListener, MouseWheelListener {
    
    private final ControleDesenho controleDesenho;
    
    public ControleInput(ControleDesenho controleDesenho){
        this.controleDesenho = controleDesenho;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        controleDesenho.setMousePos(e.getX(), e.getY());
    }

    @Override
    public void mousePressed(MouseEvent e) {
        controleDesenho.mouseClick(e.getX(), e.getY(), e.getButton());
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        controleDesenho.mouseRelease(e.getX(), e.getY(), e.getButton());
        //controleDesenho.setMousePos(e.getX(), e.getY());
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        controleDesenho.mouseArrastar(e.getX(), e.getY());
        //controleDesenho.setMousePos(e.getX(), e.getY());
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        controleDesenho.moverMouse(e.getX(), e.getY());
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        controleDesenho.zoom(e.getUnitsToScroll(), e.getX(), e.getY());
    }
    
}
