package controle;

import formas.Forma;
import formas.Linha;
import gui.PainelDesenho;
import gui.TelaPrincipal;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import javax.swing.Timer;

public class ControleDesenho implements ActionListener {
    
    private final TelaPrincipal tela;
    private final PainelDesenho painelDesenho;
    private final MouseInput controleInput;
    
    private int mouseX;
    private int mouseY;
    private boolean desenhando;
    
    private boolean pan;
    private int mouseX_anterior;
    private int mouseY_anterior;
    
    private String formaAtual;
    
    private final ArrayList<Forma> formas;
    private Forma fAtual;
    
    public static final int PROXIMIDADE = 15;
    private Point2D pontoProximidade;
    
    public static final double FATOR_ZOOM_IN = 1.5;
    public static final double FATOR_ZOOM_OUT = 0.66666667;
    
    private final Timer timer;
    public static final int DELAY = 17;

    public ControleDesenho() {
        formas = new ArrayList<>();
        controleInput = new MouseInput(this);
        painelDesenho = new PainelDesenho(this);
        tela = new TelaPrincipal(this, painelDesenho);
        
        timer = new Timer(DELAY, this);
        timer.start();
    }
    
    private void atualizar(){
        //Snap
        pontoProximidade = null;
        for (Forma forma : formas) {
            for (Point2D ponto : forma.getPontos()) {
                if (getRectProximidade(ponto.getX(), ponto.getY()).contains(mouseX, mouseY)) {
                    pontoProximidade = ponto;
                    break;
                }
            }
        }
        if (pontoProximidade != null){
            mouseX = (int) pontoProximidade.getX();
            mouseY = (int) pontoProximidade.getY();
        }
        
        if (desenhando){
            fAtual.setDistancia(mouseX, mouseY);
        }
        
        tela.atualizarGUI();
        painelDesenho.atualizar();
    }
    
    public void desenhar(Graphics2D g){
        g.setColor(Color.WHITE);
        if (desenhando){
            fAtual.desenhar(g);
        }
        
        for (Forma forma : formas) {
            //g.drawRect(getMouseX()-5, getMouseY()-5, 10, 10);
            
            if (forma.estaSelecionada()){
                g.setColor(Color.YELLOW);
            } else {
                g.setColor(Color.WHITE);
            }
            forma.desenhar(g);
        }
        
        if (pontoProximidade != null){
            g.setColor(Color.GREEN);
            g.draw(getRectProximidade(pontoProximidade.getX(), pontoProximidade.getY()));
        }
    }
    
    public void zoom(int z, int posX, int posY){
        double zoom;
        if (z < 0){
            zoom = FATOR_ZOOM_IN;
        } else {
            zoom = FATOR_ZOOM_OUT;
        }
        
        Point2D.Double referencia = new Point2D.Double(posX, posY);
        for (Forma forma : formas) {
            forma.escala(zoom, zoom, referencia);
        }
        if (desenhando){
            fAtual.escala(zoom, zoom, referencia);
        }
    }
    
    public void pan(){
        int dx = mouseX - mouseX_anterior;
        int dy = mouseY - mouseY_anterior;
        
        for (Forma forma : formas) {
            forma.translacao(dx, dy);
        }
        if (desenhando){
            fAtual.translacao(dx, dy);
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        atualizar();
        
    }
    
    private void criaForma() {
    }
    
    public void mouseClick(int posX, int posY, int botao) {
        if (botao == MouseInput.BOTAO_ESQUERDO){
            if (!desenhando){
                fAtual = new Linha(mouseX, mouseY, mouseX, mouseY);
            } else {
                formas.add(fAtual);
            }
            desenhando = !desenhando;
        } else if (botao == MouseInput.BOTAO_MEIO){
            pan = true;
            mouseX_anterior = posX;
            mouseY_anterior = posY;
            painelDesenho.setPan(pan);
        }
            
    }
    
    public void mouseRelease(int posX, int posY, int botao){
        if (botao == MouseInput.BOTAO_MEIO){
            pan = false;
            painelDesenho.setPan(pan);
        }
    }
    
    public void mouseArrastar(int posX, int posY){
        if (pan){
            mouseX = posX;
            mouseY = posY;
            pan();
        }
        mouseX_anterior = mouseX;
        mouseY_anterior = mouseY;
    }
    
    public void moverMouse(int x, int y){
        setMousePos(x, y);
        
        
    }
    
    public void setMousePos(int x, int y){
        mouseX = x;
        mouseY = y;
        /*pontoProximidade = null;
        for (Forma forma : formas) {
            for (Point2D ponto : forma.getPontos()) {
                if (getRectProximidade(ponto.getX(), ponto.getY()).contains(x, y)) {
                    pontoProximidade = ponto;
                    break;
                }
            }
        }
        if (pontoProximidade == null){
            mouseX = x;
            mouseY = y;
        } else {
            mouseX = (int) pontoProximidade.getX();
            mouseY = (int) pontoProximidade.getY();
        }*/
    }
    
    public int getMouseX() {
        return mouseX;
    }

    public int getMouseY() {
        return mouseY;
    }

    public void setFormaAtual(String formaAtual) {
        this.formaAtual = formaAtual;
    }
    
    public Rectangle2D getRectProximidade(double x, double y){
        return new Rectangle2D.Double(x - PROXIMIDADE/2, y - PROXIMIDADE/2, PROXIMIDADE, PROXIMIDADE);
    }

    public boolean isDesenhando() {
        return desenhando;
    }

    public Point2D getPontoProximidade() {
        return pontoProximidade;
    }

    public MouseInput getControleInput() {
        return controleInput;
    }

    public boolean isPan() {
        return pan;
    }

}
