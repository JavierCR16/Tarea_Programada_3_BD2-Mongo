package Controladores;

import Auxiliares.GestorJSON;
import Auxiliares.GestorMongo;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ResourceBundle;

import javafx.stage.Stage;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.*;
import org.json.*;
import org.xml.sax.InputSource;

public class ControladorVentanaPrincipal implements Initializable {

    @FXML
    public TextField directorioXML;

    @FXML
    public Button botonSeleccionarEntrada;

    @FXML
    public Button botonSeleccionarSalida;

    @FXML
    public TextField nombreColeccion;

    @FXML
    public Button botonDocumentos;

    @FXML
    public Button botonConsultas;

    @FXML
    public TextField pathSalida;

    public GestorJSON gestorJSON = new GestorJSON();

    public GestorMongo gestorMongo;

    public void initialize(URL fxmlLocations, ResourceBundle resources) {
        gestorMongo = new GestorMongo();

        botonSeleccionarEntrada.setOnAction(event -> {
            abrirDirectoryChooser(botonSeleccionarEntrada,directorioXML);
        });

        botonSeleccionarSalida.setOnAction(event -> {
            abrirDirectoryChooser(botonSeleccionarSalida,pathSalida);
        });

        botonDocumentos.setOnAction(event -> {
            if (directorioXML.getText().equals("") || nombreColeccion.getText().equals("")||pathSalida.getText().equals(""))
                mensajeAlerta("Debe seleccionar un directorio y la colección no puede ser vacia");
            else
                gestorJSON.generarJSON(directorioXML.getText(),pathSalida.getText());
                gestorMongo.setCollection(gestorMongo.getBdActual().getCollection(nombreColeccion.getText()));
                gestorMongo.insertarJSON(pathSalida.getText());
                gestorMongo.generarIndices();
        });

        botonConsultas.setOnAction(event -> {
            abrirVentanaConsultas();
        });
    }


    public void mensajeAlerta(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.WARNING);
        alerta.setTitle("Error");
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    public void abrirVentanaConsultas(){
        try {
            FXMLLoader loader = new FXMLLoader();
            Parent root = loader.load(getClass().getResource("../Interfaz/VentanaConsultas.fxml").openStream());
            ControladorVentanaConsultas controllerConsultas = loader.getController();
            controllerConsultas.gestorMongo= this.gestorMongo;
            controllerConsultas.settearColecciones();
            Stage escenario = new Stage();
            escenario.setTitle("Consultas");
            escenario.setScene(new Scene(root, 725, 513));
            escenario.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void abrirDirectoryChooser(Button boton,TextField campoDirectorio){
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory = directoryChooser.showDialog(boton.getScene().getWindow());

        if (selectedDirectory == null) {
            campoDirectorio.clear();
        } else {
            campoDirectorio.setText(selectedDirectory.getAbsolutePath());
        }
    }
}
