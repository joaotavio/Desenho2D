package formas;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class Circulo extends Forma{
    
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
            g.setColor(Color.YELLOW);
        } else if (mouseOver) {
            g.setColor(Color.CYAN.darker());
        } else {
            g.setColor(Color.WHITE);
        }
        g.draw(circulo);
    }

    @Override
    public void setDistancia(int posX, int posY, boolean modoOrtho) {
        double x = Math.min(pontoFixo.getX(), posX);
        double y = Math.min(pontoFixo.getY(), posY);
        double largura = Math.abs(posX - pontoFixo.getX());
        double altura = Math.abs(posY - pontoFixo.getY());
        
        if (modoOrtho){
            if (largura < altura){
                altura = largura;
            } else {
                largura = altura;
            }
        }
        
        circulo.setFrame(x, y, largura, altura);
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
