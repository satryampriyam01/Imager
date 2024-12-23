package controller;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * Reads a Snap image from a file. Supports PNG, JPEG, and PPM formats.
 */
public class SnapUtil {

  /**
   * Reads a Snap image from a file. Supports PNG, JPEG, and PPM formats.
   *
   * @param filename The name of the file to read the image from.
   * @return A 3D array representing the image
   * @throws IOException If an error occurs while reading the file.
   */
  public static int[][][] readSnap(String filename) throws IOException {
    String fileExtension =
            filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();

    if (fileExtension.equals("ppm")) {
      return readPPM(filename); // Custom method to read PPM files
    } else {
      return readStandardImage(filename);
    }
  }

  /**
   * Handles reading of standard image formats (JPEG, PNG).
   *
   * @param filename The name of the file to read the image from.
   * @return A 3D array representing the image.
   * @throws IOException If an error occurs while reading the file.
   */
  private static int[][][] readStandardImage(String filename) throws IOException {
    System.out.println("Filename received: " + filename);
    BufferedImage input = ImageIO.read(new File(filename));
    int[][][] output = new int[input.getHeight()][input.getWidth()][3];

    for (int i = 0; i < input.getHeight(); i++) {
      for (int j = 0; j < input.getWidth(); j++) {
        int color = input.getRGB(j, i);
        Color c = new Color(color);
        output[i][j][0] = c.getRed();
        output[i][j][1] = c.getGreen();
        output[i][j][2] = c.getBlue();
      }
    }
    //System.out.println(input.getWidth() + "x" + input.getHeight());
    return output;
  }

  /**
   * Reads a PPM image from a file.
   *
   * @param filename The name of the PPM file to read.
   * @return A 3D array representing the PPM image.
   * @throws IOException If an error occurs while reading the file.
   */
  private static int[][][] readPPM(String filename) throws IOException {
    BufferedReader br = new BufferedReader(new FileReader(filename));
    String magicNumber = br.readLine().trim();

    if (!magicNumber.equals("P3") && !magicNumber.equals("P6")) {
      br.close();
      throw new IllegalArgumentException("Invalid PPM format: Only P3 and P6 formats supported");
    }

    // Skip comments and read dimensions
    String line = br.readLine().trim();
    while (line.startsWith("#")) {
      line = br.readLine().trim();
    }

    String[] dimensions = line.split("\\s+");
    int width = Integer.parseInt(dimensions[0]);
    int height = Integer.parseInt(dimensions[1]);
    int maxVal = Integer.parseInt(br.readLine().trim());

    if (maxVal != 255) {
      br.close();
      throw new IllegalArgumentException("Unsupported max color value: " + maxVal);
    }

    int[][][] output = new int[height][width][3];

    if (magicNumber.equals("P3")) {
      for (int i = 0; i < height; i++) {
        for (int j = 0; j < width; j++) {
          output[i][j][0] = Integer.parseInt(br.readLine().trim()); // Red
          output[i][j][1] = Integer.parseInt(br.readLine().trim()); // Green
          output[i][j][2] = Integer.parseInt(br.readLine().trim()); // Blue
        }
      }
    } else if (magicNumber.equals("P6")) {
      FileInputStream fis = new FileInputStream(filename);
      skipHeader(fis);
      for (int i = 0; i < height; i++) {
        for (int j = 0; j < width; j++) {
          output[i][j][0] = fis.read(); // Red
          output[i][j][1] = fis.read(); // Green
          output[i][j][2] = fis.read(); // Blue
        }
      }
      fis.close();
    }

    br.close();
    return output;
  }

  /**
   * Skips the header of a PPM file.
   *
   * @param fis The FileInputStream to read from.
   * @throws IOException If an error occurs while reading.
   */
  private static void skipHeader(FileInputStream fis) throws IOException {
    int newlinesCount = 0;
    while (newlinesCount < 4) {
      int readByte = fis.read();
      if (readByte == '\n') {
        newlinesCount++;
      }
    }
  }

