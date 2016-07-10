package formas;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public class Retangulo extends Forma{
    
    private Polygon ret;
    private final ArrayList<Point2D> pontos;
    private final double x_fixo;
    private final double y_fixo;
    
    private boolean mouseOver;
    private boolean selecionada;
    private boolean fill;
    
    private Color cor;
    
    public Retangulo(double x1, double y1, double largura, double altura){
        x_fixo = x1;
        y_fixo = y1;
        cor = Color.WHITE;
        pontos = new ArrayList<>(4);
        pontos.add(new Point2D.Double(x1, y1));
        pontos.add(new Point2D.Double(x1+largura, y1));
        pontos.add(new Point2D.Double(x1+largura, y1+altura));
        pontos.add(new Point2D.Double(x1, y1+altura));
        
        setPoligono();
    }
    
    private void setPoligono() {
        int[] x = {(int)pontos.get(0).getX(), (int)pontos.get(1).getX(), (int)pontos.get(2).getX(), (int)pontos.get(3).getX()};
        int[] y = {(int)pontos.get(0).getY(), (int)pontos.get(1).getY(), (int)pontos.get(2).getY(), (int)pontos.get(3).getY()};
        ret = new Polygon(x, y, pontos.size());
    }
    
    private void setPontos(double x, double y, double largura, double altura) {
        pontos.get(0).setLocation(x, y);
        pontos.get(1).setLocation(x+largura, y);
        pontos.get(2).setLocation(x+largura, y+altura);
        pontos.get(3).setLocation(x, y+altura);
    }

    @Override
    public void desenhar(Graphics2D g) {
        if (selecionada){
            g.setColor(Color.YELLOW);
        } else if (mouseOver) {
            g.setColor(Color.CYAN.darker());
        } else {
            g.setColor(cor);
        }
        if (fill){
            g.fill(ret);
        } else {
            g.draw(ret);
        }
    }

    @Override
    public void atualizar(int mouseX, int mouseY) {
        mouseOver = ret.intersects(mouseX-5, mouseY-5, 10, 10);
    }

    @Override
    public void setDistancia(int posX, int posY) {
        double x = Math.min(this.x_fixo, posX);
        double y = Math.min(this.y_fixo, posY);
        double largura = Math.abs(posX - this.x_fixo);
        double altura = Math.abs(posY - this.y_fixo);
        setPontos(x, y, largura, altura);
        setPoligono();
    }

    @Override
    protected void setForma(ArrayList<Point2D> listaPontos) {
        setPoligono();
    }

    @Override
    public ArrayList<Point2D> getPontos() {
        return pontos;
    }

    @Override
    public boolean estaSelecionada() {
        return selecionada;
    }

    @Override
    public void setSelecionada(boolean s) {
        selecionada = s;
    }

    @Override
    public boolean intersecao(Rectangle2D r) {
        if (r == null){
            return false;
        }
        return ret.intersects(r);
    }
    
    public void setFill(boolean fill) {
        this.fill = fill;
    }

    public void setCor(Color cor) {
        this.cor = cor;
    }
    
    public Rectangle2D getRect2D(){
        return ret.getBounds2D();
    }
}
