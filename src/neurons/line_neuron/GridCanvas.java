package neurons.line_neuron;

import javax.swing.*;
import java.awt.*;

/**
 * Created by cogtepsum on 16.04.2015.
 */
public class GridCanvas extends JPanel {
    private double[][] data;
    private int width, height;
    private int rows, cols;
    private int rowHt, rowWid;

    public GridCanvas(int width, int height, int rows, int cols) {
        this.width = width;
        this.height = height;
        this.rows = rows;
        this.cols = cols;
        data = new double[rows][cols];
        rowHt = height / rows;
        rowWid = width / cols;
    }

    public void paintGrid(Graphics g){
        for (int i = 0; i <= rows; i++) {
            if (i < rows) {
                g.drawLine(0, i * rowHt, width, i * rowHt);
            } else {
                g.drawLine(0, i * rowHt - 1, width, i * rowHt - 1);
            }
        }
        for (int i = 0; i <= cols; i++) {
            if (i < cols) {
                g.drawLine(i * rowWid, 0, i * rowWid, height);
            } else {
                g.drawLine(i * rowWid - 1, 0, i * rowWid - 1, height);
            }
        }
    }

    protected void fillGrid(Graphics g) {
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[i].length; j++) {
                int grey = (int) ((1 - data[i][j]) * 255);
                g.setColor(new Color(grey, grey, grey));
                g.fillRect(i * rowWid + 1, j * rowHt + 1, rowWid - 1, rowHt - 1);
            }
        }
    }

    public void fillRectByPosition(int x, int y, double value) {
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[i].length; j++) {
                if(x > i*rowWid + 1 && x < i*rowWid + rowWid && y > j*rowHt - 1 && y < j*rowHt + rowHt) {
                    data[i][j] = value;
                }
            }
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        paintGrid(g);
        fillGrid(g);
    }

    public double[][] getData() {
        return data;
    }

    public void setData(double[][] data) {
        this.data = data;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getCols() {
        return cols;
    }

    public void setCols(int cols) {
        this.cols = cols;
    }
}
