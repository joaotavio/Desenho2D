package gui;

import controle.ControleDesenho;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;


public class TelaPrincipal {
    
    public static final String TITULO = "Desenho 2D";
    
    public static final int LARGURA = 980;
    public static final int ALTURA = 640;
    
    public static final int LARGURA_MIN = 640;
    public static final int ALTURA_MIN = 480;
    
    private JFrame frame;
    private JPanel painelPrincipal;
    
    private JToolBar toolBar;
    private JPanel statusBar;
    
    private JLabel labelStatus;
    
    private JLabel labelMsg;
    public static final double MAX_TEMPO_MSG = 5000.0;
    private long tempoMsg;
    
    private ButtonGroup grupoBtn;
    private JToggleButton btnLinha;
    private JToggleButton btnRetangulo;
    private JToggleButton btnCirculo;
    
    private JMenuBar menuBar;
    private JMenu arquivo;
    private JMenu ajuda;
    
    private final ControleDesenho controleDesenho;
    private final PainelDesenho painelDesenho;
    

    public TelaPrincipal(ControleDesenho controle, PainelDesenho painelDesenho) {
        this.painelDesenho = painelDesenho;
        this.controleDesenho = controle;
        iniciarComponentes();
    }
    
    private void iniciarComponentes(){
        frame = new JFrame();
        frame.setTitle(TITULO);
        frame.setMinimumSize(new Dimension(LARGURA_MIN, ALTURA_MIN));
        frame.setPreferredSize(new Dimension(LARGURA, ALTURA));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        painelPrincipal = new JPanel(new BorderLayout());
        
        iniciarMenu();
        iniciarToolBar();
        iniciarStatusBar();
        
        painelPrincipal.add(painelDesenho, BorderLayout.CENTER);
        
        frame.add(painelPrincipal);
        frame.setVisible(true);
        frame.pack();
        frame.setLocationRelativeTo(null);
        painelDesenho.requestFocus();
    }
    
    private void iniciarMenu(){
        menuBar = new JMenuBar();
        frame.setJMenuBar(menuBar);
        
        arquivo = new JMenu("Arquivo");
        ajuda = new JMenu("Ajuda");
        
        menuBar.add(arquivo);
        menuBar.add(ajuda);
        
        JMenuItem sair = new JMenuItem("Sair");
        JMenuItem sobre = new JMenuItem("Sobre");
        JMenuItem comandos = new JMenuItem("Comandos");
        
        arquivo.add(sair);
        ajuda.add(comandos);
        ajuda.addSeparator();
        ajuda.add(sobre);
        
        sair.addActionListener(new AcaoMenuSair());
        sobre.addActionListener(new TelaSobre(frame));
        comandos.addActionListener(new TelaComandos(frame));
    }
    
    private Image getScaledImage(Image srcImg, int w, int h){
        BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = resizedImg.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.drawImage(srcImg, 0, 0, w, h, null);
        g2.dispose();
        return resizedImg;
    }
    
