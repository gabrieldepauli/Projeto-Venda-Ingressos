package Venda_Ingresso.ui;

import Venda_Ingresso.errors.SemRegistrosException;
import Venda_Ingresso.model.Ingresso;
import Venda_Ingresso.service.GerenciadorIngresso;

import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ItemEvent;

class JanelaGrafica extends JDialog {

    private TelaInicial telaInicialPai;

    private JPanel painelFundo;
    private JPanel painelBotoes;
    private JTable tabelaIngressos;
    private JScrollPane scroll;
    private DefaultTableModel modelo;

    private JComboBox<String> cbxSetores;
    private String[] setores = {"Todos", "Amarelo", "Azul", "Branco", "Verde"};

    public JanelaGrafica() {
        modelo = criarModeloTabela();
        criarTabela();
        criarComponentes();
    }

    public JanelaGrafica(TelaInicial telaInicialPai) {
        this.telaInicialPai = telaInicialPai;
        modelo = criarModeloTabela();
        criarTabela();
        criarComponentes();
    }

    private DefaultTableModel criarModeloTabela() {
        return new DefaultTableModel() {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0) return Boolean.class;
                return super.getColumnClass(columnIndex);
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 0;
            }
        };
    }

    private void criarTabela() {
        tabelaIngressos = new JTable(modelo);
        tabelaIngressos.setSize(700, 800);

        modelo.addColumn("Selecionar");
        modelo.addColumn("Código");
        modelo.addColumn("Nome");
        modelo.addColumn("Setor");
        modelo.addColumn("Qtd");
        modelo.addColumn("Valor");
        modelo.addColumn("Total");
        modelo.addColumn("Data e Hora");

        tabelaIngressos.getColumnModel().getColumn(0).setPreferredWidth(10);
        tabelaIngressos.getColumnModel().getColumn(1).setPreferredWidth(5);
        tabelaIngressos.getColumnModel().getColumn(2).setPreferredWidth(70);
        tabelaIngressos.getColumnModel().getColumn(3).setPreferredWidth(20);
        tabelaIngressos.getColumnModel().getColumn(4).setPreferredWidth(1);
        tabelaIngressos.getColumnModel().getColumn(5).setPreferredWidth(15);
        tabelaIngressos.getColumnModel().getColumn(6).setPreferredWidth(15);
        tabelaIngressos.getColumnModel().getColumn(7).setPreferredWidth(70);
    }

    private void criarComponentes() {
        painelFundo = new JPanel(new BorderLayout());

        // Filtro de setor
        cbxSetores = new JComboBox<>(setores);
        cbxSetores.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                filtrarPorSetor((String) cbxSetores.getSelectedItem());
            }
        });
        painelFundo.add(cbxSetores, BorderLayout.NORTH);

        // Scroll da tabela
        scroll = new JScrollPane(tabelaIngressos);
        painelFundo.add(scroll, BorderLayout.CENTER);

        // Botões
        painelBotoes = new JPanel();

        JButton btnVoltar = new JButton("Voltar");
        btnVoltar.addActionListener(e -> {
            this.setVisible(false);
            telaInicialPai.setVisible(true);
        });
        painelBotoes.add(btnVoltar);

        JButton btnExcluirSelecionados = new JButton("Excluir Selecionados");
        btnExcluirSelecionados.addActionListener(e -> excluirSelecionados());
        painelBotoes.add(btnExcluirSelecionados);

        painelFundo.add(painelBotoes, BorderLayout.SOUTH);

        add(painelFundo);
        setTitle("Ingressos");
        setVisible(true);
        pack();
        setSize(700, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        filtrarPorSetor("Todos");
    }

    void carregarDados(DefaultTableModel modelo, ArrayList<Ingresso> ingressos) {
        modelo.setNumRows(0);
        ingressos.forEach(c -> {
            modelo.addRow(new Object[]{
                    false,
                    c.getCodigo(),
                    c.getNome(),
                    c.getSetor(),
                    c.getQuantidade(),
                    c.getValor(),
                    c.getValorTotal(),
                    c.getDataHora()
            });
        });
    }

    private void excluirSelecionados() {
        int rowCount = modelo.getRowCount();
        ArrayList<Integer> codigosParaRemover = new ArrayList<>();

        for (int i = rowCount - 1; i >= 0; i--) {
            Boolean selecionado = (Boolean) modelo.getValueAt(i, 0);
            if (selecionado != null && selecionado) {
                int codigo = (int) modelo.getValueAt(i, 1);
                codigosParaRemover.add(codigo);
                modelo.removeRow(i);
            }
        }

        ArrayList<Ingresso> lista = telaInicialPai.getGerenciador().getIngressos();
        lista.removeIf(i -> codigosParaRemover.contains(i.getCodigo()));
    }

    public void imprimirRelatorio(ArrayList<Ingresso> ingressos) {
        carregarDados(modelo, ingressos);
    }

    private void filtrarPorSetor(String setorSelecionado) {
        try {
            ArrayList<Ingresso> todos = telaInicialPai.getGerenciador().getIngressos();
            if (setorSelecionado.equalsIgnoreCase("Todos")) {
                carregarDados(modelo, todos);
                return;
            }

            ArrayList<Ingresso> filtrados = new ArrayList<>();
            for (Ingresso ingresso : todos) {
                if (ingresso.getSetor().equalsIgnoreCase(setorSelecionado)) {
                    filtrados.add(ingresso);
                }
            }

            if (filtrados.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Não há ingressos do setor '" + setorSelecionado + "'.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
            }

            carregarDados(modelo, filtrados);
        } catch (SemRegistrosException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Erro", JOptionPane.WARNING_MESSAGE);
        }
    }
}
