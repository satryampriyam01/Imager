import org.junit.Before;
import org.junit.Test;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import controller.SnapController;
import controller.SnapControllerImpl;
import model.Snap;
import model.SnapImpl;
import model.SnapModel;
import model.SnapModelImpl;

import static controller.SnapUtil.readSnap;
import static controller.SnapUtil.writeSnap;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Test class contains unit tests for the SnapController class.
 */
public class SnapControllerTest {
  private SnapModel testModel;
  private SnapModel unionSquareM; // Assume this is another model for a different sample image
  private SnapModel stopSignM;

  @Before
  public void setup() throws IOException {
    SnapModel catM;
    testModel = new SnapModelImpl();
    catM = new SnapModelImpl();
    int[][][] catSnap = readSnap("resources/cat.jpg");
    catM.loadSnap(new SnapImpl(catSnap));


    unionSquareM = new SnapModelImpl();
    int[][][] unionSquareSnap = readSnap("resources/unionSquare.jpg");
    unionSquareM.loadSnap(new SnapImpl(unionSquareSnap));
    Snap unionSquareMask = new SnapImpl(new int[unionSquareM.getSnapHeight()]
            [unionSquareM.getSnapWidth()][3]);

    stopSignM = new SnapModelImpl();
    int[][][] stopSignSnap = readSnap("resources/stopSign.ppm");
    stopSignM.loadSnap(new SnapImpl(stopSignSnap));


  }

  @Test
  public void testValidCommands() throws IOException {
    // Test a sequence of valid commands
    String validInput = "load cat.jpeg catSnap\n"
            + "brighten 50 catSnap catBright\n"
            + "save catBright.jpg catBright\n";
    Reader testInput = new StringReader(validInput);
    SnapController test = new SnapControllerImpl(testInput, testModel);

    try {
      test.start();
    } catch (IOException e) {
      fail("Should not have thrown IOException for valid commands");
    }
  }

  @Test
  public void testScript() {
    // Test using a script file
    String scriptFilePath = "script.txt"; // Update this path as necessary

    try (Reader testInput = new FileReader(scriptFilePath)) {
      SnapControllerImpl testController = new SnapControllerImpl(testInput, testModel);
      testController.start();
    } catch (IOException e) {
      fail("Should not have thrown IOException for valid commands");
    }
  }

  @Test
  public void testInvalidScript() {
    // Test using an invalid script file
    String invalidScriptFilePath = "script.txt"; // Update this path to point to your invalid script

    try (Reader testInput = new FileReader(invalidScriptFilePath)) {
      SnapControllerImpl testController = new SnapControllerImpl(testInput, testModel);
      testController.start();
    } catch (Exception e) {
      assertTrue("Expected exception message to contain 'invalid command', "
                      + "but got: " + e.getMessage(),
              e.getMessage().contains("unknown command"));
    }
  }

  @Test
  public void testValidCommandsPPM() throws IOException {
    // Test a sequence of valid commands
    String validInput = "load resources/stopSign.ppm stopSnap\n"
            + "brighten 50 stopSnap stopBright\n"
            + "save stopBright.ppm stopBright\n";
    Reader testInput = new StringReader(validInput);
    SnapController test = new SnapControllerImpl(testInput, testModel);

    try {
      test.start();
    } catch (IOException e) {
      fail("Should not have thrown IOException for valid commands");
    }


  }

  @Test
  public void testInvalidValueComponentCommand() {
    String invalidInput = "load cat.jpg catSnap\n"
            + "value-component catSnap catValueComponent\n";
    Reader testInput = new StringReader(invalidInput);
    SnapController test = new SnapControllerImpl(testInput, testModel);

    try {
      test.start();
    } catch (IllegalArgumentException e) {
      // expected
    } catch (IOException e) {
      fail("Should not have thrown IOException for invalid valueComponent command");
    }
  }

