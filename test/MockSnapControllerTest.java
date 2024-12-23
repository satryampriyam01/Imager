import org.junit.Before;
import org.junit.Test;

import controller.SnapControllerImpl;
import model.Snap;
import model.SnapImpl;
import model.SnapModel;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.StringReader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Test class for the SnapControllerImpl using a mock SnapModel.
 * This class simulates commands and verifies if the controller correctly manipulates the model.
 */
public class MockSnapControllerTest {

  private SnapControllerImpl controller;
  private SnapModel mockModel;

  /**
   * Sets up the mock model and controller before each test.
   * Initializes a simple mock model with custom behavior for testing the SnapController.
   */
  @Before
  public void setUp() {
    // Create a simple mock model
    mockModel = new SnapModel() {
      private int[][][] snap;

      @Override
      public void loadSnap(Snap snap) {
        this.snap = snap.getSnap();
      }

      @Override
      public void redComponent(Snap mask) throws IllegalArgumentException {
        if (snap != null) {
          snap[0][0][0] = 1;
        }
      }

      @Override
      public void greenComponent(Snap mask) throws IllegalArgumentException {
        if (snap != null) {
          snap[0][0][0] = 2;
        }
      }

      @Override
      public void blueComponent(Snap mask) throws IllegalArgumentException {
        if (snap != null) {
          snap[0][0][0] = 3;
        }
      }

      @Override
      public void lumaComponent(Snap mask) throws IllegalArgumentException {
        if (snap != null) {
          snap[0][0][0] = 4;
        }
      }

      @Override
      public void intensityComponent(Snap mask) throws IllegalArgumentException {
        if (snap != null) {
          snap[0][0][0] = 5;
        }
      }

      @Override
      public void compressionComponent(double value) throws IllegalArgumentException {
        if (snap != null) {
          snap[0][0][0] = 6;
        }
      }

      @Override
      public void colorCorrectionComponent(int... percentage) throws IllegalArgumentException {
        if (snap != null) {
          snap[0][0][0] = 7;
        }
      }

      @Override
      public void histogramComponent() throws IllegalArgumentException {
        if (snap != null) {
          snap[0][0][0] = 8;
        }
      }

      @Override
      public void levelAdjustment(int black, int mid, int white, int... percentage)
              throws IllegalArgumentException {
        if (snap != null) {
          snap[0][0][0] = 9;
        }
      }

      @Override
      public void downSizeComponent(int width, int height) throws IllegalArgumentException {
        if (snap != null) {
          snap[0][0][0] = 9;
        }
      }

      @Override
      public void createMaskComponent(int xStart, int yStart, int width, int height)
              throws IllegalArgumentException {
        if (snap != null) {
          snap[0][0][0] = 9;
        }
      }

      @Override
      public int[][][] getSnap() {
        return snap;
      }

      @Override
      public int getSnapWidth() {
        return 1;
      }

      @Override
      public int getSnapHeight() {
        return 1;
      }

      @Override
      public void blur(Snap mask, int... percentage) throws IllegalArgumentException {
        if (snap != null) {
          snap[0][0][0] = 10;
        }
      }

      @Override
      public void sharpen(Snap mask, int... percentage) throws IllegalArgumentException {
        if (snap != null) {
          snap[0][0][0] = 11;
        }
      }

      @Override
      public void toSepia(Snap mask, int... percentage) throws IllegalArgumentException {
        if (snap != null) {
          snap[0][0][0] = Math.min(255, (int) (0.393 * 50 + 0.769 * 100 + 0.189 * 150));
          snap[0][0][1] = Math.min(255, (int) (0.349 * 50 + 0.686 * 100 + 0.168 * 150));
          snap[0][0][2] = Math.min(255, (int) (0.272 * 50 + 0.534 * 100 + 0.131 * 150));
        }
      }

      @Override
      public void toGreyscale(Snap mask, int... percentage) throws IllegalArgumentException {
        if (snap != null) {
          snap[0][0][0] = 12;
        }
      }

      @Override
      public void brightenessAdjustment(int adjustment) throws IllegalArgumentException {
        // Mock the brighten operation by adjusting the first pixel's value
        if (snap != null) {
          snap[0][0][0] += adjustment;
        }
      }

      @Override
      public void horizontalFlip() throws IllegalArgumentException {
        if (snap != null) {
          snap[0][0][0] = 13;
        }
      }

      @Override
      public void valueComponent(Snap mask) throws IllegalArgumentException {
        if (snap != null) {
          snap[0][0][0] = 14;
        }
      }

      @Override
      public void verticalFlip() throws IllegalArgumentException {
        if (snap != null) {
          snap[0][0][0] = 15;
        }
      }

      @Override
      public Snap[] applyRGBSplit() throws IllegalArgumentException {
        return new Snap[0];
      }

      @Override
      public void applyRGBCombine(Snap red, Snap green, Snap blue) throws IllegalArgumentException {
        if (snap != null) {
          snap[0][0][0] = 16;
        }
      }
    };

    // Use a StringReader to simulate user input instead of System.in
    controller = new SnapControllerImpl(new StringReader(""), mockModel);
  }


  @Test
  public void testLoadCommand() {
    // Simulate input for loading an image
    String input = "load cat.jpeg cat";
    String[] simulatedInput = input.split(" ");


    // Call the load command directly
    controller.commands = simulatedInput;
    controller.counter = simulatedInput.length;

    // Assuming the load command is correct and doesn't throw an exception
    controller.executables.get("load").run();

    // Assert that the image has been loaded (mocked)
    assertNotNull(mockModel.getSnap());
  }

  @Test
  public void testInvalidLoadCommand() {
    // Simulate input for loading an image
    String input = "load cat.jpeg";
    String[] simulatedInput = input.split(" ");


    // Call the load command directly
    controller.commands = simulatedInput;
    controller.counter = simulatedInput.length;

    // Assuming the load command is correct and doesn't throw an exception
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    PrintStream originalOut = System.out;
    System.setOut(new PrintStream(baos));
    controller.executables.get("load").run();
    System.setOut(originalOut);
    String output = baos.toString().trim();

    assertEquals("Invalid Command", output);
  }

