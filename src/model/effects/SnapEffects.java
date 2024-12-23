package model.effects;

import model.Snap;

/**
 * Interface representing various image processing effects that can be applied to a Snap object.
 * Provides methods to perform operations such as compression, histogram generation,
 * color correction, and level adjustment on image data encapsulated within a Snap instance.
 */
public interface SnapEffects {

  /**
   * Compresses the given Snap image using a specified compression value.
   *
   * @param snap  the Snap object containing the image data to be compressed
   * @param value the compression value to be applied; should be a positive double
   * @return a new Snap object with the compressed image data
   * @throws IllegalArgumentException if the value is not valid (e.g., negative)
   */
  Snap compress(Snap snap, double value); // Redesign logic

  /**
   * Generates a histogram from the given Snap image.
   *
   * @param snap the Snap object containing the image data to generate a histogram from
   * @return a new Snap object representing the histogram of the original image
   */
  Snap histogram(Snap snap); // fully working

  /**
   * Applies color correction to the given Snap image based on specified percentages.
   *
   * @param snap       the Snap object containing the image data to be color corrected
   * @param percentage variable-length arguments representing the percentage for color correction
   * @return a new Snap object with the color-corrected image data
   */
  Snap colorCorrection(Snap snap, int... percentage); // fully working

  /**
   * Adjusts the levels of the given Snap image based on specified black, mid, and white values.
   *
   * @param snap       the Snap object containing the image data to be adjusted
   * @param black      the black level to be set
   * @param mid        the mid level to be set
   * @param white      the white level to be set
   * @param percentage variable-length arguments representing optional percentages for adjustment
   * @return a new Snap object with the adjusted image data
   * @throws IllegalArgumentException if the snap is null or if the levels are invalid
   */
  Snap levelsAdjust(Snap snap, int black, int mid, int white, int... percentage); // fully working
}
