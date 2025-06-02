package com.mycompany.taskmanagercrud;

import com.mongodb.client.MongoDatabase;
import java.util.List;
import java.util.Scanner;

/**
 * @author Thierry
 * @author Nathan
 * 
 * Permite ao usuário interagir com o sistema para adicionar, listar, editar,
 * marcar como concluída/pendente e excluir tarefas através de um menu no console.
 */

public class TaskManagerCRUD {

    /**
     * Ponto de entrada principal da aplicação em modo console.
     * Inicializa a conexão com o banco de dados, o DAO e o Scanner para entrada do usuário,
     * e então entra em um loop para exibir o menu e processar as opções do usuário.
     *
     * @param args Argumentos da linha de comando (não utilizados).
     */
    public static void main(String[] args) {
        // Estabelece a conexão com o MongoDB e armazena a referência ao banco de dados.
        MongoDatabase database = ConexaoMongo.conectar();
        // Cria uma instância de TarefaDAO, passando o banco de dados conectado.
        TarefaDAO dao = new TarefaDAO(database);
        // Cria um objeto Scanner para ler a entrada do usuário a partir do console.
        // Usar try-with-resources para garantir que o Scanner seja fechado.
        try (Scanner sc = new Scanner(System.in)) {
            boolean executando = true;
            while (executando) {
                // Imprime as opções do menu no console.
                System.out.println("\n===== GERENCIADOR DE TAREFAS =====");
                System.out.println("1 - Adicionar Tarefa");
                System.out.println("2 - Listar Tarefas");
                System.out.println("3 - Alterar Status da Tarefa");
                System.out.println("4 - Editar Tarefa (Titulo, Descricao, Prioridade)");
                System.out.println("5 - Excluir Tarefa");
                System.out.println("0 - Sair");
                System.out.print("Escolha uma opcao: ");

                // Lê a opção escolhida pelo usuário.
                String opcao = sc.nextLine().trim();

                // Estrutura switch para tratar a opção escolhida.
                switch (opcao) {
                    case "1": // Adicionar Tarefa
                        adicionarTarefa(sc, dao);
                        break;

                    case "2": // Listar Tarefas
                        listarTarefas(dao);
                        break;

                    case "3": // Alterar Status de Conclusão da Tarefa
                        alterarStatus(sc, dao);
                        break;

                    case "4": // Editar Tarefa
                        editarTarefa(sc, dao);
                        break;

                    case "5": // Excluir Tarefa
                        excluirTarefa(sc, dao);
                        break;

                    case "0": // Sair
                        System.out.println("Saindo do Gerenciador de Tarefas...");
                        executando = false; // Termina o loop while
                        break;

                    default: // Opção Inválida
                        System.out.println("Opção invalida. Por favor, tente novamente.");
                }
            }
        } // O Scanner sc é fechado automaticamente aqui pelo try-with-resources
        System.out.println("Aplicacao encerrada.");
    }

    /**
     * Solicita os dados de uma nova tarefa ao usuário e a adiciona ao banco.
     * @param sc Scanner para ler a entrada do usuário.
     * @param dao DAO para interagir com o banco de dados.
     */
    private static void adicionarTarefa(Scanner sc, TarefaDAO dao) {
        System.out.println("\n--- Adicionar Nova Tarefa ---");
        System.out.print("Titulo: ");
        String titulo = sc.nextLine().trim();
        System.out.print("Descricao: ");
        String descricao = sc.nextLine().trim();
        System.out.print("Prioridade (Ex: Alta, Media, Baixa): ");
        String prioridade = sc.nextLine().trim();

        if (titulo.isEmpty() || descricao.isEmpty() || prioridade.isEmpty()) {
            System.out.println("ERRO: Titulo, Descricao e Prioridade nao podem ser vazios.");
            return;
        }

        Tarefa nova = new Tarefa(titulo, descricao, prioridade);
        dao.adicionarTarefa(nova); // O método no DAO já imprime uma mensagem de INFO
        System.out.println("Tarefa adicionada!");
    }

