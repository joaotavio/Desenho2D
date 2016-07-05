package controle;

import javax.swing.SwingUtilities;
import gui.TelaPrincipal;

public class Main {
    
    public static void main(String[] args) {
       SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                //TelaPrincipal tela = new TelaPrincipal();
                ControleDesenho controle = new ControleDesenho();
            }
        });
    }
}
