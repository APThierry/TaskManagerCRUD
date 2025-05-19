package com.mycompany.taskmanagercrud;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Thierry
 */
public class TarefaDAO {
    private final MongoCollection<Document> colecao;

    public TarefaDAO(MongoDatabase database) {
        this.colecao = database.getCollection("tarefas");
    }

    public void adicionarTarefa(Tarefa tarefa) {
        Document doc = new Document("titulo", tarefa.getTitulo())
                .append("descricao", tarefa.getDescricao())
                .append("prioridade", tarefa.getPrioridade())
                .append("concluida", tarefa.isConcluida());
        colecao.insertOne(doc);
    }

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

    public void atualizarTarefa(String id, boolean concluida) {
        colecao.updateOne(Filters.eq("_id", new ObjectId(id)),
                new Document("$set", new Document("concluida", concluida)));
    }

    public void excluirTarefa(String id) {
        colecao.deleteOne(Filters.eq("_id", new ObjectId(id)));
    }
}
