package com.screen.advert;
import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Binarizer;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.EncodeHintType;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;


public class QRCodeTest2 {

     final static String imagePath = System.getProperty("user.dir") + File.separator +"src" + File.separator + "code.png";
     final static String logoPath = System.getProperty("user.dir") + File.separator + "src" + File.separator + "1.jpg";
     final static String FORMAT_NAME = "png";
     final static String ENCODING = "UTF-8";
     private static final int QRCODE_SIZE = 300;
     private static final int WIDTH = 60;
     private static final int HEIGHT = 60;
    
    
     public static void simpleGeneralCode(String str) throws Exception{
          String content = new String(str.getBytes(ENCODING), "ISO-8859-1");
        File file = new File(imagePath);
          QRCodeWriter writer = new QRCodeWriter();
        BitMatrix matrix = writer.encode(content, BarcodeFormat.QR_CODE, QRCODE_SIZE, QRCODE_SIZE); 
        MatrixToImageWriter.writeToFile(matrix, FORMAT_NAME, file);
       
        QRCodeReader reader = new QRCodeReader(); 
        BufferedImage image = ImageIO.read(file); 
        LuminanceSource source = new BufferedImageLuminanceSource(image); 
        Binarizer binarizer = new HybridBinarizer(source); 
        BinaryBitmap imageBinaryBitmap = new BinaryBitmap(binarizer); 
        Result result = reader.decode(imageBinaryBitmap); 
        System.out.println("result = "+ result.toString()); 
        System.out.println("resultFormat = "+ result.getBarcodeFormat()); 
        System.out.println("resultText = "+ result.getText()); 
     }
    
     public static void main(String args[]) throws Exception{
          simpleGeneralCode("https://weibo.com/u/3798065592/home");
     }
}