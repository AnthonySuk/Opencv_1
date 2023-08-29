import org.opencv.core.*;
import org.opencv.core.Point;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.core.Mat;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.PixelGrabber;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Tools  {

    static Mat input,HSV_input;
    static Point start,end;

    public static void main(String[] args){
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        input = new Mat();
        HSV_input = new Mat();
        input = Imgcodecs.imread("E://yolo-project//Labelme data//Genshin//project1_data//11.PNG");
        //裁切画面的坐标
        start = new Point(450,450);
        end = new Point(2180,1290);
        Rect cutRect = new Rect(start,end);

        input = new Mat(input,cutRect);
        Imgproc.cvtColor(input,HSV_input,Imgproc.COLOR_BGR2HSV);

        Image image_Picture = mat2Image(input);
        Dimension size = new Dimension(input.width(),input.height());

        new pictureFrame(image_Picture,size);
}
    private static Image mat2Image(Mat input){
        int type = BufferedImage.TYPE_3BYTE_BGR;
        int imageSize = input.channels() * input.cols() * input.rows();
        byte[] inputPixels = new byte[imageSize];
        //获取input的byte[]
        input.get(0,0,inputPixels);
        BufferedImage image = new BufferedImage(input.cols(),input.rows(),type);
        //获取image的byte[]
        final byte[] imagePixels = ((DataBufferByte)image.getRaster().getDataBuffer()).getData();
        System.arraycopy(inputPixels,0,imagePixels,0,inputPixels.length);
        return image;
    }

    public static void returnHSV(int x,int y){
        double[] pixel = HSV_input.get(x,y);
        if(pixel == null){
            System.out.println("Error: empty pixel\n");
        }
        else {
            double H = pixel[0];
            double S = pixel[1];
            double V = pixel[2];
            System.out.println("H = " + H + " S = " + S + " V = " + V+"\n");
        }
    }

}


