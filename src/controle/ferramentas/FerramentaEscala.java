package controle.ferramentas;

import controle.ControleDesenho;

public class FerramentaEscala implements Ferramenta {
    
    private int numClick;
    
    private int xRef;
    private int yRef;
    
    private int xAtual;
    private int yAtual;
    
    private final ControleDesenho controleDesenho;
    
    public FerramentaEscala(ControleDesenho controleDesenho){
        this.controleDesenho = controleDesenho;
        numClick = 0;
        
    }
    
    @Override
    public void atualizar(int x, int y){
        xAtual = x;
        yAtual = y;
    }
    
    @Override
    public void desenhar(){
        
    }
    
    @Override
    public void click(int x, int y){
        if (numClick == 0){
            xRef = x;
            yRef = y;
            numClick++;
        } else {
            double sx = (x - xRef) * 0.5;
            double sy = (y - yRef) * 0.5;
            numClick = 0;
        }
    }
    
}
