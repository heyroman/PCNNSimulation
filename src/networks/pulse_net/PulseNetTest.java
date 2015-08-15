package networks.pulse_net;

import CommonTools.BufferedImageProcessor;
import neurons.pulse_neuron.PulseNeuron;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by cogtepsum on 16.05.2015.
 */
public class PulseNetTest {
    private JPanel rootPanel;
    private JPanel chartPanel;
    private JLabel imageLabel;
    private JSlider alfaL;
    private JSlider alfaT;
    private JSlider Vf;
    private JSlider Vl;
    private JSlider Vt;
    private JSlider beta;
    private JSlider alfaF;
    private JButton resetButton;
    private JButton processStepButton;
    private JButton processButton;
    private JButton stopButton;

    private BufferedImage image;
    private PulseNet network;
    private NetThread netThread;

    public PulseNetTest() {
        alfaF.setValue(5);
        alfaL.setValue(10);
        alfaT.setValue(5);
        Vf.setValue(2);
        Vl.setValue(2);
        Vt.setValue(10);
        beta.setValue(2);

        reset();

        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                reset();
            }
        });

        processStepButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                network.process();
                setImage(network.getOutput());
            }
        });

        processButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (netThread == null || netThread.isStopped) {
                    netThread = new NetThread();
                    netThread.start();
                }
            }
        });

        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                netThread.isStopped = true;
            }
        });

        alfaF.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                for (PulseNeuron[] neurons : network.neurons) {
                    for (PulseNeuron neuron : neurons) {
                        neuron.alphaF = (double)alfaF.getValue()/10;
                    }
                }
            }
        });
        alfaL.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                for (PulseNeuron[] neurons : network.neurons) {
                    for (PulseNeuron neuron : neurons) {
                        neuron.alphaL = (double)alfaL.getValue()/10;
                    }
                }
            }
        });
        alfaT.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                for (PulseNeuron[] neurons : network.neurons) {
                    for (PulseNeuron neuron : neurons) {
                        neuron.alphaT = (double)alfaT.getValue()/10;
                    }
                }
            }
        });
        Vf.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                for (PulseNeuron[] neurons : network.neurons) {
                    for (PulseNeuron neuron : neurons) {
                        neuron.feedingV = (double)Vf.getValue()/100;
                    }
                }
            }
        });
        Vl.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                for (PulseNeuron[] neurons : network.neurons) {
                    for (PulseNeuron neuron : neurons) {
                        neuron.linkingV = (double)Vl.getValue()/100;
                    }
                }
            }
        });
        Vt.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                for (PulseNeuron[] neurons : network.neurons) {
                    for (PulseNeuron neuron : neurons) {
                        neuron.thetaV = (double)Vt.getValue();
                    }
                }
            }
        });
        beta.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                for (PulseNeuron[] neurons : network.neurons) {
                    for (PulseNeuron neuron : neurons) {
                        neuron.beta = (double) beta.getValue() / 10;
                    }
                }
            }
        });
        imageLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                for (PulseNeuron[] nn : network.neurons) {
                    for (PulseNeuron n : nn) {
                        if(n.xPosition == e.getX() && n.yPosition == e.getY()) {
                            System.out.println("Linking weights = " + n.linkingWeights[0] + ", " + n.linkingWeights[1] + ", " + n.linkingWeights[2]);
                            System.out.println("Linking neighbours count = " + n.linkingNeighbours.length + " Feeding neighbours count = " + n.neighbours.length);
                        }
                    }
                }
            }
        });
    }

    public void setImage(BufferedImage image) {
        imageLabel.setIcon(new ImageIcon(image));
    }

    public void reset() {
        try {
            image = BufferedImageProcessor.resize(ImageIO.read(new File(".\\src\\input_data\\tough_stewie3.JPG")), 300, 300);
//            image = BufferedImageProcessor.resize(ImageIO.read(new File(".\\src\\input_data\\T.gif")), 300, 300);
            image = BufferedImageProcessor.getGrayScale(image);
            setImage(image);
            network = new PulseNet(image, (double)alfaF.getValue()/10, (double)alfaL.getValue()/10, (double)alfaT.getValue()/10,
                    (double)Vl.getValue()/100, (double)Vf.getValue()/100, (double)Vt.getValue(), (double)beta.getValue()/10);
        } catch (IOException exc) {
            exc.printStackTrace();
        }
    }

    public static void createAndShowGui() {
        JFrame frame = new JFrame("PulseNetTest");
        frame.setContentPane(new PulseNetTest().rootPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        PulseNetTest.createAndShowGui();
    }

    public class NetThread extends Thread {
        public boolean isStopped = false;

        @Override
        public void run() {
            super.run();
            while (!isStopped) {
                network.process();
                setImage(network.getOutput());
                try {
                    Thread.sleep(40);
                } catch (InterruptedException exc) {
                    isStopped = true;
                }
            }
        }
    }
}
