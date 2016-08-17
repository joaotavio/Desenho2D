package controle;

import formas.Transformacao2D;
import java.awt.geom.Point2D;

public class Janela {
    
    private double xmin = 0;
    private double ymin = 0;
    private double xmax = 980;
    private double ymax = 640;
    
    private double mouseX;
    private double mouseY;
    
    public Janela(){
        
    }

    public Janela(double xmin, double ymin, double xmax, double ymax) {
        this.xmin = xmin;
        this.ymin = ymin;
        this.xmax = xmax;
        this.ymax = ymax;
    }
    
    public void setJanela(double xmin, double ymin, double xmax, double ymax) {
        this.xmin = xmin;
        this.ymin = ymin;
        this.xmax = xmax;
        this.ymax = ymax;
    }
    
    public void zoom(double zoom, Point2D referencia){
        Point2D p1 = Transformacao2D.escala(new Point2D.Double(xmin, ymin), referencia, 1/zoom, 1/zoom);
        Point2D p2 = Transformacao2D.escala(new Point2D.Double(xmax, ymax), referencia, 1/zoom, 1/zoom);
        setJanela(p1.getX(), p1.getY(), p2.getX(), p2.getY());
    }
    
    public void atualizarMouse(double x, double y){
        mouseX = x;
        mouseY = y;
    }

    public double getXmin() {
        return xmin;
    }

    public double getYmin() {
        return ymin;
    }

    public double getXmax() {
        return xmax;
    }

    public double getYmax() {
        return ymax;
    }

    public double getMouseX() {
        return mouseX;
    }

    public double getMouseY() {
        return mouseY;
    }
    
}
