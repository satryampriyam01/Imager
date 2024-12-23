import org.junit.Before;
import org.junit.Test;

import model.Snap;
import model.SnapImpl;
import model.SnapModelImpl;
import model.filter.HistogramGenerator;
import model.filter.HistogramGeneratorImpl;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Test class contains unit tests for the SnapController class.
 *
 * <p>Tests cover various scenarios, including but not limited to:
 * <ul>
 *   <li>Loading and saving images in different formats</li>
 *   <li>Image transformations and manipulations</li>
 *   <li>Handling of invalid input and edge cases</li>
 * </ul>
 *
 * <p>This class uses the JUnit framework for testing purposes.
 */
public class SnapModelTest {

  private SnapModelImpl snapModel;
  private Snap mockSnap;

  @Before
  public void setUp() {
    snapModel = new SnapModelImpl();
    int[][][] pixels = {
            {{255, 0, 0}, {0, 255, 0}, {0, 0, 255}, {255, 255, 0}},
            {{255, 0, 255}, {0, 255, 255}, {255, 255, 255}, {0, 0, 0}},
            {{128, 128, 128}, {64, 64, 64}, {192, 192, 192}, {32, 32, 32}},
            {{255, 100, 50}, {50, 50, 50}, {100, 100, 100}, {150, 150, 150}}
    };
    mockSnap = new SnapImpl(pixels);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testLoadEmptySnap() {
    snapModel.loadSnap(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testBlurOnEmptySnap() {
    snapModel.loadSnap(null);
    snapModel.blur(null);
    assertNull(snapModel.getSnap());
  }

  @Test
  public void testNegativeBrightnessAdjustment() {
    snapModel.loadSnap(mockSnap);
    snapModel.brightenessAdjustment(-50); // Adjust brightness down
    int[][][] expectedBrightnessAdjustedMatrix = {
            {{205, 0, 0}, {0, 205, 0}, {0, 0, 205}, {205, 205, 0}},
            {{205, 0, 205}, {0, 205, 205}, {205, 205, 205}, {0, 0, 0}},
            {{78, 78, 78}, {14, 14, 14}, {142, 142, 142}, {0, 0, 0}},
            {{205, 50, 0}, {0, 0, 0}, {50, 50, 50}, {100, 100, 100}}
    };
    assertArrayEquals(expectedBrightnessAdjustedMatrix, snapModel.getSnap());
  }

  @Test
  public void testCompressionZero() {

    snapModel.loadSnap(mockSnap);

    snapModel.compressionComponent(0.0);

    int[][][] snapExpected = {
            {{254, 0, 0}, {0, 254, 0}, {0, 0, 254}, {254, 254, 0}},
            {{254, 0, 254}, {0, 254, 254}, {254, 254, 254}, {0, 0, 0}},
            {{127, 127, 127}, {63, 63, 63}, {191, 191, 191}, {31, 31, 31}},
            {{254, 99, 49}, {49, 49, 49}, {99, 99, 99}, {149, 149, 149}}
    };


    assertArrayEquals(snapExpected, snapModel.getSnap());

  }

  @Test
  public void testCompressionhalf() {
    snapModel.loadSnap(mockSnap);

    snapModel.compressionComponent(50.0);

    int[][][] snapExpected = {
            {{251, 0, 0}, {0, 254, 0}, {0, 0, 254}, {251, 254, 0}},
            {{251, 0, 254}, {0, 254, 254}, {251, 254, 254}, {0, 0, 0}},
            {{139, 130, 72}, {75, 73, 72}, {159, 181, 198}, {54, 21, 38}},
            {{244, 130, 72}, {39, 73, 72}, {89, 76, 93}, {194, 126, 143}}
    };


    assertArrayEquals(snapExpected, snapModel.getSnap());
  }

  @Test
  public void testLoadSnap() {
    snapModel.loadSnap(mockSnap);
    assertEquals(4, snapModel.getSnapWidth());
    assertEquals(4, snapModel.getSnapHeight());
  }

  // TESTCASES FOR BRIGHTENING THE IMAGES
  @Test
  public void testBrightnessAdjustment() {
    snapModel.loadSnap(mockSnap);
    snapModel.brightenessAdjustment(10);
    int[][][] expectedBrightnessAdjustedMatrix = {
            {{255, 10, 10}, {10, 255, 10}, {10, 10, 255}, {255, 255, 10}},
            {{255, 10, 255}, {10, 255, 255}, {255, 255, 255}, {10, 10, 10}},
            {{138, 138, 138}, {74, 74, 74}, {202, 202, 202}, {42, 42, 42}},
            {{255, 110, 60}, {60, 60, 60}, {110, 110, 110}, {160, 160, 160}}
    };
    assertArrayEquals(expectedBrightnessAdjustedMatrix, snapModel.getSnap());
  }

  @Test
  public void testBrightenWithSinglePixelImage() {
    int[][][] singlePixelImage = {
            {{128, 128, 128}}
    };

    Snap singlePixelSnap = new SnapImpl(singlePixelImage);
    snapModel.loadSnap(singlePixelSnap);
    snapModel.brightenessAdjustment(50);

    int[][][] resultImage = snapModel.getSnap();
    int[][][] expectedImage = {
            {{178, 178, 178}}
    };

    assertArrayEquals(expectedImage, resultImage);
  }

  @Test
  public void testBrightenNegativeValue() {
    int[][][] inputImage = {
            {{100, 100, 100}, {150, 150, 150}},
            {{200, 200, 200}, {50, 50, 50}}
    };
    Snap inputSnap = new SnapImpl(inputImage);
    snapModel.loadSnap(inputSnap);
    snapModel.brightenessAdjustment(-50);
    int[][][] expectedOutput = {
            {{50, 50, 50}, {100, 100, 100}},
            {{150, 150, 150}, {0, 0, 0}} // Clamped at 0
    };
    assertArrayEquals(expectedOutput, snapModel.getSnap());
  }

  @Test
  public void testBrightenZeroValue() {
    int[][][] inputImage = {
            {{100, 100, 100}, {150, 150, 150}},
            {{200, 200, 200}, {50, 50, 50}}
    };
    Snap inputSnap = new SnapImpl(inputImage);
    snapModel.loadSnap(inputSnap);
    snapModel.brightenessAdjustment(0);
    int[][][] expectedOutput = {
            {{100, 100, 100}, {150, 150, 150}},
            {{200, 200, 200}, {50, 50, 50}}
    };
    assertArrayEquals(expectedOutput, snapModel.getSnap());
  }

  @Test
  public void testBrightenWithNullInputSnap() {
    try {
      snapModel.brightenessAdjustment(50);
    } catch (IllegalArgumentException e) {
      assertEquals("No Snap loaded. Please load a Snap "
              + "before adjusting brightness.", e.getMessage());
    }
  }

  @Test
  public void testBrightenWithOutOfBoundsColors() {
    int[][][] inputImage = {
            {{300, 300, 300}, {200, 200, 200}},
            {{150, 150, 150}, {100, 100, 100}}
    };

    Snap inputSnap = new SnapImpl(inputImage);
    snapModel.loadSnap(inputSnap);
    snapModel.brightenessAdjustment(100);

    int[][][] expectedOutput = {
            {{255, 255, 255}, {255, 255, 255}},
            {{250, 250, 250}, {200, 200, 200}}
    };

    assertArrayEquals(expectedOutput, snapModel.getSnap());
  }

  //TESTCASES FOR BLURRING IMAGES

  @Test
  public void testBlur() {
    snapModel.loadSnap(mockSnap);
    snapModel.blur(null);
    int[][][] expectedBlurredMatrix = {
            {{96, 48, 48}, {64, 112, 96}, {64, 112, 112}, {80, 80, 48}},
            {{116, 68, 116}, {108, 156, 172}, {110, 158, 158}, {80, 80, 64}},
            {{107, 72, 97}, {116, 122, 135}, {117, 133, 133}, {73, 73, 73}},
            {{90, 51, 39}, {86, 67, 60}, {80, 80, 80}, {67, 67, 67}}
    };

    assertArrayEquals(expectedBlurredMatrix, snapModel.getSnap());
  }

  @Test
  public void testBlurWithoutLoadedSnap() {
    try {
      snapModel.blur(null);
    } catch (IllegalArgumentException e) {
      assertEquals("No Snap loaded. Please load a Snap "
              + "before blurring.", e.getMessage());
    }
  }

  @Test
  public void testBlurWithExtremeColorValues() {
    int[][][] extremeImage = {
            {{255, 255, 255}, {0, 0, 0}},
            {{128, 128, 128}, {255, 0, 255}}
    };

    Snap extremeSnap = new SnapImpl(extremeImage);
    snapModel.loadSnap(extremeSnap);
    snapModel.blur(null);
    int[][][] resultImage = snapModel.getSnap();
    int[][][] expectedImage = {
            {{96, 80, 96}, {72, 40, 72}},
            {{96, 64, 96}, {96, 32, 96}}
    };

    assertArrayEquals(expectedImage, resultImage);
  }

  @Test
  public void testBlurWithSinglePixelImage() {
    int[][][] singlePixelImage = {
            {{128, 128, 128}}
    };

    Snap singlePixelSnap = new SnapImpl(singlePixelImage);
    snapModel.loadSnap(singlePixelSnap);
    snapModel.blur(null);
    int[][][] resultImage = snapModel.getSnap();

    int[][][] expectedImage = {
            {{32, 32, 32}}
    };

    assertArrayEquals(expectedImage, resultImage);
  }

  // TESTCASES FOR BLURRING AND BRIGHTENING THE IMAGES
  @Test
  public void testBrightenAndBlurCombinedEffects() {
    int[][][] initialImage = {
            {{100, 100, 100}},
    };

    Snap initialSnap = new SnapImpl(initialImage);
    snapModel.loadSnap(initialSnap);
    snapModel.brightenessAdjustment(50);
    snapModel.blur(null);

    int[][][] blurredImage = snapModel.getSnap();
    int[][][] expectedBlurredImage = {
            {{38, 38, 38}}
    };
    assertArrayEquals(expectedBlurredImage, blurredImage);
  }

  @Test
  public void testBlurThenBrightenCombinedEffects() {
    int[][][] initialImage = {
            {{100, 100, 100}},
    };

    Snap initialSnap = new SnapImpl(initialImage);
    snapModel.loadSnap(initialSnap);
    snapModel.blur(null);
    snapModel.brightenessAdjustment(50);

    int[][][] finalImage = snapModel.getSnap();
    int[][][] expectedFinalImage = {
            {{75, 75, 75}},
    };

    assertArrayEquals(expectedFinalImage, finalImage);
  }

  //TESTCASES FOR SHARPENING THE IMAGE

  @Test
  public void testSharpen() {
    snapModel.loadSnap(mockSnap);
    snapModel.sharpen(null);
    int[][][] expectedSharpenedMatrix = {
            {{239, 48, 16}, {108, 255, 204}, {12, 204, 255}, {255, 219, 60}},
            {{255, 90, 255}, {183, 255, 255}, {242, 255, 255}, {139, 75, 107}},
            {{185, 146, 198}, {255, 248, 255}, {212, 255, 255}, {161, 97, 129}},
            {{216, 61, 0}, {148, 109, 65}, {111, 131, 105}, {185, 153, 153}}
    };
    assertArrayEquals(expectedSharpenedMatrix, snapModel.getSnap());
  }

  @Test
  public void testSharpenSinglePixelImage() {
    int[][][] singlePixelImage = {
            {{120, 120, 120}}
    };
    Snap singlePixelSnap = new SnapImpl(singlePixelImage);
    snapModel.loadSnap(singlePixelSnap);
    snapModel.sharpen(null);

    int[][][] expectedSharpenedImage = {
            {{120, 120, 120}}
    };

    assertArrayEquals(expectedSharpenedImage, snapModel.getSnap());
  }

  @Test
  public void testSharpenWithExtremeColorValues() {
    int[][][] extremeImage = {
            {{0, 0, 0}, {255, 255, 255}},
            {{255, 0, 255}, {0, 255, 255}}
    };

    Snap extremeSnap = new SnapImpl(extremeImage);
    snapModel.loadSnap(extremeSnap);
    snapModel.sharpen(null);
    int[][][] resultImage = snapModel.getSnap();

    int[][][] expectedSharpenedImage = {
            {{128, 128, 192}, {255, 255, 255}},
            {{255, 128, 255}, {128, 255, 255}}
    };

    assertArrayEquals(expectedSharpenedImage, resultImage);
  }

  @Test
  public void testSharpenAlreadySharpenedImage() {
    snapModel.loadSnap(mockSnap);

    snapModel.sharpen(null);
    snapModel.sharpen(null);

    int[][][] expectedSharpenedMatrix = {
            {{255, 61, 42}, {192, 255, 255}, {82, 255, 255}, {240, 214, 78}},
            {{255, 202, 255}, {255, 255, 255}, {255, 255, 255}, {237, 189, 217}},
            {{255, 194, 255}, {255, 255, 255}, {255, 255, 255}, {255, 154, 212}},
            {{238, 64, 0}, {248, 205, 125}, {199, 236, 185}, {186, 156, 158}}
    };

    assertArrayEquals(expectedSharpenedMatrix, snapModel.getSnap());
  }

  @Test
  public void testSharpenCompletelyBlackImage() {
    int[][][] blackImage = {
            {{0, 0, 0}, {0, 0, 0}},
            {{0, 0, 0}, {0, 0, 0}}
    };
    Snap blackSnap = new SnapImpl(blackImage);
    snapModel.loadSnap(blackSnap);

    snapModel.sharpen(null);

    int[][][] expectedSharpenedImage = {
            {{0, 0, 0}, {0, 0, 0}},
            {{0, 0, 0}, {0, 0, 0}}
    };

    assertArrayEquals(expectedSharpenedImage, snapModel.getSnap());
  }

  @Test
  public void testSharpenCompletelyWhiteImage() {
    int[][][] whiteImage = {
            {{255, 255, 255}, {255, 255, 255}},
            {{255, 255, 255}, {255, 255, 255}}
    };
    Snap whiteSnap = new SnapImpl(whiteImage);
    snapModel.loadSnap(whiteSnap);

    snapModel.sharpen(null);

    int[][][] expectedSharpenedImage = {
            {{255, 255, 255}, {255, 255, 255}},
            {{255, 255, 255}, {255, 255, 255}}
    };

    assertArrayEquals(expectedSharpenedImage, snapModel.getSnap());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSharpenNullImage() {
    snapModel.loadSnap(null);
    snapModel.sharpen(null);
  }

  @Test
  public void testSharpenSmall2x2Image() {
    int[][][] smallImage = {
            {{50, 100, 150}, {200, 100, 50}},
            {{255, 200, 150}, {100, 150, 200}}
    };
    Snap smallSnap = new SnapImpl(smallImage);
    snapModel.loadSnap(smallSnap);

    snapModel.sharpen(null);

    int[][][] expectedSharpenedImage = {
            {{189, 213, 251}, {255, 213, 176}},
            {{255, 255, 251}, {227, 250, 255}}
    };

    assertArrayEquals(expectedSharpenedImage, snapModel.getSnap());
  }

  //TESTCASES FOR SHARPEN BLUR AND BRIGHTEN THE IMAGES
  @Test
  public void testBrightenBlurAndSharpenCombinedEffects() {
    int[][][] initialImage = {
            {{100, 100, 100}, {150, 150, 150}},
            {{200, 200, 200}, {250, 250, 250}}
    };

    Snap initialSnap = new SnapImpl(initialImage);
    snapModel.loadSnap(initialSnap);

    snapModel.brightenessAdjustment(50);  // Brighten by 50
    snapModel.blur(null);                     // Blur image
    snapModel.sharpen(null);                  // Sharpen image

    int[][][] finalImage = snapModel.getSnap();
    int[][][] expectedImage = {
            {{203, 203, 203}, {209, 209, 209}},
            {{216, 216, 216}, {218, 218, 218}}
    };

    assertArrayEquals(expectedImage, finalImage);
  }

  @Test
  public void testBlurAndSharpenCombinedEffects() {
    int[][][] initialImage = {
            {{100, 100, 100}, {150, 150, 150}},
            {{200, 200, 200}, {250, 250, 250}}
    };

    Snap initialSnap = new SnapImpl(initialImage);
    snapModel.loadSnap(initialSnap);

    // Apply Blur and then Sharpen
    snapModel.blur(null);       // Blur image
    snapModel.sharpen(null);    // Sharpen image

    int[][][] finalImage = snapModel.getSnap();
    int[][][] expectedImage = {
            {{163, 163, 163}, {170, 170, 170}},
            {{176, 176, 176}, {184, 184, 184}}
    };

    assertArrayEquals(expectedImage, finalImage);
  }

  @Test
  public void testBrightenAndSharpenCombinedEffects() {
    int[][][] initialImage = {
            {{0, 0, 0}, {0, 0, 0}},
            {{0, 0, 0}, {0, 0, 0}}
    };

    Snap initialSnap = new SnapImpl(initialImage);
    snapModel.loadSnap(initialSnap);

    // Apply Brighten and then Sharpen
    snapModel.brightenessAdjustment(50);  // Brighten by 50
    snapModel.sharpen(null);                  // Sharpen image

    int[][][] finalImage = snapModel.getSnap();
    int[][][] expectedImage = {
            {{89, 89, 89}, {89, 89, 89}},
            {{89, 89, 89}, {89, 89, 89}}
    };

    assertArrayEquals(expectedImage, finalImage);
  }

  @Test
  public void testSharpenBlurAndBrightenCombinedEffects() {
    int[][][] initialImage = {
            {{100, 100, 100}, {150, 150, 150}},
            {{200, 200, 200}, {250, 250, 250}}
    };

    Snap initialSnap = new SnapImpl(initialImage);
    snapModel.loadSnap(initialSnap);

    // Apply Sharpen, Blur, and Brighten
    snapModel.sharpen(null);                  // Sharpen image
    snapModel.blur(null);                     // Blur image
    snapModel.brightenessAdjustment(50);  // Brighten by 50

    int[][][] finalImage = snapModel.getSnap();
    int[][][] expectedImage = {
            {{193, 193, 193}, {193, 193, 193}},
            {{193, 193, 193}, {194, 194, 194}}
    };

    assertArrayEquals(expectedImage, finalImage);
  }

  @Test
  public void testBlurBrightenAndSharpenCombinedEffects() {
    int[][][] initialImage = {
            {{100, 100, 100}, {150, 150, 150}},
            {{200, 200, 200}, {250, 250, 250}}
    };

    Snap initialSnap = new SnapImpl(initialImage);
    snapModel.loadSnap(initialSnap);

    // Apply Blur, Brighten, and Sharpen
    snapModel.blur(null);                     // Blur image
    snapModel.brightenessAdjustment(50);  // Brighten by 50
    snapModel.sharpen(null);                  // Sharpen image

    int[][][] finalImage = snapModel.getSnap();
    int[][][] expectedImage = {
            {{250, 250, 250}, {255, 255, 255}},
            {{255, 255, 255}, {255, 255, 255}}
    };

    assertArrayEquals(expectedImage, finalImage);
  }

  // TESTCASES FOR SEPIA
  @Test
  public void testToSepia() {
    snapModel.loadSnap(mockSnap);
    snapModel.toSepia(null);
    int[][][] expectedSepiaMatrix = {
            {{100, 89, 69}, {196, 175, 136}, {48, 43, 33}, {255, 255, 206}},
            {{148, 132, 103}, {244, 218, 170}, {255, 255, 239}, {0, 0, 0}},
            {{173, 154, 120}, {86, 77, 60}, {255, 231, 180}, {43, 38, 30}},
            {{187, 166, 129}, {68, 60, 47}, {135, 120, 94}, {203, 180, 141}}
    };
    assertArrayEquals(expectedSepiaMatrix, snapModel.getSnap());
  }

  @Test
  public void testToGreyscale() {
    snapModel.loadSnap(mockSnap);
    snapModel.toGreyscale(null);
    int[][][] expectedGreyscaleMatrix = {
            {{54, 54, 54}, {182, 182, 182}, {18, 18, 18}, {237, 237, 237}},
            {{73, 73, 73}, {201, 201, 201}, {255, 255, 255}, {0, 0, 0}},
            {{128, 128, 128}, {64, 64, 64}, {192, 192, 192}, {32, 32, 32}},
            {{129, 129, 129}, {50, 50, 50}, {100, 100, 100}, {150, 150, 150}}
    };
    assertArrayEquals(expectedGreyscaleMatrix, snapModel.getSnap());
  }

  @Test
  public void testHorizontalFlip() {
    snapModel.loadSnap(mockSnap);
    snapModel.horizontalFlip();
    int[][][] expectedFlippedMatrix = {
            {{255, 255, 0}, {0, 0, 255}, {0, 255, 0}, {255, 0, 0}},
            {{0, 0, 0}, {255, 255, 255}, {0, 255, 255}, {255, 0, 255}},
            {{32, 32, 32}, {192, 192, 192}, {64, 64, 64}, {128, 128, 128}},
            {{150, 150, 150}, {100, 100, 100}, {50, 50, 50}, {255, 100, 50}}
    };
    assertArrayEquals(expectedFlippedMatrix, snapModel.getSnap());
  }

  @Test
  public void testVerticalFlip() {
    snapModel.loadSnap(mockSnap);
    snapModel.verticalFlip();
    int[][][] expectedFlippedMatrix = {
            {{255, 100, 50}, {50, 50, 50}, {100, 100, 100}, {150, 150, 150}},
            {{128, 128, 128}, {64, 64, 64}, {192, 192, 192}, {32, 32, 32}},
            {{255, 0, 255}, {0, 255, 255}, {255, 255, 255}, {0, 0, 0}},
            {{255, 0, 0}, {0, 255, 0}, {0, 0, 255}, {255, 255, 0}}
    };
    assertArrayEquals(expectedFlippedMatrix, snapModel.getSnap());
  }

  @Test
  public void testApplyRGBCombine() {
    int[][][] redChannel = {
            {{255, 0, 0}, {255, 0, 0}},
            {{255, 0, 0}, {255, 0, 0}}
    };

    int[][][] greenChannel = {
            {{0, 255, 0}, {0, 255, 0}},
            {{0, 255, 0}, {0, 255, 0}}
    };

    int[][][] blueChannel = {
            {{0, 0, 255}, {0, 0, 255}},
            {{0, 0, 255}, {0, 0, 255}}
    };

    Snap redSnap = new SnapImpl(redChannel);
    Snap greenSnap = new SnapImpl(greenChannel);
    Snap blueSnap = new SnapImpl(blueChannel);

    snapModel.loadSnap(redSnap);
    snapModel.loadSnap(greenSnap);
    snapModel.loadSnap(blueSnap);

    snapModel.applyRGBCombine(redSnap, greenSnap, blueSnap);

    int[][][] expectedCombinedSnap = {
            {{255, 255, 255}, {255, 255, 255}},
            {{255, 255, 255}, {255, 255, 255}}
    };

    int[][][] combinedSnap = snapModel.getSnap();
    assertArrayEquals(expectedCombinedSnap, combinedSnap);
  }

  @Test
  public void testSinglePixelSnap() {
    int[][][] singlePixel = {{{255, 0, 0}}};
    Snap singleSnap = new SnapImpl(singlePixel);
    snapModel.loadSnap(singleSnap);

    snapModel.toGreyscale(null);
    int[][][] expectedGreyscaleMatrix = {{{54, 54, 54}}};
    assertArrayEquals(expectedGreyscaleMatrix, snapModel.getSnap());
  }

  @Test
  public void testAllBlackSnap() {
    int[][][] blackSnapPixels = {
            {{0, 0, 0}, {0, 0, 0}},
            {{0, 0, 0}, {0, 0, 0}}
    };
    Snap blackSnap = new SnapImpl(blackSnapPixels);
    snapModel.loadSnap(blackSnap);

    snapModel.brightenessAdjustment(50);
    int[][][] expectedBrightnessAdjustedMatrix = {
            {{50, 50, 50}, {50, 50, 50}},
            {{50, 50, 50}, {50, 50, 50}}
    };
    assertArrayEquals(expectedBrightnessAdjustedMatrix, snapModel.getSnap());
  }

  @Test
  public void testAllWhiteSnap() {
    int[][][] whiteSnapPixels = {
            {{255, 255, 255}, {255, 255, 255}},
            {{255, 255, 255}, {255, 255, 255}}
    };
    Snap whiteSnap = new SnapImpl(whiteSnapPixels);
    snapModel.loadSnap(whiteSnap);

    snapModel.brightenessAdjustment(-50);
    int[][][] expectedBrightnessAdjustedMatrix = {
            {{205, 205, 205}, {205, 205, 205}},
            {{205, 205, 205}, {205, 205, 205}}
    };
    assertArrayEquals(expectedBrightnessAdjustedMatrix, snapModel.getSnap());
  }

  @Test
  public void testHistogramSnap() {
    int[][][] pixels = {
            {{255, 0, 0}, {0, 255, 0}, {0, 0, 255}, {255, 255, 0}},
            {{255, 0, 255}, {0, 255, 255}, {255, 255, 255}, {0, 0, 0}},
            {{128, 128, 128}, {64, 64, 64}, {192, 192, 192}, {32, 32, 32}},
            {{255, 100, 50}, {50, 50, 50}, {100, 100, 100}, {150, 150, 150}}
    };
    Snap snap = new SnapImpl(pixels);
    snapModel.loadSnap(snap);
    snapModel.histogramComponent();
    HistogramGenerator histogramGenerator = new HistogramGeneratorImpl();
    int[][] frequencies = histogramGenerator.getFrequencies(mockSnap);
    int[] expectedReds = new int[256];
    expectedReds[0] = 4; // 0
    expectedReds[32] = 1; // 32
    expectedReds[50] = 1; // 50
    expectedReds[64] = 1; // 64
    expectedReds[100] = 1; // 100
    expectedReds[128] = 1; // 128
    expectedReds[150] = 1; // 150
    expectedReds[192] = 1; // 192
    expectedReds[255] = 5; // 255

    int[] expectedGreens = new int[256];
    expectedGreens[0] = 4; // 0
    expectedGreens[32] = 1; // 32
    expectedGreens[50] = 1; // 50
    expectedGreens[64] = 1; // 64
    expectedGreens[100] = 2; // 100
    expectedGreens[128] = 1; // 128
    expectedGreens[150] = 1; // 150
    expectedGreens[192] = 1; // 192
    expectedGreens[255] = 4; // 255

    int[] expectedBlues = new int[256];
    expectedBlues[0] = 4; // 0
    expectedBlues[32] = 1;
    expectedBlues[50] = 2; // 50
    expectedBlues[64] = 1; // 64
    expectedBlues[100] = 1; // 100
    expectedBlues[128] = 1; // 128
    expectedBlues[150] = 1; // 150
    expectedBlues[192] = 1; // 192
    expectedBlues[255] = 4; // 255

    assertArrayEquals(expectedReds, frequencies[0]);
    assertArrayEquals(expectedGreens, frequencies[1]);
    assertArrayEquals(expectedBlues, frequencies[2]);
  }

  @Test
  public void testExtractValueComponent() {
    int[][][] pixelData = {
            {{0, 0, 0}, {255, 255, 255}},
            {{100, 150, 200}, {50, 75, 100}}
    };
    Snap currentSnap = new SnapImpl(pixelData);
    snapModel.loadSnap(currentSnap);
    snapModel.valueComponent(null);
    int[][][] expectedValueData = {
            {{0, 0, 0}, {255, 255, 255}},
            {{200, 200, 200}, {100, 100, 100}}
    };
    assertArrayEquals(expectedValueData, snapModel.getSnap());

  }

  @Test
  public void testExtractRedComponent() {
    int[][][] pixelData = {
            {{0, 0, 0}, {255, 255, 255}},
            {{100, 150, 200}, {50, 75, 100}}
    };
    Snap currentSnap = new SnapImpl(pixelData);
    snapModel.loadSnap(currentSnap);
    snapModel.redComponent(null);
    int[][][] expectedValueData = {
            {{0, 0, 0}, {255, 0, 0}},
            {{100, 0, 0}, {50, 0, 0}}
    };
    assertArrayEquals(expectedValueData, snapModel.getSnap());

  }

  @Test
  public void testExtractBlueComponent() {
    int[][][] pixelData = {
            {{0, 0, 0}, {255, 255, 255}},
            {{100, 150, 200}, {50, 75, 100}}
    };
    Snap currentSnap = new SnapImpl(pixelData);
    snapModel.loadSnap(currentSnap);
    snapModel.blueComponent(null);
    int[][][] expectedValueData = {
            {{0, 0, 0}, {0, 0, 255}},
            {{0, 0, 200}, {0, 0, 100}}
    };
    assertArrayEquals(expectedValueData, snapModel.getSnap());

  }

  @Test
  public void testExtractGreenComponent() {
    int[][][] pixelData = {
            {{0, 0, 0}, {255, 255, 255}},
            {{100, 150, 200}, {50, 75, 100}}
    };
    Snap currentSnap = new SnapImpl(pixelData);
    snapModel.loadSnap(currentSnap);
    snapModel.greenComponent(null);
    int[][][] expectedValueData = {
            {{0, 0, 0}, {0, 255, 0}},
            {{0, 150, 0}, {0, 75, 0}}
    };
    assertArrayEquals(expectedValueData, snapModel.getSnap());

  }

  @Test
  public void testExtractLumaComponent() {
    int[][][] pixelData = {
            {{0, 0, 0}, {255, 255, 255}},
            {{100, 150, 200}, {50, 75, 100}}
    };
    Snap currentSnap = new SnapImpl(pixelData);
    snapModel.loadSnap(currentSnap);
    snapModel.lumaComponent(null);
    int[][][] expectedValueData = {
            {{0, 0, 0}, {255, 255, 255}},
            {{141, 141, 141}, {70, 70, 70}}
    };
    assertArrayEquals(expectedValueData, snapModel.getSnap());

  }

  @Test
  public void testIntensityComponent() {
    int[][][] pixelData = {
            {{0, 0, 0}, {255, 255, 255}},
            {{100, 150, 200}, {50, 75, 100}}
    };
    Snap currentSnap = new SnapImpl(pixelData);
    snapModel.loadSnap(currentSnap);
    snapModel.intensityComponent(null);
    int[][][] expectedValueData = {
            {{0, 0, 0}, {255, 255, 255}},
            {{150, 150, 150}, {75, 75, 75}}
    };
    assertArrayEquals(expectedValueData, snapModel.getSnap());

  }

  @Test
  public void testLevelAdjustment() {
    int[][][] pixelData = {
            {{100, 150, 200}, {50, 50, 50}},
            {{255, 255, 255}, {0, 0, 0}}
    };
    Snap currentSnap = new SnapImpl(pixelData);
    snapModel.loadSnap(currentSnap);
    snapModel.levelAdjustment(0, 0, 1);
    int[][][] expectedValueData = {
            {{0, 0, 0}, {0, 0, 0}},
            {{0, 0, 0}, {0, 0, 0}}
    };
    assertArrayEquals(expectedValueData, snapModel.getSnap());
  }


  @Test
  public void testColorCorrection() {
    int[][][] pixelData = {
            {{0, 250, 200}, {250, 50, 50}},
            {{255, 255, 255}, {250, 0, 0}}
    };
    Snap currentSnap = new SnapImpl(pixelData);
    snapModel.loadSnap(currentSnap);
    snapModel.colorCorrectionComponent(50);
    int[][][] expectedValueData = {
            {{0, 255, 217}, {250, 50, 50}},
            {{222, 255, 255}, {250, 0, 0}}
    };
    assertArrayEquals(expectedValueData, snapModel.getSnap());

  }


  @Test
  public void testLevelsAdjust() {
    int[][][] pixels = {
            {{0, 0, 200}, {0, 50, 50}},
            {{255, 255, 255}, {0, 0, 0}}
    };
    // Normal case: Adjusting levels
    Snap currentSnap = new SnapImpl(pixels);
    snapModel.loadSnap(currentSnap);
    snapModel.levelAdjustment(0, 10, 205);
    assertNotNull(snapModel.getSnap());
    int[][][] expectedValueData = {
            {{0, 0, 255}, {0, 255, 255}}, // Expected values after correction
            {{0, 0, 0}, {0, 0, 0}}
    };

    assertArrayEquals(expectedValueData, snapModel.getSnap());

  }

  @Test(expected = IllegalArgumentException.class)
  public void testColorCorrectionInvalidSplitPercentage() {
    // Setup: Initialize snap with known pixel values
    int[][][] pixelData = {
            {{100, 150, 200}, {50, 50, 50}},
            {{255, 255, 255}, {0, 0, 0}}
    };
    Snap snap = new SnapImpl(pixelData); // Assume SnapImpl constructor exists
    snapModel.loadSnap(snap);
    snapModel.colorCorrectionComponent(-10);

  }

  @Test
  public void testColorCorrectionNoSplit() {
    // Setup: Initialize snap with known pixel values
    int[][][] pixelData = {
            {{100, 150, 200}, {50, 50, 50}},
            {{255, 255, 255}, {0, 0, 0}}
    };
    Snap snap = new SnapImpl(pixelData); // Assume SnapImpl constructor exists
    snapModel.loadSnap(snap);
    snapModel.colorCorrectionComponent();
    int[][][] expectedValueData = {
            {{100, 150, 200}, {50, 50, 50}},
            {{255, 255, 255}, {0, 0, 0}}
    };

    assertArrayEquals(expectedValueData, snapModel.getSnap()); // Expect original values
  }


  @Test
  public void testColorCorrectionWithValidSplit() {
    // Setup: Initialize snap with known pixel values
    int[][][] pixelData = {
            {{255, 0, 0}, {0, 255, 0}, {0, 0, 255}, {255, 255, 0}},
            {{255, 0, 255}, {0, 255, 255}, {255, 255, 255}, {0, 0, 0}},
            {{128, 128, 128}, {64, 64, 64}, {192, 192, 192}, {32, 32, 32}},
            {{255, 100, 50}, {50, 50, 50}, {100, 100, 100}, {150, 150, 150}}
    };
    Snap snap = new SnapImpl(pixelData);

    snapModel.loadSnap(snap);
    snapModel.colorCorrectionComponent(50);

    int[][][] expectedValueData = {
            {{227, 40, 0}, {0, 255, 0}, {0, 0, 255}, {255, 255, 0}},
            {{227, 40, 245}, {0, 255, 245}, {255, 255, 255}, {0, 0, 0}},
            {{100, 168, 118}, {36, 104, 54}, {192, 192, 192}, {32, 32, 32}},
            {{227, 140, 40}, {22, 90, 40}, {100, 100, 100}, {150, 150, 150}}
    };
    assertArrayEquals(expectedValueData, snapModel.getSnap());
  }

  @Test
  public void testMask() {
    snapModel.loadSnap(mockSnap);

    snapModel.createMaskComponent(0, 0, 2, 2);


    int[][][] expectedValueData = {
            {{0, 0, 0}, {0, 0, 0}, {255, 255, 255}, {255, 255, 255}},
            {{0, 0, 0}, {0, 0, 0}, {255, 255, 255}, {255, 255, 255}},
            {{255, 255, 255}, {255, 255, 255}, {255, 255, 255}, {255, 255, 255}},
            {{255, 255, 255}, {255, 255, 255}, {255, 255, 255}, {255, 255, 255}}
    };

    assertArrayEquals(expectedValueData, snapModel.getSnap());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMaskInvalidInputs() {
    snapModel.loadSnap(mockSnap);
    snapModel.createMaskComponent(-1, 0, 0, 2);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMaskInvalidWidth() {
    snapModel.loadSnap(mockSnap);
    snapModel.createMaskComponent(0, 0, 100, 2);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMaskInvalidHeight() {
    snapModel.loadSnap(mockSnap);
    snapModel.createMaskComponent(0, 0, 1, -2);
  }

  @Test
  public void testDownScaling() {
    snapModel.loadSnap(mockSnap);
    snapModel.downSizeComponent(2, 2);
    int[][][] expectedValueData = {
            {{255, 0, 0}, {0, 0, 255}},
            {{128, 128, 128}, {192, 192, 192}}
    };
    // System.out.println(Arrays.deepToString(snapModel.getSnap()));
    assertArrayEquals(expectedValueData, snapModel.getSnap());

  }


  @Test(expected = NegativeArraySizeException.class)
  public void testDownScalingInvalidInput() {
    snapModel.loadSnap(mockSnap);
    snapModel.downSizeComponent(-1, 2);
  }

  @Test
  public void testApplyRGBSplit() {
    int[][][] mockImage = {
            // First pixel (Red), Second pixel (Green), Third pixel (Blue)
            {{255, 0, 0}, {0, 255, 0}, {0, 0, 255}}
    };


    Snap mockSnap = new SnapImpl(mockImage);
    snapModel.loadSnap(mockSnap);

    Snap[] splitSnaps = snapModel.applyRGBSplit();

    int[][][] expectedRedChannel = {
            {{255, 0, 0}, {0, 0, 0}, {0, 0, 0}}  // Red channel only
    };

    int[][][] expectedGreenChannel = {
            {{0, 0, 0}, {0, 255, 0}, {0, 0, 0}}  // Green channel only
    };

    int[][][] expectedBlueChannel = {
            {{0, 0, 0}, {0, 0, 0}, {0, 0, 255}}  // Blue channel only
    };


    int[][][] redChannel = splitSnaps[0].getSnap();
    int[][][] greenChannel = splitSnaps[1].getSnap();
    int[][][] blueChannel = splitSnaps[2].getSnap();


    assertArrayEquals(expectedRedChannel, redChannel);
    assertArrayEquals(expectedGreenChannel, greenChannel);
    assertArrayEquals(expectedBlueChannel, blueChannel);
  }

  @Test
  public void testSnapImplFileNotFound() {
    String nonExistentFile = "nonExistent.snap";

    Exception exception = assertThrows(RuntimeException.class, () -> {
      new SnapImpl(nonExistentFile);
    });

    String expectedMessage = "Error reading snap " + nonExistentFile;
    assertTrue(exception.getMessage().contains(expectedMessage));
  }


}
