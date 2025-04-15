/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Venda_Ingresso.service;

import Venda_Ingresso.errors.SemRegistrosException;
import Venda_Ingresso.model.Ingresso;

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
    public ArrayList<Ingresso> getIngressos() throws SemRegistrosException{
        if(ingressos.isEmpty()){
            throw new SemRegistrosException("Ainda não há registros para a geração de um relatório");
        }
        return ingressos;
    }

    public ArrayList<Ingresso> getIngressosPorTipo(String tipo) throws SemRegistrosException {
        ArrayList<Ingresso> ingressosFiltrados = new ArrayList<>();

        for (Ingresso ingresso : ingressos) {
            if (ingresso.getSetor().equalsIgnoreCase(tipo)) {
                ingressosFiltrados.add(ingresso);
            }
        }

        if (ingressosFiltrados.isEmpty()) {
            throw new SemRegistrosException("Não há ingressos do tipo '" + tipo + "' para gerar relatório.");
        }

        return ingressosFiltrados;
    }


}

    
    
    
    

