import org.opencv.core.*;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.video.BackgroundSubtractorMOG2;
import org.opencv.videoio.VideoCapture;

import java.util.ArrayList;
import java.util.List;

public class GenshinFish
{
    //Water Hmax:105 Hmin:84 Smax:238 Smin:0 Vmax:171 Vmin:126

    static double up_X_ratio, up_Y_ratio, down_Y_ratio, x, y;
    static Scalar lower,upper;

    static Scalar lower_cloud,upper_cloud;

    public static void main(String[] args)
    {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        VideoCapture capture = new VideoCapture("E:\\yolo-project\\Labelme data\\Genshin\\project1_data\\video-chenxijiuzhuang.mp4");
        Mat firstFrame = new Mat();
        capture.read(firstFrame);

        lower = new Scalar(84,0,126);
        upper = new Scalar(105,238,171);
        lower_cloud = new Scalar(77,108,162);
        upper_cloud = new Scalar(255,255,255);

        up_X_ratio = 460.0/2560;
        up_Y_ratio = 320.0/1600;
        down_Y_ratio = 1260.0/2560;
        x = firstFrame.width();
        y = firstFrame.height();

        while (capture.isOpened()){
            Mat input = new Mat();
            capture.read(input);
            detect(input);
            HighGui.imshow("result",input);
            HighGui.waitKey(10);

        }
        capture.release();
    }

    static void detect(Mat input){
        Mat image = input;
        Mat imageWithoutCloud = new Mat();
        Mat gray = new Mat();
        Mat gaussBlur = new Mat();
        Mat canny = new Mat();
        Mat kernel_dialate;
        Mat kernel_erosion;
        Mat dilate = new Mat();
        Mat dilate1 = new Mat();
        Mat erosion = new Mat();


        Imgproc.cvtColor(image,image,Imgproc.COLOR_BGR2HSV);
        Core.inRange(image,lower_cloud,upper_cloud,imageWithoutCloud);
        Core.bitwise_not(imageWithoutCloud,imageWithoutCloud);
        Core.inRange(image,lower,upper,gray);
        //Core.add(gray,imageWithoutCloud,gray);
        Core.bitwise_not(gray,gray);
        kernel_erosion = Imgproc.getStructuringElement(Imgproc.MORPH_RECT,new Size(9,9));
        Imgproc.erode(gray,erosion,kernel_erosion);
        kernel_dialate = Imgproc.getStructuringElement(Imgproc.MORPH_RECT,new Size(6,6));
        Imgproc.dilate(erosion,dilate1,kernel_dialate);
        Imgproc.GaussianBlur(dilate1,gaussBlur, new Size(5,5),3,3);


        Imgproc.Canny(gaussBlur,canny,125,175);
        kernel_dialate = Imgproc.getStructuringElement(Imgproc.MORPH_RECT,new Size(6,6));
        Imgproc.dilate(canny,dilate,kernel_dialate);

        final List<MatOfPoint> allCounters = new ArrayList<>();
        final List<MatOfPoint> approxList = new ArrayList<>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(dilate,allCounters,hierarchy,Imgproc.RETR_EXTERNAL,Imgproc.CHAIN_APPROX_SIMPLE);

        for(int i = 0;i<allCounters.size();i++){
            double area = Imgproc.contourArea(allCounters.get(i));
            if(area > 1000 && area < 15000)
            {
                double peri = Imgproc.arcLength(new MatOfPoint2f(allCounters.get(i).toArray()) ,true);
                MatOfPoint2f approx = new MatOfPoint2f();
                Imgproc.approxPolyDP(new MatOfPoint2f(allCounters.get(i).toArray()),approx,0.02*peri,true);
                Rect boundRect = Imgproc.boundingRect(new MatOfPoint(approx.toArray()));
                if(boundRect.x > x * up_X_ratio && (boundRect.y > y * up_Y_ratio && boundRect.y < y * down_Y_ratio))
                {
                    Imgproc.rectangle(input,boundRect.tl(),boundRect.br(),new Scalar(0,0,0),2);
                    approxList.add(new MatOfPoint(approx.toArray()));
                }
            }
        }

        //HighGui.imshow("input",input);
        HighGui.imshow("gray",gray);
        //HighGui.imshow("withoutcloud",imageWithoutCloud);
        HighGui.imshow("erosion",erosion);
        HighGui.imshow("dilate1",dilate1);
//        HighGui.imshow("gauss",gaussBlur);
        HighGui.imshow("canny",canny);
       HighGui.imshow("dilate",dilate);
        //HighGui.waitKey(1);
    }
}
