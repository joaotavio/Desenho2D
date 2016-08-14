package formas;

import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class Circulo extends Forma {
    
    private final Ellipse2D circulo;
    
    public Circulo(double x1, double y1, double x2, double y2){
        super();
        pontoFixo.setLocation(x1, y1);
        circulo = new Ellipse2D.Double();
        circulo.setFrameFromDiagonal(x1, y1, x2, y2);
        forma = circulo;
    }

    @Override
    public void desenhar(Graphics2D g) {
        if (selecionada){
            g.setColor(COR_SELECIONADA);
        } else if (mouseOver) {
            g.setColor(COR_MOUSE_OVER);
        } else {
            g.setColor(cor);
        }
        
        g.draw(circulo);
    }

    @Override
    public void setDistancia(int posX, int posY, boolean modoOrtho) {
        double x = posX;
        double y = posY;
        
        if (modoOrtho) {
            double varX = Math.abs(pontoFixo.getX() - x); // Variação de X
            double varY = Math.abs(pontoFixo.getY() - y); // Variação de Y
            if (varX < varY){
                x = pontoFixo.getX()+varY;
            } else {
                y = pontoFixo.getY()+varX;
            }
        }
        
        circulo.setFrameFromCenter(pontoFixo.getX(), pontoFixo.getY(), x, y);
    }

    @Override
    protected void setForma(ArrayList<Point2D> pontos) {
        Point2D p1 = pontos.get(0);
        Point2D p2 = pontos.get(1);
        circulo.setFrameFromDiagonal(p1.getX(), p1.getY(), p2.getX(), p2.getY());
    }

    @Override
    public ArrayList<Point2D> getPontos() {
        ArrayList<Point2D> pontos = new ArrayList<>();
        pontos.add(new Point2D.Double(circulo.getMinX(), circulo.getMinY()));
        pontos.add(new Point2D.Double(circulo.getMaxX(), circulo.getMaxY()));
        return pontos;
    }
    
}