  @Test
  public void testInvalidSaveCommand() {
    // Simulate input for loading an image
    String input = "save cat.jpeg";
    String[] simulatedInput = input.split(" ");


    // Call the load command directly
    controller.commands = simulatedInput;
    controller.counter = simulatedInput.length;

    // Assuming the load command is correct and doesn't throw an exception
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    PrintStream originalOut = System.out;
    System.setOut(new PrintStream(baos));
    controller.executables.get("save").run();
    System.setOut(originalOut);
    String output = baos.toString().trim();

    assertEquals("Invalid Command", output);
  }

  @Test
  public void testBlurCommand() {
    // Load a mock image
    int[][][] mockImage = new int[1][1][3]; // A 1x1 image with RGB values
    mockImage[0][0][0] = 50;  // Red value
    mockImage[0][0][1] = 100; // Green value
    mockImage[0][0][2] = 150; // Blue value

    mockModel.loadSnap(new SnapImpl(mockImage));
    controller.modelMap.put("cat", mockImage);

    String input = "blur cat catBlur";
    String[] simulatedInput = input.split(" ");

    controller.commands = simulatedInput;
    controller.counter = simulatedInput.length;
    controller.executables.get("blur").run();

    assertEquals(10, mockModel.getSnap()[0][0][0]);
  }

  @Test
  public void testInvalidBlurCommand() {
    // Set up a mock image (1x1 image)
    int[][][] mockImage = new int[1][1][3];
    mockImage[0][0][0] = 50;  // Red value
    mockImage[0][0][1] = 100; // Green value
    mockImage[0][0][2] = 150; // Blue value

    mockModel.loadSnap(new SnapImpl(mockImage));
    controller.modelMap.put("cat", mockImage);

    String input = "blur cat"; // Invalid command here
    String[] simulatedInput = input.split(" ");

    controller.commands = simulatedInput;
    controller.counter = simulatedInput.length;

    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    PrintStream originalOut = System.out;
    System.setOut(new PrintStream(baos));
    controller.executables.get("blur").run();
    System.setOut(originalOut);
    String output = baos.toString().trim();

    assertEquals("Invalid Command", output);
  }

  @Test
  public void testBlurMaskImageCommand() {
    // Create a 2x2 mock RGB image
    int[][][] mockImage = new int[2][2][3];
    mockImage[0][0] = new int[]{50, 100, 150}; // Pixel (0, 0)
    mockImage[0][1] = new int[]{200, 150, 100}; // Pixel (0, 1)
    mockImage[1][0] = new int[]{100, 50, 200}; // Pixel (1, 0)
    mockImage[1][1] = new int[]{150, 200, 50}; // Pixel (1, 1)

    // Create a corresponding 2x2 3D mask image
    int[][][] maskImage = new int[2][2][3];
    maskImage[0][0] = new int[]{255, 255, 255}; // No Blur (0, 0)
    maskImage[0][1] = new int[]{0, 0, 0};       // blur on (0, 1)
    maskImage[1][0] = new int[]{0, 0, 0};       //  blur on (1, 0)
    maskImage[1][1] = new int[]{255, 255, 255}; // No Blur (1, 1)

    // Load the image and mask into the model
    mockModel.loadSnap(new SnapImpl(mockImage));
    mockModel.loadSnap(new SnapImpl(maskImage)); // Assume `loadMask` exists for loading masks

    // Simulate the mask and blur command
    controller.modelMap.put("cat", mockImage);
    controller.modelMap.put("mask1", maskImage);

    String input = "blur-mask cat mask1 catBlur";
    String[] simulatedInput = input.split(" ");

    controller.commands = simulatedInput;
    controller.counter = simulatedInput.length;
    controller.executables.get("blur").run();

    // Fetch the resulting image
    assertEquals(10, mockModel.getSnap()[0][0][0]);

  }


  @Test
  public void testBlurSplitCommand() {
    // Load a mock image
    int[][][] mockImage = new int[1][1][3]; // A 1x1 image with RGB values
    mockImage[0][0][0] = 50;  // Red value
    mockImage[0][0][1] = 100; // Green value
    mockImage[0][0][2] = 150; // Blue value
    controller.modelMap.put("cat", mockImage);

    String input = "blur cat catBlur split 50";
    String[] simulatedInput = input.split(" ");

    controller.commands = simulatedInput;
    controller.counter = simulatedInput.length;
    controller.executables.get("blur").run();

    assertEquals(10, mockModel.getSnap()[0][0][0]);
  }

  @Test
  public void testBrightenCommand() {
    // Load a mock image (1x1 image with RGB values)
    int[][][] mockImage = new int[1][1][3]; // A 1x1 image with RGB values
    mockImage[0][0][0] = 50;  // Red value
    mockImage[0][0][1] = 100; // Green value
    mockImage[0][0][2] = 150; // Blue value

    controller.modelMap.put("cat", mockImage);

    String input = "brighten 20 cat catBright";
    String[] simulatedInput = input.split(" ");

    // Call the brighten command through the controller
    controller.commands = simulatedInput;
    controller.counter = simulatedInput.length;
    controller.executables.get("brighten").run();

    // Assert that the image's pixel value has increased after brighten
    assertEquals(70, mockModel.getSnap()[0][0][0]);
    assertEquals(100, mockModel.getSnap()[0][0][1]);
    assertEquals(150, mockModel.getSnap()[0][0][2]);
  }

