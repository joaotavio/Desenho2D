package formas;

import java.awt.geom.Point2D;

public class Transformacao2D {
    
    public static final double GRAU_RAD = Math.PI / (double) 180.0;
    
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
        double seno = Math.sin(theta * GRAU_RAD);
        double cos = Math.cos(theta * GRAU_RAD);
        double x = (cos*p.getX()) - (seno*p.getY()) + (ref.getY()*seno - ref.getX()*cos + ref.getX());
        double y = (seno*p.getX()) + (cos*p.getY()) + (-ref.getX()*seno - ref.getY()*cos + ref.getY());
        return new Point2D.Double(x, y);
    }
}
