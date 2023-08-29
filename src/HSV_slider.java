import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class HSV_slider extends JSlider implements ChangeListener
{
    public HSV_slider(int min,int max){

       this.setMinimum(min);
       this.setMaximum(max);
       this.setValue(0);

       this.setMajorTickSpacing(20);
       this.setMinorTickSpacing(1);
       this.setPaintTicks(true);
       this.setPaintLabels(true);

    }

    @Override
    public void stateChanged(ChangeEvent e)
    {

    }
}