  @Test
  public void testInvalidBrightenCommand() {
    // Set up a mock image (1x1 image)
    int[][][] mockImage = new int[1][1][3];
    mockImage[0][0][0] = 50;  // Red value
    mockImage[0][0][1] = 100; // Green value
    mockImage[0][0][2] = 150; // Blue value

    mockModel.loadSnap(new SnapImpl(mockImage));
    controller.modelMap.put("cat", mockImage);

    String input = "brighten 20 cat";
    String[] simulatedInput = input.split(" ");

    controller.commands = simulatedInput;
    controller.counter = simulatedInput.length;

    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    PrintStream originalOut = System.out;
    System.setOut(new PrintStream(baos));
    controller.executables.get("brighten").run();
    System.setOut(originalOut);
    String output = baos.toString().trim();

    assertEquals("Invalid Command", output);
  }

  @Test
  public void testSepiaCommand() {
    // Load a mock image (1x1 image with RGB values)
    int[][][] mockImage = new int[1][1][3]; // A 1x1 image with RGB values
    mockImage[0][0][0] = 50;  // Red value
    mockImage[0][0][1] = 100; // Green value
    mockImage[0][0][2] = 150; // Blue value
    controller.modelMap.put("cat", mockImage);

    String input = "sepia cat catSepia";
    String[] simulatedInput = input.split(" ");

    controller.commands = simulatedInput;
    controller.counter = simulatedInput.length;

    controller.executables.get("sepia").run();

    int expectedRed = Math.min(255, (int) (0.393 * 50 + 0.769 * 100 + 0.189 * 150));
    int expectedGreen = Math.min(255, (int) (0.349 * 50 + 0.686 * 100 + 0.168 * 150));
    int expectedBlue = Math.min(255, (int) (0.272 * 50 + 0.534 * 100 + 0.131 * 150));

    assertEquals(expectedRed, mockModel.getSnap()[0][0][0]);
    assertEquals(expectedGreen, mockModel.getSnap()[0][0][1]);
    assertEquals(expectedBlue, mockModel.getSnap()[0][0][2]);
  }

  @Test
  public void testInvalidSepiaCommand() {
    // Set up a mock image (1x1 image)
    int[][][] mockImage = new int[1][1][3];
    mockImage[0][0][0] = 50;  // Red value
    mockImage[0][0][1] = 100; // Green value
    mockImage[0][0][2] = 150; // Blue value

    mockModel.loadSnap(new SnapImpl(mockImage));
    controller.modelMap.put("cat", mockImage);

    String input = "sepia cat";
    String[] simulatedInput = input.split(" ");

    controller.commands = simulatedInput;
    controller.counter = simulatedInput.length;

    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    PrintStream originalOut = System.out;
    System.setOut(new PrintStream(baos));
    controller.executables.get("sepia").run();
    System.setOut(originalOut);
    String output = baos.toString().trim();

    assertEquals("Invalid Command", output);
  }

  @Test
  public void testSepiaSplitCommand() {
    // Load a mock image (1x1 image with RGB values)
    int[][][] mockImage = new int[1][1][3]; // A 1x1 image with RGB values
    mockImage[0][0][0] = 50;  // Red value
    mockImage[0][0][1] = 100; // Green value
    mockImage[0][0][2] = 150; // Blue value
    controller.modelMap.put("cat", mockImage);

    String input = "sepia cat catSepia split 50";
    String[] simulatedInput = input.split(" ");

    controller.commands = simulatedInput;
    controller.counter = simulatedInput.length;

    controller.executables.get("sepia").run();

    int expectedRed = Math.min(255, (int) (0.393 * 50 + 0.769 * 100 + 0.189 * 150));
    int expectedGreen = Math.min(255, (int) (0.349 * 50 + 0.686 * 100 + 0.168 * 150));
    int expectedBlue = Math.min(255, (int) (0.272 * 50 + 0.534 * 100 + 0.131 * 150));

    assertEquals(expectedRed, mockModel.getSnap()[0][0][0]);
    assertEquals(expectedGreen, mockModel.getSnap()[0][0][1]);
    assertEquals(expectedBlue, mockModel.getSnap()[0][0][2]);
  }

  @Test
  public void testSharpenCommand() {
    // Load a mock image (1x1 image with RGB values)
    int[][][] mockImage = new int[1][1][3]; // A 1x1 image with RGB values
    mockImage[0][0][0] = 50;  // Red value
    mockImage[0][0][1] = 100; // Green value
    mockImage[0][0][2] = 150; // Blue value
    controller.modelMap.put("cat", mockImage);

    String input = "sharpen cat catSharpen";
    String[] simulatedInput = input.split(" ");

    controller.commands = simulatedInput;
    controller.counter = simulatedInput.length;

    controller.executables.get("sharpen").run();

    assertEquals(11, mockModel.getSnap()[0][0][0]);
  }

  @Test
  public void testInvalidSharpenCommand() {
    // Set up a mock image (1x1 image)
    int[][][] mockImage = new int[1][1][3];
    mockImage[0][0][0] = 50;  // Red value
    mockImage[0][0][1] = 100; // Green value
    mockImage[0][0][2] = 150; // Blue value

    mockModel.loadSnap(new SnapImpl(mockImage));
    controller.modelMap.put("cat", mockImage);

    String input = "sharpen cat";
    String[] simulatedInput = input.split(" ");

    controller.commands = simulatedInput;
    controller.counter = simulatedInput.length;

    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    PrintStream originalOut = System.out;
    System.setOut(new PrintStream(baos));
    controller.executables.get("sharpen").run();
    System.setOut(originalOut);
    String output = baos.toString().trim();

    assertEquals("Invalid Command", output);
  }

