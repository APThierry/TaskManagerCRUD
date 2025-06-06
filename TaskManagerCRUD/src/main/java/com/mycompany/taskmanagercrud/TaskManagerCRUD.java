package com.mycompany.taskmanagercrud;

import com.mongodb.client.MongoDatabase;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.util.List;

/**
 * Classe principal que cria e gerencia a Interface Gráfica do Usuário (GUI)
 * para o sistema de gerenciamento de tarefas, utilizando a biblioteca Swing.
 * Esta classe serve como a camada de apresentação e o ponto de entrada da aplicação.
 *
 * @author Thierry
 * @author Natã T. Ferreira
 * 
 */
public class TaskManagerCRUD extends JFrame {

    /** DAO para interagir com a camada de persistência de tarefas. */
    private final TarefaDAO tarefaDAO;

    // --- Componentes da UI ---
    /** Tabela para exibir as tarefas. */
    private JTable tabelaTarefas;
    /** Modelo de dados para a tabela, para facilitar a manipulação. */
    private DefaultTableModel tableModel;

    /** Campo de texto para o título da tarefa. */
    private JTextField campoTitulo;
    /** Área de texto para a descrição da tarefa. */
    private JTextArea campoDescricao;
    /** Caixa de combinação para selecionar a prioridade. */
    private JComboBox<String> comboBoxPrioridade;

    /** Botão para adicionar uma nova tarefa ou salvar uma tarefa editada. */
    private JButton botaoAdicionarSalvar;
    /** Botão para limpar os campos do formulário. */
    private JButton botaoLimpar;
    /** Botão para excluir a tarefa selecionada. */
    private JButton botaoExcluir;
    /** Botão para alternar o status de conclusão da tarefa. */
    private JButton botaoAlternarStatus;
    /** Botão para carregar dados de uma tarefa para edição. */
    private JButton botaoCarregarParaEditar;

    /**
     * Armazena o ID da tarefa em edição. Se for null, o formulário está em modo de adição.
     */
    private String idTarefaEmEdicao = null;

    /**
     * Construtor da classe. Inicializa a GUI, configura todos os componentes,
     * e conecta aos eventos.
     */
    public TaskManagerCRUD() {
        // 1. Conecta ao Banco e inicializa o DAO
        MongoDatabase database = ConexaoMongo.conectar();
        this.tarefaDAO = new TarefaDAO(database);

        // 2. Configurações da Janela Principal (o próprio JFrame)
        setTitle("Sistema de Gerenciamento de Tarefas (Swing)");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(950, 750);
        setLocationRelativeTo(null); // Centraliza a janela
        setLayout(new BorderLayout(10, 10));
        ((JPanel) getContentPane()).setBorder(new EmptyBorder(15, 15, 15, 15)); // Margem

        // 3. Criação dos painéis da UI
        criarPainelFormulario();
        criarPainelTabela();
        criarPainelAcoesTabela();

        // 4. Configura os eventos dos botões
        configurarActionListeners();

        // 5. Carrega as tarefas iniciais na tabela
        atualizarTabelaTarefas();

        // 6. Torna a janela visível
        setVisible(true);
    }

