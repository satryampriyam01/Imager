package model;

/**
 * This interface defines the operations available for manipulating
 * image snapshots in the SnapModel. It provides methods for retrieving
 * image data, applying various transformations, and managing image states.
 */
public interface SnapModel {

  /**
   * Retrieves the pixel data of the current snapshot.
   *
   * @return a 3D array representing the RGB values of the snapshot.
   */
  int[][][] getSnap();


  /**
   * Retrieves the width of the current snapshot.
   *
   * @return the width of the snapshot in pixels.
   */
  int getSnapWidth();

  /**
   * Retrieves the height of the current snapshot.
   *
   * @return the height of the snapshot in pixels.
   */
  int getSnapHeight();


  /**
   * Applies a blur effect to the current snapshot.
   *
   * @param percentage an optional parameter representing the intensity of the blur effect.
   * @throws IllegalArgumentException if the current snapshot is invalid or cannot be processed.
   */
  void blur(Snap mask,int... percentage) throws IllegalArgumentException;

  /**
   * Applies a sharpen effect to the current snapshot.
   *
   * @param percentage an optional parameter representing the intensity of the sharpen effect.
   * @throws IllegalArgumentException if the current snapshot is invalid or cannot be processed.
   */
  void sharpen(Snap mask,int... percentage) throws IllegalArgumentException;

  /**
   * Converts the current snapshot to a sepia tone.
   *
   * @param percentage an optional parameter representing the intensity of the sepia effect.
   * @throws IllegalArgumentException if the current snapshot is invalid or cannot be processed.
   */
  void toSepia(Snap mask,int... percentage) throws IllegalArgumentException;

  /**
   * Converts the current snapshot to greyscale.
   *
   * @param percentage an optional parameter representing the intensity of the greyscale effect.
   * @throws IllegalArgumentException if the current snapshot is invalid or cannot be processed.
   */
  void toGreyscale(Snap mask,int... percentage) throws IllegalArgumentException;

  /**
   * Adjusts the brightness of the current snapshot.
   *
   * @param adjustment the amount to adjust brightness; positive values increase brightness,
   *                   negative values decrease it.
   * @throws IllegalArgumentException if the current snapshot is invalid or cannot be processed.
   */
  void brightenessAdjustment(int adjustment) throws IllegalArgumentException;

  /**
   * Flips the current snapshot horizontally.
   *
   * @throws IllegalArgumentException if the current snapshot is invalid or cannot be processed.
   */
  void horizontalFlip() throws IllegalArgumentException;

  /**
   * Retrieves the value component of the current snapshot.
   *
   * @throws IllegalArgumentException if the current snapshot is invalid or cannot be processed.
   */
  void valueComponent(Snap mask) throws IllegalArgumentException;

  /**
   * Flips the current snapshot vertically.
   *
   * @throws IllegalArgumentException if the current snapshot is invalid or cannot be processed.
   */
  void verticalFlip() throws IllegalArgumentException;

  /**
   * Applies an RGB split transformation to the current snapshot,
   * generating separate images for the red, green, and blue components.
   *
   * @return an array of Snap objects representing the red, green, and blue components of the image.
   * @throws IllegalArgumentException if the current snapshot is invalid or cannot be processed.
   */
  Snap[] applyRGBSplit() throws IllegalArgumentException;

  /**
   * Combines three snapshots (representing the red, green, and blue channels)
   * into a single image.
   *
   * @param red   the snapshot containing red channel data.
   * @param green the snapshot containing green channel data.
   * @param blue  the snapshot containing blue channel data.
   * @throws IllegalArgumentException if any of the snapshots are invalid or cannot be processed.
   */
  void applyRGBCombine(Snap red, Snap green, Snap blue) throws IllegalArgumentException;

  /**
   * Loads a new snapshot into the model, replacing the current one.
   *
   * @param snap the snapshot to be loaded.
   * @throws IllegalArgumentException if the provided snapshot is invalid or cannot be processed.
   */
  void loadSnap(Snap snap) throws IllegalArgumentException;

  /**
   * Retrieves the red component of the current snapshot.
   *
   * @throws IllegalArgumentException if the current snapshot is invalid or cannot be processed.
   */
  void redComponent(Snap mask) throws IllegalArgumentException;

  /**
   * Retrieves the green component of the current snapshot.
   *
   * @throws IllegalArgumentException if the current snapshot is invalid or cannot be processed.
   */
  void greenComponent(Snap mask) throws IllegalArgumentException;

  /**
   * Retrieves the blue component of the current snapshot.
   *
   * @throws IllegalArgumentException if the current snapshot is invalid or cannot be processed.
   */
  void blueComponent(Snap mask) throws IllegalArgumentException;

  /**
   * Retrieves the luma component of the current snapshot.
   *
   * @throws IllegalArgumentException if the current snapshot is invalid or cannot be processed.
   */
  void lumaComponent(Snap mask) throws IllegalArgumentException;

  /**
   * Retrieves the intensity component of the current snapshot.
   *
   * @throws IllegalArgumentException if the current snapshot is invalid or cannot be processed.
   */
  void intensityComponent(Snap mask) throws IllegalArgumentException;

  /**
   * Compresses the image to reduce data size using a specified compression ratio.
   *
   * @param value the compression ratio, with values between 0 and 1.
   * @throws IllegalArgumentException if the value is invalid or the snapshot cannot be processed.
   */
  void compressionComponent(double value) throws IllegalArgumentException;

  /**
   * Adjusts the color correction of the image, optionally using percentage for intensity.
   *
   * @param percentage optional intensity of the color correction.
   * @throws IllegalArgumentException if the snapshot is invalid or cannot be processed.
   */
  void colorCorrectionComponent(int... percentage) throws IllegalArgumentException;

  /**
   * Generates a histogram for the red, green, and blue channels in the image.
   *
   * @throws IllegalArgumentException if the snapshot is invalid or cannot be processed.
   */
  void histogramComponent() throws IllegalArgumentException;

  /**
   * Adjusts the black, mid, and white levels of the image for improved brightness and contrast.
   *
   * @param black      the black level adjustment (0-255).
   * @param mid        the midtone level adjustment (0-255).
   * @param white      the white level adjustment (0-255).
   * @param percentage optional intensity of the level adjustment.
   * @throws IllegalArgumentException if the values are out of range or the snapshot is invalid.
   */
  void levelAdjustment(int black, int mid, int white, int... percentage)
          throws IllegalArgumentException;

  /**
   * Resizes the component to the specified width and height.
   *
   * @param width  the new width of the component. Must be greater than 0.
   * @param height the new height of the component. Must be greater than 0.
   * @throws IllegalArgumentException if the width or height is less than or equal to 0.
   */
  void downSizeComponent(int width, int height) throws IllegalArgumentException;

  /**
   * Creates a mask for the component using the specified starting coordinates, width, and height.
   *
   * @param xStart the starting x-coordinate for the mask. Must be a non-negative integer.
   * @param yStart the starting y-coordinate for the mask. Must be a non-negative integer.
   * @param width  the width of the mask. Must be greater than 0.
   * @param height the height of the mask. Must be greater than 0.
   * @throws IllegalArgumentException if xStart or yStart is negative, or if width or height
   *                                  is less than or equal to 0.
   */
  void createMaskComponent(int xStart, int yStart,
                           int width, int height) throws IllegalArgumentException;


}

