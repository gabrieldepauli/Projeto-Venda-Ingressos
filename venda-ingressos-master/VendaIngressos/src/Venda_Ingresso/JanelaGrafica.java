package Venda_Ingresso;

import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

class JanelaGrafica extends JDialog {

    private TelaInicial telaInicialPai;

    private JPanel painelFundo;
    private JPanel painelBotoes;
    private JTable tabelaIngressos;
    private JScrollPane scroll;
    private DefaultTableModel modelo;

    private GerenciadorIngresso gerenciador;

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
                if (columnIndex == 0) return Boolean.class; // checkbox
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

        scroll = new JScrollPane(tabelaIngressos);
        painelFundo = new JPanel();
        painelFundo.add(scroll);
        painelFundo.add(painelBotoes);
        add(painelFundo);

        setTitle("Ingressos");
        setVisible(true);
        pack();
        setSize(600, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    void carregarDados(DefaultTableModel modelo, ArrayList<Ingresso> ingressos) {
        modelo.setNumRows(0);
        ingressos.forEach(c -> {
            modelo.addRow(new Object[]{
                    false, // checkbox desmarcado
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
                int codigo = (int) modelo.getValueAt(i, 1); // código na coluna 1
                codigosParaRemover.add(codigo);
                modelo.removeRow(i);
            }
        }

        ArrayList<Ingresso> lista = telaInicialPai.getGerenciador().getIngressos();
        lista.removeIf(i -> codigosParaRemover.contains(i.getCodigo()));
    }

    public GerenciadorIngresso getGerenciador() {
        return gerenciador;
    }

    public void imprimirRelatorio(ArrayList<Ingresso> ingressos) {
        carregarDados(modelo, ingressos);
    }
}
