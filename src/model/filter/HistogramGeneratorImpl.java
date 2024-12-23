package model.filter;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import model.Snap;

/**
 * The HistogramGeneratorImpl class is responsible for generating histograms from RGB images.
 * It creates a 256x256 histogram image with individual frequency distributions for the red, green,
 * and blue color channels.
 */
public class HistogramGeneratorImpl implements HistogramGenerator {

  private final BufferedImage histogramImage;

  /**
   * Constructs a HistogramGeneratorImpl and initializes the histogram image with white background.
   * The image includes grid lines to help visualize frequency values across color channels.
   */
  public HistogramGeneratorImpl() {
    this.histogramImage = new BufferedImage(256, 256, BufferedImage.TYPE_INT_ARGB);
    initializeBackground();
  }

  /**
   * Finds the highest frequency value across multiple color frequency arrays.
   * This maximum value is used to scale the histogram to fit the histogram image dimensions.
   *
   * @param arrays An array of frequency arrays (one for each color channel).
   * @return The maximum frequency found across all arrays.
   */
  private static int findMaxFrequency(int[]... arrays) {
    int max = 0;
    for (int[] array : arrays) {
      for (int value : array) {
        if (value > max) {
          max = value;
        }
      }
    }
    return max;
  }

  /**
   * Initializes the histogram image background with a white color and adds grid lines.
   * This method prepares the histogram canvas by setting the background to white and
   * drawing gray grid lines every 10 pixels, aiding in color frequency visualization.
   */
  private void initializeBackground() {
    Graphics2D g2d = histogramImage.createGraphics();
    g2d.setColor(Color.WHITE);
    g2d.fillRect(0, 0, histogramImage.getWidth(), histogramImage.getHeight());

    g2d.setColor(Color.GRAY);
    for (int i = 0; i < histogramImage.getWidth(); i += 10) {
      g2d.drawLine(i, 0, i, histogramImage.getHeight());
      g2d.drawLine(0, i, histogramImage.getWidth(), i);
    }
    g2d.dispose();

  }

  /**
   * Calculates the frequency of each intensity level for the red, green, and blue channels
   * in a given image.
   *
   * @param rgbImage The Snap image for which color frequencies are calculated.
   * @return A 2D array where each row represents the frequency counts for rgb channels.
   */
  @Override
  public int[][] getFrequencies(Snap rgbImage) {
    int[] reds = new int[256];
    int[] greens = new int[256];
    int[] blues = new int[256];

    for (int i = 0; i < rgbImage.getSnapHeight(); i++) {
      for (int j = 0; j < rgbImage.getSnapWidth(); j++) {
        int[] pixelArr = rgbImage.getPixelValue(i, j);

        reds[pixelArr[0]]++;
        greens[pixelArr[1]]++;
        blues[pixelArr[2]]++;
      }
    }
    return new int[][]{reds, greens, blues};
  }

  /**
   * Generates a histogram image for the provided RGB image by drawing frequency lines for
   * each color channel on the initialized histogram image.
   *
   * @param rgbImage The Snap image for which the histogram is to be created.
   * @return A BufferedImage representing histogram with frequency distributions for rgb channel
   */
  @Override
  public BufferedImage createHistogram(Snap rgbImage) {
    int[][] frequencies = getFrequencies(rgbImage);
    int maxFrequency = findMaxFrequency(frequencies);

    if (maxFrequency == 0) {
      return histogramImage;
    }

    Graphics2D g = histogramImage.createGraphics();
    drawHistogramLines(g, frequencies[0], Color.RED, maxFrequency);
    drawHistogramLines(g, frequencies[1], Color.GREEN, maxFrequency);
    drawHistogramLines(g, frequencies[2], Color.BLUE, maxFrequency);

    g.dispose();
    return histogramImage;
  }

  /**
   * Draws a line graph for a specified color channel on the histogram image.
   * The height of each point is scaled based on the channel's frequency and the maximum frequency.
   *
   * @param g            The Graphics2D context used to draw on the histogram image.
   * @param frequencies  The frequency array for the specific color channel.
   * @param color        The color used to draw the frequency lines (e.g., red for the red channel).
   * @param maxFrequency The maximum frequency across all channels to scale the histogram heights.
   */
  private void drawHistogramLines(Graphics2D g, int[] frequencies, Color color, int maxFrequency) {
    g.setColor(color);

    int prevX = 0;
    int prevY = histogramImage.getHeight(); // Start at the bottom of the image
    for (int i = 0; i < frequencies.length; i++) {
      int currentX = i;
      int currentY =
              histogramImage.getHeight()
                      - (frequencies[i] * histogramImage.getHeight() / maxFrequency);

      // Draw a line connecting the previous point to the current point
      if (i > 0) {
        g.drawLine(prevX, prevY, currentX, currentY);
      }

      prevX = currentX;
      prevY = currentY;
    }
  }


}
