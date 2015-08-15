import CommonTools.BufferedImageProcessor;
import networks.pulse_net.PulseNet;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by cogtepsum on 02.04.2015.
 */
public class myGui {
    private JButton button1;
    private JPanel rootPanel;
    private JLabel imageLabel;
    private JButton processButton;

    private BufferedImage image;
    private JFileChooser jFileChooser;

    PulseNet network;

    public myGui() {
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                jFileChooser = new JFileChooser();
//                jFileChooser.setCurrentDirectory(new File("C:\\Users\\cogtepsum\\Desktop\\"));
//                if (jFileChooser.showOpenDialog(jFileChooser) == JFileChooser.APPROVE_OPTION) {
//                    try {
//                        image = BufferedImageProcessor.resize(ImageIO.read(jFileChooser.getSelectedFile()), 300, 300);
//                        image = BufferedImageProcessor.getGrayScale(image);
//                        setImage(image);
//                        network = new PulseNet(image, 0.5, 1, 0.3, 0.02, 0.02, 10, 0.2);
//                    } catch (IOException exc) {
//                        exc.printStackTrace();
//                    }
//                }

                try {
//                    image = BufferedImageProcessor.resize(ImageIO.read(new File("C:\\Users\\cogtepsum\\Desktop\\Снимок.JPG")), 300, 300);
                    image = BufferedImageProcessor.resize(ImageIO.read(new File("C:\\Users\\cogtepsum\\Desktop\\T.gif")), 300, 300);
                    image = BufferedImageProcessor.getGrayScale(image);
                    setImage(image);
                    network = new PulseNet(image, 0.5, 1, 0.5, 0.02, 0.02, 10, 0.2);
                } catch (IOException exc) {
                    exc.printStackTrace();
                }
            }
        });
        processButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                network.process();
                setImage(network.getOutput());
            }
        });
        imageLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
//                System.out.println(image.getRGB(e.getX(), e.getY()));
                System.out.println(image.getRaster().getSample(e.getX(), e.getY(), 0));
            }
        });
    }

    public static void createAndShowGui() {
        JFrame frame = new JFrame("myGui");
        frame.setContentPane(new myGui().rootPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        myGui.createAndShowGui();
    }

    public void setImage(BufferedImage image) {
        imageLabel.setIcon(new ImageIcon(image));
    }
}