    /**
     * Cria e configura o painel do formulário para adicionar ou editar tarefas.
     */
    private void criarPainelFormulario() {
        JPanel painelFormulario = new JPanel(new GridBagLayout());
        painelFormulario.setBorder(BorderFactory.createTitledBorder("Gerenciar Tarefa"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST;
        painelFormulario.add(new JLabel("Título:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1.0;
        campoTitulo = new JTextField(30);
        painelFormulario.add(campoTitulo, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.anchor = GridBagConstraints.NORTHWEST;
        painelFormulario.add(new JLabel("Descrição:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        campoDescricao = new JTextArea(5, 30);
        campoDescricao.setLineWrap(true);
        campoDescricao.setWrapStyleWord(true);
        JScrollPane scrollDescricao = new JScrollPane(campoDescricao);
        painelFormulario.add(scrollDescricao, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.anchor = GridBagConstraints.WEST; gbc.weighty = 0; gbc.fill = GridBagConstraints.HORIZONTAL;
        painelFormulario.add(new JLabel("Prioridade:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2;
        comboBoxPrioridade = new JComboBox<>(new String[]{"Baixa", "Média", "Alta"});
        painelFormulario.add(comboBoxPrioridade, gbc);

        JPanel painelBotoesFormulario = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        botaoAdicionarSalvar = new JButton("Adicionar Tarefa");
        painelBotoesFormulario.add(botaoAdicionarSalvar);
        botaoLimpar = new JButton("Limpar");
        painelBotoesFormulario.add(botaoLimpar);
        gbc.gridx = 1; gbc.gridy = 3; gbc.fill = GridBagConstraints.NONE; gbc.anchor = GridBagConstraints.EAST; gbc.weightx = 0;
        painelFormulario.add(painelBotoesFormulario, gbc);

        add(painelFormulario, BorderLayout.NORTH);
    }

    /**
     * Cria e configura o painel com a tabela de tarefas.
     */
    private void criarPainelTabela() {
        String[] colunas = {"ID", "Título", "Descrição", "Prioridade", "Concluída"};
        tableModel = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Células não editáveis
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 4 ? Boolean.class : String.class; // Para renderizar checkbox
            }
        };
        tabelaTarefas = new JTable(tableModel);
        tabelaTarefas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabelaTarefas.setRowHeight(28);
        tabelaTarefas.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tabelaTarefas.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        TableColumnModel columnModel = tabelaTarefas.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(200);
        columnModel.getColumn(1).setPreferredWidth(250);
        columnModel.getColumn(2).setPreferredWidth(350);
        columnModel.getColumn(3).setPreferredWidth(100);
        columnModel.getColumn(4).setPreferredWidth(80);

        add(new JScrollPane(tabelaTarefas), BorderLayout.CENTER);
    }

    /**
     * Cria o painel inferior com os botões de ação da tabela.
     */
    private void criarPainelAcoesTabela() {
        JPanel painelAcoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        botaoCarregarParaEditar = new JButton("Carregar para Editar");
        painelAcoes.add(botaoCarregarParaEditar);
        botaoAlternarStatus = new JButton("Alternar Status");
        painelAcoes.add(botaoAlternarStatus);
        botaoExcluir = new JButton("Excluir Selecionada");
        botaoExcluir.setBackground(new Color(220, 53, 69)); // Cor vermelha
        botaoExcluir.setForeground(Color.WHITE);
        painelAcoes.add(botaoExcluir);
        add(painelAcoes, BorderLayout.SOUTH);
    }

    /**
     * Configura os ActionListeners (eventos de clique) para os botões.
     */
    private void configurarActionListeners() {
        botaoAdicionarSalvar.addActionListener(e -> processarAdicionarOuSalvar());
        botaoLimpar.addActionListener(e -> limparFormulario());
        botaoExcluir.addActionListener(e -> excluirTarefa());
        botaoAlternarStatus.addActionListener(e -> alternarStatusTarefa());
        botaoCarregarParaEditar.addActionListener(e -> carregarTarefaParaEdicao());
    }

    /**
     * Processa a ação do botão "Adicionar Tarefa" / "Salvar Alterações".
     */
    private void processarAdicionarOuSalvar() {
        String titulo = campoTitulo.getText().trim();
        String descricao = campoDescricao.getText().trim();
        String prioridade = (String) comboBoxPrioridade.getSelectedItem();

        if (titulo.isEmpty() || descricao.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Título e Descrição são obrigatórios!", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (idTarefaEmEdicao == null) { // Modo ADICIONAR
            Tarefa novaTarefa = new Tarefa(titulo, descricao, prioridade);
            tarefaDAO.adicionarTarefa(novaTarefa);
            JOptionPane.showMessageDialog(this, "Tarefa adicionada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        } else { // Modo SALVAR (Update)
            boolean sucesso = tarefaDAO.atualizarTarefa(idTarefaEmEdicao, titulo, descricao, prioridade);
            if (sucesso) {
                JOptionPane.showMessageDialog(this, "Tarefa atualizada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Falha ao atualizar a tarefa.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
        limparFormulario();
        atualizarTabelaTarefas();
    }

    /**
     * Carrega os dados da tarefa selecionada para o formulário de edição.
     */
    private void carregarTarefaParaEdicao() {
        int linhaSelecionada = tabelaTarefas.getSelectedRow();
        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(this, "Selecione uma tarefa na tabela para editar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        idTarefaEmEdicao = (String) tableModel.getValueAt(linhaSelecionada, 0);
        String titulo = (String) tableModel.getValueAt(linhaSelecionada, 1);
        String descricao = (String) tableModel.getValueAt(linhaSelecionada, 2);
        String prioridade = (String) tableModel.getValueAt(linhaSelecionada, 3);

        campoTitulo.setText(titulo);
        campoDescricao.setText(descricao);
        comboBoxPrioridade.setSelectedItem(prioridade);

        botaoAdicionarSalvar.setText("Salvar Alterações");
        setTitle("Editando Tarefa: " + titulo);
    }

    /**
     * Exclui a tarefa selecionada na tabela.
     */
    private void excluirTarefa() {
        int linhaSelecionada = tabelaTarefas.getSelectedRow();
        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(this, "Selecione uma tarefa para excluir!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String id = (String) tableModel.getValueAt(linhaSelecionada, 0);
        String titulo = (String) tableModel.getValueAt(linhaSelecionada, 1);

        int confirmacao = JOptionPane.showConfirmDialog(this,
                "Tem certeza que deseja excluir a tarefa: '" + titulo + "'?",
                "Confirmar Exclusão", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirmacao == JOptionPane.YES_OPTION) {
            if (tarefaDAO.excluirTarefa(id)) {
                JOptionPane.showMessageDialog(this, "Tarefa excluída com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                if (id.equals(idTarefaEmEdicao)) {
                    limparFormulario();
                }
                atualizarTabelaTarefas();
            } else {
                JOptionPane.showMessageDialog(this, "Falha ao excluir a tarefa.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Alterna o status de conclusão da tarefa selecionada.
     */
    private void alternarStatusTarefa() {
        int linhaSelecionada = tabelaTarefas.getSelectedRow();
        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(this, "Selecione uma tarefa para alterar o status!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String id = (String) tableModel.getValueAt(linhaSelecionada, 0);
        boolean statusAtual = (Boolean) tableModel.getValueAt(linhaSelecionada, 4);

        if (tarefaDAO.atualizarStatusTarefa(id, !statusAtual)) {
            atualizarTabelaTarefas();
        } else {
            JOptionPane.showMessageDialog(this, "Falha ao alterar o status da tarefa.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Limpa os campos do formulário e retorna ao modo de adição.
     */
    private void limparFormulario() {
        campoTitulo.setText("");
        campoDescricao.setText("");
        comboBoxPrioridade.setSelectedIndex(0);
        idTarefaEmEdicao = null;
        botaoAdicionarSalvar.setText("Adicionar Tarefa");
        setTitle("Sistema de Gerenciamento de Tarefas (Swing)");
        campoTitulo.requestFocusInWindow();
    }

    /**
     * Atualiza a tabela buscando os dados mais recentes do banco.
     */
    private void atualizarTabelaTarefas() {
        int linhaSelecionadaAnteriormente = tabelaTarefas.getSelectedRow();
        tableModel.setRowCount(0); // Limpa a tabela
        List<Tarefa> tarefas = tarefaDAO.listarTarefas();
        for (Tarefa t : tarefas) {
            tableModel.addRow(new Object[]{
                    t.getId(),
                    t.getTitulo(),
                    t.getDescricao(),
                    t.getPrioridade(),
                    t.isConcluida()
            });
        }
        if (linhaSelecionadaAnteriormente != -1 && linhaSelecionadaAnteriormente < tableModel.getRowCount()) {
            tabelaTarefas.setRowSelectionInterval(linhaSelecionadaAnteriormente, linhaSelecionadaAnteriormente);
        }
    }

    /**
     * Ponto de entrada principal para iniciar a aplicação com a GUI Swing.
     * @param args Argumentos da linha de comando (não utilizados).
     */
    public static void main(String[] args) {
        // Tenta aplicar um Look and Feel mais moderno para uma aparência melhorada.
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            System.err.println("Look and Feel Nimbus não encontrado. Usando o padrão do sistema.");
        }

        // Garante que a GUI seja criada e manipulada na Event Dispatch Thread (EDT).
        // Esta é a forma correta e segura de iniciar aplicações Swing.
        SwingUtilities.invokeLater(() -> new TaskManagerCRUD());
    }
}