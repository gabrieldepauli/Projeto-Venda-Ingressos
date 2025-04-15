/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Venda_Ingresso.ui;

import Venda_Ingresso.errors.DadosInvalidosException;
import Venda_Ingresso.model.Ingresso;
import Venda_Ingresso.service.GerenciadorIngresso;

import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.time.LocalDateTime;
import javax.swing.*;

import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Junior
 */
public class JanelaCadastroIngresso extends JDialog {
    private GerenciadorIngresso gerenciador;
    private TelaInicial telaInicialPai;

    private DefaultTableModel modelo;
    private JPanel painelFundo;
    private JButton btnSalvar;
    private JButton btnVoltarTelaInicial;
    private JLabel lblCodigo;
    private JLabel lblNome;
    private JLabel lblValor;
    private JLabel lblQtde;
    private JTextField txtCodigo;
    private JTextField txtNome;
    private JTextField txtSetor;
    private JTextField txtQtde;
    
    private String[] setores
            = {"Amarelo","Azul","Branco","Verde"};

    private JComboBox<String> cbxSetores;
    
    private String[] tiposTorcedor
            = {"Inteira", "Meia"};

    private JComboBox<String> cbxTipoTorcedor;

    
    String setor = "";
    String tipoTorcedor = "";
    
        
    public JanelaCadastroIngresso(TelaInicial telaInicialPai) {
        this.telaInicialPai = telaInicialPai;
        this.gerenciador = telaInicialPai.getGerenciador();
        criarComponentesInsercao();        
    }

    private void limpar(){
        txtNome.setText("");
        txtQtde.setText("");        
    }

    private void criarComponentesInsercao() {
        painelFundo = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;


        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;

        lblNome = new JLabel("Nome:");
        painelFundo.add(lblNome, gbc);

        gbc.gridy++;
        txtNome = new JTextField(20);
        painelFundo.add(txtNome, gbc);

        gbc.gridy++;
        cbxTipoTorcedor = new JComboBox<>(tiposTorcedor);
        painelFundo.add(cbxTipoTorcedor, gbc);

        gbc.gridy++;
        lblQtde = new JLabel("Quantidade:");
        painelFundo.add(lblQtde, gbc);

        gbc.gridy++;
        txtQtde = new JTextField(5);
        painelFundo.add(txtQtde, gbc);

        gbc.gridy++;
        cbxSetores = new JComboBox<>(setores);
        painelFundo.add(cbxSetores, gbc);

        gbc.gridy++;
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        btnSalvar = new JButton("Salvar");
        btnVoltarTelaInicial = new JButton("Voltar");
        painelBotoes.add(btnSalvar);
        painelBotoes.add(btnVoltarTelaInicial);
        painelFundo.add(painelBotoes, gbc);

        btnSalvar.addActionListener((e) -> comprarIngresso());
        btnVoltarTelaInicial.addActionListener((e) -> {
            this.setVisible(false);
            telaInicialPai.setVisible(true);
        });

        add(painelFundo);
        setTitle("Cadastro de Ingresso");
        pack();
        setSize(350, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private void comprarIngresso() {
        try {
            validarCampos();

            Ingresso ingresso = new Ingresso();
            double valorIngresso = 0.00;

            ingresso.setNome(txtNome.getText());
            setor = cbxSetores.getSelectedItem().toString();
            ingresso.setSetor(setor);
            ingresso.setQuantidade(Integer.parseInt(txtQtde.getText()));

            // Cálculo do valor do ingresso por setor
            switch (setor.toLowerCase()) {
                case "amarelo":
                    valorIngresso = 180.00;
                    break;
                case "azul":
                    valorIngresso = 100.00;
                    break;
                case "branco":
                    valorIngresso = 60.00;
                    break;
                case "verde":
                    valorIngresso = 350.00;
                    break;
            }

            tipoTorcedor = cbxTipoTorcedor.getSelectedItem().toString();

            if (tipoTorcedor.equalsIgnoreCase("Meia")) {
                valorIngresso = valorIngresso / 2;
            }

            ingresso.setValor(valorIngresso);
            ingresso.setValorTotal(valorIngresso * ingresso.getQuantidade());
            ingresso.setDataHora(LocalDateTime.now());

            if (gerenciador.comprarIngresso(ingresso)) {
                limpar();
                JOptionPane.showMessageDialog(null, "Ingresso comprado com sucesso!");
            } else {
                limpar();
                JOptionPane.showMessageDialog(null, "Ingressos esgotados! Por favor, selecione outro setor.");
            }

        } catch (DadosInvalidosException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro ao Comprar", JOptionPane.WARNING_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "A quantidade deve ser um número inteiro válido.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void validarCampos() throws DadosInvalidosException {
        if (txtNome.getText().trim().isEmpty()) {
            throw new DadosInvalidosException("O nome do comprador é obrigatório.");
        }

        if (txtQtde.getText().trim().isEmpty()) {
            throw new DadosInvalidosException("Informe a quantidade de ingressos.");
        }

        try {
            int quantidade = Integer.parseInt(txtQtde.getText());
            if (quantidade <= 0 || quantidade > 10) {
                throw new DadosInvalidosException("A quantidade deve ser maior que zero e até 10 ingressos.");
            }
        } catch (NumberFormatException e) {
            throw new DadosInvalidosException("A quantidade deve ser um número inteiro válido.");
        }
    }
}
