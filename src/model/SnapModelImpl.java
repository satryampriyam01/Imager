package model;

import model.effects.SnapEffectsImpl;
import model.filter.SnapFilterImpl;
import model.transform.SnapTransformationImpl;

/**
 * The SnapModelImpl class implements the SnapModel interface and manages
 * the loading and manipulation of Snap objects, including image processing
 * functionalities such as blurring, sharpening, and transformations.
 */
public class SnapModelImpl implements SnapModel {

  private Snap currentSnap;

  /**
   * Constructor for SnapModelImpl. Initializes the currentSnap with a 1x1 image (3 channels).
   */
  public SnapModelImpl() {
    this.currentSnap = new SnapImpl(new int[1][1][3]);
  }

  /**
   * Loads a Snap image into the current Snap model.
   *
   * @param snap The Snap object to load.
   * @throws IllegalArgumentException if the provided snap is null.
   */
  @Override
  public void loadSnap(Snap snap) throws IllegalArgumentException {
    if (snap == null) {
      throw new IllegalArgumentException("Snap cannot be null");
    }
    this.currentSnap = snap;
  }

  /**
   * Retrieves the current Snap image as a 3D array of integers representing pixel RGB values.
   *
   * @return A 3D array of integers representing the RGB values of the current Snap.
   */
  @Override
  public int[][][] getSnap() {
    int[][][] newCopy = new int[this.currentSnap.getSnapHeight()]
            [this.currentSnap.getSnapWidth()][3];
    for (int i = 0; i < this.currentSnap.getSnapHeight(); i++) {
      for (int j = 0; j < this.currentSnap.getSnapWidth(); j++) {
        newCopy[i][j] = this.currentSnap.getSnap()[i][j];
      }
    }
    return newCopy;
  }

  /**
   * Retrieves the width of the current Snap image.
   *
   * @return The width of the current Snap image.
   */
  @Override
  public int getSnapWidth() {
    return this.currentSnap.getSnapWidth();
  }

  /**
   * Retrieves the height of the current Snap image.
   *
   * @return The height of the current Snap image.
   */
  @Override
  public int getSnapHeight() {
    return this.currentSnap.getSnapHeight();
  }

  /**
   * Applies a Gaussian blur filter to the current Snap image with the specified intensity.
   *
   * @param percentage The intensity percentage of the blur to apply.
   * @throws IllegalArgumentException if the percentage is invalid or the Snap is null.
   */
  @Override
  public void blur(Snap mask, int... percentage) throws IllegalArgumentException {
    double[][] gaussianKernel = {
            {1 / 16.0, 1 / 8.0, 1 / 16.0},
            {1 / 8.0, 1 / 4.0, 1 / 8.0},
            {1 / 16.0, 1 / 8.0, 1 / 16.0}
    };

    this.currentSnap =
            new SnapFilterImpl().applyFilter(this.currentSnap, gaussianKernel, mask, percentage);
  }

  /**
   * Applies a sharpening filter to the current Snap image with the specified intensity.
   *
   * @param percentage The intensity percentage of the sharpening to apply.
   * @throws IllegalArgumentException if the percentage is invalid or the Snap is null.
   */
  @Override
  public void sharpen(Snap mask, int... percentage) throws IllegalArgumentException {
    double[][] sharpenFilter = {
            {-0.125, -0.125, -0.125, -0.125, -0.125},
            {-0.125, 0.25, 0.25, 0.25, -0.125},
            {-0.125, 0.25, 1, 0.25, -0.125},
            {-0.125, 0.25, 0.25, 0.25, -0.125},
            {-0.125, -0.125, -0.125, -0.125, -0.125}
    };
    this.currentSnap =
            new SnapFilterImpl().applyFilter(this.currentSnap, sharpenFilter, mask, percentage);
  }

