package Auxiliares;

import com.mongodb.*;
import com.mongodb.async.SingleResultCallback;
import com.mongodb.client.model.Indexes;
import com.mongodb.client.model.Sorts;
import com.mongodb.util.JSON;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Javier on 6/4/2018.
 */
public class GestorMongo {
    private MongoClient mongoClient;
    private DB bdActual;
    private DBCollection collection;



    public GestorMongo(){
        establecerConexion();
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
    public MongoClient getMongoClient() {
        return mongoClient;
    }

    public void setMongoClient(MongoClient mongoClient) {
        this.mongoClient = mongoClient;
    }

    public DB getBdActual() {
        return bdActual;
    }

    public void setBdActual(DB bdActual) {
        this.bdActual = bdActual;
    }

    public DBCollection getCollection() {
        return collection;
    }

    public void setCollection(DBCollection collection) {
        this.collection = collection;
    }

    public DBCursor buscarTopicsPlaces(String topic, String place,String[] projection){
        BasicDBObject camposAUtilizar = new BasicDBObject();

        for (String campoProjection : projection) {
            String[] campoTemporal = campoProjection.split(":");
            camposAUtilizar.put(campoTemporal[0],Integer.parseInt(campoTemporal[1]));
        }


        BasicDBObject andQuery = new BasicDBObject();
        ArrayList<BasicDBObject> queriesSeparados = new ArrayList<>();
        queriesSeparados.add(new BasicDBObject("topics", topic));
        queriesSeparados.add(new BasicDBObject("places", place));
        andQuery.put("$and", queriesSeparados);

        DBCursor cursor = collection.find(andQuery,camposAUtilizar);

        return cursor;
    }

    public void generarIndices(){
        String nombreArreglos[] = {"TOPICS", "PLACES", "PEOPLE", "ORGS","EXCHANGES"};
        for(int i=0;i<nombreArreglos.length;i++) {
            collection.createIndex(new BasicDBObject(nombreArreglos[i],1),"Indice "+nombreArreglos[i]);

        }
        collection.createIndex(new BasicDBObject("TEXT.BODY","text"),"Indice Body");
    }

    public MapReduceOutput aplicarMapReduce(){
        String map = "function(){" +
                "" +
                "for (var i = 0; i<this.PLACES.length; i++){" +
                "           emit(this.PLACES[i],1)   " +
                "}" +
                "  } ";
        String reduce = "function(key,values){" +
                "" +
                "return Array.sum(values)" +
                "" +
                "}";
        DBObject query = new BasicDBObject("PLACES", new BasicDBObject("$exists", true));;

        MapReduceCommand cmd = new MapReduceCommand(collection, map, reduce,
                null, MapReduceCommand.OutputType.INLINE,query);
       return collection.mapReduce(cmd);
    }



}
