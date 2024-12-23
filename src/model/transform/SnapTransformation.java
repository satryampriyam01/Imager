package model.transform;

import model.Snap;

/**
 * The SnapTransformation interface defines a contract for applying various transformations to
 * a Snap image, including matrix-based transformations, axis flips, and color extraction.
 * Implementing classes are expected to define the specific logic for each transformation type.
 */
public interface SnapTransformation {

  /**
   * Applies a matrix-based transformation to the given Snap image, such as rotation, scaling,
   * or custom transformations defined by the provided transformation matrix.
   *
   * @param snap       The Snap image to which the transformation will be applied.
   * @param matrix     A 2D array representing the transformation matrix that determines the nature
   *                   of the transformation.
   * @param percentage Optional additional parameters such as a percentage value to
   *                   adjust the intensity
   *                   of the transformation if applicable.
   * @return A new Snap instance with the specified transformation applied.
   */
  Snap applyTransformation(Snap snap, double[][] matrix,Snap mask, int... percentage);

  /**
   * Flips the given Snap image along its vertical axis, effectively creating a mirror image
   * across the vertical line. This method transforms the Snap instance such that the left
   * and right halves are swapped.
   *
   * @param snap The Snap image to be vertically flipped.
   * @return A new Snap instance representing the vertically flipped image.
   */
  Snap applyVertical(Snap snap);

  /**
   * Flips the given Snap image along its horizontal axis, creating a mirror image across the
   * horizontal line. This transformation swaps the top and bottom halves of the Snap instance.
   *
   * @param snap The Snap image to be horizontally flipped.
   * @return A new Snap instance representing the horizontally flipped image.
   */
  Snap applyHorizontal(Snap snap);

  /**
   * Extracts a specified color component (e.g., "red," "green," or "blue") from the given
   * Snap image. Only the intensity of the specified color component will be retained, while
   * others are set to zero, allowing for isolated analysis or manipulation of individual colors.
   *
   * @param snap      The Snap image from which the color component will be extracted.
   * @param component A string indicating the color component to extract.
   * @return A new Snap instance where only the specified color component is retained.
   * @throws IllegalArgumentException if the specified component is invalid or unsupported.
   */
  Snap extractColorComponent(Snap snap, String component,Snap mask) throws IllegalArgumentException;


  Snap downsizeImage(Snap snap, int newWidth, int newHeight);

  Snap createMask(Snap snap,int xStart, int yStart, int width, int height);

}
