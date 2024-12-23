package model.filter;

import model.Snap;
import model.SnapImpl;

/**
 * This class implements the SnapFilter interface and provides methods for applying various filters
 * to a given snap (image). The snap is represented as a 3D array of integer values, where each
 * index corresponds to a pixel in the snap.
 */
public class SnapFilterImpl implements SnapFilter {

  /**
   * Applies a filter, such as blur or sharpen, to the given snap. The method uses helper functions
   * to extract a kernel around each pixel and apply the filter to that kernel.
   *
   * @param snap   the snap to which the filter will be applied.
   * @param filter the matrix representing the filter to be applied (e.g., blur or sharpen).
   * @return a new Snap object with the filtered pixels.
   * @throws IllegalArgumentException if the filter does not have odd dimensions.
   */
  @Override
  public Snap applyFilter(Snap snap, double[][] filter, Snap mask, int... percentage)
          throws IllegalArgumentException {
    if (filter.length % 2 == 0) {
      throw new IllegalArgumentException("Error: given filter must have odd dimensions");
    }
    if (filter.length != filter[0].length) {
      throw new IllegalArgumentException("Error: given filter must have equal width and height");
    }

    int width = snap.getSnapWidth();
    int height = snap.getSnapHeight();
    int[][][] newSnap = new int[height][width][3];

    int[][][] maskData = mask != null ? mask.getSnap() : null;

    // Determine split point if percentage is provided, otherwise apply to the entire image
    int splitPoint = percentage.length > 0 ? (width * percentage[0]) / 100 : width;

    // Apply the filter to each pixel
    for (int row = 0; row < height; row++) {
      for (int column = 0; column < width; column++) {
        boolean withinSplit = column < splitPoint;
        boolean applyToPixel = maskData == null || maskData[row][column][0] == 0;

        if (withinSplit && applyToPixel) {
          // Apply filter to the pixel
          newSnap[row][column] =
                  createNewPixel(getKernel(row, column, snap.getSnap(), filter.length), filter);
        } else {
          newSnap[row][column] = snap.getPixelValue(row, column);
        }
      }
    }

    return new SnapImpl(clamp(newSnap));
  }

  /**
   * Adjusts the brightness of the given snap by applying an adjustment value to each pixel.
   *
   * @param snap       the snap to adjust.
   * @param adjustment the adjustment value (positive for brightening, negative for darkening).
   * @return a new Snap object with adjusted brightness.
   */
  @Override
  public Snap applyFilter(Snap snap, int adjustment) {
    int width = snap.getSnapWidth();
    int height = snap.getSnapHeight();
    int[][][] newSnap = new int[height][width][3];

    // Adjust brightness for each pixel
    for (int row = 0; row < height; row++) {
      for (int column = 0; column < width; column++) {
        int[] originalPixel = snap.getSnap()[row][column];
        newSnap[row][column] = adjustBrightness(originalPixel, adjustment);
      }
    }
    return new SnapImpl(clamp(newSnap));
  }

  /**
   * Splits the snap into three separate color channels: red, green, and blue.
   *
   * @param snap the snap to be split.
   * @return an array of Snap objects, each representing one color channel.
   * @throws IllegalArgumentException if the snap object is invalid.
   */
  @Override
  public Snap[] applyRGBSplit(Snap snap) throws IllegalArgumentException {
    int width = snap.getSnapWidth();
    int height = snap.getSnapHeight();
    int[][][] originalPixels = snap.getSnap();

    int[][][] redChannel = new int[height][width][3];
    int[][][] greenChannel = new int[height][width][3];
    int[][][] blueChannel = new int[height][width][3];

    // Split each pixel's color channels
    for (int row = 0; row < height; row++) {
      for (int column = 0; column < width; column++) {
        int redValue = originalPixels[row][column][0];
        int greenValue = originalPixels[row][column][1];
        int blueValue = originalPixels[row][column][2];

        redChannel[row][column][0] = redValue;
        greenChannel[row][column][1] = greenValue;
        blueChannel[row][column][2] = blueValue;
      }
    }

    Snap redSnap = new SnapImpl(redChannel);
    Snap greenSnap = new SnapImpl(greenChannel);
    Snap blueSnap = new SnapImpl(blueChannel);

    return new Snap[]{redSnap, greenSnap, blueSnap};
  }

