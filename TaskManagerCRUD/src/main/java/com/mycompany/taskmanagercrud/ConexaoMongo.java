
package com.mycompany.taskmanagercrud;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

/**
 *
 * @author thier
 */
public class ConexaoMongo {
    public static MongoClient conectar() {
        String uri = "mongodb://localhost:27017";
        MongoClient client = MongoClients.create(uri);
        System.out.println("Conectado com sucesso ao MongoDB local!");
        return client;
    }
}
