import org.opencv.core.*;
import org.opencv.core.Point;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.core.Mat;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class practice {

    final static Point start = new Point(600,500);
    final static Point end = new Point(2000,1200);

    public static void main(String[] args){
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        Mat img = Imgcodecs.imread("E://yolo-project//Labelme data//Genshin//project1_data//test.PNG");
        Rect cutRect = new Rect(start,end);

        Mat analyzeZone = cutImg(img,cutRect);
        Mat noWater;
        Mat Gray =new Mat();

        Imgproc.GaussianBlur(analyzeZone,analyzeZone, new Size(7,7),1);
        Imgproc.medianBlur(analyzeZone,analyzeZone,9);

        noWater = removeWater(analyzeZone);

        Imgproc.cvtColor(noWater, Gray, Imgproc.COLOR_BGR2GRAY,0);
        Imgproc.threshold(Gray,Gray,86,0,Imgproc.THRESH_TOZERO);

        Imgproc.Canny(Gray,Gray,200,400);

        //膨胀
        Imgproc.dilate(Gray,Gray,new Mat(),new Point(-1,-1),1);

        Mat blackWhite =new Mat();
        Imgproc.threshold(Gray,blackWhite,100,150,Imgproc.THRESH_BINARY);
        Imgproc.threshold(Gray,blackWhite,116,255,Imgproc.THRESH_BINARY);

        final List<MatOfPoint> allCounters = new ArrayList<>();
        Imgproc.findContours(
                Gray,
                allCounters,
                new Mat(Gray.size(), Gray.type()),
                Imgproc.RETR_EXTERNAL,
                Imgproc.CHAIN_APPROX_NONE);

        final List<MatOfPoint> filterCounters = allCounters.stream().filter(contour ->{
                    final double value = Imgproc.contourArea(contour);
                    final Rect rect = Imgproc.boundingRect(contour);

                    final boolean isNotNoise = value>100;
                    if(isNotNoise){
                        Imgproc.putText(
                                analyzeZone,
                                "Fish",
                                new Point(rect.x + rect.width, rect.y+rect.height),
                                2,
                                0.5,
                                new Scalar(124,252,0),
                                1
                        );
                    }
                    return isNotNoise;
                }).collect(Collectors.toList());
        Imgproc.drawContours(
                analyzeZone,
                filterCounters,
                -1,
                new Scalar(124,252,0),
                1
        );

        //Imgcodecs.imwrite("E://yolo-project//Labelme data//Genshin//project1_data//test_blackWhite.PNG", blackWhite);
        HighGui.imshow("Original",analyzeZone);
        HighGui.imshow("noWater",noWater);
        HighGui.imshow("Result",Gray);
        HighGui.waitKey(0);
        System.out.print("ok");
    }
    public static Mat cutImg(Mat img,Rect rect){
        return new Mat(img,rect);
    }

    public static Mat markOutFish(final Mat processImg, final Mat originalImg){
        final List<MatOfPoint> allCounters = new ArrayList<>();
        Imgproc.findContours(
                processImg,
                allCounters,
                new Mat(processImg.size(), processImg.type()),
                Imgproc.RETR_EXTERNAL,
                Imgproc.CHAIN_APPROX_NONE);

        final List<MatOfPoint> filterCounters = allCounters.stream()
                .filter(contour ->{
                    final double value = Imgproc.contourArea(contour);
                    final Rect rect = Imgproc.boundingRect(contour);

                    final boolean isNotNoise = value>800;
                    if(isNotNoise){
                        Imgproc.putText(
                                originalImg,
                                "Fish",
                                new Point(rect.x + rect.width, rect.y+rect.height),
                                2,
                                0.5,
                                new Scalar(124,252,0),
                                1
                        );
                    }
                    return isNotNoise;
                }).collect(Collectors.toList());
        Imgproc.drawContours(
                originalImg,
                filterCounters,
                -1,
                new Scalar(124,252,0),
                1
        );
        return originalImg;
    }

    static double blueMax = 143;
    static double blueMin = 116;
    static double greenMax = 119;
    static double greenMin = 90;
    static double redMax = 51;
    static double redMin = 18;

    public static Mat removeWater(Mat img){
        Mat temp = img.clone();
        double[] pixel = new double[3];
        double[] black = {0,0,0};

        for(int i=0;i <img.rows();i++){
            for(int j=0;j<img.cols();j++){
                pixel = img.get(i,j).clone();
                // Blue-0 Green-1 Red-2
                boolean b = pixel[0]>=blueMin && pixel[0]<=blueMax;
                boolean g = pixel[1]>=greenMin && pixel[1]<=greenMax;
                boolean r = pixel[2]>=redMin && pixel[2]<=redMax;
                if(b && g && r){
                    temp.put(i,j,black);
                }
            }
        }
        return temp;
    }

}
