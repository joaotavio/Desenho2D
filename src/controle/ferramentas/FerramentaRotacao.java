package controle.ferramentas;

import controle.ControleDesenho;
import java.awt.geom.Point2D;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

public class FerramentaRotacao implements Ferramenta {
    
    private double xRef;
    private double yRef;
    
    private final ControleDesenho controleDesenho;
    
    public FerramentaRotacao(ControleDesenho controleDesenho){
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

        JSpinner thetaField = new JSpinner(new SpinnerNumberModel(90, -2000, 2000, 0.001));
        Object[] message = {
            "Ângulo de Rotação (em graus):", thetaField
        };

        int option = JOptionPane.showConfirmDialog(null, message, "Rotação", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            double theta = (double) thetaField.getValue();
            
            Point2D.Double referencia = new Point2D.Double(xRef, yRef);
            
            for (int i = 0; i < controleDesenho.getFormas().size(); i++) {
                if (controleDesenho.getFormas().get(i).estaSelecionada()) {
                    controleDesenho.getFormas().get(i).rotacao(theta, referencia);
                    controleDesenho.getFormasWCS().set(i, controleDesenho.getFormas().get(i).viewportJanela(
                            controleDesenho.getJanMin(), controleDesenho.getJanMax(), controleDesenho.getViewMin(), controleDesenho.getViewMax()));
                }
            }
            controleDesenho.setRectSelecao(null);
        }
    }
    
}
