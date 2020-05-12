package application;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.web.WebEngine;

public class EAN13 {
	public static String[] leftDigits = new String[7];
    public static String[] rightDigits = new String[6];
    public static String[] leftDigitsEncoded = new String[7];
    public static String[] rightDigitsEncoded = new String[6];
    public static String[] rightDigitEncoder = {"1110010","1100110", "1101100", "1000010", "1011100", "1001110", "1010000", "1000100", "1001000", "1110100"};
    public static String[] leftDigitEncoderImpair = {"0001101","0011001", "0010011", "0111101", "0100011", "0110001", "0101111", "0111011", "0110111", "0001011"};
    public static String[] leftDigitEncoderPair = {"0100111","0110011", "0011011", "0100001", "0011101", "0111001", "0000101", "0010001", "0001001", "0010111"};
	
	public static void barCode(TextField txtFieldTwelveDigit, String filePath2, WebEngine engine, Label checkDigitLabel, Label codeSysteme, Label companyID, Label articleID) {
		String code = txtFieldTwelveDigit.getText();

        if(code.length() != 12){
            txtFieldTwelveDigit.setText("12 numéros sont requis");
            txtFieldTwelveDigit.setStyle("-fx-text-fill: red;");
        }
        else{
            txtFieldTwelveDigit.setStyle("-fx-text-fill: black;");

            String checkDigit = checkDigit(code);
            code = code + checkDigit;

            checkDigitLabel.setText(checkDigit);
            codeSysteme.setText(String.valueOf(code.charAt(0))+ String.valueOf(code.charAt(1)));
            companyID.setText(String.valueOf(code.charAt(2))+ String.valueOf(code.charAt(3))+ String.valueOf(code.charAt(4))+ String.valueOf(code.charAt(5))+ String.valueOf(code.charAt(6)));
            articleID.setText(String.valueOf(code.charAt(7))+ String.valueOf(code.charAt(8))+ String.valueOf(code.charAt(9))+ String.valueOf(code.charAt(10))+ String.valueOf(code.charAt(11)));


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
            drawBars(encodedDigits, code, filePath2);

            //chargement de l'image
            engine.load("file://" + filePath2);
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
    public static String checkDigit(String digits){
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
    public static void gaucheDroite(String code){
        for (int i = 0; i<7; i++){
            leftDigits[i] = String.valueOf(code.charAt(i));
            if (i<6) {
                rightDigits[i] = String.valueOf(code.charAt(i + 7));
            }
        }
    }

    //Encode digit with specified parameters
    public static String digitsEncoder(String lrDigit, String[] lrEncoder){
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
    public static String[] pairImpair(String firstLeftDigits){
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
    public static void leftDigitsEncoder(String[] leftDigitEncoderPI){
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
    public static void drawBars(String[] encodedDigits, String code, String filePath2){
        try {
            FileWriter writer = new FileWriter(filePath2);
            BufferedWriter bwr = new BufferedWriter(writer);
            bwr.write("<svg xmlns=\"http://www.w3.org/2000/svg\" version=\"1.1\" width=\"500\" height=\"250\">" + "\n");
            bwr.write("<line x1=\"0\" y1=\"0\" x2=\"0\" y2=\"200\" style=\"stroke:white;stroke-width:5\" shape-rendering=\"crispEdges\"/>" + "\n");
            bwr.write("<line x1=\"5\" y1=\"0\" x2=\"5\" y2=\"200\" style=\"stroke:white;stroke-width:5\" shape-rendering=\"crispEdges\"/>" + "\n");
            bwr.write("<line x1=\"10\" y1=\"0\" x2=\"10\" y2=\"200\" style=\"stroke:white;stroke-width:5\" shape-rendering=\"crispEdges\"/>" + "\n");
            bwr.write("<line x1=\"15\" y1=\"0\" x2=\"15\" y2=\"200\" style=\"stroke:white;stroke-width:5\" shape-rendering=\"crispEdges\"/>" + "\n");
            bwr.write("<line x1=\"20\" y1=\"0\" x2=\"20\" y2=\"200\" style=\"stroke:white;stroke-width:5\" shape-rendering=\"crispEdges\"/>" + "\n");
            bwr.write("<line x1=\"25\" y1=\"0\" x2=\"25\" y2=\"200\" style=\"stroke:white;stroke-width:5\" shape-rendering=\"crispEdges\"/>" + "\n");
            bwr.write("<line x1=\"30\" y1=\"0\" x2=\"30\" y2=\"200\" style=\"stroke:white;stroke-width:5\" shape-rendering=\"crispEdges\"/>" + "\n");

            // left guard bars
            bwr.write("<line x1=\"35\" y1=\"0\" x2=\"35\" y2=\"225\" style=\"stroke:black;stroke-width:5\" shape-rendering=\"crispEdges\"/>" + "\n");
            bwr.write("<line x1=\"40\" y1=\"0\" x2=\"40\" y2=\"225\" style=\"stroke:white;stroke-width:5\" shape-rendering=\"crispEdges\"/>" + "\n");
            bwr.write("<line x1=\"45\" y1=\"0\" x2=\"45\" y2=\"225\" style=\"stroke:black;stroke-width:5\" shape-rendering=\"crispEdges\"/>" + "\n");

            int spacingDigit = 50;
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
            bwr.write("<line x1=\"260\" y1=\"0\" x2=\"260\" y2=\"225\" style=\"stroke:white;stroke-width:5\" shape-rendering=\"crispEdges\"/>" + "\n");
            bwr.write("<line x1=\"265\" y1=\"0\" x2=\"265\" y2=\"225\" style=\"stroke:black;stroke-width:5\" shape-rendering=\"crispEdges\"/>" + "\n");
            bwr.write("<line x1=\"270\" y1=\"0\" x2=\"270\" y2=\"225\" style=\"stroke:white;stroke-width:5\" shape-rendering=\"crispEdges\"/>" + "\n");
            bwr.write("<line x1=\"275\" y1=\"0\" x2=\"275\" y2=\"225\" style=\"stroke:black;stroke-width:5\" shape-rendering=\"crispEdges\"/>" + "\n");
            bwr.write("<line x1=\"280\" y1=\"0\" x2=\"280\" y2=\"225\" style=\"stroke:white;stroke-width:5\" shape-rendering=\"crispEdges\"/>" + "\n");

            //right digits
            spacingDigit = 285;
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
            bwr.write("<line x1=\"495\" y1=\"0\" x2=\"495\" y2=\"225\" style=\"stroke:black;stroke-width:5\" shape-rendering=\"crispEdges\"/>" + "\n");
            bwr.write("<line x1=\"500\" y1=\"0\" x2=\"500\" y2=\"225\" style=\"stroke:white;stroke-width:5\" shape-rendering=\"crispEdges\"/>" + "\n");
            bwr.write("<line x1=\"505\" y1=\"0\" x2=\"505\" y2=\"225\" style=\"stroke:black;stroke-width:5\" shape-rendering=\"crispEdges\"/>" + "\n");

            bwr.write("<text x=\"0\" y=\"245\" font-size=\"45\" fill=\"black\">" + code.charAt(0) + "</text>" + "\n");
            bwr.write("<text x=\"70\" y=\"245\" font-size=\"45\" fill=\"black\">" + code.charAt(1) + "</text>" + "\n");
            bwr.write("<text x=\"100\" y=\"245\" font-size=\"45\" fill=\"black\">" + code.charAt(2) + "</text>" + "\n");
            bwr.write("<text x=\"130\" y=\"245\" font-size=\"45\" fill=\"black\">" + code.charAt(3) + "</text>" + "\n");
            bwr.write("<text x=\"160\" y=\"245\" font-size=\"45\" fill=\"black\">" + code.charAt(4) + "</text>" + "\n");
            bwr.write("<text x=\"190\" y=\"245\" font-size=\"45\" fill=\"black\">" + code.charAt(5) + "</text>" + "\n");
            bwr.write("<text x=\"220\" y=\"245\" font-size=\"45\" fill=\"black\">" + code.charAt(6) + "</text>" + "\n");
            bwr.write("<text x=\"300\" y=\"245\" font-size=\"45\" fill=\"black\">" + code.charAt(7) + "</text>" + "\n");
            bwr.write("<text x=\"330\" y=\"245\" font-size=\"45\" fill=\"black\">" + code.charAt(8) + "</text>" + "\n");
            bwr.write("<text x=\"360\" y=\"245\" font-size=\"45\" fill=\"black\">" + code.charAt(9) + "</text>" + "\n");
            bwr.write("<text x=\"390\" y=\"245\" font-size=\"45\" fill=\"black\">" + code.charAt(10) + "</text>" + "\n");
            bwr.write("<text x=\"420\" y=\"245\" font-size=\"45\" fill=\"black\">" + code.charAt(11) + "</text>" + "\n");
            bwr.write("<text x=\"450\" y=\"245\" font-size=\"45\" fill=\"black\">" + code.charAt(12) + "</text>" + "\n");

            bwr.write("</svg>");
            bwr.close();
            System.out.println("succesfully written to a file");

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
