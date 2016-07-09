package gui;

import controle.ControleDesenho;
import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ButtonGroup;
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
    
    private ButtonGroup grupoBtn;
    
    private JMenuBar menuBar;
    private JMenu arquivo;
    private JMenu ajuda;
    
    private final ControleDesenho controle;
    private final PainelDesenho painelDesenho;
    

    public TelaPrincipal(ControleDesenho controle, PainelDesenho painelDesenho) {
        this.painelDesenho = painelDesenho;
        this.controle = controle;
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
        
        arquivo.add(sair);
        ajuda.add(sobre);
    }
    
    private void iniciarToolBar(){
        toolBar = new JToolBar();
        grupoBtn = new ButtonGroup();
        JToggleButton b1 = new JToggleButton("Linha");
        JToggleButton b2 = new JToggleButton("Triângulo");
        grupoBtn.add(b1);
        grupoBtn.add(b2);
        toolBar.add(b1);
        toolBar.add(b2);
        
        grupoBtn.setSelected(b1.getModel(), true);
        
        JButton zoom = new JButton("Zoom");
        JButton desfazer = new JButton("Desfazer");
        toolBar.add(Box.createHorizontalGlue());
        toolBar.add(Box.createVerticalGlue());
        toolBar.add(zoom);
        toolBar.add(desfazer);
        
        painelPrincipal.add(toolBar, BorderLayout.PAGE_START);
    }
    
    private void iniciarStatusBar(){
        statusBar = new JPanel(new BorderLayout());
        statusBar.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
        labelStatus = new JLabel("X: 0 - Y: 0");
        statusBar.add(labelStatus, BorderLayout.LINE_START);
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
    
    public void atualizarGUI(){
        atualizarStatusLabel(controle.getMouseX(), controle.getMouseY());
    }
    
}
