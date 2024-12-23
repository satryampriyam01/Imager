package model.filter;

import java.awt.image.BufferedImage;

import model.Snap;

/**
 * The HistogramGenerator interface defines methods for generating histograms and
 * calculating color channel frequencies from an image represented as a SnapModel.
 */
public interface HistogramGenerator {

  /**
   * Computes the frequencies of each intensity level for the red, green, and blue color channels
   * in the given RGB SnapModel.
   *
   * @param rgbImage The RGB SnapModel.
   * @return An array of int arrays representing the frequencies for each color channel.
   */
  int[][] getFrequencies(Snap rgbImage);

  /**
   * Creates a histogram image based on the provided RGB SnapModel.
   *
   * @param rgbImage The RGB SnapModel for which the histogram is generated.
   * @return The histogram image.
   */
  BufferedImage createHistogram(Snap rgbImage);
}
