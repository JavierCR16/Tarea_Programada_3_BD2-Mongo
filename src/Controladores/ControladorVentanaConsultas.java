package Controladores;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

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

    public void initialize(URL fxmlLocations, ResourceBundle resources){
        botonTopicsPlaces.setOnAction(event -> {

        });

        botonBody.setOnAction(event -> {

        });

        botonMapReduce.setOnAction(event -> {

        });


    }

}
