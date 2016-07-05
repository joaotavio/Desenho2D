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
    
    private final ControleDesenho controle;
    
    public PainelDesenho(ControleDesenho controle) {
        this.controle = controle;
        iniciarComponente();
    }
    
    private void iniciarComponente(){
        this.setBackground(COR_BACKGROUND);
        setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
        this.addMouseListener(controle);
        this.addMouseMotionListener(controle);
    }
    
    public void atualizar(){
        this.repaint();
    }
    
    @Override
    public void paintComponent(Graphics g){
        //USAR MENOR E MAIOR PONTO PARA FAZER REPAINT APENAS DENTRO DESSES PONTOS
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        desenharGrid(g2d);
        
        g2d.setColor(Color.WHITE);
        g2d.drawOval(controle.getMouseX()-25, controle.getMouseY()-25, 50, 50);
        
        if (controle.isDesenhando()){
            g2d.draw(controle.getLinhaAtual());
        }
        
        for (Line2D.Double linha : controle.getLinhas()) {
            if (linha.intersects(controle.getMouseX(), controle.getMouseY(), 10, 10)){
                g2d.setColor(Color.YELLOW);
            } else {
                g2d.setColor(Color.WHITE);
            }
            g2d.draw(linha);
        }
        
        Point2D p = controle.getPontoProximidade();
        if (p != null){
            g2d.setColor(Color.GREEN);
            g2d.draw(controle.getRectProximidade(p.getX(), p.getY()));
        }
    }
    
    private void desenharGrid(Graphics2D g){
        //testar o tamanho da grid com o scroll
        g.setColor(COR_BACKGROUND.brighter());
        for (int i = 0; i < this.getWidth(); i+=50) {
            g.drawLine(i, 0, i, this.getHeight());
        }
        for (int i = 0; i < this.getWidth(); i+=50) {
            g.drawLine(0, i, this.getWidth(), i);
        }
    }
}
