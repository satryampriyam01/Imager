package model;

/**
 * This interface defines the structure for a Snap image, representing
 * an image snapshot with pixel data and dimension retrieval capabilities.
 * It provides methods to access the pixel data, dimensions, and specific
 * pixel values within the image.
 */
public interface Snap {

  /**
   * Retrieves the pixel data of the Snap image.
   *
   * @return a 3D array representing the pixel colors in RGB format.
   */
  int[][][] getSnap();

  /**
   * Gets the height of the Snap image.
   *
   * @return the height of the image in pixels.
   */
  int getSnapHeight();

  /**
   * Gets the width of the Snap image.
   *
   * @return the width of the image in pixels.
   */
  int getSnapWidth();

  /**
   * Retrieves the RGB values of a specific pixel at the given coordinates.
   *
   * @param x the x-coordinate of the pixel.
   * @param y the y-coordinate of the pixel.
   * @return an array of integers representing the RGB values of the pixel
   * @throws IllegalArgumentException if the coordinates are out of the bounds.
   */
  int[] getPixelValue(int x, int y);
}