  @Test
  public void testSharpenSplitCommand() {
    // Load a mock image (1x1 image with RGB values)
    int[][][] mockImage = new int[1][1][3]; // A 1x1 image with RGB values
    mockImage[0][0][0] = 50;  // Red value
    mockImage[0][0][1] = 100; // Green value
    mockImage[0][0][2] = 150; // Blue value
    controller.modelMap.put("cat", mockImage);
    // mockModel.loadSnap(new SnapImpl(mockImage));

    String input = "sharpen cat catSharpen split 50";
    String[] simulatedInput = input.split(" ");

    controller.commands = simulatedInput;
    controller.counter = simulatedInput.length;

    controller.executables.get("sharpen").run();

    assertEquals(11, mockModel.getSnap()[0][0][0]);
  }

  @Test
  public void testColorCorrectionCommand() {
    // Load a mock image (1x1 image with RGB values)
    int[][][] mockImage = new int[1][1][3]; // A 1x1 image with RGB values
    mockImage[0][0][0] = 50;  // Red value
    mockImage[0][0][1] = 100; // Green value
    mockImage[0][0][2] = 150; // Blue value
    controller.modelMap.put("cat", mockImage);
    //mockModel.loadSnap(new SnapImpl(mockImage));

    String input = "color-correct cat catColorCorrect";
    String[] simulatedInput = input.split(" ");

    controller.commands = simulatedInput;
    controller.counter = simulatedInput.length;

    controller.executables.get("color-correct").run();

    assertEquals(7, mockModel.getSnap()[0][0][0]);
  }

  @Test
  public void testInvalidColorCorrectCommand() {
    // Set up a mock image (1x1 image)
    int[][][] mockImage = new int[1][1][3];
    mockImage[0][0][0] = 50;  // Red value
    mockImage[0][0][1] = 100; // Green value
    mockImage[0][0][2] = 150; // Blue value

    mockModel.loadSnap(new SnapImpl(mockImage));
    controller.modelMap.put("cat", mockImage);

    String input = "color-correct cat";
    String[] simulatedInput = input.split(" ");

    controller.commands = simulatedInput;
    controller.counter = simulatedInput.length;

    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    PrintStream originalOut = System.out;
    System.setOut(new PrintStream(baos));
    controller.executables.get("color-correct").run();
    System.setOut(originalOut);
    String output = baos.toString().trim();

    assertEquals("Invalid command", output);
  }

  @Test
  public void testColorCorrectionSplitCommand() {
    // Load a mock image (1x1 image with RGB values)
    int[][][] mockImage = new int[1][1][3]; // A 1x1 image with RGB values
    mockImage[0][0][0] = 50;  // Red value
    mockImage[0][0][1] = 100; // Green value
    mockImage[0][0][2] = 150; // Blue value
    controller.modelMap.put("cat", mockImage);
    //mockModel.loadSnap(new SnapImpl(mockImage));

    String input = "color-correct cat catColorCorrect split 50";
    String[] simulatedInput = input.split(" ");

    controller.commands = simulatedInput;
    controller.counter = simulatedInput.length;

    controller.executables.get("color-correct").run();

    assertEquals(7, mockModel.getSnap()[0][0][0]);
  }

  @Test
  public void testLevelAdjustmentCommand() {
    // Load a mock image
    int[][][] mockImage = new int[1][1][3]; // A 1x1 image with RGB values
    mockImage[0][0][0] = 50;  // Red value
    mockImage[0][0][1] = 100; // Green value
    mockImage[0][0][2] = 150; // Blue value
    controller.modelMap.put("cat", mockImage);
    //  mockModel.loadSnap(new SnapImpl(mockImage));

    String input = "level-adjust 100 150 200 cat catLevelAdjust";
    String[] simulatedInput = input.split(" ");

    controller.commands = simulatedInput;
    controller.counter = simulatedInput.length;
    controller.executables.get("level-adjust").run();

    assertEquals(9, mockModel.getSnap()[0][0][0]);
  }

  @Test
  public void testInvalidLevelAdjustmentCommand() {
    // Set up a mock image (1x1 image)
    int[][][] mockImage = new int[1][1][3];
    mockImage[0][0][0] = 50;  // Red value
    mockImage[0][0][1] = 100; // Green value
    mockImage[0][0][2] = 150; // Blue value

    mockModel.loadSnap(new SnapImpl(mockImage));
    controller.modelMap.put("cat", mockImage);

    String input = "level-adjust cat";
    String[] simulatedInput = input.split(" ");

    controller.commands = simulatedInput;
    controller.counter = simulatedInput.length;

    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    PrintStream originalOut = System.out;
    System.setOut(new PrintStream(baos));
    controller.executables.get("level-adjust").run();
    System.setOut(originalOut);
    String output = baos.toString().trim();

    assertEquals("Invalid command", output);
  }

  @Test
  public void testLevelAdjustmentSplitCommand() {
    // Load a mock image
    int[][][] mockImage = new int[1][1][3]; // A 1x1 image with RGB values
    mockImage[0][0][0] = 50;  // Red value
    mockImage[0][0][1] = 100; // Green value
    mockImage[0][0][2] = 150; // Blue value
    controller.modelMap.put("cat", mockImage);
    //mockModel.loadSnap(new SnapImpl(mockImage));

    String input = "level-adjust 100 150 200 cat catLevelAdjust split 50";
    String[] simulatedInput = input.split(" ");

    controller.commands = simulatedInput;
    controller.counter = simulatedInput.length;
    controller.executables.get("level-adjust").run();

    assertEquals(9, mockModel.getSnap()[0][0][0]);
  }

  @Test
  public void testGreyscaleCommand() {
    // Load a mock image
    int[][][] mockImage = new int[1][1][3]; // A 1x1 image with RGB values
    mockImage[0][0][0] = 50;  // Red value
    mockImage[0][0][1] = 100; // Green value
    mockImage[0][0][2] = 150; // Blue value
    controller.modelMap.put("cat", mockImage);
    //mockModel.loadSnap(new SnapImpl(mockImage));

    String input = "greyscale cat catGrey";
    String[] simulatedInput = input.split(" ");

    controller.commands = simulatedInput;
    controller.counter = simulatedInput.length;
    controller.executables.get("greyscale").run();

    assertEquals(12, mockModel.getSnap()[0][0][0]);
  }

