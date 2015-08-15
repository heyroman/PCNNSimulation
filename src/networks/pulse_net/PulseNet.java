package networks.pulse_net;

import neurons.pulse_neuron.PulseNeuron;

import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.util.ArrayList;

/**
 * Created by cogtepsum on 14.05.2015.
 */
public class PulseNet {
    BufferedImage image;
    BufferedImage output;
    PulseNeuron[][] neurons;

    public PulseNet(BufferedImage image, double alphaF, double alphaL, double alphaT, double linkingV, double feedingV, double thetaV, double beta) {
        this.image = image;
        output = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        neurons = new PulseNeuron[image.getWidth()][image.getHeight()];
        for (int x = 0; x < image.getWidth(); x ++) {
            for (int y = 0; y < image.getHeight(); y++) {
                neurons[x][y] = new PulseNeuron(x, y, alphaF, alphaL, alphaT, linkingV, feedingV, thetaV, beta);
                neurons[x][y].inputStimulus = 1.0 - (double) image.getRaster().getSample(x, y, 0) / 255.0;
            }
        }
        for (int x = 0; x < neurons.length; x ++) {
            for (int y = 0; y < neurons[x].length; y++) {
                setNeighbours(neurons[x][y]);
//                neurons[x][y].initializeWeights(false);
                neurons[x][y].initializeWeights(0.7);
            }
        }
    }

    public void setNeighbours(PulseNeuron neuron) {
        ArrayList<PulseNeuron> feedingNeighbours = new ArrayList<PulseNeuron>();
        ArrayList<PulseNeuron> linkingNeighbours = new ArrayList<PulseNeuron>();
        for (int i = -2; i < 3; i++) {
            for (int j = -2; j < 3; j++) {
                if (i != 0 || j != 0) {
                    try {
                        linkingNeighbours.add(neurons[neuron.xPosition + i][neuron.yPosition + j]);
                    } catch (Exception ignored) {}
                    if (Math.abs(i) < 2 && Math.abs(j) < 2) {
                        try {
                            feedingNeighbours.add(neurons[neuron.xPosition + i][neuron.yPosition + j]);
                        } catch (Exception ignored) {}
                    }
                }
            }
        }
        neuron.neighbours = feedingNeighbours.toArray(new PulseNeuron[feedingNeighbours.size()]);
        neuron.linkingNeighbours = linkingNeighbours.toArray(new PulseNeuron[linkingNeighbours.size()]);
    }

    public void process() {
        for (int x = 1; x < neurons.length - 1; x ++) {
            for (int y = 1; y < neurons[x].length - 1; y++) {
                neurons[x][y].calcFeeding();
                neurons[x][y].calcLinking();
                neurons[x][y].calcInternal();
                neurons[x][y].calcOutput();
                if (neurons[x][y].toutput == 1.0) {
                    output.setRGB(x, y, 0x000000);
                } else {
                    output.setRGB(x, y, 0xFFFFFF);
                }
            }
        }
        for (int x = 0; x < neurons.length; x ++) {
            for (int y = 0; y < neurons[x].length; y++) {
                neurons[x][y].rewriteOutput();
                neurons[x][y].calcTheta();
            }
        }

        PulseNeuron n = neurons[150][150];
        System.out.println("In = " + n.inputStimulus + " Out = " + n.output + " Feeding = " + n.feedingCompartment + " Linking = " + n.linkingCompartment + " Threshold = " + n.theta);
    }

    public BufferedImage getOutput() {
        return output;
    }
}