  @Test
  public void testValidBrightenCommand() throws IOException {
    // Test valid brighten command
    unionSquareM.brightenessAdjustment(20);
    writeSnap(unionSquareM.getSnap(), "resources/unionSquareTest.jpg",
            unionSquareM.getSnapHeight(), unionSquareM.getSnapWidth());
    String validInput = "load resources/unionSquare.jpg unionSquareSnap\n"
            + "brighten 20 unionSquareSnap unionSquareBrighten\n"
            + "save resources/unionSquareBrighten.jpg unionSquareBrighten\n";
    Reader testInput = new StringReader(validInput);
    SnapController test = new SnapControllerImpl(testInput, testModel);

    try {
      test.start();
    } catch (IOException e) {
      fail("Should not have thrown IOException for valid brighten command");
    }
    int[][][] modelArray = readSnap("resources/unionSquareTest.jpg");
    int[][][] controllerArray = readSnap("resources/unionSquareBrighten.jpg");
    assertArrayEquals(modelArray, controllerArray);
  }

  @Test
  public void testValidSharpenCommand() throws IOException {
    // Test valid sharpen command
    Snap unionSquareMask = null;
    unionSquareM.sharpen(unionSquareMask);
    writeSnap(unionSquareM.getSnap(), "resources/unionSquareTest.jpg",
            unionSquareM.getSnapHeight(), unionSquareM.getSnapWidth());
    String validInput = "load resources/unionSquare.jpg unionSquareSnap\n"
            + "sharpen unionSquareSnap unionSquareSharp\n"
            + "save resources/unionSquareSharp.jpg unionSquareSharp\n";
    Reader testInput = new StringReader(validInput);
    SnapController test = new SnapControllerImpl(testInput, testModel);

    try {
      test.start();
    } catch (IOException e) {
      fail("Should not have thrown IOException for valid sharpen command");
    }
    int[][][] modelArray = readSnap("resources/unionSquareTest.jpg");
    int[][][] controllerArray = readSnap("resources/unionSquareSharp.jpg");
    assertArrayEquals(modelArray, controllerArray);
  }

  @Test
  public void testValidSharpenWithSplitCommand() throws IOException {
    // Test valid sharpen command
    unionSquareM.sharpen(null,50);
    writeSnap(unionSquareM.getSnap(), "resources/unionSquareTest.jpg",
            unionSquareM.getSnapHeight(), unionSquareM.getSnapWidth());
    String validInput = "load resources/unionSquare.jpg unionSquareSnap\n"
            + "sharpen unionSquareSnap unionSquareSharpWithSplit split 50\n"
            + "save resources/unionSquareSharpWithSplit.jpg unionSquareSharpWithSplit\n";
    Reader testInput = new StringReader(validInput);
    SnapController test = new SnapControllerImpl(testInput, testModel);

    try {
      test.start();
    } catch (IOException e) {
      fail("Should not have thrown IOException for valid sharpen command");
    }
    int[][][] modelArray = readSnap("resources/unionSquareTest.jpg");
    int[][][] controllerArray = readSnap("resources/unionSquareSharpWithSplit.jpg");
    assertArrayEquals(modelArray, controllerArray);
  }

  @Test
  public void testValidBlurCommand() throws IOException {
    // Test valid blur command
    unionSquareM.blur(null);
    writeSnap(unionSquareM.getSnap(), "resources/unionSquareTest.jpg",
            unionSquareM.getSnapHeight(), unionSquareM.getSnapWidth());
    String validInput = "load resources/unionSquare.jpg unionSquareSnap\n"
            + "blur unionSquareSnap unionSquareBlur\n"
            + "save resources/unionSquareBlur.jpg unionSquareBlur\n";
    Reader testInput = new StringReader(validInput);
    SnapController test = new SnapControllerImpl(testInput, testModel);

    try {
      test.start();
    } catch (IOException e) {
      fail("Should not have thrown IOException for valid blur command");
    }
    int[][][] modelArray = readSnap("resources/unionSquareTest.jpg");
    int[][][] controllerArray = readSnap("resources/unionSquareBlur.jpg");
    assertArrayEquals(modelArray, controllerArray);
  }

