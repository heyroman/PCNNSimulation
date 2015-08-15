package neurons.line_neuron;

/**
 * Created by cogtepsum on 16.04.2015.
 */
public class LineNeuron {
    public double[][] synapseSignals;
    public double[][] synapseWeights;
    private double feedbackWeight;
    public LineNeuron[] lateralNeighbours;
    public double[] lateralWeights;
    public double out;
    private static double a = 0.3;

    public LineNeuron(int dataLength) {
        synapseSignals = new double[dataLength][dataLength];
        synapseWeights = new double[dataLength][dataLength];
    }

    public void setSynapseSignals(double[][] input) {
        synapseSignals = input;
    }

    public void setLateralNeighbours(LineNeuron[] cluster) {
        lateralNeighbours = new LineNeuron[cluster.length - 1];
        lateralWeights = new double[cluster.length - 1];
        int i = 0;
        for(LineNeuron n : cluster) {
            if(!n.equals(this)) {
                lateralNeighbours[i] = n;
                i++;
            }
        }
    }

    public void doSpike() {
        double synapseSum = 0;
        double lateralSum = 0;

        for(int i = 0; i < synapseSignals.length; i++) {
            for(int j = 0; j < synapseSignals.length; j++) {
                synapseSum += synapseSignals[i][j] * synapseWeights[i][j];
            }
        }
        synapseSum += this.out * feedbackWeight;

        for(int k = 0; k < lateralNeighbours.length; k++) {
            lateralSum += lateralNeighbours[k].out * lateralWeights[k];
        }

        double s = Math.max(0.0,synapseSum + lateralSum );
        this.out = (Math.exp(a * s) - Math.exp(-a * s))/(Math.exp(a * s) + Math.exp(-a * s));
    }

    public void educate(double speed, boolean infiniteWeights) {
        // educate synapses
        double synapseSum = 0;
        for(int i = 0; i < synapseSignals.length; i++) {
            for(int j = 0; j < synapseSignals.length; j++) {
                synapseWeights[i][j] += speed * this.out * (synapseSignals[i][j] - this.out * synapseWeights[i][j]);
                synapseSum += synapseWeights[i][j] * synapseWeights[i][j];
            }
        }
        // educate positive feedback
        feedbackWeight += speed * this.out * (this.out - this.out * feedbackWeight);
        synapseSum += feedbackWeight * feedbackWeight;
        // normalize synapses
        if(!infiniteWeights) {
            for(int i = 0; i < synapseSignals.length; i++) {
                for(int j = 0; j < synapseSignals.length; j++) {
                    synapseWeights[i][j] = synapseWeights[i][j] / Math.sqrt(synapseSum);
                }
            }
        }
        // normalize feedback
        feedbackWeight /= Math.sqrt(synapseSum);
        // educate lateral connections
        double lateralSum = 0;
        for(int i = 0; i < lateralNeighbours.length; i++) {
            lateralWeights[i] -= speed * this.out * (lateralNeighbours[i].out - this.out * lateralWeights[i]);
            lateralSum += lateralWeights[i] * lateralWeights[i];
        }
        // normalize lateral connections
        if(!infiniteWeights) {
            for(int i = 0; i < lateralNeighbours.length; i++) {
                lateralWeights[i] = lateralWeights[i] / Math.sqrt(lateralSum);
            }
        }
    }

    public void initializeWeights(boolean bidirectionalInitialization) {
        for(int i = 0; i < synapseSignals.length; i++) {
            for(int j = 0; j < synapseSignals.length; j++) {
                if(bidirectionalInitialization) {
                    if(Math.random() > 0.5) {
                        synapseWeights[i][j] = Math.random() * 0.3;
                    } else {
                        synapseWeights[i][j] = Math.random() * -0.3;
                    }
                } else {
                    synapseWeights[i][j] = Math.random() * 0.3;
                }
            }
        }

        for(int i = 0; i < lateralNeighbours.length; i++) {
            if(bidirectionalInitialization) {
                if(Math.random() > 0.5) {
                    lateralWeights[i] = Math.random() * 0.3;
                } else {
                    lateralWeights[i] = Math.random() * -0.3;
                }
            } else {
                lateralWeights[i] = Math.random() * -0.3;
            }
        }
    }
}
