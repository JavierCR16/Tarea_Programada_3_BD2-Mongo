package Auxiliares;

import com.mongodb.*;
import com.mongodb.util.JSON;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Javier on 6/4/2018.
 */
public class GestorMongo {
    public MongoClient mongoClient;
    public DB bdActual;
    public DBCollection collection;

    public GestorMongo(String nombreCollection){
        establecerConexion();
        collection = this.bdActual.getCollection(nombreCollection);
    }
    public void insertarJSON(String directorioJSON) {
        try {
            File directorioJs = new File(directorioJSON);
            File[] archivosJSON = directorioJs.listFiles();

            for (File reuterJSON : archivosJSON) {
                DBObject objetoDocumento = (DBObject) JSON.parse(new String(Files.readAllBytes(Paths.get(reuterJSON.toURI())), "UTF-8"));
                this.collection.insert(objetoDocumento);
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void establecerConexion(){
        this.mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));
        bdActual = mongoClient.getDB("TareaProgramada3BD2");
    }

}
