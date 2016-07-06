package gui;

import controle.ControleDesenho;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
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
        this.addMouseListener(controleDesenho.getControleInput());
        this.addMouseMotionListener(controleDesenho.getControleInput());
        this.addMouseWheelListener(controleDesenho.getControleInput());
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
        
        g2d.setColor(Color.WHITE);
        g2d.drawOval(controleDesenho.getMouseX()-25, controleDesenho.getMouseY()-25, 50, 50);
        
        if (controleDesenho.isDesenhando()){
            g2d.draw(controleDesenho.getLinhaAtual());
        }
        
        for (Line2D.Double linha : controleDesenho.getLinhas()) {
            if (linha.intersects(controleDesenho.getMouseX(), controleDesenho.getMouseY(), 10, 10)){
                g2d.setColor(Color.YELLOW);
            } else {
                g2d.setColor(Color.WHITE);
            }
            g2d.draw(linha);
        }
        
        Point2D p = controleDesenho.getPontoProximidade();
        if (p != null){
            g2d.setColor(Color.GREEN);
            g2d.draw(controleDesenho.getRectProximidade(p.getX(), p.getY()));
        }
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
