package formas;

import java.awt.geom.Point2D;

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
        return null;
    }
}
