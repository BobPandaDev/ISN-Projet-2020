package application;

import com.google.zxing.WriterException;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML Button btnMin;
    @FXML Button btnClose;
    @FXML Pane topPane;
    @FXML Label checkDigitLabel;
    @FXML Label codeSysteme;
    @FXML Label companyID;
    @FXML Label articleID;
    @FXML ToggleButton eanButton;
    @FXML ToggleButton qrcodeButton;
    @FXML TextField txtFieldTwelveDigit;
    @FXML Pane rightPane;
    @FXML Pane centerPane;
    @FXML Label infoLabel;
    @FXML WebView webSVG;
    public WebEngine engine;

    private double xOffset = 0;
    private double yOffset = 0;
    public String barcodeType = "";
    public String filePath = System.getProperty("user.dir") +"\\qrCode.png";
    public String filePath2 = System.getProperty("user.dir") + "\\ean13.svg";
    public String filePath3 = System.getProperty("user.dir") + "\\info.html";




    @FXML
    protected void eanSelected(ActionEvent event){
        //texte en noir
        txtFieldTwelveDigit.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(final ObservableValue<? extends String> observable, final String oldValue, final String newValue) {
                txtFieldTwelveDigit.setStyle("-fx-text-fill: black;");
            }
        });
        eanButton.setSelected(true);
        qrcodeButton.setSelected(false);
        barcodeType = "ean";
        txtFieldTwelveDigit.clear();
        rightPane.setPrefWidth(217);
        webSVG.setLayoutX(0);
        engine.load("file://" + filePath2);
    }
    @FXML
    protected void qrcodeSelected(ActionEvent event){
        //texte en noir
        txtFieldTwelveDigit.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(final ObservableValue<? extends String> observable, final String oldValue, final String newValue) {
                txtFieldTwelveDigit.setStyle("-fx-text-fill: black;");
            }
        });
        eanButton.setSelected(false);
        qrcodeButton.setSelected(true);
        barcodeType = "qrcode";
        txtFieldTwelveDigit.clear();
        rightPane.setPrefWidth(0);
        webSVG.setLayoutX(90);
        engine.load("file://" + filePath);
    }

    @FXML
    protected void infoClick(MouseEvent event){
        eanButton.setSelected(false);
        qrcodeButton.setSelected(false);
        barcodeType = "";
        txtFieldTwelveDigit.clear();
        rightPane.setPrefWidth(0);
        webSVG.setLayoutX(90);
        engine.load("file://" + filePath3);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        engine = webSVG.getEngine();
        webSVG.setZoom(1.4);

        eanButton.setSelected(false);
        qrcodeButton.setSelected(false);
        barcodeType = "";
        txtFieldTwelveDigit.clear();
        rightPane.setPrefWidth(0);
        webSVG.setLayoutX(90);
        engine.load("file://" + filePath3);
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
    public void generateClick(MouseEvent event) throws IOException, WriterException {
        if (barcodeType == "ean"){
            EAN13.barCode(txtFieldTwelveDigit, filePath2, engine, checkDigitLabel, codeSysteme, companyID, articleID);
        }
        else if (barcodeType == "qrcode"){
            QRcode.qrCode(txtFieldTwelveDigit, filePath, engine);
        }
        else{
            txtFieldTwelveDigit.setText("Sélectionnez EAN-13 ou QR-Code.");
            txtFieldTwelveDigit.setStyle("-fx-text-fill: red;");
        }
    }
}
