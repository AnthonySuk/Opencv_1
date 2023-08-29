import org.opencv.core.*;
import org.opencv.core.Point;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

import java.awt.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class virtualPainter
{
    //Pink Hmax 190 Hmin 99 Smax 222 Smin 87 Vmax 255 Vmin 191
    //Yellow Hmax 40 Hmin 23 Smax 210 Smin 20 Vamx 255 Vmin 205
    static Vector<double[]> myColor = new Vector<>(2);
    static Vector<Scalar> standardValue = new Vector<>(2);
    static Mat camera;
    static class pointType{
        Point point;
        int color;
        pointType(Point p,int color){
            this.point=p;
            this.color=color;
        }
    }
    static ArrayList<pointType> pointList;

    public static void main(String[] args)
    {
        System.loadLibrary("opencv_java3416");

        VideoCapture capture = new VideoCapture(0);
        camera = new Mat();

        //From HSV max to HSV min
        double[] pink = {175,255,255,133,140,131};
        double[] yellow = {37,229,237,25,105,113};

        Scalar standard_Pink = new Scalar(203,192,255);
        Scalar standard_Yellow = new Scalar(0,255,255);

        myColor.add(pink);
        myColor.add(yellow);
        standardValue.add(standard_Pink);
        standardValue.add(standard_Yellow);

        while (true){
            capture.read(camera);
            findColor(camera);
            drawCircle();
            HighGui.imshow("Camera",camera);
            HighGui.waitKey(1);
            //camera.release();
        }
    }
    public static void findColor(Mat input){
        Mat image = new Mat();
        Imgproc.cvtColor(input,image,Imgproc.COLOR_BGR2HSV);
        pointList = new ArrayList<>();

        for(int i=0;i<myColor.size();i++){
            Scalar upper = new Scalar(myColor.elementAt(i)[0],myColor.elementAt(i)[1],myColor.elementAt(i)[2]);
            Scalar lower = new Scalar(myColor.elementAt(i)[3],myColor.elementAt(i)[4],myColor.elementAt(i)[5]);
            Mat mask = new Mat();
            Core.inRange(image,lower,upper,mask);
            Point myPoint = getContours(mask);
            if(myPoint.x!=0 && myPoint.y!=0){
                pointType pointType = new pointType(myPoint,i);
                pointList.add(pointType);
            }
            //HighGui.imshow(""+i,mask);
        }
    }

    static Point getContours(Mat image){
        Mat gray;
        Mat gaussBlur = new Mat();
        Mat cannyImg = new Mat();
        Mat kernel;
        Mat dilateImg = new Mat();

        gray = image;
        Imgproc.GaussianBlur(gray,gaussBlur, new Size(3,3),3,0);
        Imgproc.Canny(gaussBlur,cannyImg,25,75);
        kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT,new Size(6,6));
        Imgproc.dilate(cannyImg,dilateImg,kernel);

        final List<MatOfPoint> allCounters = new ArrayList<>();
        final List<MatOfPoint> approxList = new ArrayList<>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(dilateImg,allCounters,hierarchy,Imgproc.RETR_EXTERNAL,Imgproc.CHAIN_APPROX_SIMPLE);

        Point drawPoint = new Point(0,0);

        for(int i = 0;i<allCounters.size();i++){
            double area = Imgproc.contourArea(allCounters.get(i));
            if(area > 620)
            {
                double peri = Imgproc.arcLength(new MatOfPoint2f(allCounters.get(i).toArray()) ,true);
                MatOfPoint2f approx = new MatOfPoint2f();
                Imgproc.approxPolyDP(new MatOfPoint2f(allCounters.get(i).toArray()),approx,0.02*peri,true);
                Rect boundRect = Imgproc.boundingRect(new MatOfPoint(approx.toArray()));
                Imgproc.rectangle(camera,boundRect.tl(),boundRect.br(),new Scalar(0,0,0),2);
                drawPoint.x = boundRect.x + boundRect.width / 2;
                drawPoint.y = boundRect.y;
                approxList.add(new MatOfPoint(approx.toArray()));
            }
        }

        //Imgproc.drawContours(camera, approxList, -1, new Scalar(255, 0, 255), 2);

        //HighGui.imshow("gray",gray);
        //HighGui.imshow("gauss",gaussBlur);
        HighGui.imshow("canny",cannyImg);
        //HighGui.imshow("kernel",kernel);
        HighGui.imshow("dilate",dilateImg);

        return drawPoint;
    }

    static void drawCircle(){
        if(pointList.size()!=0){
            for(int i=0;i<pointList.size();i++){
                    Imgproc.circle(camera,pointList.get(i).point,10,standardValue.get(pointList.get(i).color),-1,Core.FILLED);
                }
            }
        }
    }

