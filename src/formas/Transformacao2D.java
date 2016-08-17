package formas;

import java.awt.geom.Point2D;
import java.math.BigDecimal;

public class Transformacao2D {
    
    private Transformacao2D() {}
    
    /* 
        Faz a translação do ponto p com os valores dx e dy.
    */
    public static Point2D translacao(Point2D p, double dx, double dy){
        double x = p.getX() + dx;
        double y = p.getY() + dy;
        return new Point2D.Double(x, y);
    }
    
    /* 
        Faz a escala de um ponto p, com os valores sx e sy, em relação ao ponto ref.
    */
    public static Point2D escala(Point2D p, Point2D ref, double sx, double sy){
        double x = (sx * p.getX()) + (ref.getX() - ref.getX() * sx);
        double y = (sy * p.getY()) + (ref.getY() - ref.getY() * sy);
        return new Point2D.Double(x, y);
    }
    
    /* 
        Faz a rotação de um ponto p, com o ângulo theta, em relação ao ponto ref.
    */
    public static Point2D rotacao(Point2D p, Point2D ref, double theta){
        double seno = Math.sin(Math.toRadians(theta));
        double cos = Math.cos(Math.toRadians(theta));
        double x = (cos*p.getX()) - (seno*p.getY()) + (ref.getY()*seno - ref.getX()*cos + ref.getX());
        double y = (seno*p.getX()) + (cos*p.getY()) + (-ref.getX()*seno - ref.getY()*cos + ref.getY());
        return new Point2D.Double(x, y);
    }
    
    /* 
        Faz a transformação Janela-Viewport.
    */
    public static Point2D janelaViewport(Point2D jan_min, Point2D jan_max, Point2D view_min, Point2D view_max, Point2D p){
        double umax = view_max.getX();
        double vmax = view_max.getY();
        double umin = view_min.getX();
        double vmin = view_min.getY();
        
        double xmax = jan_max.getX();
        double ymax = jan_max.getY();
        double xmin = jan_min.getX();
        double ymin = jan_min.getY();
        
        double sx = (umax - umin)/(xmax - xmin);
        double sy = (vmax - vmin)/(ymax - ymin); 
        
        double x = (sx * p.getX()) - (sx * xmin + umin);
        double y = (-sy * p.getY()) + (sy * ymax + vmin);
        
        return new Point2D.Double(x, y);
    }
    
    /* 
        Faz a transformação Viewport-Janela.
        1  0 0 | 1 0  0 | 1 0 xmin | 1/sx    0  0 | 1 0 -umin |
        0 -1 0 | 0 1 -z | 0 1 ymin | 0    1/sy  0 | 0 1 -vmin |
        0  0 1 | 0 0  1 | 0 0    0 | 0       0  1 | 0 0    1  |
    
        z = volta da janela invertida pro primeiro quadrante = ymax+ymin
    
        | 1/sx    0    1/sx*-umin+xmin     | 
        | 0    -1/sy  -(1/sy*-vmin+ymin-z) | 
        | 0       0                1       | 
    */
    public static Point2D viewportJanela(Point2D jan_min, Point2D jan_max, Point2D view_min, Point2D view_max, Point2D p){
        double umax = view_max.getX();
        double vmax = view_max.getY();
        double umin = view_min.getX();
        double vmin = view_min.getY();
        
        double xmax = jan_max.getX();
        double ymax = jan_max.getY();
        double xmin = jan_min.getX();
        double ymin = jan_min.getY();
        
        double sx = (umax - umin)/(xmax - xmin);
        double sy = (vmax - vmin)/(ymax - ymin); 
        
        double x = (1/sx * p.getX()) + (-1/sx * umin + xmin);
        double y = (-1/sy * p.getY()) - (-1/sy * vmin - ymax);
        
        return new Point2D.Double(x, y);
    }
}
