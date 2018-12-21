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


public class QRCodeTest {

     final static String imagePath = System.getProperty("user.dir") + File.separator +"src" + File.separator + "code.png";
     //婵炲瓨绮岄惉鐓幥庨锟介幆宥嗘媴閸涘﹦顩梺缁橆殔濞测晠鎯岄幆褜鍤楅柨鐕傛嫹
     final static String logoPath = System.getProperty("user.dir") + File.separator + "src" + File.separator + "1.jpg";
     //闂佹悶鍎辨晶鑺ユ櫠閺嶎厼鍐�闁绘挸娴风涵锟�
     final static String FORMAT_NAME = "png";
     //缂傚倸鍊归悧婊堟偉濠婂牆妫橀柣鐔稿绾拷
     final static String ENCODING = "UTF-8";
     // 婵炲瓨绮岄惉鐓幥庨锟介幆宥嗘媴閸涘﹥顫呴柣搴ｎ暜閹凤拷
     private static final int QRCODE_SIZE = 300;
     // LOGO闁诲海顢婂Λ鍕拷鐧告嫹
     private static final int WIDTH = 60;
     // LOGO婵°倕鍊归敋閻庣櫢鎷�
     private static final int HEIGHT = 60;
    
    
     public static void simpleGeneralCode(String str) throws Exception{
          String content = new String(str.getBytes(), ENCODING);
        File file = new File(imagePath);
          QRCodeWriter writer = new QRCodeWriter();
          //闂佹眹鍨婚崰鎰板垂濮橆厾顩茬�癸拷閿熻棄菐椤曪拷閹秹鏁撻敓锟� 
        BitMatrix matrix = writer.encode(content, BarcodeFormat.QR_CODE, QRCODE_SIZE, QRCODE_SIZE); 
        MatrixToImageWriter.writeToFile(matrix, FORMAT_NAME, file);
       
        //闁荤姴娲╅褑銇愰崶銊ь洸鐎癸拷閿熻棄菐椤曪拷閹秹鏁撻敓锟� 
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
    
     public static void generalPicCode(String str) throws Exception{
          String content = new String(str.getBytes(), ENCODING);
          MultiFormatWriter mutiWriter = new MultiFormatWriter();
         
          Hashtable<EncodeHintType, Object> hint = new Hashtable<EncodeHintType, Object>();
          hint.put(EncodeHintType.CHARACTER_SET, ENCODING);
          hint.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
          BitMatrix bMatrix = mutiWriter.encode(content, BarcodeFormat.QR_CODE, QRCODE_SIZE, QRCODE_SIZE, hint);
         
          int width = bMatrix.getWidth(); 
        int height = bMatrix.getHeight(); 
        BufferedImage image = new BufferedImage(width, height,BufferedImage.TYPE_INT_RGB); 
        for (int x = 0; x < width; x++) { 
            for (int y = 0; y < height; y++) { 
                image.setRGB(x, y, bMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);  //0xFFFFFFFF闂佽皫鍐挎嫹閻斿妫�, 0xFF000000婵帗绋掗崹鍏肩珶閿燂拷
            } 
        }
       
          //闂佹悶鍎辨晶鑺ユ櫠閿燂拷
        insertImage(image, logoPath, true);
       
        BufferedImage image2 = new BufferedImage(400, 400,BufferedImage.TYPE_INT_RGB);
        Image src = rescale(image, true, 400, 400);//婵炴垶鎼╅崢鑲╃紦妤ｅ啫绀冮柛娑卞弾閸熷檻cale
        Graphics2D graph = image2.createGraphics();
        graph.drawImage(ImageIO.read(new File(logoPath)), 0, 0, 400, 400, null);
          graph.drawImage(src, 50, 50, 300, 300, null);
          Shape shape = new RoundRectangle2D.Float(50, 50, 300, 300, 6, 6);//闂侀潻绠戝Λ婊堬綖閿燂拷
          Shape shape2 = new RoundRectangle2D.Float(0, 0, 400, 400, 6, 6);//闂侀潻绠戝Λ婊堬綖閿燂拷
          graph.setStroke(new BasicStroke(3f));
          graph.draw(shape);
          graph.draw(shape2);
          graph.dispose();
         
          //婵烇絽娲︾换鍌炴偤閿燂拷
        ImageIO.write(image2, FORMAT_NAME, new File(imagePath));
       
     }
    
     //闁荤喐鐟辩徊楣冩倵娴犲瀚夊璺猴攻缁傚牓鏌ｅΔ锟藉ú銊モ枔閹寸偟顩茬�癸拷閿熻棄菐椤曪拷閹秹鏁撻敓锟�
     public static void decodePicCode(String imagePath) throws Exception{
          BufferedImage image = ImageIO.read(new File(imagePath));
          LuminanceSource source = new BufferedImageLuminanceSource(image);
          BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
          Hashtable<DecodeHintType, Object> hints = new Hashtable<DecodeHintType, Object>();
          hints.put(DecodeHintType.CHARACTER_SET, ENCODING);
          Result result = new MultiFormatReader().decode(bitmap, hints);
          System.out.println("result = "+ result.toString()); 
        System.out.println("resultFormat = "+ result.getBarcodeFormat()); 
          System.out.println("resultText = "+ result.getText());
     }
    
     /**
     * 闂佸湱绮敮鎺楀矗閸濆儗GO
     *
     * @param source
     *            婵炲瓨绮岄惉鐓幥庨锟介幆宥嗘媴閸涘﹦顩梺缁橆殣閹凤拷
     * @param imgPath
     *            LOGO闂佹悶鍎辨晶鑺ユ櫠閺嶎厼鎹堕柡澶嬪缁诧拷
     * @param needCompress
     *            闂佸搫瀚烽崹浼村箚娓氾拷瀹曘垻锟斤綆鍓涚槐锟�
     * @return
     * @throws Exception
     */
     private static void insertImage(BufferedImage source, String imgPath, boolean needCompress) throws Exception {
          File file = new File(imgPath);
          if (!file.exists()) {
               System.err.println(""+imgPath+"   闁荤姴娲㈤崕鏌ュ几閸愨晝顩烽柡鍫㈡暩閻熸繈鎮楀☉娅亜锕㈤鍫熸櫖闁跨噦鎷�");
               return;
          }
         
          //rescale
          Image img = ImageIO.read(new File(imgPath));
          Image src = rescale(img, needCompress, WIDTH, HEIGHT);
         
          // 闂佸湱绮敮鎺楀矗閸濆儗GO
          int width = src.getWidth(null);
          int height = src.getHeight(null);
          if (needCompress) { // 闂佸憡锚椤戝洨绱撴繅濠矴O
               if (width > WIDTH) {
                    width = WIDTH;
               }
               if (height > HEIGHT) {
                    height = HEIGHT;
               }
          }
          Graphics2D graph = source.createGraphics();
          int x = (QRCODE_SIZE - width) / 2;
          int y = (QRCODE_SIZE - height) / 2;
          graph.drawImage(src, x, y, width, height, null);
          Shape shape = new RoundRectangle2D.Float(x, y, width, height, 6, 6);//闂侀潻绠戝Λ婊堬綖閿燂拷
          graph.setStroke(new BasicStroke(3f));
          graph.draw(shape);
          graph.dispose();
     }
    
     public static Image rescale(Image  src, boolean needCompress, int w, int h) throws IOException{
          int width = src.getWidth(null);
          int height = src.getHeight(null);
          if (needCompress) { // 闂佸憡锚椤戝洨绱撴繅濠矴O
               if (width > w) {
                    width = w;
               }
               if (height > h) {
                    height = h;
               }
               src = src.getScaledInstance(width, height,Image.SCALE_SMOOTH);
               BufferedImage tag = new BufferedImage(width, height,BufferedImage.TYPE_INT_RGB);
               Graphics g = tag.getGraphics();
               g.drawImage(src, 0, 0, null); // 缂傚倷鐒﹂敋闁糕晜顨堢槐鎾诲煛娴ｇ儤鐦撻梺鍛婅壘濞村嘲鈻撻幋锕�鐐婇柨鐕傛嫹
               g.dispose();
          }
          return src;
     }
    
     public static void main(String args[]) throws Exception{
          simpleGeneralCode("ღ爱你 家敏ღ");
//          generalPicCode("婵炴垶鎼╅崢鐓幟归敓锟�");
//          decodePicCode(imagePath);
     }
}