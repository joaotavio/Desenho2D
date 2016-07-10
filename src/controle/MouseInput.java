package controle;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

public class MouseInput implements MouseListener, MouseMotionListener, MouseWheelListener {
    
    public static final int BOTAO_ESQUERDO = MouseEvent.BUTTON1;
    public static final int BOTAO_MEIO = MouseEvent.BUTTON2;
    public static final int BOTAO_DIREITO = MouseEvent.BUTTON3;
    
    private final ControleDesenho controleDesenho;
    
    public MouseInput(ControleDesenho controleDesenho){
        this.controleDesenho = controleDesenho;
    }

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {
        controleDesenho.mouseClick(e.getX(), e.getY(), e.getButton());
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        controleDesenho.mouseRelease(e.getX(), e.getY(), e.getButton());
    }

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    @Override
    public void mouseDragged(MouseEvent e) {
        controleDesenho.mouseArrastar(e.getX(), e.getY());
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        controleDesenho.moverMouse(e.getX(), e.getY());
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        if (e.getUnitsToScroll() < 0){
            controleDesenho.zoomIn(e.getX(), e.getY());
        } else {
            controleDesenho.zoomOut(e.getX(), e.getY());
        }
    }
    
}
