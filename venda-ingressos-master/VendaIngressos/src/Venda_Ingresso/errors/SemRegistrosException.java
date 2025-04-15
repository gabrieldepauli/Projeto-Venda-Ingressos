package Venda_Ingresso.errors;

public class SemRegistrosException extends RuntimeException{

    public SemRegistrosException(String mensagem){
        super(mensagem);
    }

}
