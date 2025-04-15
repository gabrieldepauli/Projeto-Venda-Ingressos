/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Venda_Ingresso;

import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Junior
 */
class JanelaGrafica extends JDialog{

    private TelaInicial telaInicialPai;

    private JPanel painelFundo;
    private JPanel painelBotoes;
    private JTable tabelaIngressos;
    private JScrollPane scroll;
    private DefaultTableModel modelo;//Modelo da tabela

    private GerenciadorIngresso gerenciador;

    public JanelaGrafica() {
        modelo = new DefaultTableModel();
        criarTabela();
        criarComponentes();
    }

    public JanelaGrafica(TelaInicial telaInicialPai) {
        this.telaInicialPai = telaInicialPai;
        modelo = new DefaultTableModel();
        criarTabela();
        criarComponentes();
    }

    private void criarTabela() {
        tabelaIngressos = new JTable(modelo);//Cria a tabela
        tabelaIngressos.setSize(700, 800);
        
        modelo.addColumn("Código");
        modelo.addColumn("Nome");
        modelo.addColumn("Setor");
        modelo.addColumn("Qtd");
        modelo.addColumn("Valor");
        modelo.addColumn("Total");
        modelo.addColumn("Data e Hora");
        
         tabelaIngressos.getColumnModel().getColumn(0)//primeira coluna
                .setPreferredWidth(5);//largura da coluna
         tabelaIngressos.getColumnModel().getColumn(1)
                .setPreferredWidth(70);
         tabelaIngressos.getColumnModel().getColumn(2)
                .setPreferredWidth(20);
         tabelaIngressos.getColumnModel().getColumn(3)
                .setPreferredWidth(1);         
         tabelaIngressos.getColumnModel().getColumn(4)
                .setPreferredWidth(15);
         tabelaIngressos.getColumnModel().getColumn(5)
                .setPreferredWidth(15);
         tabelaIngressos.getColumnModel().getColumn(6)
                .setPreferredWidth(70);
    }

    private void criarComponentes() {

        painelBotoes = new JPanel();

        JButton btnVoltar = new JButton("Voltar");
        btnVoltar.addActionListener((e) -> {
            this.setVisible(false);  // Fecha a JanelaGrafica
            telaInicialPai.setVisible(true);  // Reabre a TelaInicial
        });
        painelBotoes.add(btnVoltar);

        scroll = new JScrollPane(tabelaIngressos);
        painelFundo = new JPanel();
        painelFundo.add(scroll);
        painelFundo.add(painelBotoes);
        add(painelFundo);

        setTitle("Ingressos");
        setVisible(true);
        pack();
        setSize(600,600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    void carregarDados(DefaultTableModel modelo, ArrayList<Ingresso> ingressos) {
        modelo.setNumRows(0);//Seta o número de colunas - Limpa a tabela
        
        ingressos.forEach(c -> { //lambda
            modelo.addRow(new Object[]{//instância um novo vetor com os parâmetros
                c.getCodigo(),//cada contato adiciona uma nova linha
                c.getNome(),
                c.getSetor(),
                c.getQuantidade(),
                c.getValor(),
                c.getValorTotal(),
                c.getDataHora()
            });
        });
    }

    public GerenciadorIngresso getGerenciador() {
        return gerenciador;
    }

    public void imprimirRelatorio(ArrayList<Ingresso> ingressos) {
        carregarDados(modelo, ingressos);
    }
    
}
