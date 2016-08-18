package formas;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public abstract class Forma {
    
    public static final Color COR_PADRAO = Color.WHITE;
    public static final Color COR_SELECIONADA = Color.YELLOW;
    public static final Color COR_MOUSE_OVER = Color.CYAN.darker();
    
    protected Shape forma;
    protected Color cor = COR_PADRAO;
    protected boolean fill;
    
    protected Point2D pontoFixo;
    
    protected boolean mouseOver;
    protected boolean selecionada;
    
    public Forma() {
        pontoFixo = new Point2D.Double();
        fill = false;
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
    
    public Forma janelaViewport(Point2D jan_min, Point2D jan_max, Point2D view_min, Point2D view_max){
        Forma nova = this.criarCopia();
        ArrayList<Point2D> pontos = nova.getPontos();
        for (Point2D ponto : pontos) {
            ponto.setLocation(Transformacao2D.janelaViewport(jan_min, jan_max, view_min, view_max, ponto));
        }
        nova.pontoFixo.setLocation(Transformacao2D.janelaViewport(jan_min, jan_max, view_min, view_max, nova.pontoFixo));
        nova.setForma(pontos);
        return nova;
    }
    
    public Forma viewportJanela(Point2D jan_min, Point2D jan_max, Point2D view_min, Point2D view_max){
        Forma nova = this.criarCopia();
        ArrayList<Point2D> pontos = nova.getPontos();
        for (Point2D ponto : pontos) {
            ponto.setLocation(Transformacao2D.viewportJanela(jan_min, jan_max, view_min, view_max, ponto));
        }
        nova.pontoFixo.setLocation(Transformacao2D.viewportJanela(jan_min, jan_max, view_min, view_max, nova.pontoFixo));
        nova.setForma(pontos);
        return nova;
    }
    
    public void atualizar(double mouseX, double mouseY) {
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
    
    public void setFill(boolean fill) {
        this.fill = fill;
    }

    public void setCor(Color cor) {
        this.cor = cor;
    }
    
    public abstract void desenhar(Graphics2D g);
    public abstract void setDistancia(double x, double y, boolean modoOrtho);
    protected abstract void setForma(ArrayList<Point2D> pontos);
    public abstract ArrayList<Point2D> getPontos();
    public abstract Forma criarCopia();
    
}