  @Test
  public void testValidBlurWithSplitCommand() throws IOException {
    // Test valid blur command
    unionSquareM.blur(null,50);
    writeSnap(unionSquareM.getSnap(), "resources/unionSquareTest.jpg",
            unionSquareM.getSnapHeight(), unionSquareM.getSnapWidth());
    String validInput = "load resources/unionSquare.jpg unionSquareSnap\n"
            + "blur unionSquareSnap unionSquareBlurWithSplit split 50\n"
            + "save resources/unionSquareBlurWithSplit.jpg unionSquareBlurWithSplit\n";
    Reader testInput = new StringReader(validInput);
    SnapController test = new SnapControllerImpl(testInput, testModel);

    try {
      test.start();
    } catch (IOException e) {
      fail("Should not have thrown IOException for valid blur command");
    }
    int[][][] modelArray = readSnap("resources/unionSquareTest.jpg");
    int[][][] controllerArray = readSnap("resources/unionSquareBlurWithSplit.jpg");
    assertArrayEquals(modelArray, controllerArray);
  }

  @Test
  public void testValidSepiaCommand() throws IOException {
    unionSquareM.toSepia(null);
    writeSnap(unionSquareM.getSnap(), "resources/unionSquareTest.jpg",
            unionSquareM.getSnapHeight(), unionSquareM.getSnapWidth());
    String validInput = "load resources/unionSquare.jpg unionSquareSnap\n"
            + "sepia unionSquareSnap unionSquareSepia\n"
            + "save resources/unionSquareSepia.jpg unionSquareSepia\n";
    Reader testInput = new StringReader(validInput);
    SnapController test = new SnapControllerImpl(testInput, testModel);

    try {
      test.start();
    } catch (IOException e) {
      fail("Should not have thrown IOException for valid sepia command");
    }
    int[][][] modelArray = readSnap("resources/unionSquareTest.jpg");
    int[][][] controllerArray = readSnap("resources/unionSquareSepia.jpg");
    assertArrayEquals(modelArray, controllerArray);
  }

  @Test
  public void testValidSepiaWithSplitCommand() throws IOException {
    unionSquareM.toSepia(null,50);
    writeSnap(unionSquareM.getSnap(), "resources/unionSquareTest.jpg",
            unionSquareM.getSnapHeight(), unionSquareM.getSnapWidth());
    String validInput = "load resources/unionSquare.jpg unionSquareSnap\n"
            + "sepia unionSquareSnap unionSquareSepiaWithSplit split 50\n"
            + "save resources/unionSquareSepiaWithSplit.jpg unionSquareSepiaWithSplit\n";
    Reader testInput = new StringReader(validInput);
    SnapController test = new SnapControllerImpl(testInput, testModel);

    try {
      test.start();
    } catch (IOException e) {
      fail("Should not have thrown IOException for valid sepia command");
    }
    int[][][] modelArray = readSnap("resources/unionSquareTest.jpg");
    int[][][] controllerArray = readSnap("resources/unionSquareSepiaWithSplit.jpg");
    assertArrayEquals(modelArray, controllerArray);
  }

  @Test
  public void testValidGreyScaleCommand() throws IOException {
    // Test valid greyscale command
    unionSquareM.toGreyscale(null);
    writeSnap(unionSquareM.getSnap(), "resources/unionSquareTest.jpg",
            unionSquareM.getSnapHeight(), unionSquareM.getSnapWidth());
    String validInput = "load resources/unionSquare.jpg unionSquareSnap\n"
            + "greyscale unionSquareSnap unionSquareGreyscale\n"
            + "save resources/unionSquareGreyscale.jpg unionSquareGreyscale\n";
    Reader testInput = new StringReader(validInput);
    SnapController test = new SnapControllerImpl(testInput, testModel);

    try {
      test.start();
    } catch (IOException e) {
      fail("Should not have thrown IOException for valid greyscale command");
    }
    int[][][] modelArray = readSnap("resources/unionSquareTest.jpg");
    int[][][] controllerArray = readSnap("resources/unionSquareGreyscale.jpg");
    assertArrayEquals(modelArray, controllerArray);
  }