    /**
     * Lista todas as tarefas cadastradas.
     * @param dao DAO para buscar as tarefas.
     */
    private static void listarTarefas(TarefaDAO dao) {
        System.out.println("\n--- Lista de Tarefas ---");
        List<Tarefa> tarefas = dao.listarTarefas(); // Obtém todas as tarefas.
        if (tarefas.isEmpty()) {
            System.out.println("Nenhuma tarefa cadastrada.");
        } else {
            for (int i = 0; i < tarefas.size(); i++) {
                Tarefa t = tarefas.get(i);
                // Imprime os detalhes de cada tarefa.
                System.out.println("-----------------------------------------");
                System.out.println("Tarefa #" + (i + 1) + " (ID: " + t.getId() + ")");
                System.out.println("  Titulo: " + t.getTitulo());
                System.out.println("  Descricao: " + t.getDescricao());
                System.out.println("  Prioridade: " + t.getPrioridade());
                System.out.println("  Concluida: " + (t.isConcluida() ? "Sim" : "Nao"));
            }
            System.out.println("-----------------------------------------");
        }
    }

    /**
     * Permite ao usuário selecionar uma tarefa e alterar seu status de conclusão.
     * @param sc Scanner para entrada do usuário.
     * @param dao DAO para interagir com o banco.
     */
    private static void alterarStatus(Scanner sc, TarefaDAO dao) {
        System.out.println("\n--- Alterar Status de Conclusao ---");
        List<Tarefa> tarefas = dao.listarTarefas();
        if (tarefas.isEmpty()) {
            System.out.println("Nenhuma tarefa para atualizar.");
            return;
        }

        System.out.println("Escolha o numero da tarefa para alterar o status:");
        for (int i = 0; i < tarefas.size(); i++) {
            Tarefa t = tarefas.get(i);
            System.out.println((i + 1) + " - " + t.getTitulo() + (t.isConcluida() ? " (Status: Concluida)" : " (Status: Pendente)"));
        }
        System.out.print("Numero da tarefa: ");

        try {
            int numTarefa = Integer.parseInt(sc.nextLine().trim());
            int indiceSelecionado = numTarefa - 1; // Ajusta para índice base 0.

            if (indiceSelecionado >= 0 && indiceSelecionado < tarefas.size()) {
                Tarefa tarefaSelecionada = tarefas.get(indiceSelecionado);
                boolean novoStatus = !tarefaSelecionada.isConcluida(); // Inverte o status atual
                if (dao.atualizarStatusTarefa(tarefaSelecionada.getId(), novoStatus)) {
                    System.out.println("Status da tarefa '" + tarefaSelecionada.getTitulo() + "' alterado para: " + (novoStatus ? "Concluida" : "Pendente"));
                } else {
                    System.out.println("FALHA: Não foi possivel atualizar o status da tarefa. Verifique o ID ou os logs.");
                }
            } else {
                System.out.println("ERRO: Numero da tarefa invalido.");
            }
        } catch (NumberFormatException e) {
            System.out.println("ERRO: Entrada invalida. Por favor, insira um número correspondente a tarefa.");
        }
    }
    
