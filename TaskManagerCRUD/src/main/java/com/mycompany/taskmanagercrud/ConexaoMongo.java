package com.mycompany.taskmanagercrud;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

/**
 *
 * @author Thierry
 * 
 * Responsavel por estabalecer e gerenciar a conexão com o banco de dados MongoDB.
 * garantindo que apenas uma instância do cliente MongoDB e do banco de dados seja criada e reutilizada.
 */
public class ConexaoMongo {
    
     /**
     * Cliente MongoDB. Interface principal para interagir com uma instância do MongoDB.
     * É estático para ser compartilhado através da classe.
     */
    private static MongoClient mongoClient;
     /**
     * Representa o banco de dados específico ('TaskManagerCRUD') com o qual a aplicação interage.
     * É estático para ser compartilhado através da classe.
     */
    
    private static MongoDatabase database;
     /**
     * Construtor privado para previnir instanciação direta,
     * reforçando o aspecto de utilitário da classe.
     */
    
     /**
     * Estabelece (se ainda não estiver estabelecida) e retorna a conexão com o banco de dados MongoDB.
     * Se o cliente MongoDB não foi inicializado, cria uma nova conexão com o servidor
     * MongoDB em "mongodb://localhost:27017" e obtém o banco de dados "TaskManagerCRUD".
     *
     * @return A instância do {@link MongoDatabase} conectada ao banco "TaskManagerCRUD".
     */
    public static MongoDatabase conectar() {
        // Verifica se o mongoClient ainda não foi inicializado
        if (mongoClient == null) {
            // Cria uma nova instância do MongoClient para o servidor local na porta padrão
            mongoClient = MongoClients.create("mongodb://localhost:27017");
            // Obtém uma referência ao banco de dados "TaskManagerCRUD"
            // Se o banco não existir, o MongoDB geralmente o cria na primeira inserção de dados.
            database = mongoClient.getDatabase("TaskManagerCRUD");
            }
        // Imprime uma mensagem no console indicando que a conexão foi processada.
        // Esta mensagem aparecerá toda vez que conectar() for chamado.
        // Para evitar múltiplas mensagens, poderia ser movido para dentro do if.
        System.out.println("Conectado com sucesso ao MongoDB local!");
        // Retorna a instância do MongoDatabase, seja ela recém-criada ou a existente.
        return database;
    }
}
