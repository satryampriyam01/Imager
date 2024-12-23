package model.filter;

import model.Snap;

/**
 * Interface representing a filter for manipulating images (Snap objects).
 * This interface defines methods for applying different types of filters
 * such as convolution filters, brightness adjustments, RGB splitting, and combining.
 */
public interface SnapFilter {

  /**
   * Applies a convolution filter to the given image (Snap).
   * The filter is a matrix that alters the pixel values of the image.
   *
   * @param snap   the Snap object representing the image to be filtered
   * @param filter a 2D array representing the convolution filter matrix
   * @return a new Snap object with the filter applied
   */
  Snap applyFilter(Snap snap, double[][] filter,Snap mask, int... percentage);

  /**
   * Adjusts the brightness or other scalar properties of the given image (Snap).
   * The adjustment is applied uniformly across all pixels.
   *
   * @param snap       the Snap object representing the image to be adjusted
   * @param adjustment an integer representing the amount of adjustment (e.g., brightness)
   * @return a new Snap object with the adjustment applied
   */
  Snap applyFilter(Snap snap, int adjustment);

  /**
   * Splits the given image (Snap) into a single color channel (red, green, or blue).
   * This method extracts the specified color component from the image.
   *
   * @return a new Snap object containing only the specified color component
   */
  Snap[] applyRGBSplit(Snap snap) throws IllegalArgumentException;

  /**
   * Combines three Snap objects, each representing a color channel (red, green, and blue),
   * into a single full-color image.
   *
   * @param redSnap   the Snap object containing the red color channel
   * @param greenSnap the Snap object containing the green color channel
   * @param blueSnap  the Snap object containing the blue color channel
   * @return a new Snap object representing the combined RGB image
   */
  Snap applyRGBCombine(Snap redSnap, Snap greenSnap, Snap blueSnap);

}