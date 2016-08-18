package controle.ferramentas;

import controle.ControleDesenho;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

public class FerramentaMover implements Ferramenta {
    
    private final ControleDesenho controleDesenho;
    
    public FerramentaMover(ControleDesenho controleDesenho){
        this.controleDesenho = controleDesenho;
        ferramentaMover();
    }

    @Override
    public void click(double x, double y) {
        
    }
    
    public void ferramentaMover(){
        if (controleDesenho.getRectSelecao() == null){
            controleDesenho.mostrarMensagem("Não há objetos selecionados.");
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
            
            for (int i = 0; i < controleDesenho.getFormas().size(); i++) {
                if (controleDesenho.getFormas().get(i).estaSelecionada()) {
                    controleDesenho.getFormasWCS().get(i).translacao(dx, dy);
                    controleDesenho.getFormas().set(i, controleDesenho.getFormasWCS().get(i).janelaViewport(
                            controleDesenho.getJanMin(), controleDesenho.getJanMax(), controleDesenho.getViewMin(), controleDesenho.getViewMax()));
                }
            }
            controleDesenho.setRectSelecao(null);
        }
    }
    
    
}