  @Test
  public void testInvalidGreyscaleCommand() {
    // Set up a mock image (1x1 image)
    int[][][] mockImage = new int[1][1][3];
    mockImage[0][0][0] = 50;  // Red value
    mockImage[0][0][1] = 100; // Green value
    mockImage[0][0][2] = 150; // Blue value

    mockModel.loadSnap(new SnapImpl(mockImage));
    controller.modelMap.put("cat", mockImage);

    String input = "greyscale cat";
    String[] simulatedInput = input.split(" ");

    controller.commands = simulatedInput;
    controller.counter = simulatedInput.length;

    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    PrintStream originalOut = System.out;
    System.setOut(new PrintStream(baos));
    controller.executables.get("greyscale").run();
    System.setOut(originalOut);
    String output = baos.toString().trim();

    assertEquals("Invalid command", output);
  }

  @Test
  public void testGreyscaleSplitCommand() {
    // Load a mock image
    int[][][] mockImage = new int[1][1][3]; // A 1x1 image with RGB values
    mockImage[0][0][0] = 50;  // Red value
    mockImage[0][0][1] = 100; // Green value
    mockImage[0][0][2] = 150; // Blue value

    controller.modelMap.put("cat", mockImage);
    // mockModel.loadSnap(new SnapImpl(mockImage));

    String input = "greyscale cat catGrey split 50";
    String[] simulatedInput = input.split(" ");

    controller.commands = simulatedInput;
    controller.counter = simulatedInput.length;
    controller.executables.get("greyscale").run();

    assertEquals(12, mockModel.getSnap()[0][0][0]);
  }

  @Test
  public void testCompressionComponentCommand() {
    // Load a mock image
    int[][][] mockImage = new int[1][1][3]; // A 1x1 image with RGB values
    mockImage[0][0][0] = 50;  // Red value
    mockImage[0][0][1] = 100; // Green value
    mockImage[0][0][2] = 150; // Blue value
    controller.modelMap.put("cat", mockImage);
    // mockModel.loadSnap(new SnapImpl(mockImage));

    String input = "compress 50 cat catCompress";
    String[] simulatedInput = input.split(" ");

    controller.commands = simulatedInput;
    controller.counter = simulatedInput.length;
    controller.executables.get("compress").run();

    assertEquals(6, mockModel.getSnap()[0][0][0]);
  }

  @Test
  public void testInvalidCompressionCommand() {
    // Set up a mock image (1x1 image)
    int[][][] mockImage = new int[1][1][3];
    mockImage[0][0][0] = 50;  // Red value
    mockImage[0][0][1] = 100; // Green value
    mockImage[0][0][2] = 150; // Blue value

    mockModel.loadSnap(new SnapImpl(mockImage));
    controller.modelMap.put("cat", mockImage);

    String input = "compress cat";
    String[] simulatedInput = input.split(" ");

    controller.commands = simulatedInput;
    controller.counter = simulatedInput.length;

    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    PrintStream originalOut = System.out;
    System.setOut(new PrintStream(baos));
    controller.executables.get("compress").run();
    System.setOut(originalOut);
    String output = baos.toString().trim();

    assertEquals("Invalid command", output);
  }

  @Test
  public void testHistogramComponentCommand() {
    // Load a mock image
    int[][][] mockImage = new int[1][1][3]; // A 1x1 image with RGB values
    mockImage[0][0][0] = 50;  // Red value
    mockImage[0][0][1] = 100; // Green value
    mockImage[0][0][2] = 150; // Blue value
    controller.modelMap.put("cat", mockImage);
    // mockModel.loadSnap(new SnapImpl(mockImage));

    String input = "histogram cat catHistogram";
    String[] simulatedInput = input.split(" ");

    controller.commands = simulatedInput;
    controller.counter = simulatedInput.length;
    controller.executables.get("histogram").run();

    assertEquals(8, mockModel.getSnap()[0][0][0]);
  }

  @Test
  public void testInvalidHistogramCommand() {
    // Set up a mock image (1x1 image)
    int[][][] mockImage = new int[1][1][3];
    mockImage[0][0][0] = 50;  // Red value
    mockImage[0][0][1] = 100; // Green value
    mockImage[0][0][2] = 150; // Blue value

    mockModel.loadSnap(new SnapImpl(mockImage));
    controller.modelMap.put("cat", mockImage);

    String input = "histogram cat";
    String[] simulatedInput = input.split(" ");

    controller.commands = simulatedInput;
    controller.counter = simulatedInput.length;

    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    PrintStream originalOut = System.out;
    System.setOut(new PrintStream(baos));
    controller.executables.get("histogram").run();
    System.setOut(originalOut);
    String output = baos.toString().trim();

    assertEquals("Invalid command", output);
  }

  @Test
  public void testRedComponentCommand() {
    // Load a mock image
    int[][][] mockImage = new int[1][1][3]; // A 1x1 image with RGB values
    mockImage[0][0][0] = 50;  // Red value
    mockImage[0][0][1] = 100; // Green value
    mockImage[0][0][2] = 150; // Blue value
    controller.modelMap.put("cat", mockImage);
    //  mockModel.loadSnap(new SnapImpl(mockImage));

    String input = "red-component cat catRed";
    String[] simulatedInput = input.split(" ");

    controller.commands = simulatedInput;
    controller.counter = simulatedInput.length;
    controller.executables.get("red-component").run();

    assertEquals(1, mockModel.getSnap()[0][0][0]);
  }

