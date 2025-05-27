package com.mycompany.taskmanagercrud;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

/**
 *
 * @author Thierry
 */
public class ConexaoMongo {
    private static MongoClient mongoClient;
    private static MongoDatabase database;
    
    public static MongoDatabase conectar() {
        if (mongoClient == null) {
            mongoClient = MongoClients.create("mongodb://localhost:27017");
            database = mongoClient.getDatabase("TaskManagerCRUD");
            }
        
        System.out.println("Conectado com sucesso ao MongoDB local!");
        return database;
    }
}