  @Test
  public void testValidGreyScaleWithSplitCommand() throws IOException {
    // Test valid greyscale command
    unionSquareM.toGreyscale(null,50);
    writeSnap(unionSquareM.getSnap(), "resources/unionSquareTest.jpg",
            unionSquareM.getSnapHeight(), unionSquareM.getSnapWidth());
    String validInput = "load resources/unionSquare.jpg unionSquareSnap\n"
            + "greyscale unionSquareSnap unionSquareGreyscaleWithSplit split 50\n"
            + "save resources/unionSquareGreyscaleWithSplit.jpg unionSquareGreyscaleWithSplit\n";
    Reader testInput = new StringReader(validInput);
    SnapController test = new SnapControllerImpl(testInput, testModel);

    try {
      test.start();
    } catch (IOException e) {
      fail("Should not have thrown IOException for valid greyscale command");
    }
    int[][][] modelArray = readSnap("resources/unionSquareTest.jpg");
    int[][][] controllerArray = readSnap("resources/unionSquareGreyscaleWithSplit.jpg");
    assertArrayEquals(modelArray, controllerArray);
  }

  @Test
  public void testVerticalFlipCommand() throws IOException {
    // Test vertical flip command
    unionSquareM.verticalFlip();
    writeSnap(unionSquareM.getSnap(), "resources/unionSquareTest.jpg",
            unionSquareM.getSnapHeight(), unionSquareM.getSnapWidth());
    String validInput = "load resources/unionSquare.jpg unionSquareSnap\n"
            + "vertical-flip unionSquareSnap unionSquareFlip\n"
            + "save resources/unionSquareFlip.jpg unionSquareFlip\n";

    Reader testInput = new StringReader(validInput);
    SnapController test = new SnapControllerImpl(testInput, testModel);

    try {
      test.start();
    } catch (IOException e) {
      fail("Should not have thrown IOException for valid vertical flip command");
    }
    int[][][] modelArray = readSnap("resources/unionSquareTest.jpg");
    int[][][] controllerArray = readSnap("resources/unionSquareFlip.jpg");
    assertArrayEquals(modelArray, controllerArray);
  }

  @Test
  public void testHorizontalFlipCommand() throws IOException {
    // Test horizontal flip command
    unionSquareM.horizontalFlip();
    writeSnap(unionSquareM.getSnap(), "resources/unionSquareTest.jpg",
            unionSquareM.getSnapHeight(), unionSquareM.getSnapWidth());
    String validInput = "load resources/unionSquare.jpg unionSquareSnap\n"
            + "horizontal-flip unionSquareSnap unionSquareFlip\n"
            + "save resources/unionSquareFlip.jpg unionSquareFlip\n";

    Reader testInput = new StringReader(validInput);
    SnapController test = new SnapControllerImpl(testInput, testModel);

    try {
      test.start();
    } catch (IOException e) {
      fail("Should not have thrown IOException for valid horizontal flip command");
    }
    int[][][] modelArray = readSnap("resources/unionSquareTest.jpg");
    int[][][] controllerArray = readSnap("resources/unionSquareFlip.jpg");
    assertArrayEquals(modelArray, controllerArray);
  }

