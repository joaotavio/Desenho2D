package gui;

import controle.ControleDesenho;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JPanel;

public class PainelDesenho extends JPanel {
    
    public static final Color COR_BACKGROUND = new Color(33, 40, 48);
    
    public static final int LARGURA = 980;
    public static final int ALTURA = 640;
    
    private int dx;
    private int dy;
    
    private final ControleDesenho controleDesenho;
    
    public PainelDesenho(ControleDesenho controle) {
        dx = 0;
        dy = 0;
        this.controleDesenho = controle;
        iniciarComponente();
    }
    
    private void iniciarComponente(){
        this.setBackground(COR_BACKGROUND);
        setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
        this.addMouseListener(controleDesenho.getMouseInput());
        this.addMouseMotionListener(controleDesenho.getMouseInput());
        this.addMouseWheelListener(controleDesenho.getMouseInput());
        this.addKeyListener(controleDesenho.getTecladoInput());
        
        this.setPreferredSize(new Dimension(LARGURA, ALTURA));
    }
    
    public void atualizar(){
        this.repaint();
    }
    
    public void setPan(boolean isPan){
        if (isPan) {
            setCursor(new Cursor(Cursor.MOVE_CURSOR));
        } else {
            setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
        }
    }
    
    public void pan(int dx, int dy){
        this.dx += dx;
        this.dy += dy;
    }
    
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        desenharGrid(g2d);
        
        controleDesenho.desenhar(g2d);
    }
    
    private void desenharGrid(Graphics2D g){
        g.setColor(COR_BACKGROUND.brighter());
        int tam_grid = (int)(50*controleDesenho.getZoomAcc());
        
        if (dx > tam_grid){
            dx = 0;
        }
        
        if (dy > tam_grid){
            dy = 0;
        }
        
        for (int i = dx; i < this.getWidth(); i+=tam_grid) {
            g.drawLine(i, 0, i, this.getHeight());
        }
        for (int i = dy; i < this.getWidth(); i+=tam_grid) {
            g.drawLine(0, i, this.getWidth(), i);
        }
    }
}