  /**
   * Applies a sepia tone transformation to the current Snap image with the specified intensity.
   *
   * @param percentage The intensity percentage of the sepia effect to apply.
   * @throws IllegalArgumentException if the percentage is invalid or the Snap is null.
   */
  @Override
  public void toSepia(Snap mask, int... percentage) throws IllegalArgumentException {
    double[][] sepia = {{0.393, 0.769, 0.189}, {0.349, 0.686, 0.168}, {0.272, 0.534, 0.131}};
    this.currentSnap =
            new SnapTransformationImpl().applyTransformation(this.currentSnap,
                    sepia, mask, percentage);
  }

  /**
   * Applies a greyscale transformation to the current Snap image with the specified intensity.
   *
   * @param percentage The intensity percentage of the greyscale effect to apply.
   * @throws IllegalArgumentException if the percentage is invalid or the Snap is null.
   */
  @Override
  public void toGreyscale(Snap mask, int... percentage) throws IllegalArgumentException {
    double[][] grayscaleMatrix = {{0.2126, 0.7152, 0.0722}};
    this.currentSnap =
            new SnapTransformationImpl().applyTransformation(this.currentSnap,
                    grayscaleMatrix, mask, percentage);
  }

  /**
   * Adjusts the brightness of the current Snap image by the specified amount.
   *
   * @param adjustment The amount of brightness adjustment (positive or negative).
   * @throws IllegalArgumentException if the adjustment value is invalid or the Snap is null.
   */
  @Override
  public void brightenessAdjustment(int adjustment) throws IllegalArgumentException {
    this.currentSnap = new SnapFilterImpl().applyFilter(this.currentSnap, adjustment);
  }

  /**
   * Flips the current Snap image horizontally.
   *
   * @throws IllegalArgumentException if the Snap is null.
   */
  @Override
  public void horizontalFlip() throws IllegalArgumentException {
    this.currentSnap = new SnapTransformationImpl().applyHorizontal(this.currentSnap);
  }

  /**
   * Flips the current Snap image vertically.
   *
   * @throws IllegalArgumentException if the Snap is null.
   */
  @Override
  public void verticalFlip() throws IllegalArgumentException {
    this.currentSnap = new SnapTransformationImpl().applyVertical(this.currentSnap);
  }

  /**
   * Extracts and isolates the "value" component (brightness) of the current Snap image.
   *
   * @throws IllegalArgumentException if the Snap is null.
   */
  @Override
  public void valueComponent(Snap mask) throws IllegalArgumentException {
    this.currentSnap =
            new SnapTransformationImpl().extractColorComponent(this.currentSnap,
                    "value", mask);
  }

  /**
   * Splits the current Snap image into its RGB components and returns them as separate Snap images.
   *
   * @return An array of Snap images, each containing one of the RGB color components.
   * @throws IllegalArgumentException if the Snap is null.
   */
  @Override
  public Snap[] applyRGBSplit() throws IllegalArgumentException {
    Snap[] rgbSnaps = new SnapFilterImpl().applyRGBSplit(this.currentSnap);
    return rgbSnaps;
  }


  /**
   * Combines the separate red, green, and blue Snap images into a single RGB image.
   *
   * @param red   The Snap image containing the red component.
   * @param green The Snap image containing the green component.
   * @param blue  The Snap image containing the blue component.
   * @throws IllegalArgumentException if any of the input Snap objects are null or invalid.
   */
  @Override
  public void applyRGBCombine(Snap red, Snap green, Snap blue) throws IllegalArgumentException {
    this.currentSnap = new SnapFilterImpl().applyRGBCombine(red, green, blue);
  }

  /**
   * Retrieves the red component of the current Snap.
   *
   * @throws IllegalArgumentException if no Snap is loaded
   */
  @Override
  public void redComponent(Snap mask) throws IllegalArgumentException {

    this.currentSnap =
            new SnapTransformationImpl().extractColorComponent(this.currentSnap,
                    "red", mask);
  }

