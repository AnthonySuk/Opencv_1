import com.sun.source.tree.AssertTree;
import org.opencv.core.Mat;
import org.opencv.core.Core;
import org.opencv.core.Scalar;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

import org.junit.Assert;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;


public class colorFinder
{
    static int Hmin,Hmax,Smin,Smax,Vmin,Vmax;
    static JLabel l_Hmin,l_Hmax,l_Smin,l_Smax,l_Vmin,l_Vmax;
    static Scalar lower,upper;
    static HSV_slider H_min,H_max,S_min,S_max,V_min,V_max;
    private static void addComponentToPanel(Container container){
        Assert.assertTrue(container.getLayout() instanceof BorderLayout);

        //滑块看板
        JPanel slidePanel = new JPanel();
        slidePanel.setLayout(new BoxLayout(slidePanel,BoxLayout.PAGE_AXIS));

        H_min = new HSV_slider(0,255);
        H_max = new HSV_slider(0,255);
        S_min = new HSV_slider(0,255);
        S_max = new HSV_slider(0,255);
        V_min = new HSV_slider(0,255);
        V_max = new HSV_slider(0,255);

        H_min.addChangeListener(new ChangeListener()
        {
            @Override
            public void stateChanged(ChangeEvent e)
            {
                JSlider source = (JSlider) e.getSource();
                Hmin = source.getValue();
            }
        });

        H_max.addChangeListener(new ChangeListener()
        {
            @Override
            public void stateChanged(ChangeEvent e)
            {
                JSlider source = (JSlider) e.getSource();
                Hmax = source.getValue();
            }
        });

        S_max.addChangeListener(new ChangeListener()
        {
            @Override
            public void stateChanged(ChangeEvent e)
            {
                JSlider source = (JSlider) e.getSource();
                Smax = source.getValue();
            }
        });

        S_min.addChangeListener(new ChangeListener()
        {
            @Override
            public void stateChanged(ChangeEvent e)
            {
                JSlider source = (JSlider) e.getSource();
                Smin = source.getValue();
            }
        });

        V_max.addChangeListener(new ChangeListener()
        {
            @Override
            public void stateChanged(ChangeEvent e)
            {
                JSlider source = (JSlider) e.getSource();
                Vmax = source.getValue();
            }
        });

        V_min.addChangeListener(new ChangeListener()
        {
            @Override
            public void stateChanged(ChangeEvent e)
            {
                JSlider source = (JSlider) e.getSource();
                Vmin = source.getValue();
            }
        });

        l_Hmin = new JLabel(String.format("Hmin %d",0));
        l_Hmax = new JLabel(String.format("Hmax %d",0));
        l_Smin = new JLabel(String.format("Smin %d",0));
        l_Smax = new JLabel(String.format("Smax %d",0));
        l_Vmin = new JLabel(String.format("Vmin %d",0));
        l_Vmax = new JLabel(String.format("Vmax %d",0));

        slidePanel.add(l_Hmax);
        slidePanel.add(H_max);
        slidePanel.add(l_Hmin);
        slidePanel.add(H_min);
        slidePanel.add(l_Smax);
        slidePanel.add(S_max);
        slidePanel.add(l_Smin);
        slidePanel.add(S_min);
        slidePanel.add(l_Vmax);
        slidePanel.add(V_max);
        slidePanel.add(l_Vmin);
        slidePanel.add(V_min);
        container.add(slidePanel,BorderLayout.NORTH);

    }

    public static void main(String[] args)
    {
        System.loadLibrary("opencv_java3416");

        VideoCapture capture = new VideoCapture(0);


        //Mat image = new Mat();

        Mat HSVimage = new Mat();
        Mat HSVimage2 = new Mat();

        Mat mask = new Mat();
        Mat mask2 = new Mat();

        //判断传入的值是否为空，不为空则接着运行
        //Assert.assertNotNull(image);

        //创建窗口
        JFrame frame = new JFrame("tracker");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addComponentToPanel(frame.getContentPane());
        frame.pack();
        frame.setVisible(true);

        while (true){

            //capture.read(image);
            Mat image = Imgcodecs.imread("E:\\yolo-project\\Labelme data\\Genshin\\project1_data\\chenxi.png");
            Mat image2 = Imgcodecs.imread("E:\\yolo-project\\Labelme data\\Genshin\\project1_data\\mengde.png");
            Imgproc.cvtColor(image,HSVimage,Imgproc.COLOR_BGR2HSV);
            Imgproc.cvtColor(image2,HSVimage2,Imgproc.COLOR_BGR2HSV);

            l_Hmin.setText("Hmin "+Hmin);
            l_Hmax.setText("Hmax "+Hmax);
            l_Smin.setText("Smin "+Smin);
            l_Smax.setText("Smax "+Smax);
            l_Vmin.setText("Vmin "+Vmin);
            l_Vmax.setText("Vmax "+Vmax);

            lower = new Scalar(Hmin,Smin,Vmin);
            upper = new Scalar(Hmax,Smax,Vmax);

            Core.inRange(HSVimage,lower,upper,mask);
            Core.inRange(HSVimage2,lower,upper,mask2);

            HighGui.imshow("image1",image);
            HighGui.imshow("image2",image2);
            HighGui.imshow("mask1",mask);
            HighGui.imshow("mask2",mask2);
            HighGui.waitKey(1);
            image.release();
        }
    }
}
