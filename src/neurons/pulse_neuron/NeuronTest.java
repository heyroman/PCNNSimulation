package neurons.pulse_neuron;

import com.xeiam.xchart.Chart;
import com.xeiam.xchart.XChartPanel;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by cogtepsum on 13.04.2015.
 */
public class NeuronTest {
    private JPanel rootPanel;
    private JButton processButton;
    private JPanel chartPanel;
    private JSlider alfaF;
    private JSlider alfaL;
    private JSlider alfaT;
    private JSlider Vf;
    private JSlider Vl;
    private JSlider Vt;
    private JSlider beta;
    private JButton initWButton;
    private XChartPanel XChartPanel1;
    private JButton resetButton;
    private JButton stimulusButton;

    PulseNeuron neuron;

    Chart chart;
    List<Double> xDataT;
    List<Double> yDataT;

    List<Double> xDataO;
    List<Double> yDataO;

    List<Double> xDataI;
    List<Double> yDataI;

    int counter;

    public NeuronTest() {
        alfaF.setValue(5);
        alfaL.setValue(10);
        alfaT.setValue(10);
        Vf.setValue(2);
        Vl.setValue(2);
        Vt.setValue(10);
        beta.setValue(2);

        neuron = createNeuron();
        counter = 0;

        processButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                processNeuron();
            }
        });

        stimulusButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                neuron.inputStimulus = 1.0;
                processNeuron();
                neuron.inputStimulus = 0.0;
            }
        });

        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                counter = 0;

                neuron.feedingCompartment = 0;
                neuron.linkingCompartment = 0;
                neuron.internal = 0;
                neuron.theta = 0;
                neuron.output = 0;

                xDataT = new ArrayList<Double>();
                yDataT = new ArrayList<Double>();
                xDataT.add(0.0);
                yDataT.add(0.0);
                XChartPanel1.updateSeries("Threshold", xDataT, yDataT);

                xDataO = new ArrayList<Double>();
                yDataO = new ArrayList<Double>();
                xDataO.add(0.0);
                yDataO.add(0.0);
                XChartPanel1.updateSeries("Output", xDataO, yDataO);

                xDataI = new ArrayList<Double>();
                yDataI = new ArrayList<Double>();
                xDataI.add(0.0);
                yDataI.add(0.0);
                XChartPanel1.updateSeries("Input", xDataI, yDataI);
            }
        });
        alfaF.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                neuron.alphaF = (double)alfaF.getValue()/10;
            }
        });
        alfaL.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                neuron.alphaL = (double)alfaL.getValue()/10;
            }
        });
        alfaT.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                neuron.alphaT = (double)alfaT.getValue()/10;
            }
        });
        Vf.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                neuron.feedingV = (double)Vf.getValue()/100;
            }
        });
        Vl.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                neuron.linkingV = (double)Vl.getValue()/100;
            }
        });
        Vt.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                neuron.thetaV = (double)Vt.getValue();
            }
        });
        beta.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                neuron.beta = (double)beta.getValue()/10;
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("NeuronTest");
        frame.setContentPane(new NeuronTest().rootPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        chart = new Chart(500, 300);
        chart.setChartTitle("Output activity");
        chart.setXAxisTitle("Iteration");
        chart.setYAxisTitle("Value");

        xDataT = new ArrayList<Double>();
        xDataT.add(0.0);
        yDataT = new ArrayList<Double>();
        yDataT.add(0.0);
        chart.addSeries("Threshold", xDataT, yDataT);

        xDataO = new ArrayList<Double>(); yDataO = new ArrayList<Double>();
        xDataO.add(0.0); yDataO.add(0.0);
        chart.addSeries("Output", xDataO, yDataO);

        xDataI = new ArrayList<Double>(); yDataI = new ArrayList<Double>();
        xDataI.add(0.0); yDataI.add(0.0);
        chart.addSeries("Input", xDataI, yDataI);

        XChartPanel1 = new XChartPanel(chart);
    }

    public PulseNeuron createNeuron() {
        double aF = (double)alfaF.getValue()/10;
        double aL = (double)alfaL.getValue()/10;
        double aT = (double)alfaT.getValue()/10;
        double vf = (double)Vf.getValue()/100;
        double vl = (double)Vl.getValue()/100;
        double vt = (double)Vt.getValue();
        double b = (double)beta.getValue()/10;

        PulseNeuron result = new PulseNeuron(0, 0, aF, aL, aT, vl, vf, vt, b);

        result.linkingWeights = new double[0];
//        result.linkingWeights = new double[1];
//        result.linkingWeights[0] = 10.0;
//        result.linkingCompartment = 10.0;

        result.feedingWeights = new double[0];
//        result.feedingWeights = new double[1];
//        result.feedingWeights[0] = 10.0;
//        result.feedingCompartment = 10.0;

        result.neighbours = new PulseNeuron[0];
        result.linkingNeighbours = new PulseNeuron[0];
//        result.neighbours = new PulseNeuron[1];
//        result.neighbours[0] = new PulseNeuron(0, 0, aF, aL, aT, vl, vf, vt, b);

        return result;
    }

    public void processNeuron() {
        neuron.calcFeeding();
        System.out.println("Feeding = " + neuron.feedingCompartment);
        neuron.calcLinking();
        neuron.calcInternal();
        System.out.println("Internal = " + neuron.internal);
        neuron.calcOutput();
        neuron.rewriteOutput();
        neuron.calcTheta();
        System.out.println("Theta = " + neuron.theta);
        System.out.println();

        counter++;
        xDataT.add((double)counter); xDataO.add((double)counter); xDataI.add((double)counter);
        yDataT.add(neuron.theta); yDataO.add(neuron.output); yDataI.add(neuron.inputStimulus);

        XChartPanel1.updateSeries("Threshold", xDataT, yDataT);
        XChartPanel1.updateSeries("Input", xDataI, yDataI);
        XChartPanel1.updateSeries("Output", xDataO, yDataO);
    }
}
