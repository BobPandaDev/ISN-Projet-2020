package application;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import javafx.scene.control.TextField;
import javafx.scene.web.WebEngine;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;

//Class pernettant la creation d'un QRcode a l'aide de la librairy zxing de google
public class QRcode {
	
	public static void qrCode(TextField txtFieldTwelveDigit, String filePath, WebEngine engine) throws IOException, WriterException {
        String code = txtFieldTwelveDigit.getText(); //obtient les données fournit par l'utilisateur
        
        int size = 270; //definit la taille de l'image
        String fileType = "png"; //definit le type de l'image
        File qrFile = new File(filePath); //creer le fichier
        
        createQRImage(qrFile, code, size, fileType); //creer le QRcode
        engine.load("file://" + filePath); //charge le QRcode dans le WebView
    }
	
	// Methode fournit par zxing
    public static void createQRImage(File qrFile, String qrCodeText, int size, String fileType) throws WriterException, IOException {
        
    	// Creer le ByteMatrix
    	Hashtable<EncodeHintType, ErrorCorrectionLevel> hintMap = new Hashtable<>(); 
        hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix byteMatrix = qrCodeWriter.encode(qrCodeText, BarcodeFormat.QR_CODE, size, size, hintMap);
        
        // Realise l'image tampon qui doit contenir le QRcode
        int matrixWidth = byteMatrix.getWidth(); 
        BufferedImage image = new BufferedImage(matrixWidth, matrixWidth, BufferedImage.TYPE_INT_RGB);
        image.createGraphics();

        Graphics2D graphics = (Graphics2D) image.getGraphics();
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, matrixWidth, matrixWidth);
        
        // Definit la couleur et sauvegarde l'image en utilisant le ByteMatrix
        graphics.setColor(Color.BLACK);
        for (int i = 0; i < matrixWidth; i++) {
            for (int j = 0; j < matrixWidth; j++) {
                if (byteMatrix.get(i, j)) {
                    graphics.fillRect(i, j, 1, 1);
                }
            }
        }
        ImageIO.write(image, fileType, qrFile);
    }
}