    /**
     * Permite ao usuário selecionar uma tarefa e editar seus detalhes (título, descrição, prioridade).
     * @param sc Scanner para entrada do usuário.
     * @param dao DAO para interagir com o banco.
     */
    private static void editarTarefa(Scanner sc, TarefaDAO dao) {
        System.out.println("\n--- Editar Tarefa ---");
        List<Tarefa> tarefas = dao.listarTarefas();
        if (tarefas.isEmpty()) {
            System.out.println("Nenhuma tarefa para editar.");
            return;
        }

        System.out.println("Escolha o numero da tarefa para editar:");
        for (int i = 0; i < tarefas.size(); i++) {
            System.out.println((i + 1) + " - " + tarefas.get(i).getTitulo());
        }
        System.out.print("Numero da tarefa: ");
        
        try {
            int numTarefa = Integer.parseInt(sc.nextLine().trim());
            int indiceSelecionado = numTarefa - 1;

            if (indiceSelecionado >= 0 && indiceSelecionado < tarefas.size()) {
                Tarefa tarefaParaEditar = tarefas.get(indiceSelecionado);
                System.out.println("Editando tarefa: " + tarefaParaEditar.getTitulo());
                System.out.println("Deixe em branco para nao alterar o campo.");

                System.out.print("Novo Titulo (" + tarefaParaEditar.getTitulo() + "): ");
                String novoTitulo = sc.nextLine().trim();
                System.out.print("Nova Descricao (" + tarefaParaEditar.getDescricao() + "): ");
                String novaDescricao = sc.nextLine().trim();
                System.out.print("Nova Prioridade (" + tarefaParaEditar.getPrioridade() + "): ");
                String novaPrioridade = sc.nextLine().trim();

                // Usa os valores atuais se os novos estiverem em branco
                novoTitulo = novoTitulo.isEmpty() ? tarefaParaEditar.getTitulo() : novoTitulo;
                novaDescricao = novaDescricao.isEmpty() ? tarefaParaEditar.getDescricao() : novaDescricao;
                novaPrioridade = novaPrioridade.isEmpty() ? tarefaParaEditar.getPrioridade() : novaPrioridade;

                if (dao.atualizarTarefa(tarefaParaEditar.getId(), novoTitulo, novaDescricao, novaPrioridade)) {
                    System.out.println("Tarefa atualizada!");
                } else {
                    System.out.println("FALHA: Nao foi possivel atualizar a tarefa, ou nenhuma alteracao foi detectada.");
                }
            } else {
                System.out.println("ERRO: Numero da tarefa invalido.");
            }
        } catch (NumberFormatException e) {
            System.out.println("ERRO: Entrada invalida. Por favor, insira um numero.");
        }
    }

    /**
     * Permite ao usuário selecionar uma tarefa para exclusão, com confirmação.
     * @param sc Scanner para entrada do usuário.
     * @param dao DAO para interagir com o banco.
     */
    private static void excluirTarefa(Scanner sc, TarefaDAO dao) {
        System.out.println("\n--- Excluir Tarefa ---");
        List<Tarefa> tarefas = dao.listarTarefas();
        if (tarefas.isEmpty()) {
            System.out.println("Nenhuma tarefa para excluir.");
            return;
        }

        System.out.println("Escolha o numero da tarefa para excluir:");
        for (int i = 0; i < tarefas.size(); i++) {
            System.out.println((i + 1) + " - " + tarefas.get(i).getTitulo());
        }
        System.out.print("Numero da tarefa: ");

        try {
            int numTarefa = Integer.parseInt(sc.nextLine().trim());
            int indiceSelecionado = numTarefa - 1;

            if (indiceSelecionado >= 0 && indiceSelecionado < tarefas.size()) {
                Tarefa tarefaSelecionada = tarefas.get(indiceSelecionado);
                System.out.print("Tem certeza que deseja excluir a tarefa '" + tarefaSelecionada.getTitulo() + "'? (s/N): ");
                String confirmacao = sc.nextLine().trim().toLowerCase();

                if (confirmacao.equals("s")) {
                    if (dao.excluirTarefa(tarefaSelecionada.getId())) {
                        System.out.println("Tarefa excluida!");
                    } else {
                        System.out.println("FALHA: Não foi possivel excluir a tarefa. Verifique o ID ou os logs.");
                    }
                } else {
                    System.out.println("Exclusao cancelada.");
                }
            } else {
                System.out.println("ERRO: Número da tarefa invalido.");
            }
        } catch (NumberFormatException e) {
            System.out.println("ERRO: Entrada invalida. Por favor, insira um numero.");
        }
    }
}