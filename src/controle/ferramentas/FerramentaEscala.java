package controle.ferramentas;

import controle.ControleDesenho;
import java.awt.geom.Point2D;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

public class FerramentaEscala implements Ferramenta {

    private double xRef;
    private double yRef;
    
    private final ControleDesenho controleDesenho;
    
    public FerramentaEscala(ControleDesenho controleDesenho){
        this.controleDesenho = controleDesenho;
    }
    
    @Override
    public void click(double x, double y){
        xRef = x;
        yRef = y;
        ferramentaEscala();
    }
    
    public void ferramentaEscala(){
        if (controleDesenho.getRectSelecao() == null){
            controleDesenho.mostrarMensagem("Não há objetos selecionados.");
            return;
        }
        
        JSpinner sxField = new JSpinner(new SpinnerNumberModel(1, -2000, 2000, 0.001));
        JSpinner syField = new JSpinner(new SpinnerNumberModel(1, -2000, 2000, 0.001));
        Object[] message = {
            "Mudança de Escala em X:", sxField,
            "Mudança de Escala em Y:", syField
        };

        int option = JOptionPane.showConfirmDialog(null, message, "Escala", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            double sx = (double) sxField.getValue();
            double sy = (double) syField.getValue();
            
            Point2D.Double referencia = new Point2D.Double(xRef, yRef);

            for (int i = 0; i < controleDesenho.getFormas().size(); i++) {
                if (controleDesenho.getFormas().get(i).estaSelecionada()) {
                    controleDesenho.getFormas().get(i).escala(sx, sy, referencia);
                    controleDesenho.getFormasWCS().set(i, controleDesenho.getFormas().get(i).viewportJanela(
                            controleDesenho.getJanMin(), controleDesenho.getJanMax(), controleDesenho.getViewMin(), controleDesenho.getViewMax()));
                }
            }
            controleDesenho.setRectSelecao(null);
        }
    }
    
}
