package formas;

import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class Linha extends Forma {

    private final Line2D linha;
    
    public Linha(double x1, double y1, double x2, double y2){
        super();
        pontoFixo.setLocation(x1, y1);
        linha = new Line2D.Double(x1, y1, x2, y2);
        forma = linha;
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
        
        g.draw(linha);
    }
    
    @Override
    public void setDistancia(int x, int y, boolean modoOrtho) {
        if (modoOrtho){
            double difX = Math.abs(linha.getX1() - x);
            double difY = Math.abs(linha.getY1() - y);
            if (difX > difY){
                y = (int)linha.getY1();
            } else {
                x = (int)linha.getX1();
            }
        }

        linha.setLine(linha.getX1(), linha.getY1(), x, y);
    }

    @Override
    protected void setForma(ArrayList<Point2D> pontos) {
        Point2D p1 = pontos.get(0);
        Point2D p2 = pontos.get(1);
        linha.setLine(p1, p2);
    }

    @Override
    public ArrayList<Point2D> getPontos() {
        ArrayList<Point2D> pontos = new ArrayList<>();
        pontos.add(linha.getP1());
        pontos.add(linha.getP2());
        return pontos;
    }

}
