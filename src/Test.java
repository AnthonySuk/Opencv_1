import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Size;
import org.opencv.highgui.HighGui;
import org.opencv.imgproc.Imgproc;
import org.opencv.video.BackgroundSubtractorKNN;
import org.opencv.video.BackgroundSubtractorMOG2;
import org.opencv.video.Video;
import org.opencv.videoio.VideoCapture;

public class Test
{
    public static void main(String[] args)
    {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        VideoCapture capture = new VideoCapture("E:\\yolo-project\\Labelme data\\Genshin\\project1_data\\video-chenxijiuzhuang.mp4");
        Mat video = new Mat();
        Mat fgmask = new Mat();
        Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(3, 3), new Point(-1, -1));
        BackgroundSubtractorMOG2 subtractor = Video.createBackgroundSubtractorMOG2();
        BackgroundSubtractorKNN subtractorKNN = Video.createBackgroundSubtractorKNN();
        while (capture.isOpened())
        {
            capture.read(video);
            Imgproc.GaussianBlur(video,video, new Size(7,7),3,0);
            //subtractor.apply(video,fgmask);
            subtractorKNN.apply(video,fgmask);
            Imgproc.morphologyEx(fgmask, fgmask, Imgproc.MORPH_OPEN, kernel, new Point(-1, -1));
            HighGui.imshow("video",video);
            HighGui.imshow("mask",fgmask);
            HighGui.waitKey(10);
        }
    }
}
