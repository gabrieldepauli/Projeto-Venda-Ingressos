package Venda_Ingresso.ui;

import Venda_Ingresso.errors.SemRegistrosException;
import Venda_Ingresso.model.Ingresso;
import Venda_Ingresso.service.GerenciadorIngresso;

import javax.swing.*;
import java.util.ArrayList;

public class TelaInicial extends JDialog {

    private JPanel painelFundo;
    private JButton btnComprar;
    private JButton btnSair;
    private JButton btnGerarRelatorio;

    GerenciadorIngresso gerenciador = new GerenciadorIngresso();

    public TelaInicial() {
        criarComponentesTela();
    }

    private void criarComponentesTela() {
        btnComprar = new JButton("Comprar Ingresso");
        btnGerarRelatorio = new JButton("Gerar Relatório");
        btnSair = new JButton("Sair");

        btnComprar.addActionListener((e) -> {
            JanelaCadastroIngresso janelaCadastroIngresso = new JanelaCadastroIngresso(this);
            janelaCadastroIngresso.setVisible(true);
        });

        btnGerarRelatorio.addActionListener((e) -> {
            try {
                ArrayList<Ingresso> list = gerenciador.getIngressos();
                if (list.isEmpty()) {
                    throw new SemRegistrosException("Nenhum ingresso encontrado.");
                }
                setVisible(false);
                JanelaGrafica janelaGrafica = new JanelaGrafica(this);
                janelaGrafica.setVisible(true); // carrega os dados internamente com o filtro
            } catch (SemRegistrosException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Relatório Indisponível", JOptionPane.WARNING_MESSAGE);
            }
        });

        btnSair.addActionListener(e -> {
            System.exit(0);
        });

        painelFundo = new JPanel();
        painelFundo.add(btnComprar);
        painelFundo.add(btnGerarRelatorio);
        painelFundo.add(btnSair);

        add(painelFundo);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        pack();
        setSize(300, 200);
        setVisible(true);
    }

    public GerenciadorIngresso getGerenciador() {
        return gerenciador;
    }
}
