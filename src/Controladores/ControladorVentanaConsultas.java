package Controladores;

import Auxiliares.GestorMongo;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MapReduceOutput;
import com.mongodb.MongoClient;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Set;

/**
 * Created by Javier on 6/5/2018.
 */
public class ControladorVentanaConsultas implements Initializable {

    @FXML
    public TextField buscarTopics;

    @FXML
    public TextField buscarPlaces;

    @FXML
    public TextField buscarBody;

    @FXML
    public TextArea resultadoConsultas;

    @FXML
    public Button botonTopicsPlaces;

    @FXML
    public Button botonBody;

    @FXML
    public Button botonMapReduce;

    @FXML
    public TextArea resultadoMapReduce;

    @FXML
    public ComboBox listaColecciones;

    @FXML
    public TextField projection;

    @FXML
    public ComboBox listaColeccionesMap;

    GestorMongo gestorMongo;

    public void initialize(URL fxmlLocations, ResourceBundle resources){
        botonTopicsPlaces.setOnAction(event -> {
            resultadoConsultas.clear();

            gestorMongo.setCollection(gestorMongo.getBdActual().getCollection(listaColecciones.getSelectionModel().getSelectedItem().toString()));// Por si no creo documentos y solo quiero consultar alguna coleccion
            imprimirConsulta(gestorMongo.buscarTopicsPlaces(buscarTopics.getText(),buscarPlaces.getText(),projection.getText().split(",")));
        });

        botonBody.setOnAction(event -> {
            resultadoConsultas.clear();

        });

        botonMapReduce.setOnAction(event -> {
            resultadoMapReduce.clear();

            gestorMongo.setCollection(gestorMongo.getBdActual().getCollection(listaColeccionesMap.getSelectionModel().getSelectedItem().toString()));
            imprimirMapReduce(gestorMongo.aplicarMapReduce());
        });


    }

    public void settearColecciones(){
        Set<String> collections = gestorMongo.getBdActual().getCollectionNames();

        for (String coleccion : collections) {
            listaColecciones.getItems().add(coleccion);
            listaColeccionesMap.getItems().add(coleccion);
        }
    }

    public void imprimirConsulta(DBCursor cursor){
        while(cursor.hasNext()){
            resultadoConsultas.appendText(cursor.next().toString()+"\n");
        }
    }

    public void imprimirMapReduce(MapReduceOutput salida){
        for (DBObject o : salida.results()) {

            resultadoMapReduce.appendText(o.toString()+"\n");

        }

    }
}
