package controle;

import gui.PainelDesenho;
import gui.TelaPrincipal;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import javax.swing.Timer;

public class ControleDesenho implements ActionListener {
    
    private final TelaPrincipal tela;
    private final PainelDesenho painelDesenho;
    private final ControleInput controleInput;
    
    private int mouseX;
    private int mouseY;
    private boolean desenhando;
    
    private String shape;
    
    private ArrayList<Line2D.Double> linhas;
    private Line2D.Double linhaAtual;
    
    public static final int PROXIMIDADE = 15;
    private Point2D pontoProximidade;
    
    private final Timer timer;
    public static final int DELAY = 17;
    
    public static final double FATOR_ZOOM_IN = 1.5;
    public static final double FATOR_ZOOM_OUT = 0.66666667;

    public ControleDesenho() {
        linhas = new ArrayList<>();
        controleInput = new ControleInput(this);
        painelDesenho = new PainelDesenho(this);
        tela = new TelaPrincipal(this, painelDesenho);
        
        timer = new Timer(DELAY, this);
        timer.start();
    }
    
    private void atualizar(){
        
        
        tela.atualizarGUI();
        painelDesenho.atualizar();
    }
    
    private void desenhar(){
        
    }
    
    public void zoom(int z, int posX, int posY){
        double zoom;
        if (z < 0){
            zoom = FATOR_ZOOM_IN;
        } else {
            zoom = FATOR_ZOOM_OUT;
        }
        
        Point2D.Double referencia = new Point2D.Double(posX, posY);
        for (Line2D.Double linha : linhas) {
            Point2D p1 = escala(linha.getP1(), referencia, zoom, zoom);
            Point2D p2 = escala(linha.getP2(), referencia, zoom, zoom);
            linha.setLine(p1, p2);
        }
    }
    
    // Faz a escala de um ponto p em relação ao ponto ref
    public Point2D escala(Point2D p, Point2D ref, double sx, double sy){
        double x = (sx * p.getX()) + (ref.getX() - ref.getX() * sx);
        double y = (sy * p.getY()) + (ref.getY() - ref.getY() * sy);
        return new Point2D.Double(x, y);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        atualizar();
        desenhar();
    }
    
    public void mouseClick(int x, int y) {
        setMousePos(x, y);
        if (!desenhando){
            linhaAtual = new Line2D.Double(mouseX, mouseY, mouseX, mouseY);
        } else {
            linhas.add(linhaAtual);
        }
        desenhando = !desenhando;
    }
    
    public void moverMouse(int x, int y){
        setMousePos(x, y);
        
        if (desenhando){
            linhaAtual.setLine(linhaAtual.getX1(), linhaAtual.getY1(), mouseX, mouseY);
        }
    }
    
    public int getMouseX() {
        return mouseX;
    }

    public int getMouseY() {
        return mouseY;
    }

    public void setShape(String shape) {
        this.shape = shape;
    }
    
    public void setMousePos(int x, int y){
        int i;
        for (i = 0; i < linhas.size(); i++){
            Line2D.Double linha = linhas.get(i);
            if (getRectProximidade(linha.x1, linha.y1).contains(x, y)){
                pontoProximidade = linha.getP1();
                break;
            }
            if (getRectProximidade(linha.x2, linha.y2).contains(x, y)){
                pontoProximidade = linha.getP2();
                break;
            }
        }
        if (i >= linhas.size()){
            pontoProximidade = null;
            mouseX = x;
            mouseY = y;
        } else {
            mouseX = (int) pontoProximidade.getX();
            mouseY = (int) pontoProximidade.getY();
        }
    }
    
    public Rectangle2D getRectProximidade(double x, double y){
        return new Rectangle2D.Double(x - PROXIMIDADE/2, y - PROXIMIDADE/2, PROXIMIDADE, PROXIMIDADE);
    }

    public boolean isDesenhando() {
        return desenhando;
    }

    public ArrayList<Line2D.Double> getLinhas() {
        return linhas;
    }

    public Line2D.Double getLinhaAtual() {
        return linhaAtual;
    }

    public Point2D getPontoProximidade() {
        return pontoProximidade;
    }

    public ControleInput getControleInput() {
        return controleInput;
    }

}
