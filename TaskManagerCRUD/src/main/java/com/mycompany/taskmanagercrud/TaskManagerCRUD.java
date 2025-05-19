package com.mycompany.taskmanagercrud;

import com.mongodb.client.MongoDatabase;

/**
 *
 * @author Thierry
 */

public class TaskManagerCRUD {

    public static void main(String[] args) {
    MongoDatabase db = ConexaoMongo.conectar();
    TarefaDAO dao = new TarefaDAO(db);

    Tarefa nova = new Tarefa("Estudar Java", "Revisar orientação a objetos", "Alta");
    dao.adicionarTarefa(nova);
    System.out.println("Tarefa adicionada!");
    }
}


