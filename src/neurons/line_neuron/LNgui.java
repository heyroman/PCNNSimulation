//
//Generate and draw 5x5 lines with random direction
//

package neurons.line_neuron;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by cogtepsum on 16.04.2015.
 */
public class LNgui {
    private JPanel rootPanel;
    private GridCanvas gridCanvas1;
    private JButton button1;

    int gridW, gridH, gridR, gridC;

    LineFactory lineFactory;

    public LNgui() {
        gridCanvas1.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                int xPos = e.getX();
                int yPos = e.getY();
                gridCanvas1.fillRectByPosition(xPos, yPos, 1.0);
                gridCanvas1.repaint();
            }
        });
        gridCanvas1.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
                int xPos = e.getX();
                int yPos = e.getY();
                gridCanvas1.fillRectByPosition(xPos, yPos, 1.0);
                gridCanvas1.repaint();
            }
        });
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean[][] line = lineFactory.nextLine();
                for (boolean[] ll : line) {
                    for (boolean l : ll) {
                        System.out.print(l + " ");
                    }
                    System.out.println();
                }
                System.out.println();
                viewLine(line);
                gridCanvas1.repaint();
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("LNgui");
        frame.setContentPane(new LNgui().rootPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        if(gridW == 0){
            gridW = 300;
        }
        if (gridH == 0) {
            gridH = 300;
        }
        if (gridR == 0) {
            gridR = 5;
        }
        if (gridC == 0) {
            gridC = 5;
        }

        gridCanvas1 = new GridCanvas(gridW, gridH, gridR, gridC);

        lineFactory = new LineFactory(gridR);
    }

    public void viewLine(boolean[][] line) {
        for (int i = 0; i < line.length; i++) {
            for (int j = 0; j < line.length; j++) {
                if (line[i][j]) {
                    gridCanvas1.fillRectByPosition(i*gridW/gridC + gridW/(gridC*2), j*gridH/gridR + gridH/(gridR*2), 1.0);
                } else {
                    gridCanvas1.fillRectByPosition(i*gridW/gridC + gridW/(gridC*2), j*gridH/gridR + gridH/(gridR*2), 0.0);
                }
            }
        }
    }
}