  @Test
  public void testInvalidRedComponentCommand() {
    // Set up a mock image (1x1 image)
    int[][][] mockImage = new int[1][1][3];
    mockImage[0][0][0] = 50;  // Red value
    mockImage[0][0][1] = 100; // Green value
    mockImage[0][0][2] = 150; // Blue value

    mockModel.loadSnap(new SnapImpl(mockImage));
    controller.modelMap.put("cat", mockImage);

    String input = "red-component cat";
    String[] simulatedInput = input.split(" ");

    controller.commands = simulatedInput;
    controller.counter = simulatedInput.length;

    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    PrintStream originalOut = System.out;
    System.setOut(new PrintStream(baos));
    controller.executables.get("red-component").run();
    System.setOut(originalOut);
    String output = baos.toString().trim();

    assertEquals("Invalid command", output);
  }

  @Test
  public void testGreenComponentCommand() {
    // Load a mock image
    int[][][] mockImage = new int[1][1][3]; // A 1x1 image with RGB values
    mockImage[0][0][0] = 50;  // Red value
    mockImage[0][0][1] = 100; // Green value
    mockImage[0][0][2] = 150; // Blue value
    controller.modelMap.put("cat", mockImage);
    //mockModel.loadSnap(new SnapImpl(mockImage));

    String input = "green-component cat catGreen";
    String[] simulatedInput = input.split(" ");

    controller.commands = simulatedInput;
    controller.counter = simulatedInput.length;
    controller.executables.get("green-component").run();

    assertEquals(2, mockModel.getSnap()[0][0][0]);
  }

  @Test
  public void testInvalidGreenComponentCommand() {
    // Set up a mock image (1x1 image)
    int[][][] mockImage = new int[1][1][3];
    mockImage[0][0][0] = 50;  // Red value
    mockImage[0][0][1] = 100; // Green value
    mockImage[0][0][2] = 150; // Blue value

    mockModel.loadSnap(new SnapImpl(mockImage));
    controller.modelMap.put("cat", mockImage);

    String input = "green-component cat";
    String[] simulatedInput = input.split(" ");

    controller.commands = simulatedInput;
    controller.counter = simulatedInput.length;

    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    PrintStream originalOut = System.out;
    System.setOut(new PrintStream(baos));
    controller.executables.get("green-component").run();
    System.setOut(originalOut);
    String output = baos.toString().trim();

    assertEquals("Invalid command", output);
  }

  @Test
  public void testBlueComponentCommand() {
    // Load a mock image
    int[][][] mockImage = new int[1][1][3]; // A 1x1 image with RGB values
    mockImage[0][0][0] = 50;  // Red value
    mockImage[0][0][1] = 100; // Green value
    mockImage[0][0][2] = 150; // Blue value
    controller.modelMap.put("cat", mockImage);
    // mockModel.loadSnap(new SnapImpl(mockImage));

    String input = "blue-component cat catBlue";
    String[] simulatedInput = input.split(" ");

    controller.commands = simulatedInput;
    controller.counter = simulatedInput.length;
    controller.executables.get("blue-component").run();

    assertEquals(3, mockModel.getSnap()[0][0][0]);
  }

  @Test
  public void testInvalidBlueComponentCommand() {
    // Set up a mock image (1x1 image)
    int[][][] mockImage = new int[1][1][3];
    mockImage[0][0][0] = 50;  // Red value
    mockImage[0][0][1] = 100; // Green value
    mockImage[0][0][2] = 150; // Blue value

    mockModel.loadSnap(new SnapImpl(mockImage));
    controller.modelMap.put("cat", mockImage);

    String input = "blue-component cat";
    String[] simulatedInput = input.split(" ");

    controller.commands = simulatedInput;
    controller.counter = simulatedInput.length;

    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    PrintStream originalOut = System.out;
    System.setOut(new PrintStream(baos));
    controller.executables.get("blue-component").run();
    System.setOut(originalOut);
    String output = baos.toString().trim();

    assertEquals("Invalid command", output);
  }

  @Test
  public void testLumaComponentCommand() {
    // Load a mock image
    int[][][] mockImage = new int[1][1][3]; // A 1x1 image with RGB values
    mockImage[0][0][0] = 50;  // Red value
    mockImage[0][0][1] = 100; // Green value
    mockImage[0][0][2] = 150; // Blue value
    controller.modelMap.put("cat", mockImage);
    // mockModel.loadSnap(new SnapImpl(mockImage));

    String input = "luma-component cat catLuma";
    String[] simulatedInput = input.split(" ");

    controller.commands = simulatedInput;
    controller.counter = simulatedInput.length;
    controller.executables.get("luma-component").run();

    assertEquals(4, mockModel.getSnap()[0][0][0]);
  }

  @Test
  public void testInvalidLumaCommand() {
    // Set up a mock image (1x1 image)
    int[][][] mockImage = new int[1][1][3];
    mockImage[0][0][0] = 50;  // Red value
    mockImage[0][0][1] = 100; // Green value
    mockImage[0][0][2] = 150; // Blue value

    mockModel.loadSnap(new SnapImpl(mockImage));
    controller.modelMap.put("cat", mockImage);

    String input = "luma-component cat";
    String[] simulatedInput = input.split(" ");

    controller.commands = simulatedInput;
    controller.counter = simulatedInput.length;

    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    PrintStream originalOut = System.out;
    System.setOut(new PrintStream(baos));
    controller.executables.get("luma-component").run();
    System.setOut(originalOut);
    String output = baos.toString().trim();

    assertEquals("Invalid command", output);
  }

  @Test
  public void testIntensityComponentCommand() {
    // Load a mock image
    int[][][] mockImage = new int[1][1][3]; // A 1x1 image with RGB values
    mockImage[0][0][0] = 50;  // Red value
    mockImage[0][0][1] = 100; // Green value
    mockImage[0][0][2] = 150; // Blue value
    controller.modelMap.put("cat", mockImage);
    //mockModel.loadSnap(new SnapImpl(mockImage));

    String input = "intensity-component cat catRed";
    String[] simulatedInput = input.split(" ");


    controller.commands = simulatedInput;
    controller.counter = simulatedInput.length;
    controller.executables.get("intensity-component").run();

    assertEquals(5, mockModel.getSnap()[0][0][0]);
  }