  /**
   * Retrieves the green component of the current Snap.
   *
   * @throws IllegalArgumentException if no Snap is loaded
   */
  @Override
  public void greenComponent(Snap mask) throws IllegalArgumentException {

    this.currentSnap =
            new SnapTransformationImpl().extractColorComponent(this.currentSnap,
                    "green", mask);
  }

  /**
   * Retrieves the blue component of the current Snap.
   *
   * @throws IllegalArgumentException if no Snap is loaded
   */
  @Override
  public void blueComponent(Snap mask) throws IllegalArgumentException {
    this.currentSnap =
            new SnapTransformationImpl().extractColorComponent(this.currentSnap,
                    "blue", mask);
  }

  /**
   * Retrieves the luma component of the current Snap.
   *
   * @throws IllegalArgumentException if no Snap is loaded
   */
  @Override
  public void lumaComponent(Snap mask) throws IllegalArgumentException {

    this.currentSnap =
            new SnapTransformationImpl().extractColorComponent(this.currentSnap,
                    "luma", mask);
  }

  /**
   * Retrieves the intensity component of the current Snap.
   *
   * @throws IllegalArgumentException if no Snap is loaded
   */
  @Override
  public void intensityComponent(Snap mask) throws IllegalArgumentException {
    this.currentSnap =
            new SnapTransformationImpl().extractColorComponent(this.currentSnap,
                    "intensity", mask);
  }

  /**
   * Compresses the current Snap image using a specified compression value.
   *
   * @param value The compression value indicating the degree of compression to apply.
   * @throws IllegalArgumentException if the value is invalid or if the Snap is null.
   */
  @Override
  public void compressionComponent(double value) throws IllegalArgumentException {
    this.currentSnap = new SnapEffectsImpl().compress(this.currentSnap, value);
  }

  /**
   * Applies color correction to the current Snap image. Adjusts the color balance
   * based on the specified percentage, which can help align histogram peaks for
   * enhanced visual consistency.
   *
   * @param percentage Optional parameter specifying the intensity of the color correction.
   * @throws IllegalArgumentException if the percentage is invalid or if the Snap is null.
   */
  @Override
  public void colorCorrectionComponent(int... percentage) throws IllegalArgumentException {
    this.currentSnap = new SnapEffectsImpl().colorCorrection(this.currentSnap, percentage);
  }

  /**
   * Generates a histogram for the current Snap image, showing the distribution of
   * colors or intensity values within the image.
   */
  @Override
  public void histogramComponent() {
    this.currentSnap = new SnapEffectsImpl().histogram(this.currentSnap);
  }

  /**
   * Adjusts the levels of the current Snap image, including black, mid, and white levels.
   * Optionally, an intensity percentage can be specified to fine-tune the adjustment.
   *
   * @param black      The black level adjustment (0-255).
   * @param mid        The mid level adjustment (0-255).
   * @param white      The white level adjustment (0-255).
   * @param percentage Optional parameter to adjust the intensity of level adjustment.
   * @throws IllegalArgumentException if any level or percentage is invalid or if the Snap is null.
   */
  @Override
  public void levelAdjustment(int black, int mid, int white, int... percentage)
          throws IllegalArgumentException {
    this.currentSnap = new SnapEffectsImpl().levelsAdjust(this.currentSnap, black, mid,
            white, percentage);
  }

  /**
   * Resizes the component to the specified width and height.
   *
   * @param width  the new width of the component. Must be greater than 0.
   * @param height the new height of the component. Must be greater than 0.
   * @throws IllegalArgumentException if the width or height is less than or equal to 0.
   */
  @Override
  public void downSizeComponent(int width, int height) throws IllegalArgumentException {
    this.currentSnap = new SnapTransformationImpl().downsizeImage(this.currentSnap, width, height);
  }

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
  @Override
  public void createMaskComponent(int xStart, int yStart, int width, int height)
          throws IllegalArgumentException {
    this.currentSnap = new SnapTransformationImpl().createMask(this.currentSnap, xStart,
            yStart, width, height);
  }
}
