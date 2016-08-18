package controle;

import controle.ferramentas.Ferramenta;
import formas.Circulo;
import formas.Forma;
import formas.Linha;
import formas.Retangulo;
import formas.Transformacao2D;
import gui.PainelDesenho;
import gui.TelaPrincipal;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.Timer;

public class ControleDesenho implements ActionListener {
    
    private final TelaPrincipal tela;
    private final PainelDesenho painelDesenho;
    private final MouseInput mouseInput;
    private final TecladoInput tecladoInput;
    
    private int mouseX;
    private int mouseY;
    private boolean desenhando;
    
    private boolean selecionando;
    private Retangulo rectSelecao;
    
    private boolean panning;
    private int mouseX_anterior;
    private int mouseY_anterior;
    
    private ArrayList<Forma> formas;
    private Forma formaDesenhando;
    
    private Ferramenta ferramenta;
    
    public static final int PROXIMIDADE = 15;
    private Point2D pontoProximidade;
    
    public static final double FATOR_ZOOM_IN = 1.5;
    public static final double FATOR_ZOOM_OUT = 0.66666667;
    public static final double ZOOM_MAX = 20;
    private double zoomAcc;
    
    private boolean modoOrtho;
    
    private final Timer timer;
    public static final int DELAY = 17;
    
    private final Janela janela;
    private ArrayList<Forma> formasWCS; // formas em coordenadas do mundo

    public ControleDesenho() {
        formas = new ArrayList<>();
        formasWCS = new ArrayList<>();
        mouseInput = new MouseInput(this);
        tecladoInput = new TecladoInput(this);
        painelDesenho = new PainelDesenho(this);
        tela = new TelaPrincipal(this, painelDesenho);
        janela = new Janela();
        zoomAcc = 1;
        
        timer = new Timer(DELAY, this);
        timer.start();
        
        pedirTamanhoJanela();
    }
    
    private void pedirTamanhoJanela(){
        JSpinner xMinField = new JSpinner(new SpinnerNumberModel(janela.getXmin(), 0, 2000, 1));
        JSpinner yMinField = new JSpinner(new SpinnerNumberModel(janela.getYmin(), 0, 2000, 1));
        JSpinner xMaxField = new JSpinner(new SpinnerNumberModel(janela.getXmax(), 0, 2000, 1));
        JSpinner yMaxField = new JSpinner(new SpinnerNumberModel(janela.getYmax(), 0, 2000, 1));
        Object[] message = {
            "X Mínimo da Janela:", xMinField,
            "Y Mínimo da Janela:", yMinField,
            "X Máximo da Janela:", xMaxField,
            "Y Máximo da Janela:", yMaxField
        };

        int option = JOptionPane.showConfirmDialog(null, message, "Definir Janela", JOptionPane.DEFAULT_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            double janelaX_min = (double)xMinField.getValue();
            double janelaY_min = (double)yMinField.getValue();
            double janelaX_max = (double)xMaxField.getValue();
            double janelaY_max = (double)yMaxField.getValue();
            janela.setJanela(janelaX_min, janelaY_min, janelaX_max, janelaY_max);
        }
    }
    
    private Point2D viewportJanela(Point2D p){
        Point2D jan_min = new Point2D.Double(janela.getXmin(), janela.getYmin());
        Point2D jan_max = new Point2D.Double(janela.getXmax(), janela.getYmax());
        Point2D view_min = new Point2D.Double(0, 0);
        Point2D view_max = new Point2D.Double(painelDesenho.getWidth(), painelDesenho.getHeight());
        Point2D novoP = Transformacao2D.viewportJanela(jan_min, jan_max, view_min, view_max, p);
        return novoP;
    }
    
    private void atualizar(){
        //Snap
        pontoProximidade = null;
        for (Forma forma : formas) {
            forma.atualizar(mouseX, mouseY);
            
            if (rectSelecao != null && forma.intersecao(rectSelecao.getRect2D())){
                forma.setSelecionada(true);
            } else {
                forma.setSelecionada(false);
            }
            
            for (Point2D ponto : forma.getPontos()) {
                if (getRectProximidade(ponto.getX(), ponto.getY()).contains(mouseX, mouseY)) {
                    pontoProximidade = ponto;
                    break;
                }
            }
        }
        if (pontoProximidade != null){
            moverMouse((int) pontoProximidade.getX(), (int) pontoProximidade.getY());
        }
        
        if (desenhando){
            formaDesenhando.setDistancia(mouseX, mouseY, modoOrtho);
        }
        
        tela.atualizarGUI();
        painelDesenho.atualizar();
    }
    