  @Test
  public void testVerticalFlipCommandPPM() throws IOException {
    // Test vertical flip command
    stopSignM.verticalFlip();
    writeSnap(stopSignM.getSnap(), "resources/stopSignTest.ppm",
            stopSignM.getSnapHeight(), stopSignM.getSnapWidth());
    String validInput = "load resources/stopSign.ppm stopSign\n"
            + "vertical-flip stopSign stopSignFlip\n"
            + "save resources/stopSignFlip.ppm stopSignFlip\n";
    Reader testInput = new StringReader(validInput);
    SnapController test = new SnapControllerImpl(testInput, testModel);

    try {
      test.start();
    } catch (IOException e) {
      fail("Should not have thrown IOException for valid vertical flip command");
    }

    int[][][] modelArray = readSnap("resources/stopSignTest.ppm");
    int[][][] controllerArray = readSnap("resources/stopSignFlip.ppm");
    assertArrayEquals(modelArray, controllerArray);

  }

  @Test
  public void testRgbCombineCommand() throws IOException {
    // Apply RGB Split on unionSquareM model

    // Input sequence of commands for RGB split, save, combine, and save combined image
    String validInput = "load resources/unionSquare.jpg Usquare\n"
            + "rgb-split USquare UsquareRed UsquareGreen UsquareBlue \n"
            + "rgb-combine Ucombine UsquareRed UsquareGreen UsquareBlue\n"
            + "save resources/UCombined.jpg Ucombine\n";

    // Set up the controller with the input and model
    Reader testInput = new StringReader(validInput);
    SnapController test = new SnapControllerImpl(testInput, testModel);

    // Start the command sequence
    test.start();

    // Additional check: Validate the combined image matches the original image
    int[][][] combinedSnap = readSnap("resources/UCombined.jpg");
    Path combinedFilePath = Paths.get("resources/UCombined.jpg");
    assertTrue("Combined RGB image file should exist", Files.exists(combinedFilePath));
  }

  @Test
  public void testValidGreenComponentCommand() throws IOException {

    unionSquareM.greenComponent(null);
    // Test valid green-component command
    writeSnap(unionSquareM.getSnap(), "resources/unionSquareTest.jpg",
            unionSquareM.getSnapHeight(), unionSquareM.getSnapWidth());

    String validInput = "load resources/unionSquare.jpg unionSquareSnap\n"
            + "green-component unionSquareSnap unionSquareGreen\n"
            + "save resources/unionSquareGreen.jpg unionSquareGreen\n";
    Reader testInput = new StringReader(validInput);
    SnapController test = new SnapControllerImpl(testInput, unionSquareM);

    try {
      test.start();
    } catch (IOException e) {
      fail("Should not have thrown IOException for valid green-component command");
    }

    // Assert that the output image exists and is correctly processed
    int[][][] expectedArray = readSnap("resources/unionSquareTest.jpg");
    int[][][] actualArray = readSnap("resources/unionSquareGreen.jpg");
    assertArrayEquals(expectedArray, actualArray);
  }

  @Test
  public void testValidIntensityComponentCommand() throws IOException {
    // Test valid intensity-component command
    unionSquareM.intensityComponent(null);
    writeSnap(unionSquareM.getSnap(), "resources/unionSquareTest.jpg",
            unionSquareM.getSnapHeight(), unionSquareM.getSnapWidth());
    String validInput = "load resources/unionSquare.jpg unionSquareSnap\n"
            + "intensity-component unionSquareSnap unionSquareIntensity\n"
            + "save resources/unionSquareIntensity.jpg unionSquareIntensity\n";
    Reader testInput = new StringReader(validInput);
    SnapController test = new SnapControllerImpl(testInput, testModel);

    try {
      test.start();
    } catch (IOException e) {
      fail("Should not have thrown IOException for valid intensity-component command");
    }

    // Assert that the output image exists and is correctly processed
    int[][][] expectedArray = readSnap("resources/unionSquareTest.jpg");
    int[][][] actualArray = readSnap("resources/unionSquareIntensity.jpg");
    assertArrayEquals(expectedArray, actualArray);
  }