  @Test
  public void testInvalidIntensityComponentCommand() {
    // Set up a mock image (1x1 image)
    int[][][] mockImage = new int[1][1][3];
    mockImage[0][0][0] = 50;  // Red value
    mockImage[0][0][1] = 100; // Green value
    mockImage[0][0][2] = 150; // Blue value

    mockModel.loadSnap(new SnapImpl(mockImage));
    controller.modelMap.put("cat", mockImage);

    String input = "intensity-component cat";
    String[] simulatedInput = input.split(" ");

    controller.commands = simulatedInput;
    controller.counter = simulatedInput.length;

    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    PrintStream originalOut = System.out;
    System.setOut(new PrintStream(baos));
    controller.executables.get("intensity-component").run();
    System.setOut(originalOut);
    String output = baos.toString().trim();

    assertEquals("Invalid command", output);
  }

  @Test
  public void testValueComponentCommand() {
    // Load a mock image
    int[][][] mockImage = new int[1][1][3]; // A 1x1 image with RGB values
    mockImage[0][0][0] = 50;  // Red value
    mockImage[0][0][1] = 100; // Green value
    mockImage[0][0][2] = 150; // Blue value
    // mockModel.loadSnap(new SnapImpl(mockImage));
    controller.modelMap.put("cat", mockImage);

    String input = "value-component cat catValue";
    String[] simulatedInput = input.split(" ");

    controller.commands = simulatedInput;
    controller.counter = simulatedInput.length;
    controller.executables.get("value-component").run();

    assertEquals(14, mockModel.getSnap()[0][0][0]);
  }

  @Test
  public void testInvalidValueComponentCommand() {
    // Set up a mock image (1x1 image)
    int[][][] mockImage = new int[1][1][3];
    mockImage[0][0][0] = 50;  // Red value
    mockImage[0][0][1] = 100; // Green value
    mockImage[0][0][2] = 150; // Blue value

    mockModel.loadSnap(new SnapImpl(mockImage));
    controller.modelMap.put("cat", mockImage);

    String input = "value-component cat";
    String[] simulatedInput = input.split(" ");

    controller.commands = simulatedInput;
    controller.counter = simulatedInput.length;

    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    PrintStream originalOut = System.out;
    System.setOut(new PrintStream(baos));
    controller.executables.get("value-component").run();
    System.setOut(originalOut);
    String output = baos.toString().trim();

    assertEquals("Invalid command", output);
  }

  @Test
  public void testVerticalFlipCommand() {
    // Load a mock image
    int[][][] mockImage = new int[1][1][3]; // A 1x1 image with RGB values
    mockImage[0][0][0] = 50;  // Red value
    mockImage[0][0][1] = 100; // Green value
    mockImage[0][0][2] = 150; // Blue value
    controller.modelMap.put("cat", mockImage);
    //mockModel.loadSnap(new SnapImpl(mockImage));

    String input = "vertical-flip cat catVerticalFlip";
    String[] simulatedInput = input.split(" ");

    controller.commands = simulatedInput;
    controller.counter = simulatedInput.length;
    controller.executables.get("vertical-flip").run();

    assertEquals(15, mockModel.getSnap()[0][0][0]);
  }

  @Test
  public void testInvalidVerticalFlipCommand() {
    // Set up a mock image (1x1 image)
    int[][][] mockImage = new int[1][1][3];
    mockImage[0][0][0] = 50;  // Red value
    mockImage[0][0][1] = 100; // Green value
    mockImage[0][0][2] = 150; // Blue value

    mockModel.loadSnap(new SnapImpl(mockImage));
    controller.modelMap.put("cat", mockImage);

    String input = "vertical-flip cat";
    String[] simulatedInput = input.split(" ");

    controller.commands = simulatedInput;
    controller.counter = simulatedInput.length;

    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    PrintStream originalOut = System.out;
    System.setOut(new PrintStream(baos));
    controller.executables.get("vertical-flip").run();
    System.setOut(originalOut);
    String output = baos.toString().trim();

    assertEquals("Invalid command", output);
  }

  @Test
  public void testHorizontalFlipCommand() {
    // Load a mock image
    int[][][] mockImage = new int[1][1][3]; // A 1x1 image with RGB values
    mockImage[0][0][0] = 50;  // Red value
    mockImage[0][0][1] = 100; // Green value
    mockImage[0][0][2] = 150; // Blue value
    controller.modelMap.put("cat", mockImage);
    // mockModel.loadSnap(new SnapImpl(mockImage));

    String input = "horizontal-flip cat catHorizontal";
    String[] simulatedInput = input.split(" ");

    controller.commands = simulatedInput;
    controller.counter = simulatedInput.length;
    controller.executables.get("horizontal-flip").run();

    assertEquals(13, mockModel.getSnap()[0][0][0]);
  }

  @Test
  public void testInvalidHorizontalFlipCommand() {
    // Set up a mock image (1x1 image)
    int[][][] mockImage = new int[1][1][3];
    mockImage[0][0][0] = 50;  // Red value
    mockImage[0][0][1] = 100; // Green value
    mockImage[0][0][2] = 150; // Blue value

    mockModel.loadSnap(new SnapImpl(mockImage));
    controller.modelMap.put("cat", mockImage);

    String input = "horizontal-flip cat";
    String[] simulatedInput = input.split(" ");

    controller.commands = simulatedInput;
    controller.counter = simulatedInput.length;

    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    PrintStream originalOut = System.out;
    System.setOut(new PrintStream(baos));
    controller.executables.get("horizontal-flip").run();
    System.setOut(originalOut);
    String output = baos.toString().trim();

    assertEquals("Invalid command", output);
  }

