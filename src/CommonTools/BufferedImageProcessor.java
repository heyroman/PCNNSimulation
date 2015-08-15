package CommonTools;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;

/**
 * Created by cogtepsum on 03.04.2015.
 */
public class BufferedImageProcessor {

    public static BufferedImage resize(BufferedImage img, int newW, int newH) {
        int w = img.getWidth();
        int h = img.getHeight();
        BufferedImage dimg = new BufferedImage(newW, newH, img.getType());
        Graphics2D g = dimg.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(img, 0, 0, newW, newH, 0, 0, w, h, null);
        g.dispose();
        return dimg;
    }

    public static BufferedImage getGrayScale(BufferedImage image) {
        BufferedImage img = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
        Graphics g = img.getGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();
        return img;
    }

    public static byte[][] getByteArray(BufferedImage input, boolean normalize) {
        int w = input.getWidth();
        int h = input.getHeight();
        Raster raster = input.getRaster();

        byte[][] result = new byte[h][w];
        for(int i = 0; i < h; i++){
            for(int j = 0; j < w; j++){
                if(normalize) {
                    result[i][j] = (byte) (raster.getSampleDouble(i, j, 0)/255);
                } else {
                    result[i][j] = (byte) raster.getSampleDouble(i, j, 0);
                }
            }
        }
        return result;
    }

    public static double[][] getDoubleArray(BufferedImage input, boolean normalize) {
        int w = input.getWidth();
        int h = input.getHeight();
        Raster raster = input.getData();

        double[][] result = new double[h][w];
        for(int i = 0; i < h; i++){
            for(int j = 0; j < w; j++){
                if(normalize) {
                    result[i][j] = raster.getSampleDouble(i, j, 0)/255;
                } else {
                    result[i][j] = raster.getSampleDouble(i, j, 0);
                }
            }
        }
        return result;
    }
}
