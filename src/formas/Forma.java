package formas;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public abstract class Forma {
    
    public Forma() {}
    
    public void translacao(double dx, double dy){
        ArrayList<Point2D> pontos = getPontos();
        for (Point2D ponto : pontos) {
            ponto.setLocation(Transformacao2D.translacao(ponto, dx, dy));
        }
        setForma(pontos);
    }
    
    public void escala(double sx, double sy, Point2D ref){
        ArrayList<Point2D> pontos = getPontos();
        for (Point2D ponto : pontos) {
            ponto.setLocation(Transformacao2D.escala(ponto, ref, sx, sy));
        }
        setForma(pontos);
    }
    
    public void rotacao(double theta, Point2D ref){
        ArrayList<Point2D> pontos = getPontos();
        for (Point2D ponto : pontos) {
            ponto.setLocation(Transformacao2D.rotacao(ponto, ref, theta));
        }
        setForma(pontos);
    }
    
    public abstract void desenhar(Graphics2D g);
    public abstract void atualizar(int mouseX, int mouseY);
    public abstract void setDistancia(int x, int y);
    protected abstract void setForma(ArrayList<Point2D> listaPontos);
    public abstract ArrayList<Point2D> getPontos();
    public abstract boolean estaSelecionada();
    public abstract void setSelecionada(boolean s);
    public abstract boolean intersecao(Rectangle2D r);
    
}
