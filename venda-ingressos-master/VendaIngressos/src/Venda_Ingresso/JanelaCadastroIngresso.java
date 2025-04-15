/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Venda_Ingresso;

import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.time.LocalDateTime;
import javax.swing.*;

import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;
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
        Ingresso ingresso = new Ingresso();
        double valorIngresso = 0.00;
        
        ingresso.setNome(txtNome.getText());          
        
        setor = cbxSetores.getSelectedItem().toString();
        
        // Em caso de alteracao, novo item eh adicionado ao atributo setor
        cbxSetores.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED){   
                    setor = cbxSetores.getSelectedItem().toString();                                     
                }
            }
        });        
        
        ingresso.setSetor(setor);   
        ingresso.setQuantidade(Integer.parseInt(txtQtde.getText())); 
        
        // Identifica valores dos ingressos
        if (setor.equalsIgnoreCase("Amarelo")){
            valorIngresso = 180.00;            
        }else{
            if (setor.equalsIgnoreCase("Azul")){
                valorIngresso = 100.00;
            }else{
                if (setor.equalsIgnoreCase("Branco")){
                    valorIngresso = 60.00;
                }else{
                    if (setor.equalsIgnoreCase("Verde")){
                        valorIngresso = 350.00;
                    }
                }
            }
        }
        
        tipoTorcedor = cbxTipoTorcedor.getSelectedItem().toString();
        
        // Em caso de alteracao, novo item eh adicionado ao atributo setor
        cbxSetores.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED){   
                    tipoTorcedor = cbxTipoTorcedor.getSelectedItem().toString();                                     
                }
            }
        });     
        
        // se for estudante ou aposentado, calcula meia entrada
        if (tipoTorcedor.equalsIgnoreCase("Meia")){
            valorIngresso = valorIngresso/2;
        }
        
        ingresso.setValor(valorIngresso);        
        
        // calcula o valor total
        double valorTotal = ingresso.getValor() * ingresso.getQuantidade();
        ingresso.setValorTotal(valorTotal);
        
        // captura a data e hora local da maquina
        ingresso.setDataHora(LocalDateTime.now());            
        
        if (gerenciador.comprarIngresso(ingresso)) {            
            limpar();
            JOptionPane.showMessageDialog(null, "Ingresso comprado com sucesso!");
        } else {
            limpar();
            JOptionPane.showMessageDialog(null, "Ingressos esgotados! Por favor, selecione outro setor.");
        }  
        
    }               
}
