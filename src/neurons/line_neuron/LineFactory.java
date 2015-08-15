package neurons.line_neuron;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.util.Random;

/**
 * Created by cogtepsum on 31.05.2015.
 */
public class LineFactory {
    int dimension;

    public LineFactory(int dimension) {
        this.dimension = dimension;
    }

    public boolean[][] nextLine() {
        boolean[][] line = new boolean[this.dimension][this.dimension];

        BufferedImage bi = new BufferedImage(dimension, dimension, BufferedImage.TYPE_BYTE_GRAY);
        Graphics2D g = bi.createGraphics();
        Random r = new Random();
        int x = r.nextInt(dimension);
        int y = r.nextInt(dimension);

        while(Math.abs(x - dimension/2) < dimension/2 && Math.abs(y - dimension/2) < dimension/2) {
            x = r.nextInt(dimension);
            y = r.nextInt(dimension);
        }

        g.drawLine(dimension/2, dimension/2, x, y);
        g.drawLine(dimension/2, dimension/2, dimension-1-x, dimension-1-y);

        Raster raster = bi.getData();

        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                if (raster.getSample(i, j, 0) == 255) {
                    line[i][j] = true;
                }
            }
        }

        return line;
    }
}