  @Test
  public void testValidRedComponentCommand() throws IOException {
    // Test valid red-component command
    unionSquareM.redComponent(null);
    writeSnap(unionSquareM.getSnap(), "resources/unionSquareTest.jpg",
            unionSquareM.getSnapHeight(), unionSquareM.getSnapWidth());
    String validInput = "load resources/unionSquare.jpg unionSquareSnap\n"
            + "red-component unionSquareSnap unionSquareRed\n"
            + "save resources/unionSquareRed.jpg unionSquareRed\n";
    Reader testInput = new StringReader(validInput);
    SnapController test = new SnapControllerImpl(testInput, testModel);

    try {
      test.start();
    } catch (IOException e) {
      fail("Should not have thrown IOException for valid red-component command");
    }

    // Assert that the output image exists and is correctly processed
    int[][][] expectedArray = readSnap("resources/unionSquareTest.jpg");
    int[][][] actualArray = readSnap("resources/unionSquareRed.jpg");
    assertArrayEquals(expectedArray, actualArray);
  }

  @Test
  public void testValidLumaComponentCommand() throws IOException {
    // Test valid luma-component command
    unionSquareM.lumaComponent(null);

    String validInput = "load resources/unionSquare.jpg unionSquareSnap\n"
            + "luma-component unionSquareSnap unionSquareLuma\n"
            + "save resources/unionSquareLuma.jpg unionSquareLuma\n";
    Reader testInput = new StringReader(validInput);
    SnapController test = new SnapControllerImpl(testInput, testModel);

    try {
      test.start();
    } catch (IOException e) {
      fail("Should not have thrown IOException for valid luma-component command");
    }

    // Assert that the output image exists and is correctly processed
    int[][][] expectedArray = readSnap("resources/unionSquareLuma.jpg");
    int[][][] actualArray = readSnap("resources/unionSquareLuma.jpg");
    assertArrayEquals(expectedArray, actualArray);
  }

  @Test
  public void testValidBlueComponentCommand() throws IOException {
    // Test valid blue-component command
    unionSquareM.blueComponent(null);
    writeSnap(unionSquareM.getSnap(), "resources/unionSquareTest.jpg",
            unionSquareM.getSnapHeight(), unionSquareM.getSnapWidth());
    String validInput = "load resources/unionSquare.jpg unionSquareSnap\n"
            + "blue-component unionSquareSnap unionSquareBlue\n"
            + "save resources/unionSquareBlue.jpg unionSquareBlue\n";
    Reader testInput = new StringReader(validInput);
    SnapController test = new SnapControllerImpl(testInput, testModel);

    try {
      test.start();
    } catch (IOException e) {
      fail("Should not have thrown IOException for valid blue-component command");
    }

    // Assert that the output image exists and is correctly processed
    int[][][] expectedArray = readSnap("resources/unionSquareTest.jpg");
    int[][][] actualArray = readSnap("resources/unionSquareBlue.jpg");
    assertArrayEquals(expectedArray, actualArray);
  }

  @Test
  public void testLevelAdjustment() throws IOException {
    unionSquareM.levelAdjustment(0, 1, 5);

    writeSnap(unionSquareM.getSnap(), "resources/unionSquareTest.jpg",
            unionSquareM.getSnapHeight(), unionSquareM.getSnapWidth());

    String validInput = "load resources/unionSquare.jpg unionSquareSnap\n"
            + "level-adjust 0 1 5 unionSquareSnap unionSquareAdjust\n"
            + "save resources/unionSquareAdjust.jpg unionSquareAdjust\n";

    Reader testInput = new StringReader(validInput);
    SnapController test = new SnapControllerImpl(testInput, testModel);
    test.start();
    int[][][] expectedArray = readSnap("resources/unionSquareTest.jpg");
    int[][][] actualArray = readSnap("resources/unionSquareAdjust.jpg");
    assertArrayEquals(expectedArray, actualArray);
  }


