package Venda_Ingresso.errors;

public class DadosInvalidosException extends RuntimeException{

    public DadosInvalidosException(String mensagem){
        super(mensagem);
    }

}
