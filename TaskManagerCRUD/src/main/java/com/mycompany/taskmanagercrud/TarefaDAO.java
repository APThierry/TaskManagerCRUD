package com.mycompany.taskmanagercrud;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import static com.mongodb.client.model.Filters.eq;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import java.util.ArrayList;
import java.util.List;
import org.bson.Document;
import org.bson.types.ObjectId;

/**
 * @author Thierry
 * 
 * DAO (Data Access Object) para a entidade {@link Tarefa}.
 * Esta classe encapsula toda a lógica de acesso e manipulação dos dados das tarefas
 * no banco de dados MongoDB. Ela abstrai as operações CRUD (Create, Read, Update, Delete)
 * para a coleção de tarefas.
 */
public class TarefaDAO {

    /**
     * A coleção MongoDB onde as tarefas são armazenadas.
     * É final porque sua referência não deve mudar após a inicialização.
     */
    private final MongoCollection<Document> colecao;

    /**
     * Construtor da classe TarefaDAO.
     * Inicializa o DAO com uma referência à coleção "tarefas" do banco de dados fornecido.
     *
     * @param database A instância do {@link MongoDatabase} a ser utilizada para obter a coleção de tarefas.
     * Se a coleção "tarefas" não existir, ela será criada pelo MongoDB
     * quando o primeiro documento for inserido nela.
     */
    public TarefaDAO(MongoDatabase database) {
        // Obtém uma referência à coleção chamada "tarefas" dentro do banco de dados.
        this.colecao = database.getCollection("tarefas");
    }

    /**
     * Adiciona uma nova tarefa ao banco de dados.
     * Converte o objeto {@link Tarefa} em um {@link Document} BSON e o insere na coleção.
     *
     * @param tarefa O objeto {@link Tarefa} a ser adicionado. O ID da tarefa será
     * automaticamente gerado pelo MongoDB se não estiver presente.
     */
    public void adicionarTarefa(Tarefa tarefa) {
        // Cria um novo objeto Document (BSON) a partir dos dados da tarefa.
        Document doc = new Document("titulo", tarefa.getTitulo())
                .append("descricao", tarefa.getDescricao())
                .append("prioridade", tarefa.getPrioridade())
                .append("concluida", tarefa.isConcluida());
        // Insere o documento na coleção MongoDB.
        colecao.insertOne(doc);
        System.out.println("Tarefa '" + tarefa.getTitulo() + "' adicionada ao banco de dados.");
    }

    /**
     * Lista todas as tarefas armazenadas no banco de dados.
     * Recupera todos os documentos da coleção, converte cada um em um objeto {@link Tarefa}
     * e os retorna em uma lista.
     *
     * @return Uma {@link List} de objetos {@link Tarefa} representando todas as tarefas.
     * Retorna uma lista vazia se não houver tarefas.
     */
    public List<Tarefa> listarTarefas() {
        // Cria uma nova ArrayList para armazenar os objetos Tarefa recuperados.
        List<Tarefa> listaDeTarefas = new ArrayList<>();
        // colecao.find() executa uma consulta na coleção para retornar todos os documentos.
        // O loop itera sobre cada Document (representado pela variável doc) retornado.
        for (Document doc : colecao.find()) {
            // Cria uma nova instância da classe Tarefa com os dados do documento.
            Tarefa tarefa = new Tarefa(
                    doc.getString("titulo"),
                    doc.getString("descricao"),
                    doc.getString("prioridade")
            );
            // Define o status de conclusão da tarefa.
            // O segundo parâmetro de getBoolean é um valor default caso o campo não exista no documento.
            tarefa.setConcluida(doc.getBoolean("concluida", false));
            // Obtém o ObjectId do campo "_id", converte para String e define o ID da tarefa.
            tarefa.setId(doc.getObjectId("_id").toHexString());
            // Adiciona o objeto tarefa (preenchido com os dados do documento) à lista.
            listaDeTarefas.add(tarefa);
        }
        // Retorna a lista contendo todos os objetos Tarefa recuperados.
        return listaDeTarefas;
    }

