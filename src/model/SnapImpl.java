package model;

import java.io.IOException;

import controller.SnapUtil;

/**
 * The SnapImpl class implements the Snap interface and provides functionality
 * to manage and access the pixel data of an image. This class can be initialized
 * either by reading from a file or from a 3D array of pixel data.
 */
public class SnapImpl implements Snap {

  // The 3D array representing the RGB pixel data of the image.
  private final int[][][] snap;
  // The height of the image in pixels.
  private final int height;
  // The width of the image in pixels.
  private final int width;

  /**
   * Constructs a SnapImpl object by reading pixel data from an image file.
   * The SnapUtil class is used to extract pixel data and the image's dimensions.
   *
   * @param snapName the name of the file containing the image data.
   * @throws IOException if an error occurs while reading the image file.
   */
  public SnapImpl(String snapName) throws IOException {
    try {
      this.snap = controller.SnapUtil.readSnap(snapName);
      this.width = controller.SnapUtil.getWidth(snapName);
      this.height = SnapUtil.getHeight(snapName);
    } catch (Exception e) {
      throw new RuntimeException("Error reading snap " + snapName, e);
    }
  }

  /**
   * Constructs a SnapImpl object using a provided 3D array of pixel data.
   * The pixel data is represented in RGB format, where the first dimension
   * corresponds to the image's height, the second dimension to its width,
   * and the third dimension contains the RGB values for each pixel.
   *
   * @param snap a 3D array representing the pixel colors in RGB format.
   */
  public SnapImpl(int[][][] snap) {
    if (snap == null || snap.length == 0 || snap[0].length == 0 || snap[0][0].length != 3) {
      throw new IllegalArgumentException("Invalid pixel data provided");
    }
    this.snap = snap;
    this.width = snap[0].length;
    this.height = snap.length;
  }

  /**
   * Returns the 3D array of RGB pixel data representing the image.
   *
   * @return the 3D array containing the pixel data.
   */
  @Override
  public int[][][] getSnap() {
    return this.snap;
  }

  /**
   * Returns the height of the image in pixels.
   *
   * @return the height of the image.
   */
  @Override
  public int getSnapHeight() {
    return this.height;
  }

  /**
   * Returns the width of the image in pixels.
   *
   * @return the width of the image.
   */
  @Override
  public int getSnapWidth() {
    return this.width;
  }

  @Override
  public int[] getPixelValue(int x, int y) {
    if (x < 0 || x >= height || y < 0 || y >= width) {
      throw new ArrayIndexOutOfBoundsException("Coordinates out of bounds");
    }
    return this.snap[x][y];
  }
}