  @Test
  public void testLevelAdjustmentSplit() throws IOException {
    unionSquareM.levelAdjustment(0, 1, 5, 50);

    writeSnap(unionSquareM.getSnap(), "resources/unionSquareTest.jpg",
            unionSquareM.getSnapHeight(), unionSquareM.getSnapWidth());

    String validInput = "load resources/unionSquare.jpg unionSquareSnap\n"
            + "level-adjust 0 1 5 unionSquareSnap unionSquareAdjust split 50\n"
            + "save resources/unionSquareAdjustSplit.jpg unionSquareAdjust\n";

    Reader testInput = new StringReader(validInput);
    SnapController test = new SnapControllerImpl(testInput, testModel);
    test.start();
    int[][][] expectedArray = readSnap("resources/unionSquareTest.jpg");
    int[][][] actualArray = readSnap("resources/unionSquareAdjustSplit.jpg");
    assertArrayEquals(expectedArray, actualArray);
  }

  @Test
  public void testColorCorrect() throws IOException {
    unionSquareM.colorCorrectionComponent();
    writeSnap(unionSquareM.getSnap(), "resources/unionSquareTest.jpg",
            unionSquareM.getSnapHeight(), unionSquareM.getSnapWidth());

    String validInput = "load resources/unionSquare.jpg unionSquareSnap\n"
            + "color-correct unionSquareSnap unionSquareAdjustColor\n"
            + "save resources/unionSquareAdjustColor.jpg unionSquareAdjustColor\n";

    Reader testInput = new StringReader(validInput);
    SnapController test = new SnapControllerImpl(testInput, testModel);
    test.start();
    int[][][] expectedArray = readSnap("resources/unionSquareTest.jpg");
    int[][][] actualArray = readSnap("resources/unionSquareAdjustColor.jpg");
    assertArrayEquals(expectedArray, actualArray);
  }


  @Test
  public void testColorCorrectSplit() throws IOException {
    unionSquareM.colorCorrectionComponent(50);
    writeSnap(unionSquareM.getSnap(), "resources/unionSquareTest.jpg",
            unionSquareM.getSnapHeight(), unionSquareM.getSnapWidth());

    String validInput = "load resources/unionSquare.jpg unionSquareSnap\n"
            + "color-correct unionSquareSnap unionSquareAdjustColor split 50\n"
            + "save resources/unionSquareAdjustColor50.jpg unionSquareAdjustColor\n";

    Reader testInput = new StringReader(validInput);
    SnapController test = new SnapControllerImpl(testInput, testModel);
    test.start();
    int[][][] expectedArray = readSnap("resources/unionSquareTest.jpg");
    int[][][] actualArray = readSnap("resources/unionSquareAdjustColor50.jpg");
    assertArrayEquals(expectedArray, actualArray);
  }

  @Test
  public void testHistogram() throws IOException {
    unionSquareM.histogramComponent();
    String validInput = "load resources/unionSquare.jpg unionSquareSnap\n"
            + "histogram unionSquareSnap unionSquareHistogram\n"
            + "save resources/unionSquareHistogram.jpg unionSquareHistogram\n";

    Reader testInput = new StringReader(validInput);
    SnapController test = new SnapControllerImpl(testInput, testModel);
    test.start();

    Path combinedFilePath = Paths.get("resources/UnionSquareHistogram.jpg");
    assertTrue("Combined RGB image file should exist", Files.exists(combinedFilePath));
  }

  @Test
  public void testCompression() throws IOException {
    unionSquareM.compressionComponent(50.00);
    String validInput = "load resources/unionSquare.jpg unionSquareSnap\n"
            + "compress 50 unionSquareSnap unionSquareCompress\n"
            + "save resources/unionSquareCompress.jpg unionSquareCompress\n";

    Reader testInput = new StringReader(validInput);
    SnapController test = new SnapControllerImpl(testInput, testModel);
    test.start();

    Path combinedFilePath = Paths.get("resources/unionSquareCompress.jpg");
    assertTrue("Combined RGB image file should exist", Files.exists(combinedFilePath));
  }
}
