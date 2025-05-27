package com.mycompany.taskmanagercrud;

import com.mongodb.client.MongoDatabase;
import java.util.List;
import java.util.Scanner;

/**
 * Classe principal que exibe o menu e interage com o usuário.
 */

public class TaskManagerCRUD {

    public static void main(String[] args) {
        MongoDatabase database = ConexaoMongo.conectar();
        TarefaDAO dao = new TarefaDAO(database);
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n===== GERENCIADOR DE TAREFAS =====");
            System.out.println("1 - Adicionar Tarefa");
            System.out.println("2 - Listar Tarefas");
            System.out.println("3 - Marcar Tarefa como Concluída");
            System.out.println("4 - Excluir Tarefa");
            System.out.println("0 - Sair");
            System.out.print("Escolha uma opção: ");

            String opcao = sc.nextLine();

            switch (opcao) {
                case "1":
                    System.out.print("Título: ");
                    String titulo = sc.nextLine();
                    System.out.print("Descrição: ");
                    String descricao = sc.nextLine();
                    System.out.print("Prioridade (Alta, Média, Baixa): ");
                    String prioridade = sc.nextLine();

                    Tarefa nova = new Tarefa(titulo, descricao, prioridade);
                    dao.adicionarTarefa(nova);
                    System.out.println("Tarefa adicionada com sucesso!");
                    break;

                case "2":
                    List<Tarefa> tarefas = dao.listarTarefas();
                    if (tarefas.isEmpty()) {
                        System.out.println("Nenhuma tarefa cadastrada.");
                    } else {
                        System.out.println("\n==== LISTA DE TAREFAS ====");
                        for (int i = 0; i < tarefas.size(); i++) {
                            Tarefa t = tarefas.get(i);
                            System.out.println((i + 1) + " - " + t.getTitulo() +
                                    " | Prioridade: " + t.getPrioridade() +
                                    " | Concluída: " + (t.isConcluida() ? "Sim" : "Não"));
                            System.out.println("Descrição: " + t.getDescricao());
                            System.out.println("--------------------------------");
                        }
                    }
                    break;

                case "3":
                    tarefas = dao.listarTarefas();
                    if (tarefas.isEmpty()) {
                        System.out.println("Nenhuma tarefa para atualizar.");
                        break;
                    }
                    System.out.println("Escolha o número da tarefa para marcar como concluída:");
                    for (int i = 0; i < tarefas.size(); i++) {
                        System.out.println((i + 1) + " - " + tarefas.get(i).getTitulo());
                    }
                    int numAtualizar = Integer.parseInt(sc.nextLine()) - 1;
                    if (numAtualizar >= 0 && numAtualizar < tarefas.size()) {
                        Tarefa tarefaSelecionada = tarefas.get(numAtualizar);
                        dao.atualizarTarefa(tarefaSelecionada.getId(), true);
                        System.out.println("Tarefa marcada como concluída!");
                    } else {
                        System.out.println("Número inválido.");
                    }
                    break;

                case "4":
                    tarefas = dao.listarTarefas();
                    if (tarefas.isEmpty()) {
                        System.out.println("Nenhuma tarefa para excluir.");
                        break;
                    }
                    System.out.println("Escolha o número da tarefa para excluir:");
                    for (int i = 0; i < tarefas.size(); i++) {
                        System.out.println((i + 1) + " - " + tarefas.get(i).getTitulo());
                    }
                    int numExcluir = Integer.parseInt(sc.nextLine()) - 1;
                    if (numExcluir >= 0 && numExcluir < tarefas.size()) {
                        Tarefa tarefaSelecionada = tarefas.get(numExcluir);
                        dao.excluirTarefa(tarefaSelecionada.getId());
                        System.out.println("Tarefa excluída com sucesso!");
                    } else {
                        System.out.println("Número inválido.");
                    }
                    break;

                case "0":
                    System.out.println("Saindo...");
                    System.exit(0);
                    break;

                default:
                    System.out.println("Opção inválida, tente novamente.");
            }
        }
    }
}
