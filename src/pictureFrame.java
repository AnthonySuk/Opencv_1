import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class pictureFrame extends JFrame implements MouseListener {

    JLabel picture;
    public pictureFrame(Image icon, Dimension size){
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //给窗口添加关闭按钮

        picture = new JLabel(new ImageIcon(icon));
        picture.addMouseListener(this);

        this.add(picture);
        //this.setSize(size);
        this.setVisible(true);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        System.out.println("X = "+x+" ; Y = "+y);
        Tools.returnHSV(x,y);
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
