package model.transform;

import model.Snap;
import model.SnapImpl;

/**
 * This class implements the SnapTransformation interface, providing methods to apply
 * transformations such as matrix operations, vertical flips, horizontal flips, and value
 * adjustments to Snap images.
 */
public class SnapTransformationImpl implements SnapTransformation {

  /**
   * Applies a transformation to the given Snap image using a specified transformation matrix.
   * The transformation can either be a color matrix transformation or a greyscale conversion.
   *
   * @param image  the Snap image to which the transformation will be applied.
   * @param matrix the transformation matrix, where each row defines how to transform components.
   * @param mask   an optional mask Snap, where black pixels (0,0,0) indicate the areas to extract.
   * @return a new Snap instance with the transformation applied.
   */
  @Override
  public Snap applyTransformation(Snap image, double[][] matrix, Snap mask, int... percentage) {
    int width = image.getSnapWidth();
    int height = image.getSnapHeight();

    int[][][] newImage = new int[height][width][3];

    int[][][] maskData = mask != null ? mask.getSnap() : null;

    // Determine split point if percentage is provided, otherwise apply to the entire image
    int splitPoint = percentage.length > 0 ? (width * percentage[0]) / 100 : width;

    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        if (j < splitPoint && (maskData == null || maskData[i][j][0] == 0)) {
          // Apply transformation only to the left part (up to the split point)
          if (matrix.length == 1) {
            // Greyscale transformation using a single matrix row.
            int grayValue = (int) Math.round(
                    matrix[0][0] * image.getSnap()[i][j][0] +  // Red
                            matrix[0][1] * image.getSnap()[i][j][1] +  // Green
                            matrix[0][2] * image.getSnap()[i][j][2]    // Blue
            );
            newImage[i][j][0] = grayValue;
            newImage[i][j][1] = clamp(grayValue);
            newImage[i][j][2] = clamp(grayValue);
          } else {
            // Color transformation using the provided matrix.
            for (int k = 0; k < 3; k++) {
              newImage[i][j][k] =
                      clamp((int) Math.round(transform(image.getSnap()[i][j], matrix[k])));
            }
          }
        } else {
          // Keep the right part of the image unchanged
          newImage[i][j] = image.getPixelValue(i, j);
        }
      }
    }
    return new SnapImpl(newImage);
  }

  /**
   * Applies a vertical flip to the given Snap image, transforming it along the vertical axis.
   *
   * @param snap the Snap image to be transformed.
   * @return a new Snap instance with the vertical flip applied.
   */
  @Override
  public Snap applyVertical(Snap snap) {
    int[][][] snapData = snap.getSnap();
    int height = snap.getSnapHeight();
    int width = snap.getSnapWidth();

    int[][][] flippedSnap = new int[height][width][3];

    // Perform the vertical flip by swapping rows.
    for (int i = 0; i < height; i++) {
      flippedSnap[height - 1 - i] = snapData[i];
    }

    return new SnapImpl(flippedSnap);
  }

  /**
   * Applies a horizontal flip to the given Snap image, transforming it along the horizontal axis.
   *
   * @param snap the Snap image to be transformed.
   * @return a new Snap instance with the horizontal flip applied.
   */
  @Override
  public Snap applyHorizontal(Snap snap) {
    int[][][] snapData = snap.getSnap();
    int height = snap.getSnapHeight();
    int width = snap.getSnapWidth();

    int[][][] flippedSnap = new int[height][width][3];

    // Perform the horizontal flip by swapping columns within each row.
    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        flippedSnap[i][width - 1 - j] = snapData[i][j];
      }
    }

    return new SnapImpl(flippedSnap);
  }

  /**
   * Transforms a pixel's color values using a specified filter matrix row.
   *
   * @param pixel  an array representing the RGB values of the pixel.
   * @param filter an array representing the filter to apply to the pixel.
   * @return the transformed pixel value as a double.
   */
  private double transform(int[] pixel, double[] filter) {
    double newPixel = 0;
    for (int i = 0; i < pixel.length; i++) {
      newPixel += pixel[i] * filter[i];
    }
    return newPixel;
  }

  /**
   * Ensures that the pixel color value is within the valid RGB range (0-255).
   *
   * @param pixel the color value to be clamped.
   * @return the clamped value, ensuring it is between 0 and 255.
   */
  private int clamp(int pixel) {
    return Math.max(0, Math.min(255, pixel));
  }

  /**
   * Extracts the specified color component from the given Snap image.
   *
   * @param snap      the Snap image to extract the component from.
   * @param component the component to extract
   * @param mask      an optional mask Snap, where black pixels (0,0,0)
   * @return a new Snap instance containing only the specified component.
   * @throws IllegalArgumentException if an unsupported component is requested.
   */
  public Snap extractColorComponent(Snap snap, String component, Snap mask)
          throws IllegalArgumentException {
    switch (component.toLowerCase()) {
      case "red":
        return extractRedComponent(snap, mask);
      case "green":
        return extractGreenComponent(snap, mask);
      case "blue":
        return extractBlueComponent(snap, mask);
      case "luma":
        return extractLumaComponent(snap, mask);
      case "intensity":
        return extractIntensityComponent(snap, mask);
      case "value":
        return extractValueComponent(snap, mask);
      default:
        throw new IllegalArgumentException("Unsupported component: " + component);
    }
  }

  /**
   * Extracts the red component from the given Snap image.
   *
   * @param snap the Snap image to extract the red component from.
   * @param mask an optional mask Snap, where black pixels (0,0,0) indicate the areas to extract.
   * @return a new Snap instance containing only the red component in greyscale.
   */
  private Snap extractRedComponent(Snap snap, Snap mask) throws IllegalArgumentException {
    return extractSingleColorComponent(snap, 0, mask); // 0 for red channel
  }

  /**
   * Extracts the green component from the given Snap image.
   *
   * @param snap the Snap image to extract the green component from.
   * @param mask an optional mask Snap, where black pixels (0,0,0) indicate the areas to extract.
   * @return a new Snap instance containing only the green component in greyscale.
   */
  private Snap extractGreenComponent(Snap snap, Snap mask) throws IllegalArgumentException {
    return extractSingleColorComponent(snap, 1, mask); // 1 for green channel
  }

  /**
   * Extracts the blue component from the given Snap image.
   *
   * @param snap the Snap image to extract the blue component from.
   * @param mask an optional mask Snap, where black pixels (0,0,0) indicate the areas to extract.
   * @return a new Snap instance containing only the blue component in greyscale.
   */
  private Snap extractBlueComponent(Snap snap, Snap mask) throws IllegalArgumentException {
    return extractSingleColorComponent(snap, 2, mask); // 2 for blue channel
  }

  /**
   * Extracts a single color component (red, green, or blue) from the Snap image.
   *
   * @param snap    the Snap image to extract from.
   * @param channel the index of the color channel to extract (0 for red, 1 for green, 2 for blue).
   * @param mask    an optional mask Snap, where black pixels (0,0,0) indicate the areas to extract.
   * @return a new Snap instance with only the specified color channel.
   */
  private Snap extractSingleColorComponent(Snap snap, int channel, Snap mask)
          throws IllegalArgumentException {
    int[][][] snapData = snap.getSnap();
    int height = snap.getSnapHeight();
    int width = snap.getSnapWidth();
    int[][][] colorSnap = new int[height][width][3];

    int[][][] maskData = mask != null ? mask.getSnap() : null; // Fix: Add missing semicolon

    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        if (maskData == null || maskData[i][j][0] == 0) {
          // Apply operation for all pixels (no mask) or only for black pixels (with mask)
          colorSnap[i][j][0] = (channel == 0) ? snapData[i][j][0] : 0; // Red
          colorSnap[i][j][1] = (channel == 1) ? snapData[i][j][1] : 0; // Green
          colorSnap[i][j][2] = (channel == 2) ? snapData[i][j][2] : 0; // Blue
        } else {
          colorSnap[i][j][0] = snapData[i][j][0];
          colorSnap[i][j][1] = snapData[i][j][1];
          colorSnap[i][j][2] = snapData[i][j][2];
        }
      }
    }
    return new SnapImpl(colorSnap);
  }

  /**
   * Extracts the luma component from the given Snap image using the formula
   * Luma = 0.299 * R + 0.587 * G + 0.114 * B.
   *
   * @param snap the Snap image to extract the luma component from.
   * @param mask an optional mask Snap, where black pixels (0,0,0) indicate the areas to extract.
   * @return a new Snap instance containing only the luma component in greyscale.
   */
  private Snap extractLumaComponent(Snap snap, Snap mask) throws IllegalArgumentException {
    int[][][] snapData = snap.getSnap();
    int height = snap.getSnapHeight();
    int width = snap.getSnapWidth();
    int[][][] lumaSnap = new int[height][width][3];

    int[][][] maskData = mask != null ? mask.getSnap() : null;

    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        if (maskData == null || maskData[i][j][0] == 0) {
          int lumaValue = (int) Math.round(0.299 * snapData[i][j][0] +  // Red
                  0.587 * snapData[i][j][1] +  // Green
                  0.114 * snapData[i][j][2]); // Blue
          lumaSnap[i][j][0] = lumaValue;
          lumaSnap[i][j][1] = lumaValue;
          lumaSnap[i][j][2] = lumaValue;
        } else {
          lumaSnap[i][j][0] = snapData[i][j][0];
          lumaSnap[i][j][1] = snapData[i][j][1];
          lumaSnap[i][j][2] = snapData[i][j][2];
        }
      }
    }
    return new SnapImpl(lumaSnap);
  }

  /**
   * Extracts the intensity component from the given Snap image using the average of RGB values.
   *
   * @param snap the Snap image to extract the intensity component from.
   * @param mask an optional mask Snap, where black pixels (0,0,0) indicate the areas to extract.
   * @return a new Snap instance containing only the intensity component in greyscale.
   */
  private Snap extractIntensityComponent(Snap snap, Snap mask) throws IllegalArgumentException {
    int[][][] snapData = snap.getSnap();
    int height = snap.getSnapHeight();
    int width = snap.getSnapWidth();
    int[][][] intensitySnap = new int[height][width][3];
    int[][][] maskData = mask != null ? mask.getSnap() : null;


    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        if (maskData == null || maskData[i][j][0] == 0) {
          // Calculate intensity as the average of the RGB values
          int intensityValue = (snapData[i][j][0] + snapData[i][j][1] + snapData[i][j][2]) / 3;
          intensitySnap[i][j][0] = intensityValue; // Set all components to the intensity value
          intensitySnap[i][j][1] = intensityValue;
          intensitySnap[i][j][2] = intensityValue;
        } else {
          intensitySnap[i][j][0] = snapData[i][j][0];
          intensitySnap[i][j][1] = snapData[i][j][1];
          intensitySnap[i][j][2] = snapData[i][j][2];
        }
      }
    }
    return new SnapImpl(intensitySnap);
  }

  /**
   * Extracts the value component from the given Snap image, where each pixel is converted to
   * its value component (the maximum of the red, green, or blue values).
   *
   * @param snap the Snap image to be transformed.
   * @param mask an optional mask Snap, where black pixels (0,0,0) indicate the areas to extract.
   * @return a new Snap instance where each pixel is replaced by its value component in greyscale.
   */
  private Snap extractValueComponent(Snap snap, Snap mask) throws IllegalArgumentException {
    int[][][] snapData = snap.getSnap();
    int height = snap.getSnapHeight();
    int width = snap.getSnapWidth();
    int[][][] valueSnap = new int[height][width][3];

    int[][][] maskData = mask != null ? mask.getSnap() : null;

    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        if (maskData == null || maskData[i][j][0] == 0) {
          int maxValue = Math.max(snapData[i][j][0],
                  Math.max(snapData[i][j][1], snapData[i][j][2]));
          valueSnap[i][j][0] = maxValue;
          valueSnap[i][j][1] = maxValue;
          valueSnap[i][j][2] = maxValue;
        } else {
          valueSnap[i][j][0] = snapData[i][j][0];
          valueSnap[i][j][1] = snapData[i][j][1];
          valueSnap[i][j][2] = snapData[i][j][2];
        }
      }
    }
    return new SnapImpl(valueSnap);
  }

  /**
   * Resizes the given Snap image to the specified new width and
   * height using bilinear interpolation.
   *
   * @param image     The original Snap image to be resized.
   * @param newWidth  The desired width of the resized image.
   * @param newHeight The desired height of the resized image.
   * @return A new Snap instance representing the resized image.
   */
  @Override
  public Snap downsizeImage(Snap image, int newWidth, int newHeight) {
    int originalWidth = image.getSnapWidth();
    int originalHeight = image.getSnapHeight();

    int[][][] originalSnap = image.getSnap();
    int[][][] resizedSnap = new int[newHeight][newWidth][3];

    // Calculate scaling factors
    double xScale = (double) originalWidth / newWidth;
    double yScale = (double) originalHeight / newHeight;

    for (int yDest = 0; yDest < newHeight; yDest++) {
      for (int xDest = 0; xDest < newWidth; xDest++) {
        // Map destination pixel to source coordinates
        double xSrc = xDest * xScale;
        double ySrc = yDest * yScale;

        int x1 = (int) Math.floor(xSrc);
        int y1 = (int) Math.floor(ySrc);
        int x2 = x1 + 1;
        int y2 = y1 + 1;

        // Handle edge cases by clamping to the image boundaries
        if (x2 >= originalWidth) {
          x2 = x1;
        }
        if (y2 >= originalHeight) {
          y2 = y1;
        }

        // Calculate the fractional part
        double xFrac = xSrc - x1;
        double yFrac = ySrc - y1;

        // Retrieve pixel colors
        int[] pixel11 = originalSnap[y1][x1];
        int[] pixel21 = originalSnap[y1][x2];
        int[] pixel12 = originalSnap[y2][x1];
        int[] pixel22 = originalSnap[y2][x2];


        int[] newPixel = new int[3];
        for (int channel = 0; channel < 3; channel++) {
          double top = pixel11[channel] * (1 - xFrac) + pixel21[channel] * xFrac;
          double bottom = pixel12[channel] * (1 - xFrac) + pixel22[channel] * xFrac;
          double value = top * (1 - yFrac) + bottom * yFrac;
          newPixel[channel] = clamp((int) Math.round(value));
        }


        resizedSnap[yDest][xDest] = newPixel;
      }
    }

    return new SnapImpl(resizedSnap);
  }

  /**
   * Creates a mask Snap with the specified rectangle region set to black (0,0,0)
   * and the rest of the image set to white (255,255,255).
   *
   * @param snap   the original Snap image to use for dimensions.
   * @param xStart the starting x-coordinate of the rectangle.
   * @param yStart the starting y-coordinate of the rectangle.
   * @param width  the width of the rectangle.
   * @param height the height of the rectangle.
   * @return a new Snap instance representing the mask.
   * @throws IllegalArgumentException if the rectangle dimensions are invalid or exceed bounds.
   */
  @Override
  public Snap createMask(Snap snap, int xStart, int yStart, int width, int height) {

    if (xStart < 0 || yStart < 0 || width < 0 || height < 0
            || height > snap.getSnapHeight() || width > snap.getSnapWidth()
            || height > snap.getSnapHeight()) {
      throw new IllegalArgumentException();
    }

    int imageWidth = snap.getSnapWidth();
    int imageHeight = snap.getSnapHeight();
    int[][][] maskImage = new int[imageHeight][imageWidth][3];

    // Loop through each pixel in the image
    for (int row = 0; row < imageHeight; row++) {
      for (int col = 0; col < imageWidth; col++) {
        // If the pixel is within the specified rectangle, make it black; otherwise, white
        if (row >= yStart && row < yStart + height && col >= xStart && col < xStart + width) {
          maskImage[row][col] = new int[]{0, 0, 0}; // Black for the mask
        } else {
          maskImage[row][col] = new int[]{255, 255, 255}; // White for unmasked areas
        }
      }
    }

    // Return a new Snap object representing the mask
    return new SnapImpl(maskImage);
  }


}