    private void iniciarToolBar(){
        toolBar = new JToolBar();
        grupoBtn = new ButtonGroup();
        
        ImageIcon lineIcon = new ImageIcon(getScaledImage(new ImageIcon("images/lineIcon.png").getImage(), 20, 20));
        ImageIcon rectIcon = new ImageIcon(getScaledImage(new ImageIcon("images/rectIcon.png").getImage(), 25, 20));
        ImageIcon elipseIcon = new ImageIcon(getScaledImage(new ImageIcon("images/ellipseIcon.png").getImage(), 20, 20));
        
        btnLinha = new JToggleButton("Linha", lineIcon);
        btnLinha.setActionCommand("Linha");
        btnRetangulo = new JToggleButton("Retângulo", rectIcon);
        btnRetangulo.setActionCommand("Retângulo");
        btnCirculo = new JToggleButton("Elipse", elipseIcon);
        btnCirculo.setActionCommand("Circulo");
        
        
        grupoBtn.add(btnLinha);
        grupoBtn.add(btnRetangulo);
        grupoBtn.add(btnCirculo);
        
        toolBar.add(btnLinha);
        toolBar.add(btnRetangulo);
        toolBar.add(btnCirculo);
        
        grupoBtn.setSelected(btnLinha.getModel(), true);
        
        JButton btnMover = new JButton("Mover");
        JButton btnEscala = new JButton("Escala");
        JButton btnRotacao = new JButton("Rotação");
        
        JButton btnZoomIn = new JButton("Zoom In");
        JButton btnZoomOut = new JButton("Zoom Out");
        JButton btnZoomExtend = new JButton("Zoom Extend");
        
        JButton btnLimpar = new JButton("Limpar");
        
        toolBar.add(Box.createHorizontalGlue());
        toolBar.add(Box.createVerticalGlue());
        toolBar.add(btnMover);
        toolBar.add(btnEscala);
        toolBar.add(btnRotacao);
        
        toolBar.addSeparator();
        toolBar.add(btnZoomIn);
        toolBar.add(btnZoomOut);
        toolBar.add(btnZoomExtend);
        
        toolBar.addSeparator();
        toolBar.add(btnLimpar);
        
        btnMover.addActionListener(new AcaoBotaoMover());
        btnEscala.addActionListener(new AcaoBotaoEscala());
        btnRotacao.addActionListener(new AcaoBotaoRotacao());
        btnZoomIn.addActionListener(new AcaoBotaoZoomIn());
        btnZoomOut.addActionListener(new AcaoBotaoZoomOut());
        btnZoomExtend.addActionListener(new AcaoBotaoZoomExtend());
        btnLimpar.addActionListener(new AcaoBotaoLimpar());
        
        painelPrincipal.add(toolBar, BorderLayout.PAGE_START);
    }
    
    private void iniciarStatusBar(){
        statusBar = new JPanel(new BorderLayout());
        statusBar.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        
        labelMsg = new JLabel("");
        labelStatus = new JLabel("X: 0 - Y: 0");
        statusBar.add(labelStatus, BorderLayout.LINE_END);
        statusBar.add(labelMsg, BorderLayout.LINE_START);
        painelPrincipal.add(statusBar, BorderLayout.PAGE_END);
        
        /*JTextField f = new JTextField(50);
        statusBar.add(f, BorderLayout.LINE_START);
        
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                f.requestFocus();
            }
        });*/
    }
    
    private void atualizarStatusLabel(int x, int y){
        labelStatus.setText("X: " + x + " - Y: " + y);
    }
    
    public void mostrarMensagem(String msg){
        labelMsg.setText(msg);
        tempoMsg = System.currentTimeMillis() + (long)MAX_TEMPO_MSG;
    }
    
    public void atualizarGUI(){
        atualizarStatusLabel(controleDesenho.getMouseX(), controleDesenho.getMouseY());
        
        if (tempoMsg < System.currentTimeMillis()){
            labelMsg.setText("");
            tempoMsg = Long.MAX_VALUE;
        }
    }
    
    public String getFormaSelecionada(){
        painelDesenho.requestFocus();
        return grupoBtn.getSelection().getActionCommand();
    }
    
    /* Ações Botões */
    
    private class AcaoBotaoMover implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            controleDesenho.ferramentaMover();
        }
    }
    
    private class AcaoBotaoEscala implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            controleDesenho.ferramentaEscala();
        }
    }
    
    private class AcaoBotaoRotacao implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            controleDesenho.ferramentaRotacao();
        }
    }
    
    private class AcaoBotaoZoomIn implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            controleDesenho.zoomIn();
        }
    }
    
    private class AcaoBotaoZoomOut implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            controleDesenho.zoomOut();
        }
    }
    
    private class AcaoBotaoZoomExtend implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            controleDesenho.zoomExtend();
        }
    }
    
    private class AcaoBotaoLimpar implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            controleDesenho.limpar();
        }
    }
    
    /* Ações do Menu */
    
    private class AcaoMenuSair implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            frame.dispose();
            System.exit(0);
        }
    }
    
}
