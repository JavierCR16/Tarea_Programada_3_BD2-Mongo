package Controladores;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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

    public void initialize(URL fxmlLocations, ResourceBundle resources){

        botonSeleccionar.setOnAction(event -> {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            File selectedDirectory = directoryChooser.showDialog(botonSeleccionar.getScene().getWindow());

            if(selectedDirectory == null){
                directorioXML.clear();
            }else{
                directorioXML.setText(selectedDirectory.getAbsolutePath());
            }
        });

        botonDocumentos.setOnAction(event -> {
         //   if(directorioXML.getText().equals(""))

                //Poner mensaje error :v
        //    else
                generarJSON(directorioXML.getText());
        });
    }

    public JSONArray extraerTagsD(String tagABuscar,Element reuter){


        JSONArray nombresExtraidos = new JSONArray();
        NodeList tag = reuter.getElementsByTagName(tagABuscar);
        NodeList textoTags = tag.item(0).getChildNodes();

        for(int i =0;i<textoTags.getLength();i++){
            nombresExtraidos.add(textoTags.item(i).getTextContent().trim());
        }
        return nombresExtraidos;
    }

    public void extraerTagsIndividuales(String tagABuscar,Element reuter,JSONObject objeto){

        objeto.put(tagABuscar.toLowerCase(),reuter.getElementsByTagName(tagABuscar).item(0).getTextContent());

    }

    public JSONObject extraerTagText(Element elementoReuter){
        JSONObject objetoJS = new JSONObject();

        NodeList tag = elementoReuter.getElementsByTagName("TEXT");
        NodeList textoTags = tag.item(0).getChildNodes();
        for(int i =0;i<textoTags.getLength();i++){

            switch(textoTags.item(i).getNodeName()){
                case "TITLE":
                    objetoJS.put("title",textoTags.item(i).getTextContent());
                    break;

                case "AUTHOR":
                    objetoJS.put("author",textoTags.item(i).getTextContent());
                    break;

                case "DATELINE":
                    objetoJS.put("dateline",textoTags.item(i).getTextContent());
                    break;

                case "BODY":
                    objetoJS.put("body",textoTags.item(i).getTextContent());
                    break;
            }
        }
        return objetoJS;
    }

    public Document retornarDoc(File archivoXML){// TODO ITERAR SOBRE TODOS LOS XMLS

        try {
    /*        FileReader fr = new FileReader(archivoXML);
            BufferedReader br = new BufferedReader(fr);
            String line = "";
            StringBuilder contents = new StringBuilder();
            while((line = br.readLine())!=null) {
                contents.append(line + "\n");
            }
            String strippedcontents =  stripNonValidXMLCharacters(contents.toString());
            StringReader sr = new StringReader(strippedcontents);*/



           // File inputFile = new File("C:\\Users\\Javier\\Desktop\\Bases de Datos II\\Proyectos\\Proyecto 3\\Proyecto 3\\reuters21578\\reut2-000.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

            return dBuilder.parse(archivoXML);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public void generarJSON(String directorioXMLS){

        File dir = new File(directorioXMLS);
        File[] directoryListing = dir.listFiles();
        int contador =0;

        for (File reuterXML : directoryListing) {


            Document doc = retornarDoc(reuterXML);
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("REUTERS");
            String nombreArreglos[] = {"topics", "places", "people", "orgs","exchanges"};

            for (int i = 0; i <nList.getLength() ; i++) {//nList.getLength()

                ArrayList<JSONArray> arreglosJson = new ArrayList<>();

                Element elementoReuter = (Element) nList.item(i);

                JSONObject objetoJson = new JSONObject();

                objetoJson.put("NewID", elementoReuter.getAttribute("NEWID"));
                extraerTagsIndividuales("DATE", elementoReuter, objetoJson);

                JSONArray topics = extraerTagsD("TOPICS", elementoReuter);
                JSONArray places = extraerTagsD("PLACES", elementoReuter);
                JSONArray people = extraerTagsD("PEOPLE", elementoReuter);
                JSONArray orgs = extraerTagsD("ORGS", elementoReuter);
                JSONArray exchanges = extraerTagsD("EXCHANGES", elementoReuter);
                Collections.addAll(arreglosJson, topics, places, people, orgs, exchanges);

                prepararArreglosJSON(objetoJson, arreglosJson, nombreArreglos);

                arreglosJson.clear();

                objetoJson.put("text", extraerTagText(elementoReuter));

                escribirJSON(objetoJson, contador);
                contador++;

            }
        }

    }

    public void escribirJSON(JSONObject objetoJson,int numero){
        try {
            FileWriter file = new FileWriter("Archivos JSON\\reuterJSON"+numero+".json") ;

            file.write(objetoJson.toJSONString());
            file.flush();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void prepararArreglosJSON(JSONObject objetoJson, ArrayList<JSONArray> arreglos,String [] nombreArreglos){
        for(int i =0;i<arreglos.size();i++){
            if(arreglos.get(i).size()>0)
                objetoJson.put(nombreArreglos[i],arreglos.get(i));
        }
    }

   /* public static String stripNonValidXMLCharacters(String in) {
        StringBuilder out = new StringBuilder();
        char current;

        if (in == null || ("".equals(in))) return "";
        for (int i = 0; i < in.length(); i++) {
            current = in.charAt(i);
            if ((current == 0x9) ||
                    (current == 0xA) ||
                    (current == 0xD) ||
                    ((current >= 0x20) && (current <= 0xD7FF)) ||
                    ((current >= 0xE000) && (current <= 0xFFFD)) ||
                    ((current >= 0x10000) && (current <= 0x10FFFF)))
                out.append(current);
        }
        return out.toString();
    }*/
}
