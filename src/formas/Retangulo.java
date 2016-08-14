package formas;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public class Retangulo extends Forma {
    
    private final Rectangle2D rect;
    private final ArrayList<Point2D> pontos;
    
    public Retangulo(double x1, double y1, double largura, double altura){
        super();
        pontoFixo.setLocation(x1, y1);
        
        pontos = new ArrayList<>(4);
        pontos.add(new Point2D.Double(x1, y1));
        pontos.add(new Point2D.Double(x1+largura, y1));
        pontos.add(new Point2D.Double(x1+largura, y1+altura));
        pontos.add(new Point2D.Double(x1, y1+altura));
        
        rect = new Rectangle2D.Double(x1, y1, largura, altura);
        forma = rect;
    }
    
    private void setPontos(double x, double y, double largura, double altura) {
        pontos.get(0).setLocation(x, y);
        pontos.get(1).setLocation(x+largura, y);
        pontos.get(2).setLocation(x, y+altura);
        pontos.get(3).setLocation(x+largura, y+altura);
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
        
        rect.setFrameFromDiagonal(pontoFixo.getX(), pontoFixo.getY(), x, y);
        setPontos(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
    }

    @Override
    protected void setForma(ArrayList<Point2D> pontos) {
        double x = pontos.get(0).getX();
        double y = pontos.get(0).getY();
        double largura = pontos.get(3).getX() - x;
        double altura = pontos.get(3).getY() - y;
        rect.setFrame(x, y, largura, altura);
    }

    @Override
    public ArrayList<Point2D> getPontos() {
        return pontos;
    }
    
    public Rectangle2D getRect2D(){
        return rect.getBounds2D();
    }
}
