package formas;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public abstract class Forma {
    
    protected Shape forma;
    protected Point2D pontoFixo;
    
    protected boolean mouseOver;
    protected boolean selecionada;
    
    public Forma() {
        pontoFixo = new Point2D.Double();
    }
    
    public void translacao(double dx, double dy){
        ArrayList<Point2D> pontos = getPontos();
        for (Point2D ponto : pontos) {
            ponto.setLocation(Transformacao2D.translacao(ponto, dx, dy));
        }
        pontoFixo.setLocation(Transformacao2D.translacao(pontoFixo, dx, dy));
        setForma(pontos);
    }
    
    public void escala(double sx, double sy, Point2D ref){
        ArrayList<Point2D> pontos = getPontos();
        for (Point2D ponto : pontos) {
            ponto.setLocation(Transformacao2D.escala(ponto, ref, sx, sy));
        }
        pontoFixo.setLocation(Transformacao2D.escala(pontoFixo, ref, sx, sy));
        setForma(pontos);
    }
    
    public void rotacao(double theta, Point2D ref){
        ArrayList<Point2D> pontos = getPontos();
        for (Point2D ponto : pontos) {
            ponto.setLocation(Transformacao2D.rotacao(ponto, ref, theta));
        }
        pontoFixo.setLocation(Transformacao2D.rotacao(pontoFixo, ref, theta));
        setForma(pontos);
    }
    
    public void atualizar(int mouseX, int mouseY) {
        mouseOver = forma.intersects(mouseX-5, mouseY-5, 10, 10);
    }
    
    public boolean estaSelecionada() {
        return selecionada;
    }

    public void setSelecionada(boolean s) {
        selecionada = s;
    }
    
    public boolean intersecao(Rectangle2D r) {
        if (r == null){
            return false;
        }
        return forma.intersects(r);
    }
    
    public abstract void desenhar(Graphics2D g);
    public abstract void setDistancia(int x, int y, boolean modoOrtho);
    protected abstract void setForma(ArrayList<Point2D> pontos);
    public abstract ArrayList<Point2D> getPontos();
    
}
