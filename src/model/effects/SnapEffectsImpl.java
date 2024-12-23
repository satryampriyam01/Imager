package model.effects;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import model.Snap;
import model.SnapImpl;
import model.filter.HistogramGenerator;
import model.filter.HistogramGeneratorImpl;


/**
 * The SnapEffectsImpl class provides various image processing effects on Snap objects.
 * It includes methods for compressing images, generating histograms, and applying
 * color correction. Compression is achieved using the Haar wavelet transform.
 */

public class SnapEffectsImpl implements SnapEffects {

  /**
   * Compresses an image by reducing the number of pixels based on a percentage, using
   * the Haar wavelet transform to minimize information loss.
   *
   * @param snap       The Snap object representing the image to compress.
   * @param percentage The percentage of pixel data to retain.
   * @return A new Snap object with the compressed image.
   */

  @Override
  public Snap compress(Snap snap, double percentage) {
    int width = snap.getSnapWidth();
    int height = snap.getSnapHeight();

    double[][] reds = new double[height][width];
    double[][] greens = new double[height][width];
    double[][] blues = new double[height][width];

    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        reds[i][j] = snap.getPixelValue(i, j)[0];
        greens[i][j] = snap.getPixelValue(i, j)[1];
        blues[i][j] = snap.getPixelValue(i, j)[2];
      }
    }

    // Unpad each compressed channel to get the original size
    double[][][] compressedChannels = compressRGBChannels(reds, greens, blues,
            percentage, width, height);
    double[][] processedReds = compressedChannels[0];
    double[][] processedGreens = compressedChannels[1];
    double[][] processedBlues = compressedChannels[2];

    int[][][] compressedPixels = new int[height][width][3];
    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        compressedPixels[i][j][0] = clamp((int) processedReds[i][j]);
        compressedPixels[i][j][1] = clamp((int) processedGreens[i][j]);
        compressedPixels[i][j][2] = clamp((int) processedBlues[i][j]);

      }
    }

    return new SnapImpl(compressedPixels);
  }

  /**
   * Compresses a 2D channel array using Haar wavelet transformation and thresholding.
   *
   * @param channel    The input channel array.
   * @param percentage The percentage of values to keep during compression.
   * @return The compressed channel array.
   */
  private double[][] compressChannel(double[][] channel, double percentage) {
    int height = channel.length;
    int width = channel[0].length;

    // 2D Haar wavelet transform
    double[][] transformedImage = haarTransform2D(channel, width);

    // Thresholding
    double[] uniqueValues = getUniqueValues(transformedImage);
    double threshold = findThreshold(uniqueValues, percentage);

    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        if (Math.abs(transformedImage[i][j]) < threshold) {
          transformedImage[i][j] = 0.0;
        }
      }
    }

    // Inverting the 2D Haar wavelet transform
    transformedImage = inverseHaarTransform2D(transformedImage, width);

    return transformedImage;
  }


  /**
   * Compresses the RGB channels of an image by padding each channel to the nearest power of
   * two, applying Haar wavelet compression, and then unpadding the result to its original size.
   *
   * @param reds       2D array of red channel values.
   * @param greens     2D array of green channel values.
   * @param blues      2D array of blue channel values.
   * @param percentage Percentage of pixel data to retain.
   * @param widthSnap  Width of the original Snap image.
   * @param heightSnap Height of the original Snap image.
   * @return A 3D array containing compressed RGB channels.
   */
  private double[][][] compressRGBChannels(double[][] reds, double[][] greens,
                                           double[][] blues, double percentage, int widthSnap,
                                           int heightSnap) {
    int width = widthSnap;
    int height = heightSnap;

    // Calculate the padded size based on the maximum of width and height
    int paddedSize = padSize(Math.max(width, height));

    // Pad each RGB channel
    double[][] paddedReds = padChannel(reds, paddedSize);
    double[][] paddedGreens = padChannel(greens, paddedSize);
    double[][] paddedBlues = padChannel(blues, paddedSize);

    // Calculate the compression percentage for the Haar wavelet transformation
    double compressionPercentage = percentage / 100;
    // Compress each padded channel
    double[][] compressedReds = compressChannel(paddedReds, compressionPercentage);
    double[][] compressedGreens = compressChannel(paddedGreens, compressionPercentage);
    double[][] compressedBlues = compressChannel(paddedBlues, compressionPercentage);

    // Unpad each compressed channel to obtain the original size
    double[][] unpaddedReds = unpadChannel(compressedReds, height, width);
    double[][] unpaddedGreens = unpadChannel(compressedGreens, height, width);
    double[][] unpaddedBlues = unpadChannel(compressedBlues, height, width);

    // Return the result as a 3D array representing the compressed RGB channels
    return new double[][][]{unpaddedReds, unpaddedGreens, unpaddedBlues};
  }

  /**
   * Pads a 2D channel array to a specified size.
   *
   * @param channel    The original channel array to pad.
   * @param paddedSize The size to which the channel should be padded.
   * @return The padded channel array.
   */
  private double[][] padChannel(double[][] channel, int paddedSize) {
    double[][] paddedChannel = new double[paddedSize][paddedSize];
    for (int i = 0; i < channel.length; i++) {
      System.arraycopy(channel[i], 0, paddedChannel[i], 0, channel[i].length);
    }
    return paddedChannel;
  }

  /**
   * Unpads a padded channel array to its original dimensions.
   *
   * @param paddedImage    The padded channel array to unpad.
   * @param originalHeight The original height of the channel.
   * @param originalWidth  The original width of the channel.
   * @return The unpadded channel array.
   */
  private double[][] unpadChannel(double[][] paddedImage, int originalHeight,
                                  int originalWidth) {
    double[][] originalImage = new double[originalHeight][originalWidth];

    for (int i = 0; i < originalHeight; i++) {
      System.arraycopy(paddedImage[i], 0, originalImage[i], 0, originalWidth);
    }

    return originalImage;
  }

  /**
   * Determines the size to which a channel should be padded (nearest power of two).
   *
   * @param size The original size of the channel.
   * @return The padded size.
   */
  private int padSize(int size) {
    int paddedSize = 1;
    while (paddedSize < size) {
      paddedSize *= 2;
    }
    return paddedSize;
  }

  /**
   * Applies the 2D Haar wavelet transform to an input matrix.
   *
   * @param x The input matrix.
   * @param s The size of the matrix.
   * @return The transformed matrix.
   */
  private double[][] haarTransform2D(double[][] x, int s) {
    int c = s;

    while (c > 1) {
      for (int i = 0; i < s; i++) {
        double[] row = new double[c];
        System.arraycopy(x[i], 0, row, 0, c);
        row = transformSequence1D(row);
        System.arraycopy(row, 0, x[i], 0, c);
      }

      for (int j = 0; j < s; j++) {
        double[] column = new double[c];
        for (int i = 0; i < c; i++) {
          column[i] = x[i][j];
        }
        column = transformSequence1D(column);
        for (int i = 0; i < c; i++) {
          x[i][j] = column[i];
        }
      }

      c = c / 2;
    }

    return x;
  }

  /**
   * Applies the inverse Haar wavelet transform to a transformed sequence.
   *
   * @param s The transformed sequence.
   * @return The inverted sequence.
   */
  private double[][] inverseHaarTransform2D(double[][] x, int s) {
    int c = 2;

    while (c <= s) {
      for (int j = 0; j < s; j++) {
        double[] column = new double[c];
        for (int i = 0; i < c; i++) {
          column[i] = x[i][j];
        }
        column = inverseTransform1D(column);
        for (int i = 0; i < c; i++) {
          x[i][j] = column[i];
        }
      }

      for (int i = 0; i < s; i++) {
        double[] row = new double[c];
        System.arraycopy(x[i], 0, row, 0, c);
        row = inverseTransform1D(row);
        System.arraycopy(row, 0, x[i], 0, c);
      }

      c = c * 2;
    }

    return x;
  }

  /**
   * Applies the Haar wavelet transform to a sequence of values.
   *
   * @param s The input sequence.
   * @return The transformed sequence.
   */
  private double[] transformSequence1D(double[] s) {
    List<Double> avg = new ArrayList<>();
    List<Double> diff = new ArrayList<>();

    for (int i = 0; i < s.length; i += 2) {
      double a = s[i];
      double b = s[i + 1];
      double av = (a + b) / Math.sqrt(2);
      double di = (a - b) / Math.sqrt(2);
      avg.add(av);
      diff.add(di);
    }

    List<Double> result = new ArrayList<>(avg);
    result.addAll(diff);

    return result.stream().mapToDouble(Double::doubleValue).toArray();
  }

  /**
   * Applies the inverse Haar wavelet transform to a transformed sequence.
   *
   * @param s The transformed sequence.
   * @return The inverted sequence.
   */
  private double[] inverseTransform1D(double[] s) {
    double[] avg = Arrays.copyOfRange(s, 0, s.length / 2);
    double[] diff = Arrays.copyOfRange(s, s.length / 2, s.length);

    List<Double> result = new ArrayList<>();
    for (int i = 0, j = 0; i < avg.length; i++, j++) {
      double a = avg[i];
      double b = diff[j];
      double av = (a + b) / Math.sqrt(2);
      double di = (a - b) / Math.sqrt(2);
      result.add(av);
      result.add(di);
    }

    return result.stream().mapToDouble(Double::doubleValue).toArray();
  }

  /**
   * Extracts unique non-zero values from a 2D matrix.
   *
   * @param image The input matrix.
   * @return An array containing unique non-zero values.
   */
  private double[] getUniqueValues(double[][] image) {
    Set<Double> uniqueValues = new HashSet<>();

    for (double[] row : image) {
      for (double value : row) {
        if (value != 0.0) {
          uniqueValues.add(Math.abs(value));
        }
      }
    }

    return uniqueValues.stream().mapToDouble(Double::doubleValue).toArray();
  }

  /**
   * Finds the threshold for channel compression based on a given percentage of unique values.
   *
   * @param values     The array of unique values.
   * @param percentage The percentage of values to keep.
   * @return The calculated threshold.
   */
  private double findThreshold(double[] values, double percentage) {
    int numToReset = (int) (values.length * percentage);
    if (numToReset < 1) {
      return 0.0;
    }

    // Sort the unique values
    Arrays.sort(values);

    return values[numToReset - 1];
  }

  /**
   * Generates a histogram image for the Snap object.
   *
   * @param snap The Snap object representing the image.
   * @return A Snap object containing the histogram as an image.
   */
  @Override
  public Snap histogram(Snap snap) {
    HistogramGenerator histogramGenerator = new HistogramGeneratorImpl();
    BufferedImage image = histogramGenerator.createHistogram(snap);
    int width = image.getWidth();
    int height = image.getHeight();
    int[][][] output = new int[height][width][3];

    // Convert BufferedImage to 3D array format
    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        int color = image.getRGB(j, i);
        output[i][j][0] = (color >> 16) & 0xff; // Red
        output[i][j][1] = (color >> 8) & 0xff;  // Green
        output[i][j][2] = color & 0xff;         // Blue
      }
    }

    return new SnapImpl(output);
  }


  /**
   * Applies color correction to a Snap image, allowing for optional percentage-based
   * color splitting.
   *
   * @param snap            The Snap object representing the image.
   * @param splitPercentage Optional parameter for splitting the color correction across
   *                        a specific image width percentage.
   * @return A new Snap object with color correction applied.
   */
  @Override
  public Snap colorCorrection(Snap snap, int... splitPercentage) {
    int width = snap.getSnapWidth();
    int height = snap.getSnapHeight();
    int[][][] originalPixels = snap.getSnap();

    int splitPoint = width;
    if (splitPercentage.length > 0) {
      if (splitPercentage[0] > 100 || splitPercentage[0] < 0) {
        throw new IllegalArgumentException("splitPercentage > 100");
      }
      splitPoint = (width * splitPercentage[0]) / 100;
    }

    HistogramGenerator histogramGenerator = new HistogramGeneratorImpl();
    int[][] frequencies = histogramGenerator.getFrequencies(snap);
    int redPeak = findPeak(frequencies[0]);
    int greenPeak = findPeak(frequencies[1]);
    int bluePeak = findPeak(frequencies[2]);
    int avgPeak = (redPeak + greenPeak + bluePeak) / 3;

    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        if (j < splitPoint) {
          originalPixels[i][j][0] = clamp(snap.getPixelValue(i, j)[0] - avgPeak + redPeak);
          originalPixels[i][j][1] = clamp(snap.getPixelValue(i, j)[1] - avgPeak + greenPeak);
          originalPixels[i][j][2] = clamp(snap.getPixelValue(i, j)[2] - avgPeak + bluePeak);
        } else {
          originalPixels[i][j] = snap.getPixelValue(i, j);
        }
        //System.out.println("one pixel");
      }
    }

    return new SnapImpl(originalPixels);
  }


  /**
   * Adjusts the black, mid, and white levels of the given image (`Snap` object)
   * and applies these adjustments selectively based on a specified split point.
   *
   * <p>This method allows the image to be split horizontally, applying level adjustments
   * only to the specified portion of the image up to the split point (defined by a percentage),
   * while leaving the remaining portion of the image unaltered.</p>
   *
   * @param snap       The `Snap` object representing the image to be adjusted.
   * @param black      The black level value, determining the darkest parts of the image.
   * @param mid        The mid level value, representing the midpoint or gray level.
   * @param white      The white level value, indicating the brightest parts of the image.
   * @param percentage (Optional) An integer percentage indicating where the split point lies
   * @return A new `Snap` object with the levels adjusted.
   */

  @Override
  public Snap levelsAdjust(Snap snap, int black, int mid, int white, int... percentage) {
    int width = snap.getSnapWidth();
    int height = snap.getSnapHeight();
    int[][][] originalPixels = snap.getSnap();
    int[][][] newPixels = new int[height][width][3];

    int splitPoint = percentage.length > 0 ? (width * percentage[0]) / 100 : width;

    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        if (j < splitPoint) {
          newPixels[i][j][0] = clamp(fittingProcess(black, mid, white, originalPixels[i][j][0]));
          newPixels[i][j][1] = clamp(fittingProcess(black, mid, white, originalPixels[i][j][1]));
          newPixels[i][j][2] = clamp(fittingProcess(black, mid, white, originalPixels[i][j][2]));
        } else {
          newPixels[i][j] = snap.getPixelValue(i, j);
        }
      }
    }

    return new SnapImpl(newPixels);
  }

  private int findPeak(int[] histogram) {
    int peakValue = 0;
    int peakPosition = 0;
    for (int i = 10; i <= 245; i++) {
      if (histogram[i] > peakValue) {
        peakValue = histogram[i];
        peakPosition = i;
      }
    }
    return peakPosition;
  }

  /**
   * Clamps a value to ensure it falls within the 0-255 range.
   *
   * @param value The integer value to clamp.
   * @return Clamped value.
   */

  private int clamp(int value) {
    return Math.max(0, Math.min(255, value));
  }


  /**
   * Applies a quadratic fitting process to adjust a pixel's intensity.
   * Based on specified black, mid, and white levels.
   *
   * <p>This method uses a quadratic equation to smoothly map an input intensity (`signal`)
   * to a new value
   * that aligns with the specified black, mid, and white levels. This adjustment enhances
   * the image's tonal range
   * by redistributing pixel intensities according to a quadratic transformation, which
   * helps in balancing highlights,
   * midtones, and shadows.</p>
   *
   * <p>The fitting process computes coefficients for a quadratic function using
   * the black, mid, and white level
   * values. These coefficients (`a`, `b`, and `c`) are calculated based on the given black,
   * mid, and white
   * levels and are used to transform the input signal.</p>
   *
   * @param black  The black level, defining the darkest intensity in the image,
   *               (typically between 0 and 255).
   * @param mid    The mid level, representing the midpoint or average intensity,
   *               (typically between 0 and 255).
   * @param white  The white level, defining the brightest intensity in the image,
   *               (typically between 0 and 255).
   * @param signal The current intensity of a pixel that needs to be adjusted.
   * @return The adjusted intensity value as an integer, after applying the transformation.
   */

  private int fittingProcess(int black, int mid, int white, int signal) {
    double calculateA = Math.pow(black, 2) * (mid - white) - black * (Math.pow(mid, 2)
            - Math.pow(white, 2))
            + white * Math.pow(mid, 2) - mid * Math.pow(white, 2);

    double calculateAa = -black * (128 - 255) + 128 * white - 255 * mid;

    double calculateAb = Math.pow(black, 2) * (128 - 255) + 255 * Math.pow(mid, 2) - 128
            * Math.pow(white, 2);

    double calculateAc = Math.pow(black, 2) * (255 * mid - 128 * white) - black
            * (255 * Math.pow(mid, 2) - 128 * Math.pow(white, 2));

    double a = calculateAa / calculateA;

    double b = calculateAb / calculateA;

    double c = calculateAc / calculateA;

    return (int) (a * Math.pow(signal, 2) + b * signal + c);
  }
}
