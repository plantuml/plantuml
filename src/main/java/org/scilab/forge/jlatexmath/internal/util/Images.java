package org.scilab.forge.jlatexmath.internal.util;

import java.awt.Color;
import java.awt.image.BufferedImage;

public final class Images {

    private Images() {
        // prevent instantiation
    }

    public static double DISTANCE_THRESHOLD = 40;

    public static double distance(BufferedImage imgA, BufferedImage imgB) {
        // The images must be the same size.
        if (imgA.getWidth() == imgB.getWidth() && imgA.getHeight() == imgB.getHeight()) {
            int width = imgA.getWidth();
            int height = imgA.getHeight();

            double mse = 0;
            // Loop over every pixel.
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    Color ca = new Color(imgA.getRGB(x, y));
                    Color cb = new Color(imgB.getRGB(x, y));
                    double variance = sqr(ca.getRed() - cb.getRed()) //
                                      + sqr(ca.getBlue() - cb.getBlue()) //
                                      + sqr(ca.getGreen() - cb.getGreen()) //
                                      + sqr(ca.getAlpha() - cb.getAlpha());
                    mse += variance;
                }
            }
            return Math.sqrt(mse / height / width);
        } else {
            return -1;
        }
    }

    private static double sqr(double x) {
        return x * x;
    }

}
