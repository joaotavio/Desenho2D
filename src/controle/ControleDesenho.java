package controle;

import gui.PainelDesenho;
import gui.TelaPrincipal;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import javax.swing.Timer;

public class ControleDesenho implements MouseListener, MouseMotionListener, ActionListener {
    
    private final TelaPrincipal tela;
    private final PainelDesenho painelDesenho;
    
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

    public ControleDesenho() {
        linhas = new ArrayList<>();
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
    
    @Override
    public void actionPerformed(ActionEvent e) {
        atualizar();
        desenhar();
    }
    
    @Override
    public void mouseClicked(MouseEvent e) {
        setMousePos(e);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        setMousePos(e);
        if (!desenhando){
            linhaAtual = new Line2D.Double(mouseX, mouseY, mouseX, mouseY);
        } else {
            linhas.add(linhaAtual);
        }
        desenhando = !desenhando;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        setMousePos(e);
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) { 
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        setMousePos(e);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        setMousePos(e);
        
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
    
    private void setMousePos(MouseEvent e){
        int i;
        for (i = 0; i < linhas.size(); i++){
            Line2D.Double linha = linhas.get(i);
            if (getRectProximidade(linha.x1, linha.y1).contains(e.getPoint())){
                pontoProximidade = linha.getP1();
                break;
            }
            if (getRectProximidade(linha.x2, linha.y2).contains(e.getPoint())){
                pontoProximidade = linha.getP2();
                break;
            }
        }
        if (i >= linhas.size()){
            pontoProximidade = null;
            mouseX = e.getX();
            mouseY = e.getY();
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
}