  /**
   * Combines three separate Snap objects, each representing a color channel (red, green, and blue),
   * into a single snap by adding the color values together.
   *
   * @param redSnap   the Snap containing the red channel.
   * @param greenSnap the Snap containing the green channel.
   * @param blueSnap  the Snap containing the blue channel.
   * @return a new Snap object combining the RGB channels.
   */
  @Override
  public Snap applyRGBCombine(Snap redSnap, Snap greenSnap, Snap blueSnap) {
    int width = redSnap.getSnapWidth();
    int height = redSnap.getSnapHeight();
    int[][][] combinedImage = new int[height][width][3];

    // Combine the RGB channels
    for (int row = 0; row < height; row++) {
      for (int column = 0; column < width; column++) {
        combinedImage[row][column][0] =
                redSnap.getSnap()[row][column][0] + greenSnap.getSnap()[row][column][0]
                        + blueSnap.getSnap()[row][column][0];
        combinedImage[row][column][1] =
                redSnap.getSnap()[row][column][1] + greenSnap.getSnap()[row][column][1]
                        + blueSnap.getSnap()[row][column][1];
        combinedImage[row][column][2] =
                redSnap.getSnap()[row][column][2] + greenSnap.getSnap()[row][column][2]
                        + blueSnap.getSnap()[row][column][2];
      }
    }

    return new SnapImpl(combinedImage);
  }

  /**
   * Helper method to create a new pixel by applying a filter matrix to a kernel of pixels.
   *
   * @param kernel the kernel of neighboring pixels.
   * @param filter the filter matrix.
   * @return a new pixel represented by an array of red, green, and blue values.
   */
  private int[] createNewPixel(int[][][] kernel, double[][] filter) {
    int[] newPixel = new int[3];
    for (int i = 0; i < kernel.length; i++) {
      for (int j = 0; j < kernel.length; j++) {
        newPixel[0] += (int) Math.round((kernel[i][j][0] * filter[i][j]));
        newPixel[1] += (int) Math.round((kernel[i][j][1] * filter[i][j]));
        newPixel[2] += (int) Math.round((kernel[i][j][2] * filter[i][j]));
      }
    }
    return newPixel;
  }

  /**
   * Retrieves a kernel of pixels centered around the specified row and column in the snap.
   *
   * @param row    the row of the center pixel.
   * @param column the column of the center pixel.
   * @param snap   the snap from which the kernel will be extracted.
   * @param size   the size of the kernel.
   * @return a kernel of pixels as a 3D array.
   */
  private int[][][] getKernel(int row, int column, int[][][] snap, int size) {
    int[][][] kernel = new int[size][size][3];
    int bounds = (int) Math.floor(size / 2.0);
    for (int i = 0; i < size; i++) {
      for (int j = 0; j < size; j++) {
        try {
          kernel[i][j][0] = snap[i + (row - bounds)][j + (column - bounds)][0];
          kernel[i][j][1] = snap[i + (row - bounds)][j + (column - bounds)][1];
          kernel[i][j][2] = snap[i + (row - bounds)][j + (column - bounds)][2];
        } catch (IndexOutOfBoundsException e) {
          kernel[i][j][0] = 0;
          kernel[i][j][1] = 0;
          kernel[i][j][2] = 0;
        }
      }
    }
    return kernel;
  }

  /**
   * Clamps the values of the pixels in the snap to ensure they are within the valid range (0-255).
   *
   * @param snap the 3D array of pixels to be clamped.
   * @return a new 3D array of clamped pixel values.
   */
  private int[][][] clamp(int[][][] snap) {
    int min = 0;
    int max = 255;
    for (int i = 0; i < snap.length; i++) {
      for (int j = 0; j < snap[0].length; j++) {
        for (int k = 0; k < 3; k++) {
          snap[i][j][k] = Math.max(min, Math.min(max, snap[i][j][k]));
        }
      }
    }
    return snap;
  }

  /**
   * Adjusts the brightness of a pixel by adding the adjustment value to the pixel's color values.
   *
   * @param pixel      the original pixel array (red, green, blue values).
   * @param adjustment the adjustment value.
   * @return the adjusted pixel.
   */
  private int[] adjustBrightness(int[] pixel, int adjustment) {
    int[] newPixel = new int[3];
    newPixel[0] = pixel[0] + adjustment;
    newPixel[1] = pixel[1] + adjustment;
    newPixel[2] = pixel[2] + adjustment;
    return newPixel;
  }
}