  /**
   * Writes a Snap image to a file. Supports PNG, JPEG, and PPM formats.
   *
   * @param snap     A 3D array representing the image to be written.
   * @param filename The name of the file to write the image to.
   * @param height   The height of the image.
   * @param width    The width of the image.
   * @throws IOException If an error occurs while writing the file.
   */
  public static void writeSnap(int[][][] snap, String filename,
                               int height, int width) throws IOException {
    String fileExtension =
            filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();

    if (fileExtension.equals("ppm")) {
      writePPM(snap, filename, height, width);
    } else {
      writeStandardImage(snap, filename, height, width);
    }
  }

  /**
   * Writes a standard image (PNG, JPEG) to a file.
   *
   * @param snap     A 3D array representing the image to be written.
   * @param filename The name of the file to write the image to.
   * @param height   The height of the image.
   * @param width    The width of the image.
   * @throws IOException If an error occurs while writing the file.
   */
  private static void writeStandardImage(int[][][] snap, String filename,
                                         int height, int width) throws IOException {
    BufferedImage output = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        int red = snap[i][j][0];
        int green = snap[i][j][1];
        int blue = snap[i][j][2];
        int color = (red << 16) + (green << 8) + (blue);
        output.setRGB(j, i, color);
      }
    }

    String fileExtension = filename.substring(filename.lastIndexOf(".") + 1);
    // System.out.println(output.getWidth() + "x" + output.getHeight());
    ImageIO.write(output, fileExtension, new File(filename));
  }

  /**
   * Writes a PPM image to a file.
   *
   * @param snap     A 3D array representing the PPM image to be written.
   * @param filename The name of the PPM file to write.
   * @param height   The height of the image.
   * @param width    The width of the image.
   * @throws IOException If an error occurs while writing the file.
   */
  private static void writePPM(int[][][] snap, String filename,
                               int height, int width) throws IOException {
    BufferedWriter bw = new BufferedWriter(new FileWriter(filename));

    bw.write("P3\n");
    bw.write(width + " " + height + "\n");
    bw.write("255\n");

    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        bw.write(snap[i][j][0] + "\n"); // Red
        bw.write(snap[i][j][1] + "\n"); // Green
        bw.write(snap[i][j][2] + "\n"); // Blue
      }
    }
    bw.close();
  }

  /**
   * Gets the width of an image file. Supports PNG, JPEG, and PPM formats.
   *
   * @param filename The name of the file to check the width.
   * @return The width of the image.
   * @throws IOException If an error occurs while reading the file.
   */
  public static int getWidth(String filename) throws IOException {
    String fileExtension =
            filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
    if (fileExtension.equals("ppm")) {
      return getPPMWidth(filename);
    } else {
      BufferedImage input = ImageIO.read(new File(filename));
      return input.getWidth();
    }
  }

  /**
   * Gets the height of an image file. Supports PNG, JPEG, and PPM formats.
   *
   * @param filename The name of the file to check the height.
   * @return The height of the image.
   * @throws IOException If an error occurs while reading the file.
   */
  public static int getHeight(String filename) throws IOException {
    String fileExtension =
            filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
    if (fileExtension.equals("ppm")) {
      return getPPMHeight(filename);
    } else {
      BufferedImage input = ImageIO.read(new File(filename));
      return input.getHeight();
    }
  }

  /**
   * Gets the width of a PPM image.
   *
   * @param filename The name of the PPM file.
   * @return The width of the PPM image.
   * @throws IOException If an error occurs while reading the file.
   */
  private static int getPPMWidth(String filename) throws IOException {
    BufferedReader br = new BufferedReader(new FileReader(filename));
    br.readLine();
    String line = br.readLine().trim();
    while (line.startsWith("#")) {
      line = br.readLine().trim();
    }
    String[] dimensions = line.split("\\s+");
    br.close();
    return Integer.parseInt(dimensions[0]);
  }

  /**
   * Gets the height of a PPM image.
   *
   * @param filename The name of the PPM file.
   * @return The height of the PPM image.
   * @throws IOException If an error occurs while reading the file.
   */
  private static int getPPMHeight(String filename) throws IOException {
    BufferedReader br = new BufferedReader(new FileReader(filename));
    br.readLine();
    String line = br.readLine().trim();
    while (line.startsWith("#")) {
      line = br.readLine().trim();
    }
    String[] dimensions = line.split("\\s+");
    br.close();
    return Integer.parseInt(dimensions[1]);
  }


}