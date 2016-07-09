package gui;

import controle.ControleDesenho;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JPanel;

public class PainelDesenho extends JPanel {
    
    public static final Color COR_BACKGROUND = new Color(33, 40, 48);
    
    private final ControleDesenho controleDesenho;
    
    public PainelDesenho(ControleDesenho controle) {
        this.controleDesenho = controle;
        iniciarComponente();
    }
    
    private void iniciarComponente(){
        this.setBackground(COR_BACKGROUND);
        setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
        this.addMouseListener(controleDesenho.getMouseInput());
        this.addMouseMotionListener(controleDesenho.getMouseInput());
        this.addMouseWheelListener(controleDesenho.getMouseInput());
    }
    
    public void atualizar(){
        //USAR MENOR E MAIOR PONTO PARA FAZER REPAINT APENAS DENTRO DESSES PONTOS
        this.repaint();
    }
    
    public void setPan(boolean isPan){
        if (isPan) {
            setCursor(new Cursor(Cursor.MOVE_CURSOR));
        } else {
            setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
        }
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
        for (int i = 0; i < this.getWidth(); i+=50) {
            g.drawLine(i, 0, i, this.getHeight());
        }
        for (int i = 0; i < this.getWidth(); i+=50) {
            g.drawLine(0, i, this.getWidth(), i);
        }
    }
}
