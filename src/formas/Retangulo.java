package formas;

import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public class Retangulo extends Forma {
    
    private Polygon rect;
    private final ArrayList<Point2D> pontos;
    
    public Retangulo(double x1, double y1, double largura, double altura){
        super();
        pontoFixo.setLocation(x1, y1);
        
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
        rect = new Polygon(x, y, pontos.size());
        forma = rect;
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
            g.setColor(COR_SELECIONADA);
        } else if (mouseOver) {
            g.setColor(COR_MOUSE_OVER);
        } else {
            g.setColor(cor);
        }
        
        if (fill){
            g.fill(rect);
        } else {
            g.draw(rect);
        }
    }

    @Override
    public void setDistancia(int posX, int posY, boolean modoOrtho) {
        double x = posX;
        double y = posY;
        
        if (modoOrtho) {
            double largura = Math.abs(pontoFixo.getX() - x);
            double altura = Math.abs(pontoFixo.getY() - y);
            
            if (largura > altura){
                if (pontoFixo.getX() < x)
                    x = pontoFixo.getX() + altura;
                else
                    x = pontoFixo.getX() - altura;
            } else {
                if (pontoFixo.getY() < y)
                    y = pontoFixo.getY() + largura;
                else 
                    y = pontoFixo.getY() - largura;
            }
        }
        
        Rectangle2D ret = new Rectangle2D.Double();
        ret.setFrameFromDiagonal(pontoFixo.getX(), pontoFixo.getY(), x, y);
        setPontos(ret.getX(), ret.getY(), ret.getWidth(), ret.getHeight());
        setPoligono();
    }

    @Override
    protected void setForma(ArrayList<Point2D> pontos) {
        setPoligono();
    }

    @Override
    public ArrayList<Point2D> getPontos() {
        return pontos;
    }
    
    public Rectangle2D getRect2D(){
        return rect.getBounds2D();
    }
}
