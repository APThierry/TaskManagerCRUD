package com.mycompany.taskmanagercrud;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import static com.mongodb.client.model.Filters.eq;
import java.util.ArrayList;
import java.util.List;
import org.bson.Document;
import org.bson.types.ObjectId;

/**
 * Classe responsável pelas operações no banco de dados (DAO)
 */
public class TarefaDAO {

    private final MongoCollection<Document> colecao;

    // Construtor que conecta na coleção "tarefas"
    public TarefaDAO(MongoDatabase database) {
        this.colecao = database.getCollection("tarefas");
    }

    // Adiciona uma tarefa no banco
    public void adicionarTarefa(Tarefa tarefa) {
        Document doc = new Document("titulo", tarefa.getTitulo())
                .append("descricao", tarefa.getDescricao())
                .append("prioridade", tarefa.getPrioridade())
                .append("concluida", tarefa.isConcluida());
        colecao.insertOne(doc);
    }

    // Lista todas as tarefas
    public List<Tarefa> listarTarefas() {
        List<Tarefa> lista = new ArrayList<>();
        for (Document doc : colecao.find()) {
            Tarefa tarefa = new Tarefa(
                    doc.getString("titulo"),
                    doc.getString("descricao"),
                    doc.getString("prioridade")
            );
            tarefa.setConcluida(doc.getBoolean("concluida"));
            tarefa.setId(doc.getObjectId("_id").toHexString());
            lista.add(tarefa);
        }
        return lista;
    }

    // Atualiza o status de concluída
    public void atualizarTarefa(String id, boolean concluida) {
        colecao.updateOne(eq("_id", new ObjectId(id)),
                new Document("$set", new Document("concluida", concluida)));
    }

    // Exclui uma tarefa
    public void excluirTarefa(String id) {
        colecao.deleteOne(eq("_id", new ObjectId(id)));
    }
}
