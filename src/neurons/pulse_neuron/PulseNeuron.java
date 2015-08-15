package neurons.pulse_neuron;

/**
 * Created by cogtepsum on 04.04.2015.
 */
public class PulseNeuron {
    public int xPosition, yPosition;
    public PulseNeuron[] neighbours;
    public PulseNeuron[] linkingNeighbours;
    public double[] feedingWeights;
    public double[] linkingWeights;
    public double feedingV, linkingV, thetaV;
    public double feedingCompartment = 0.0, linkingCompartment = 0.0;
    public double inputStimulus;
    public double alphaF, alphaL, alphaT;
    public double beta;
    public double theta = 0.0;
    public double internal = 0.0, output = 0.0, toutput = 0.0;

    public PulseNeuron(int xPosition, int yPosition, double alphaF, double alphaL, double alphaT, double linkingV, double feedingV, double thetaV, double beta) {
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.alphaF = alphaF;
        this.alphaL = alphaL;
        this.alphaT = alphaT;
        this.linkingV = linkingV;
        this.feedingV = feedingV;
        this.thetaV = thetaV;
        this.beta = beta;
    }

    public void calcFeeding() {
        double sum = 0;
        for(int i = 0; i < neighbours.length; i++) {
            sum += feedingWeights[i]*neighbours[i].inputStimulus;
        }
        feedingCompartment =  Math.exp(-1 * alphaF) * feedingCompartment + inputStimulus + feedingV * sum;
    }

    public void calcLinking() {
        double sum = 0;
        for(int i = 0; i < linkingNeighbours.length; i++) {
            sum += linkingWeights[i]*linkingNeighbours[i].output;
        }
        linkingCompartment =  Math.exp(-1 * alphaL) * linkingCompartment + linkingV * (sum + 1);
    }

    public void calcInternal() {
        internal = feedingCompartment * (1 + beta * linkingCompartment);
    }

    public void calcTheta() {
        theta = Math.exp(-1 * alphaT) * theta + thetaV * output;
    }

    public void calcOutput() {
        toutput = internal > theta ? 1.0 : 0.0;
    }

    public void rewriteOutput() {
        output = toutput;
    }

    public void initializeWeights(boolean bidirectionalInitialization) {
        feedingWeights = new double[neighbours.length];
        for(int i = 0; i < feedingWeights.length; i++) {
            if(bidirectionalInitialization) {
                if(Math.random() > 0.5) {
                    feedingWeights[i] = Math.random() * 0.3;
                } else {
                    feedingWeights[i] = Math.random() * -0.3;
                }
            } else {
                feedingWeights[i] = Math.random() * 1;
            }
        }

        linkingWeights = new double[linkingNeighbours.length];
        for(int i = 0; i < linkingWeights.length; i++) {
            if(bidirectionalInitialization) {
                if(Math.random() > 0.5) {
                    linkingWeights[i] = Math.random() * 0.3;
                } else {
                    linkingWeights[i] = Math.random() * -0.3;
                }
            } else {
                linkingWeights[i] = Math.random() * 1;
            }
        }
    }

    public void initializeWeights(double module) {
        feedingWeights = new double[neighbours.length];
        for(int i = 0; i < feedingWeights.length; i++) {
            feedingWeights[i] = module;
        }

        linkingWeights = new double[linkingNeighbours.length];
        for(int i = 0; i < linkingWeights.length; i++) {
            linkingWeights[i] = module / Math.sqrt((this.xPosition - linkingNeighbours[i].xPosition)*(this.xPosition - linkingNeighbours[i].xPosition) + (this.yPosition - linkingNeighbours[i].yPosition)*(this.yPosition - linkingNeighbours[i].yPosition));
        }
    }
}
