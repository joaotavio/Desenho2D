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

public class TelaSobre extends JDialog implements ActionListener{

    private final JFrame framePai;
    private final JPanel container;
    private final JPanel panel;

    public TelaSobre(JFrame framePai){
        this.framePai = framePai;

        setTitle("Sobre");
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
        JLabel titulo = new JLabel(framePai.getTitle());
        titulo.setFont(new Font("Arial", Font.BOLD, 48));
        addLabel(titulo, panel);

        JSeparator j = new JSeparator();
        j.setMaximumSize(new Dimension(450,1));
        panel.add(j);

        addLabel(new JLabel("Universidade Estadual de Maringá"), panel);
        addLabel(new JLabel("Curso: Ciência da Computação"), panel);
        addLabel(new JLabel("Disciplina: Computação Gráfica"), panel);
        addLabel(new JLabel("Professor: Dante Alves Medeiros Filho"), panel);
        addLabel(new JLabel("Alunos: João Otávio Biondo - Marco Aurélio Deoldoto Paulino"), panel);
    }

    private void addLabel(JLabel label, JPanel panel){
        panel.add(Box.createRigidArea(new Dimension(0,8)));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(label);
    }

    @Override
    public void actionPerformed(ActionEvent e){
        setVisible(true);
    }
}