    /**
     * Atualiza o status de conclusão de uma tarefa existente no banco de dados.
     *
     * @param id O ID (como String hexadecimal) da tarefa a ser atualizada.
     * @param concluida O novo status de conclusão (true para concluída, false para não concluída).
     * @return {@code true} se a atualização foi bem-sucedida e pelo menos um documento foi modificado,
     * {@code false} caso contrário ou se o ID for inválido.
     */
    public boolean atualizarStatusTarefa(String id, boolean concluida) {
        try {
            ObjectId objectId = new ObjectId(id); // Converte a String ID para ObjectId
            // Cria o documento de atualização usando o operador $set para modificar o campo "concluida".
            Document updateDoc = new Document("$set", new Document("concluida", concluida));
            // Executa a atualização na coleção, filtrando pelo _id.
            UpdateResult result = colecao.updateOne(eq("_id", objectId), updateDoc);
            System.out.println("INFO: Tentativa de atualizar status da tarefa ID " + id + ". Documentos modificados: " + result.getModifiedCount());
            return result.getModifiedCount() > 0;
        } catch (IllegalArgumentException e) {
            System.err.println("ERRO: Formato de ID invalido ao tentar atualizar status da tarefa: " + id + " - " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Atualiza os detalhes (título, descrição, prioridade) de uma tarefa existente.
     * O status de conclusão não é alterado por este método.
     *
     * @param id O ID (como String hexadecimal) da tarefa a ser atualizada.
     * @param novoTitulo O novo título para a tarefa.
     * @param novaDescricao A nova descrição para a tarefa.
     * @param novaPrioridade A nova prioridade para a tarefa.
     * @return {@code true} se a atualização foi bem-sucedida e pelo menos um documento foi modificado,
     * {@code false} caso contrário, se nenhum campo foi alterado ou se o ID for inválido.
     */
    public boolean atualizarTarefa(String id, String novoTitulo, String novaDescricao, String novaPrioridade) {
        try {
            ObjectId objectId = new ObjectId(id);
            Document updateFields = new Document();
            boolean algumaAlteracao = false;

            // Adiciona campos ao $set somente se eles forem fornecidos e diferentes do valor atual (opcional, mas bom para evitar updates desnecessários)
            // Para uma verificação mais robusta de "diferente do valor atual", seria preciso buscar a tarefa primeiro.
            // Aqui, apenas atualizamos se um novo valor não nulo/vazio for fornecido.
            if (novoTitulo != null && !novoTitulo.trim().isEmpty()) {
                updateFields.append("titulo", novoTitulo.trim());
                algumaAlteracao = true;
            }
            if (novaDescricao != null && !novaDescricao.trim().isEmpty()) {
                updateFields.append("descricao", novaDescricao.trim());
                algumaAlteracao = true;
            }
            if (novaPrioridade != null && !novaPrioridade.trim().isEmpty()) {
                updateFields.append("prioridade", novaPrioridade.trim());
                algumaAlteracao = true;
            }

            if (!algumaAlteracao) {
                System.out.println("INFO: Nenhum detalhe valido fornecido para atualizar a tarefa ID " + id);
                return false; // Nada para atualizar
            }

            Document updateDoc = new Document("$set", updateFields);
            UpdateResult result = colecao.updateOne(eq("_id", objectId), updateDoc);
            System.out.println("INFO: Tentativa de atualizar detalhes da tarefa ID " + id + ". Documentos modificados: " + result.getModifiedCount());
            return result.getModifiedCount() > 0;
        } catch (IllegalArgumentException e) {
            System.err.println("ERRO: Formato de ID invalido ao tentar atualizar detalhes da tarefa: " + id + " - " + e.getMessage());
            return false;
        }
    }

    /**
     * Exclui uma tarefa do banco de dados com base no seu ID.
     *
     * @param id O ID (como String hexadecimal) da tarefa a ser excluída.
     * @return {@code true} se a exclusão foi bem-sucedida e um documento foi removido,
     * {@code false} caso contrário ou se o ID for inválido.
     */
    public boolean excluirTarefa(String id) {
        try {
            ObjectId objectId = new ObjectId(id); // Converte a String ID para ObjectId
            // Executa a exclusão na coleção, filtrando pelo _id.
            DeleteResult result = colecao.deleteOne(eq("_id", objectId));
            System.out.println("INFO: Tentativa de excluir tarefa ID " + id + ". Documentos removidos: " + result.getDeletedCount());
            return result.getDeletedCount() > 0;
        } catch (IllegalArgumentException e) {
            System.err.println("ERRO: Formato de ID invalido ao tentar excluir tarefa: " + id + " - " + e.getMessage());
            return false;
        }
    }
}