    public void desenhar(Graphics2D g){
        g.setColor(Color.WHITE);
        
        if (desenhando){
            formaDesenhando.desenhar(g);
        }
        
        for (Forma forma : formas) {
            forma.desenhar(g);
        }
        
        if (pontoProximidade != null){
            g.setColor(Color.GREEN);
            g.draw(getRectProximidade(pontoProximidade.getX(), pontoProximidade.getY()));
        }
        
        if (selecionando){
            desenharSelecao(g);
        }
    }
    
    private void desenharSelecao(Graphics2D g){
        Color c1 = Color.GREEN.darker();
        Color c2 = new Color(c1.getRed(), c1.getGreen(), c1.getBlue(), 50);
        
        rectSelecao.setCor(c2);
        rectSelecao.setFill(true);
        rectSelecao.desenhar(g);
        
        Stroke original = g.getStroke();
        float dash[] = { 10.0f };
        g.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f));
        
        rectSelecao.setCor(Color.WHITE);
        rectSelecao.setFill(false);
        rectSelecao.desenhar(g);
        g.setStroke(original);
    }
    
    private void zoom(double zoom, int posX, int posY){
        if (zoomAcc*zoom > ZOOM_MAX){
            tela.mostrarMensagem("Zoom in máximo atingido.");
            return;
        }
        if (zoomAcc*zoom < 1/ZOOM_MAX){
            tela.mostrarMensagem("Zoom out máximo atingido.");
            return;
        }
        
        zoomAcc *= zoom;
        
        Point2D.Double referencia = new Point2D.Double(posX, posY);
        for (Forma forma : formas) {
            forma.escala(zoom, zoom, referencia);
        }
        if (desenhando){
            formaDesenhando.escala(zoom, zoom, referencia);
        }
        if (rectSelecao != null){
            rectSelecao.escala(zoom, zoom, referencia);
        }
        
        zoomJanela(zoom, referencia);
        /*for (int i = 0; i < formas.size(); i++) {
            formas.set(i, formasWCS.get(i).janelaViewport(getJanMin(), getJanMax(), getViewMin(), getViewMax()));
        }*/
    }
    
    public void zoomIn(int posX, int posY){
        zoom(FATOR_ZOOM_IN, posX, posY);
    }
    
    public void zoomIn(){
        zoomIn(painelDesenho.getWidth()/2, painelDesenho.getHeight()/2);
    }
    
    public void zoomOut(int posX, int posY){
        zoom(FATOR_ZOOM_OUT, posX, posY);
    }
    
    public void zoomOut(){
        zoomOut(painelDesenho.getWidth()/2, painelDesenho.getHeight()/2);
    }
    
    public void zoomExtend(){
        if (formas.isEmpty())
            return;

        //Coordeanadas Dispositivo
        Double Umax = (double) painelDesenho.getWidth();
        Double Umin = 0.0;
        Double Vmax = (double) painelDesenho.getHeight();
        Double Vmin = 0.0;

        //Coordenadas Janela
        Double Xmax = Double.MIN_VALUE;
        Double Ymax = Double.MIN_VALUE;
        Double Xmin = Double.MAX_VALUE;
        Double Ymin = Double.MAX_VALUE;

        for (Forma forma : formas) {
            for (Point2D ponto : forma.getPontos()) {
                if (ponto.getX() > Xmax) {
                    Xmax = ponto.getX();
                }
                if (ponto.getX() < Xmin) {
                    Xmin = ponto.getX();
                }
                if (ponto.getY() > Ymax) {
                    Ymax = ponto.getY();
                }
                if (ponto.getY() < Ymin) {
                    Ymin = ponto.getY();
                }
            }
        }

        Xmin = Xmin - 0.15 * Math.abs(Xmax - Xmin);
        Xmax = Xmax + 0.15*Math.abs(Xmax-Xmin);
        Ymin = Ymin - 0.15*Math.abs(Ymax-Ymin);
        Ymax = Ymax + 0.15*Math.abs(Ymax-Ymin);
        
        Point2D pmin = viewportJanela(new Point2D.Double(Xmin, Ymax));
        Point2D pmax = viewportJanela(new Point2D.Double(Xmax, Ymin));
        janela.setJanela(pmin.getX(), pmin.getY(), pmax.getX(), pmax.getY());

        //translação a origem
        for (Forma forma : formas) {
            forma.translacao(-Xmin, -Ymin);
        }

        //calculando o aspect ratio
        Double rv = (Umax - Umin) / (Vmax - Vmin);
        Double rw = (Xmax - Xmin) / (Ymax - Ymin);

        if (rw > rv) {
            Vmax = (Umax - Umin) / rw + Vmin;
        }
        if (rw < rv) {
            Umax = rw * (Vmax - Vmin) + Umin;
        }

        //Enquadramento da janela
        Double sx = (Umax - Umin) / (Xmax - Xmin);
        Double sy = (Vmax - Vmin) / (Ymax - Ymin);
        Point2D.Double origem = new Point2D.Double(0.0, 0.0);

        for (Forma forma : formas) {
            forma.escala(sx, sy, origem);
        }

        //centralizando objetos
        Xmax = Double.MIN_VALUE;
        Ymax = Double.MIN_VALUE;
        Xmin = Double.MAX_VALUE;
        Ymin = Double.MAX_VALUE;
        for (Forma forma : formas) {
            for (Point2D ponto : forma.getPontos()) {
                if (ponto.getX() > Xmax) {
                    Xmax = ponto.getX();
                }
                if (ponto.getX() < Xmin) {
                    Xmin = ponto.getX();
                }
                if (ponto.getY() > Ymax) {
                    Ymax = ponto.getY();
                }
                if (ponto.getY() < Ymin) {
                    Ymin = ponto.getY();
                }
            }
        }

        Double Umet = (double) painelDesenho.getWidth() / 2;
        Double Vmet = (double) painelDesenho.getHeight() / 2;
        Double Xmet = (Xmax + Xmin) / 2;
        Double Ymet = (Ymax + Ymin) / 2;
        Double Xtrans = Umet - Xmet;
        Double Ytrans = Vmet - Ymet;

        for (Forma forma : formas) {
            forma.translacao(Xtrans, Ytrans);
        }
        
        for (int i = 0; i < formas.size(); i++) {
            formasWCS.set(i, formas.get(i).viewportJanela(getJanMin(), getJanMax(), getViewMin(), getViewMax()));
        }
    }
    
    private void zoomJanela(double zoom, Point2D referencia){
        Point2D ref2 = viewportJanela(referencia);
        janela.zoom(zoom, ref2);
    }
    
    public void pan(){
        int dx = mouseX - mouseX_anterior;
        int dy = mouseY - mouseY_anterior;
        
        for (Forma forma : formas) {
            forma.translacao(dx, dy);
        }
        if (desenhando){
            formaDesenhando.translacao(dx, dy);
        }
        if (rectSelecao != null){
            rectSelecao.translacao(dx, dy);
        }
        painelDesenho.pan(dx, dy);
    }
    
    public void cancelarForma(){
        desenhando = false;
        formaDesenhando = null;
        rectSelecao = null;
    }
    
    public void deletarSelecionado(){
        for (int i = formas.size()-1; i >= 0; i--) {
            if (formas.get(i).estaSelecionada()){
                formas.remove(i);
                formasWCS.remove(i);
            }
        }
        rectSelecao = null;
    }
    
    public void limpar(){
        formas = new ArrayList<>();
        formasWCS = new ArrayList<>();
        rectSelecao = null;
    }
    
    public void ativarModoOrtho(){
        modoOrtho = true;
    }
    
    public void desativarModoOrtho(){
        modoOrtho = false;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        atualizar();
    }
    
    public void ferramentaMover(){
        if (rectSelecao == null){
            tela.mostrarMensagem("Não há objetos selecionados.");
            return;
        }
        
        JSpinner dxField = new JSpinner(new SpinnerNumberModel(0, -2000, 2000, 0.001));
        JSpinner dyField = new JSpinner(new SpinnerNumberModel(0, -2000, 2000, 0.001));
        Object[] message = {
            "Deslocamento em X:", dxField,
            "Deslocamento em Y:", dyField
        };

        int option = JOptionPane.showConfirmDialog(null, message, "Mover", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            double dx = (double) dxField.getValue();
            double dy = (double) dyField.getValue();
            
            /*for (Forma forma : getSelecionados()) {
                forma.translacao(dx, dy);
            }*/
            for (int i = 0; i < formas.size(); i++) {
                if (formas.get(i).estaSelecionada()) {
                    formasWCS.get(i).translacao(dx, dy);
                    formas.set(i, formasWCS.get(i).janelaViewport(getJanMin(), getJanMax(), getViewMin(), getViewMax()));
                }
            }
            rectSelecao = null;
        }
    }
    
    public void ferramentaEscala(){
        if (rectSelecao == null){
            tela.mostrarMensagem("Não há objetos selecionados.");
            return;
        }
        
        JSpinner xRefField = new JSpinner(new SpinnerNumberModel(0, -2000, 2000, 0.001));
        JSpinner yRefField = new JSpinner(new SpinnerNumberModel(0, -2000, 2000, 0.001));
        JSpinner sxField = new JSpinner(new SpinnerNumberModel(1, -2000, 2000, 0.001));
        JSpinner syField = new JSpinner(new SpinnerNumberModel(1, -2000, 2000, 0.001));
        Object[] message = {
            "X Referência:", xRefField,
            "Y Referência:", yRefField,
            "Mudança de Escala em X:", sxField,
            "Mudança de Escala em Y:", syField
        };

        int option = JOptionPane.showConfirmDialog(null, message, "Escala", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            double xRef = (double) xRefField.getValue();
            double yRef = (double) yRefField.getValue();
            double sx = (double) sxField.getValue();
            double sy = (double) syField.getValue();
            
            Point2D.Double referencia = new Point2D.Double(xRef, yRef);
            
            /*for (Forma forma : getSelecionados()) {
                forma.escala(sx, sy, referencia);
            }*/
            for (int i = 0; i < formas.size(); i++) {
                if (formas.get(i).estaSelecionada()) {
                    formasWCS.get(i).escala(sx, sy, referencia);
                    formas.set(i, formasWCS.get(i).janelaViewport(getJanMin(), getJanMax(), getViewMin(), getViewMax()));
                }
            }
            rectSelecao = null;
        }
    }
    
    public void ferramentaRotacao(){
        if (rectSelecao == null){
            tela.mostrarMensagem("Não há objetos selecionados.");
            return;
        }
        
        JSpinner xRefField = new JSpinner(new SpinnerNumberModel(0, -2000, 2000, 0.001));
        JSpinner yRefField = new JSpinner(new SpinnerNumberModel(0, -2000, 2000, 0.001));
        JSpinner thetaField = new JSpinner(new SpinnerNumberModel(90, -2000, 2000, 0.001));
        Object[] message = {
            "X Referência:", xRefField,
            "Y Referência:", yRefField,
            "Ângulo de Rotação (em graus):", thetaField
        };

        int option = JOptionPane.showConfirmDialog(null, message, "Rotação", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            double xRef = (double) xRefField.getValue();
            double yRef = (double) yRefField.getValue();
            double theta = (double) thetaField.getValue();
            
            Point2D.Double referencia = new Point2D.Double(xRef, yRef);
            
            /*for (Forma forma : getSelecionados()) {
                forma.rotacao(theta, referencia);
            }*/
            for (int i = 0; i < formas.size(); i++) {
                if (formas.get(i).estaSelecionada()) {
                    formasWCS.get(i).rotacao(theta, referencia);
                    formas.set(i, formasWCS.get(i).janelaViewport(getJanMin(), getJanMax(), getViewMin(), getViewMax()));
                }
            }
            rectSelecao = null;
        }
    }
    
    public ArrayList<Forma> getSelecionados(){
        ArrayList<Forma> selecionados = new ArrayList<>();
        for (int i = 0; i < formas.size(); i++) {
            if (formas.get(i).estaSelecionada()){
                selecionados.add(formasWCS.get(i));
            }
        }
        /*for (Forma forma : formas) {
            if (forma.estaSelecionada()){
                selecionados.add(forma);
            }
        }*/
        return selecionados;
    }
    
    public void mostrarMensagem(String msg){
        tela.mostrarMensagem(msg);
    }
    
    private void criarForma() {
        rectSelecao = null;
        if (!desenhando) {
            switch (tela.getFerramentaSelecionada()){
                case "LINHA":
                    formaDesenhando = new Linha(mouseX, mouseY, mouseX, mouseY);
                    break;
                case "RETANGULO":
                    formaDesenhando = new Retangulo(mouseX, mouseY, 1, 1);
                    break;
                case "ELIPSE":
                    formaDesenhando = new Circulo(mouseX, mouseY, mouseX, mouseY);
                    break;
            }
        } else {
            formas.add(formaDesenhando);
            formasWCS.add(formaDesenhando.viewportJanela(getJanMin(), getJanMax(), getViewMin(), getViewMax()));
        }
        desenhando = !desenhando;
    }
    
    private void ferramentaTransformacao(){
        /*if (rectSelecao == null){
            mostrarMensagem("Não há objetos selecionados.");
            return;
        }
        
        if (ferramenta == null){
            switch (tela.getFerramentaSelecionada()) {
                case "MOVER":
                    break;
                case "ESCALA":
                    ferramenta = new FerramentaEscala(this);
                    break;
                case "ROTACAO":
                    break;
            }
        }
        
        ferramenta.click(mouseX, mouseY);*/
    }
    
    private void acaoFerramenta(){
        switch (tela.getFerramentaSelecionada()) {
            case "LINHA":
            case "RETANGULO":
            case "ELIPSE":
                criarForma();
                break;
            case "MOVER":
            case "ESCALA":
            case "ROTACAO":
                ferramentaTransformacao();
                break;
        }
    }
    
    public void mouseClick(int posX, int posY, int botao) {
        switch (botao) {
            case MouseInput.BOTAO_ESQUERDO:
                acaoFerramenta();
                break;
            case MouseInput.BOTAO_MEIO:
                panning = true;
                mouseX_anterior = posX;
                mouseY_anterior = posY;
                painelDesenho.setPan(panning);
                break;
            case MouseInput.BOTAO_DIREITO:
                selecionando = true;
                rectSelecao = new Retangulo(mouseX, mouseY, 1, 1);
                break;
        }
    }
    
    public void mouseRelease(int posX, int posY, int botao){
        if (botao == MouseInput.BOTAO_MEIO){
            panning = false;
            painelDesenho.setPan(panning);
        } else if (botao == MouseInput.BOTAO_DIREITO){
            selecionando = false;
        }
    }
    
    public void mouseArrastar(int posX, int posY){
        moverMouse(posX, posY);
        if (panning){
            pan();
            mouseX_anterior = mouseX;
            mouseY_anterior = mouseY;
        } else if (selecionando){
            rectSelecao.setDistancia(posX, posY, modoOrtho);
        }
    }
    
    public void moverMouse(int x, int y){
        mouseX = x;
        mouseY = y;
        Point2D p = viewportJanela(new Point2D.Double(mouseX, mouseY));
        janela.atualizarMouse(p.getX(), p.getY());
    }
    
    public int getMouseX() {
        return mouseX;
    }

    public int getMouseY() {
        return mouseY;
    }
    
    public double getMouseX_janela(){
        return janela.getMouseX();
    }
    
    public double getMouseY_janela(){
        return janela.getMouseY();
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

    public MouseInput getMouseInput() {
        return mouseInput;
    }

    public TecladoInput getTecladoInput() {
        return tecladoInput;
    }

    public boolean isPanning() {
        return panning;
    }
    
    public double getZoomAcc(){
        return zoomAcc;
    }
    
    public Point2D getJanMin(){
        return new Point2D.Double(janela.getXmin(), janela.getYmin());
    }
    
    public Point2D getJanMax(){
        return new Point2D.Double(janela.getXmax(), janela.getYmax());
    }
    
    public Point2D getViewMin(){
        return new Point2D.Double(0, 0);
    }
    
    public Point2D getViewMax(){
        return new Point2D.Double(painelDesenho.getWidth(), painelDesenho.getHeight());
    }

}
