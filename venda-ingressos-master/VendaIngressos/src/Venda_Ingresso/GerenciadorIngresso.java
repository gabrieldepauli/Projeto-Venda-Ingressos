/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Venda_Ingresso;

import java.util.ArrayList;

/**
 *
 * @author Junior
 */
public class GerenciadorIngresso {
    
    private ArrayList<Ingresso> ingressos;
    private static int prox = 0;        

    public GerenciadorIngresso() {
        
        ingressos = new ArrayList<>();
    }
    
    public boolean comprarIngresso(Ingresso ingresso) {
        
        if (ingresso != null) {
            ingresso.setCodigo(++prox);
            ingressos.add(ingresso);//Adiciona um elemento ao final do ArrayList            
            return true;
        }else{
            return false;
        }       
    }
    
    //Retorna os ingressos adquiridos
    public ArrayList<Ingresso> getIngressos() {
        return ingressos;
    }
    
}

    
    
    
    

