package Controladores;

import Auxiliares.GestorJSON;
import Auxiliares.GestorMongo;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ResourceBundle;

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
    public Button botonSeleccionar;

    @FXML
    public TextField nombreColeccion;

    @FXML
    public Button botonDocumentos;

    @FXML
    public Button botonConsultas;

    public GestorJSON gestorJSON = new GestorJSON();

    public GestorMongo gestorMongo;

    public void initialize(URL fxmlLocations, ResourceBundle resources) {

        botonSeleccionar.setOnAction(event -> {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            File selectedDirectory = directoryChooser.showDialog(botonSeleccionar.getScene().getWindow());

            if (selectedDirectory == null) {
                directorioXML.clear();
            } else {
                directorioXML.setText(selectedDirectory.getAbsolutePath());
            }
        });

        botonDocumentos.setOnAction(event -> {
            if (directorioXML.getText().equals(""))
                mensajeAlerta("Debe seleccionar un directorio");
            else
               // gestorJSON.generarJSON(directorioXML.getText());
                gestorMongo = new GestorMongo(nombreColeccion.getText());
                gestorMongo.insertarJSON("Archivos JSON");
        });
    }


    public void mensajeAlerta(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.WARNING);
        alerta.setTitle("Error");
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

}
