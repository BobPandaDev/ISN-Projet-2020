package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML Button btnMin;
    @FXML Button btnClose;
    @FXML Pane topPane;
    @FXML TextField txtFieldTwelveDigit;
    @FXML WebView webSVG;
    WebEngine engine;

    private double xOffset = 0;
    private double yOffset = 0;
    public String[] leftDigits = new String[7];
    public String[] rightDigits = new String[6];
    public String[] leftDigitsEncoded = new String[7];
    public String[] rightDigitsEncoded = new String[6];
    public String[] rightDigitEncoder = {"1110010","1100110", "1101100", "1000010", "1011100", "1001110", "1010000", "1000100", "1001000", "1110100"};
    public String[] leftDigitEncoderImpair = {"0001101","0011001", "0010011", "0111101", "0100011", "0110001", "0101111", "0111011", "0110111", "0001011"};
    public String[] leftDigitEncoderPair = {"0100111","0110011", "0011011", "0100001", "0011101", "0111001", "0000101", "0010001", "0001001", "0010111"};







    @Override
    public void initialize(URL url, ResourceBundle rb) {
        engine = webSVG.getEngine();
    }

    @FXML
    protected void handleCloseAction(ActionEvent event){
        Stage stage = (Stage) btnClose.getScene().getWindow();
        stage.close();
    }

    @FXML
    protected void handleMinAction(ActionEvent event){
        Stage stage = (Stage) btnMin.getScene().getWindow();
        stage.setIconified(true);
    }

    @FXML
    protected void handleClickAction(MouseEvent event){
        xOffset = event.getSceneX();
        yOffset = event.getSceneY();
    }
    @FXML
    protected void handleMovementAction(MouseEvent event){
        Stage stage = (Stage) topPane.getScene().getWindow();
        stage.setX(event.getScreenX() - xOffset);
        stage.setY(event.getScreenY() - yOffset);
    }

    @FXML
    public void generateClick(MouseEvent event){
        barCode();
        //engine.load(System.getProperty("user.dir")+"\\codeBar.html");
        engine.load("D:\\Axel\\Programmation\\Java\\codeBar.html"); //probleme avec le chargement de l'image.
        //txtFieldTwelveDigit.setText(System.getProperty("user.dir")+"\\codeBar.html");

    }


    public void barCode() {
        String code = txtFieldTwelveDigit.getText();

        if(code.length() != 12){
            txtFieldTwelveDigit.setText("12 numéros sont requis");
            txtFieldTwelveDigit.setStyle("-fx-text-fill: red;");
        }
        else{
            txtFieldTwelveDigit.setStyle("-fx-text-fill: black;");

            code = code + checkDigit(code);
            gaucheDroite(code);

            //rightDigitsEncoder
            for (int i = 0; i < rightDigits.length; i++) {
                rightDigitsEncoded[i] = digitsEncoder(rightDigits[i], rightDigitEncoder);
            }
            //leftDigitsEncoder
            leftDigitsEncoder(pairImpair(leftDigits[0]));

            String[] encodedDigits = new String[12];
            for (int i = 0; i < encodedDigits.length; i++) {
                if(i < 6){
                    encodedDigits[i] = leftDigitsEncoded[i];
                }
                else{
                    encodedDigits[i] = rightDigitsEncoded[i-6];
                }
            }

            //Draw bars
            drawBars(encodedDigits);
        }

        //texte en noir
        txtFieldTwelveDigit.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(final ObservableValue<? extends String> observable, final String oldValue, final String newValue) {
                txtFieldTwelveDigit.setStyle("-fx-text-fill: black;");
            }
        });

    }

    //calculate Check Digit
    public String checkDigit(String digits){
        StringBuilder codeBuilder = new StringBuilder(digits);
        String codeReversed = codeBuilder.reverse().toString();

        String position = "impair";
        int impairs = 0;
        int pairs = 0;
        for (int i = 0; i < 12; i++) {
            char s = codeReversed.charAt(i);
            int ch = Integer.parseInt(String.valueOf(s));
            if (position == "impair") {
                impairs = impairs + (ch * 3);
                position = "pair";
            } else {
                pairs = pairs + ch;
                position = "impair";
            }
        }
        int total = impairs + pairs;

        int checkDigit = 0;
        while ((total + checkDigit) % 10 != 0) {
            checkDigit++;
        }

        return String.valueOf(checkDigit);
    }

    //Calculate bars
    public void gaucheDroite(String code){
        for (int i = 0; i<7; i++){
            leftDigits[i] = String.valueOf(code.charAt(i));
            if (i<6) {
                rightDigits[i] = String.valueOf(code.charAt(i + 7));
            }
        }
    }

    //Encode digit with specified parameters
    public String digitsEncoder(String lrDigit, String[] lrEncoder){
        String a = "error";
        switch (lrDigit){
            case "0":
                a = lrEncoder[0];
                break;
            case "1":
                a = lrEncoder[1];
                break;
            case "2":
                a = lrEncoder[2];
                break;
            case "3":
                a = lrEncoder[3];
                break;
            case "4":
                a = lrEncoder[4];
                break;
            case "5":
                a = lrEncoder[5];
                break;
            case "6":
                a = lrEncoder[6];
                break;
            case "7":
                a = lrEncoder[7];
                break;
            case "8":
                a = lrEncoder[8];
                break;
            case "9":
                a = lrEncoder[9];
                break;
        }
        return a;
    }

    //Set the encoding pattern
    public String[] pairImpair(String firstLeftDigits){
        String[] leftDigitEncoderPI;

        switch (firstLeftDigits){
            case "0":
                leftDigitEncoderPI = new String[]{"impair", "impair", "impair", "impair", "impair", "impair"};
                break;
            case "1":
                leftDigitEncoderPI = new String[]{"impair", "impair", "pair", "impair", "pair", "pair"};
                break;
            case "2":
                leftDigitEncoderPI = new String[]{"impair", "impair", "pair", "pair", "impair", "pair"};
                break;
            case "3":
                leftDigitEncoderPI = new String[]{"impair", "impair", "pair", "pair", "pair", "impair"};
                break;
            case "4":
                leftDigitEncoderPI = new String[]{"impair", "pair", "impair", "impair", "pair", "pair"};
                break;
            case "5":
                leftDigitEncoderPI = new String[]{"impair", "pair", "pair", "impair", "impair", "pair"};
                break;
            case "6":
                leftDigitEncoderPI = new String[]{"impair", "pair", "pair", "pair", "impair", "impair"};
                break;
            case "7":
                leftDigitEncoderPI = new String[]{"impair", "pair", "impair", "pair", "impair", "pair"};
                break;
            case "8":
                leftDigitEncoderPI = new String[]{"impair", "pair", "impair", "pair", "pair", "impair"};
                break;
            case "9":
                leftDigitEncoderPI = new String[]{"impair", "pair", "pair", "impair", "pair", "impair"};
                break;
            default:
                leftDigitEncoderPI = new String[]{"erreur", "erreur", "erreur", "erreur", "erreur", "erreur"};
        }
        return leftDigitEncoderPI;
    }

    //Encode left values BCP D'ERREUR
    public void leftDigitsEncoder(String[] leftDigitEncoderPI){
        for (int i = 0; i < leftDigitEncoderPI.length; i++) {
            if(leftDigitEncoderPI[i] == "impair"){
                leftDigitsEncoded[i] = digitsEncoder(leftDigits[i+1], leftDigitEncoderImpair);
            }
            else if(leftDigitEncoderPI[i] == "pair"){
                leftDigitsEncoded[i] = digitsEncoder(leftDigits[i+1], leftDigitEncoderPair);
            }
            else{
                leftDigitsEncoded[i] = "error2";
            }
        }
    }

    //Draw bars
    public void drawBars(String[] encodedDigits){
        try {
            FileWriter writer = new FileWriter("codeBar.html");
            BufferedWriter bwr = new BufferedWriter(writer);
            bwr.write("<svg xmlns=\"http://www.w3.org/2000/svg\" version=\"1.1\" width=\"1000\" height=\"300\">" + "\n");
            bwr.write("<line x1=\"0\" y1=\"0\" x2=\"0\" y2=\"200\" style=\"stroke:white;stroke-width:5\" shape-rendering=\"crispEdges\"/>" + "\n");
            bwr.write("<line x1=\"5\" y1=\"0\" x2=\"5\" y2=\"200\" style=\"stroke:white;stroke-width:5\" shape-rendering=\"crispEdges\"/>" + "\n");
            bwr.write("<line x1=\"10\" y1=\"0\" x2=\"10\" y2=\"200\" style=\"stroke:white;stroke-width:5\" shape-rendering=\"crispEdges\"/>" + "\n");

            // left guard bars
            bwr.write("<line x1=\"15\" y1=\"0\" x2=\"15\" y2=\"225\" style=\"stroke:black;stroke-width:5\" shape-rendering=\"crispEdges\"/>" + "\n");
            bwr.write("<line x1=\"20\" y1=\"0\" x2=\"20\" y2=\"225\" style=\"stroke:white;stroke-width:5\" shape-rendering=\"crispEdges\"/>" + "\n");
            bwr.write("<line x1=\"25\" y1=\"0\" x2=\"25\" y2=\"225\" style=\"stroke:black;stroke-width:5\" shape-rendering=\"crispEdges\"/>" + "\n");

            int spacingDigit = 30;
            for (int i = 0; i < 6; i++) {
                String bars = encodedDigits[i];
                for (int j = 0; j < bars.length(); j++) {
                    if(bars.charAt(j) == '1'){
                        bwr.write("<line x1=\"" + spacingDigit + "\" y1=\"0\" x2=\"" + spacingDigit + "\" y2=\"200\" style=\"stroke:black;stroke-width:5\" shape-rendering=\"crispEdges\"/>" + "\n");
                        spacingDigit = spacingDigit + 5;
                    }
                    else if(bars.charAt(j) == '0'){
                        bwr.write("<line x1=\"" + spacingDigit + "\" y1=\"0\" x2=\"" + spacingDigit + "\" y2=\"200\" style=\"stroke:white;stroke-width:5\" shape-rendering=\"crispEdges\"/>" + "\n");
                        spacingDigit = spacingDigit + 5;
                    }
                    else{
                        System.out.println("error while written to a file");
                        spacingDigit = spacingDigit + 5;
                    }
                }

            }

            //center guard bars
            bwr.write("<line x1=\"240\" y1=\"0\" x2=\"240\" y2=\"225\" style=\"stroke:white;stroke-width:5\" shape-rendering=\"crispEdges\"/>" + "\n");
            bwr.write("<line x1=\"245\" y1=\"0\" x2=\"245\" y2=\"225\" style=\"stroke:black;stroke-width:5\" shape-rendering=\"crispEdges\"/>" + "\n");
            bwr.write("<line x1=\"250\" y1=\"0\" x2=\"250\" y2=\"225\" style=\"stroke:white;stroke-width:5\" shape-rendering=\"crispEdges\"/>" + "\n");
            bwr.write("<line x1=\"255\" y1=\"0\" x2=\"255\" y2=\"225\" style=\"stroke:black;stroke-width:5\" shape-rendering=\"crispEdges\"/>" + "\n");
            bwr.write("<line x1=\"260\" y1=\"0\" x2=\"260\" y2=\"225\" style=\"stroke:white;stroke-width:5\" shape-rendering=\"crispEdges\"/>" + "\n");

            //right digits
            spacingDigit = 265;
            for (int i = 6; i < 12; i++) {
                String bars = encodedDigits[i];
                for (int j = 0; j < bars.length(); j++) {
                    if(bars.charAt(j) == '1'){
                        bwr.write("<line x1=\"" + spacingDigit + "\" y1=\"0\" x2=\"" + spacingDigit + "\" y2=\"200\" style=\"stroke:black;stroke-width:5\" shape-rendering=\"crispEdges\"/>" + "\n");
                        spacingDigit = spacingDigit + 5;
                    }
                    else if(bars.charAt(j) == '0'){
                        bwr.write("<line x1=\"" + spacingDigit + "\" y1=\"0\" x2=\"" + spacingDigit + "\" y2=\"200\" style=\"stroke:white;stroke-width:5\" shape-rendering=\"crispEdges\"/>" + "\n");
                        spacingDigit = spacingDigit + 5;
                    }
                    else{
                        System.out.println("error while written to a file");
                        spacingDigit = spacingDigit + 5;
                    }
                }
            }

            // right guard bars
            bwr.write("<line x1=\"475\" y1=\"0\" x2=\"475\" y2=\"225\" style=\"stroke:black;stroke-width:5\" shape-rendering=\"crispEdges\"/>" + "\n");
            bwr.write("<line x1=\"480\" y1=\"0\" x2=\"480\" y2=\"225\" style=\"stroke:white;stroke-width:5\" shape-rendering=\"crispEdges\"/>" + "\n");
            bwr.write("<line x1=\"485\" y1=\"0\" x2=\"485\" y2=\"225\" style=\"stroke:black;stroke-width:5\" shape-rendering=\"crispEdges\"/>" + "\n");

            bwr.write("</svg>");
            bwr.close();
            System.out.println("succesfully written to a file");

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}

























