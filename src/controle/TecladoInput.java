package controle;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class TecladoInput implements KeyListener{
    
    private final ControleDesenho controleDesenho;

    public TecladoInput(ControleDesenho controleDesenho) {
        this.controleDesenho = controleDesenho;
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        switch(e.getKeyCode()){
            case KeyEvent.VK_ESCAPE:
                controleDesenho.cancelarForma();
                break;
            case KeyEvent.VK_DELETE:
                controleDesenho.deletarSelecionado();
                break;
            case KeyEvent.VK_SHIFT:
                controleDesenho.ativarModoOrtho();
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch(e.getKeyCode()){
            case KeyEvent.VK_SHIFT:
                controleDesenho.desativarModoOrtho();
                break;
        }
    }
    
}
