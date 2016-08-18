package gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.border.EmptyBorder;

public class TelaComandos extends JDialog implements ActionListener{

    private final JFrame framePai;
    private final JPanel container;
    private final JPanel panel;

    public TelaComandos(JFrame framePai){
        this.framePai = framePai;

        setTitle("Comandos Básicos");
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setModalityType(JDialog.DEFAULT_MODALITY_TYPE);

        container = new JPanel(new BorderLayout());
        container.setBorder(new EmptyBorder(0, 20, 20, 20));

        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        container.add(panel);
        add(container);

        addTexto();

        pack();
        setLocationRelativeTo(null);
    }

    private void addTexto(){
        JLabel titulo = new JLabel("Comandos Básicos");
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        addLabel(titulo, panel);

        JSeparator j = new JSeparator();
        j.setMaximumSize(new Dimension(800,1));
        panel.add(j);

        addLabel(new JLabel("Botão esquerdo do mouse: Desenhar objetos."), panel);
        addLabel(new JLabel("Botão direito do mouse: Selecionar objetos."), panel);
        addLabel(new JLabel("Botão do meio do mouse [pressionado]: Mover janela de visualização."), panel);
        addLabel(new JLabel("Botão do meio do mouse [girar]: Zoom in e zoom out."), panel);
        addLabel(new JLabel("ESC: Cancelar desenho ou seleção."), panel);
        addLabel(new JLabel("DELETE: Deletar formas selecionadas."), panel);
        addLabel(new JLabel("SHIFT [pressionado]: Ativer modo ortho de desenho."), panel);
    }

    private void addLabel(JLabel label, JPanel panel){
        panel.add(Box.createRigidArea(new Dimension(0,8)));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(label);
    }

    @Override
    public void actionPerformed(ActionEvent e){
        setVisible(true);
    }
}