  @Test
  public void testInvalidRGBSplitCommand() {
    // Set up a mock image (1x1 image)
    int[][][] mockImage = new int[1][1][3];
    mockImage[0][0][0] = 50;  // Red value
    mockImage[0][0][1] = 100; // Green value
    mockImage[0][0][2] = 150; // Blue value

    mockModel.loadSnap(new SnapImpl(mockImage));
    controller.modelMap.put("cat", mockImage);

    String input = "rgb-split cat";
    String[] simulatedInput = input.split(" ");

    controller.commands = simulatedInput;
    controller.counter = simulatedInput.length;

    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    PrintStream originalOut = System.out;
    System.setOut(new PrintStream(baos));
    controller.executables.get("rgb-split").run();
    System.setOut(originalOut);
    String output = baos.toString().trim();

    assertEquals("Invalid command", output);
  }

  @Test
  public void testRGBCombineCommand() {
    // Create mock RGB channel images for red, green, and blue
    int[][][] redChannel = new int[1][1][3];
    int[][][] greenChannel = new int[1][1][3];
    int[][][] blueChannel = new int[1][1][3];

    // Set initial values for each channel
    redChannel[0][0][0] = 50;   // Red value
    greenChannel[0][0][1] = 100; // Green value
    blueChannel[0][0][2] = 150; // Blue value

    // Load each channel as separate Snap objects
    Snap redSnap = new SnapImpl(redChannel);
    Snap greenSnap = new SnapImpl(greenChannel);
    Snap blueSnap = new SnapImpl(blueChannel);

    // Load a base image into the model
    int[][][] baseImage = new int[1][1][3];
    baseImage[0][0][0] = 0;
    baseImage[0][0][1] = 0;
    baseImage[0][0][2] = 0;
    controller.modelMap.put("cat", baseImage);
    mockModel.loadSnap(new SnapImpl(baseImage));

    // Execute the RGB combine command with mock inputs
    String input = "rgb-combine cat catRed catGreen catBlue";
    String[] simulatedInput = input.split(" ");

    controller.commands = simulatedInput;
    controller.counter = simulatedInput.length;

    // Run the combine method and test its result
    mockModel.applyRGBCombine(redSnap, greenSnap, blueSnap);

    // Assert that the pixel value is updated as expected
    assertEquals(16, mockModel.getSnap()[0][0][0]);
  }

  @Test
  public void testInvalidRGBCombineCommand() {
    // Set up a mock image (1x1 image)
    int[][][] mockImage = new int[1][1][3];
    mockImage[0][0][0] = 50;  // Red value
    mockImage[0][0][1] = 100; // Green value
    mockImage[0][0][2] = 150; // Blue value

    mockModel.loadSnap(new SnapImpl(mockImage));
    controller.modelMap.put("cat", mockImage);

    String input = "rgb-combine cat";
    String[] simulatedInput = input.split(" ");

    controller.commands = simulatedInput;
    controller.counter = simulatedInput.length;

    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    PrintStream originalOut = System.out;
    System.setOut(new PrintStream(baos));
    controller.executables.get("rgb-combine").run();
    System.setOut(originalOut);
    String output = baos.toString().trim();

    assertEquals("Invalid command", output);
  }

  @Test
  public void testMaskCommand() {
    int[][][] mockImage = new int[1][1][3];
    mockImage[0][0][0] = 50;
    mockImage[0][0][1] = 100;
    mockImage[0][0][2] = 150;
    controller.modelMap.put("cat", mockImage);
    String input = "mask cat catMask 0 0 1 1";
    String[] simulatedInput = input.split(" ");
    controller.commands = simulatedInput;
    controller.counter = simulatedInput.length;
    controller.executables.get("maskimage").run();

    assertEquals(9, mockModel.getSnap()[0][0][0]);
  }

  @Test
  public void testInvalidMaskCommand() {
    // Set up a mock image (1x1 image)
    int[][][] mockImage = new int[1][1][3];
    mockImage[0][0][0] = 50;  // Red value
    mockImage[0][0][1] = 100; // Green value
    mockImage[0][0][2] = 150; // Blue value

    mockModel.loadSnap(new SnapImpl(mockImage));
    controller.modelMap.put("cat", mockImage);

    String input = "mask cat";
    String[] simulatedInput = input.split(" ");

    controller.commands = simulatedInput;
    controller.counter = simulatedInput.length;

    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    PrintStream originalOut = System.out;
    System.setOut(new PrintStream(baos));
    controller.executables.get("maskimage").run();
    System.setOut(originalOut);
    String output = baos.toString().trim();

    assertEquals("Invalid command.", output);
  }

  @Test
  public void testDownsize() {
    int[][][] mockImage = new int[1][1][3];
    mockImage[0][0][0] = 50;
    mockImage[0][0][1] = 100;
    mockImage[0][0][2] = 150;
    controller.modelMap.put("cat", mockImage);
    String input = "downsize cat catDownsize 100 100";
    String[] simulatedInput = input.split(" ");
    controller.commands = simulatedInput;
    controller.counter = simulatedInput.length;
    controller.executables.get("downsize").run();
    assertEquals(9, mockModel.getSnap()[0][0][0]);
  }

  @Test
  public void testInvalidDownsizeCommand() {
    // Set up a mock image (1x1 image)
    int[][][] mockImage = new int[1][1][3];
    mockImage[0][0][0] = 50;  // Red value
    mockImage[0][0][1] = 100; // Green value
    mockImage[0][0][2] = 150; // Blue value

    mockModel.loadSnap(new SnapImpl(mockImage));
    controller.modelMap.put("cat", mockImage);

    String input = "downsize cat";
    String[] simulatedInput = input.split(" ");

    controller.commands = simulatedInput;
    controller.counter = simulatedInput.length;

    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    PrintStream originalOut = System.out;
    System.setOut(new PrintStream(baos));
    controller.executables.get("downsize").run();
    System.setOut(originalOut);
    String output = baos.toString().trim();

    assertEquals("Invalid command", output);
  }

}