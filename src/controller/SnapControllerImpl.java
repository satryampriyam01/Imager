package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import model.Snap;
import model.SnapImpl;
import model.SnapModel;

import static controller.SnapUtil.readSnap;
import static controller.SnapUtil.writeSnap;

/**
 * The {@code SnapControllerImpl} class is responsible for parsing user input, executing commands,
 * and managing interactions between the user and the {@link SnapModel}. It implements the
 * {@link SnapController} interface.
 */
public class SnapControllerImpl implements SnapController {

  public final Map<String, Runnable> executables;
  public final Map<String, int[][][]> modelMap;
  private final Readable input;
  private final SnapModel model;
  public String[] commands;
  public int counter;
  private String lastOutputImage;
  private String fileExtension;

  /**
   * Constructs a new {@code SnapControllerImpl} with the provided input and model.
   *
   * @param input the input source (can be a script or user input)
   * @param model the image processing model to perform operations on
   */
  public SnapControllerImpl(Readable input, SnapModel model) {
    this.input = input;
    this.model = model;
    this.commands = new String[100];
    this.executables = new HashMap<>();
    this.modelMap = new HashMap<>();

    // Register commands
    executables.put("load", new Load());
    executables.put("save", new Save());
    executables.put("blur", new Blur());
    executables.put("brighten", new Brighten());
    executables.put("vertical-flip", new VerticalFlip());
    executables.put("horizontal-flip", new HorizontalFlip());
    executables.put("value-component", new ValueComponent());
    executables.put("rgb-split", new RgbSplit());
    executables.put("rgb-combine", new RgbCombine());
    executables.put("greyscale", new Greyscale());
    executables.put("sepia", new Sepia());
    executables.put("sharpen", new Sharpen());
    executables.put("red-component", new RedComponent());
    executables.put("green-component", new GreenComponent());
    executables.put("blue-component", new BlueComponent());
    executables.put("luma-component", new LumaComponent());
    executables.put("intensity-component", new IntensityComponent());
    executables.put("compress", new CompressComponent());
    executables.put("color-correct", new ColorCorrectionComponent());
    executables.put("histogram", new HistogramComponent());
    executables.put("level-adjust", new LevelAdjustComponent());
    executables.put("downsize", new DownsizeComponent());
    executables.put("maskimage", new CreateMask());
    executables.put("exit", new Exit());
  }

  /**
   * Starts the controller, reading user commands from the input and executing
   * the appropriate operations.
   *
   * @throws IOException if an unknown command is encountered or an I/O operation fails
   */
  @Override
  public void start() throws IOException {
    Scanner scan = new Scanner(input);

    while (scan.hasNextLine()) {
      String line = scan.nextLine().trim();

      if (line.startsWith("#") || line.isEmpty()) {
        continue;
      }

      // Parse input line, respecting quotes
      List<String> tokens = new ArrayList<>();
      Matcher matcher = Pattern.compile("\"([^\"]*)\"|(\\S+)").matcher(line);
      while (matcher.find()) {
        if (matcher.group(1) != null) {
          tokens.add(matcher.group(1)); // Quoted argument
        } else {
          tokens.add(matcher.group(2)); // Non-quoted argument
        }
      }

      commands = tokens.toArray(new String[0]);
      System.out.println("Parsed commands: " + Arrays.toString(commands));

      if (executables.containsKey(commands[0])) {
        System.out.println("Executing command: " + Arrays.toString(commands));
        this.executables.get(commands[0]).run();
      } else {
        System.err.println("Unknown command: " + commands[0]);
        System.err.println("Please check the command syntax or refer to the help documentation.");
      }
    }
  }


  /**
   * Command class for loading an image into the model.
   */
  private class Load implements Runnable {
    @Override
    public void run() {
      if (counter < 3) {
        System.out.println("Invalid Command");
        return;
      }
      try {
        String inputFileName = commands[1];
        String imageName = commands[2];
        int dotIndex = inputFileName.lastIndexOf('.');
        if (dotIndex > 0 && dotIndex < inputFileName.length() - 1) {
          fileExtension = inputFileName.substring(dotIndex);
        } else {
          System.out.println("Error: Unable to determine file extension.");
        }

        int[][][] image = readSnap(inputFileName);
        model.loadSnap(new SnapImpl(image));
        modelMap.put(imageName, image);
        lastOutputImage = commands[2];
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * Command class for saving the image from the model.
   */
  private class Save implements Runnable {
    @Override
    public void run() {
      if (counter < 3) {
        System.out.println("Invalid Command");
        return;
      }
      int[][][] snapToSave = modelMap.get(commands[2]);
      if (snapToSave != null) {
        model.loadSnap(new SnapImpl(snapToSave));
      } else {
        System.out.println("Snap not found for key: " + commands[2]);
        return;
      }
      try {
        writeSnap(snapToSave, commands[1], model.getSnapHeight(), model.getSnapWidth());
        lastOutputImage = commands[2];
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * Command class for brightening an image.
   */
  private class Brighten implements Runnable {
    @Override
    public void run() {
      if (counter < 4) {
        System.out.println("Invalid Command");
        return;
      }

      int value = Integer.parseInt(commands[1]);
      int[][][] snap = modelMap.get(commands[2]);

      if (snap != null) {
        model.loadSnap(new SnapImpl(snap));
      } else {
        System.out.println("Snap not found for key: " + commands[1]);
        return;
      }

      model.brightenessAdjustment(value);
      lastOutputImage = commands[3];
      modelMap.put(lastOutputImage, model.getSnap());
    }
  }

  /**
   * Command class for applying a blur filter to an image.
   */
  private class Blur implements Runnable {
    @Override
    public void run() {
      // Ensure there are at least 2 parameters (source and destination)
      if (counter < 3) {
        System.out.println("Invalid Command");
        return;
      }


      int[][][] snap = modelMap.get(commands[1]);

      // If the snap is found, load it using the SnapImpl constructor
      if (snap != null) {
        model.loadSnap(new SnapImpl(snap));
      } else {
        // If the snap is not found, throw an error and return early
        System.out.println("Snap not found for key: " + commands[1]);
        return;
      }


      // Default mask is null (no mask)
      int[][][] mask = null;

      // Handle the 'blur source-image mask-image dest-image' case
      if (counter == 4) {
        mask = modelMap.get(commands[2]);
        if (mask == null) {
          System.out.println("Mask image not found");
          return;
        }
        model.blur(new SnapImpl(mask));
        lastOutputImage = commands[3];
        modelMap.put(lastOutputImage, model.getSnap());
      }
      // Handle the 'blur source-image dest-image split 70' case
      else if (counter == 5 && commands[3].equals("split")) {
        try {
          int percentage = Integer.parseInt(commands[4]);
          if (percentage < 0 || percentage > 100) {
            System.out.println("Invalid Command");
            return;
          }

          int snapHeight = model.getSnapHeight();
          int snapWidth = model.getSnapWidth();
          model.blur(new SnapImpl(new int[snapHeight][snapWidth][3]), percentage);

          lastOutputImage = commands[commands.length - 3];
          modelMap.put(lastOutputImage, model.getSnap());
        } catch (NumberFormatException e) {
          System.out.println("Invalid Command");
        }
      }
      // Handle the 'blur source-image dest-image' case without a mask
      else if (counter == 3) {
        int snapHeight = model.getSnapHeight();
        int snapWidth = model.getSnapWidth();
        model.blur(new SnapImpl(new int[snapHeight][snapWidth][3]));
        lastOutputImage = commands[commands.length - 1]; // Use the correct destination image name
        modelMap.put(lastOutputImage, model.getSnap());
      }
      // Handle the 'blur source-image mask-image dest-image' case with a mask
      else if (counter == 4 && mask != null) {
        model.blur(new SnapImpl(mask));
        lastOutputImage = commands[commands.length - 1];
        modelMap.put(lastOutputImage, model.getSnap());
      } else {
        System.out.println("Invalid Command");
      }
    }
  }


  /**
   * Command class for applying a sepia filter to an image.
   */
  private class Sepia implements Runnable {
    @Override
    public void run() {
      // Ensure there are at least 2 parameters (source and destination)
      if (counter < 3) {
        System.out.println("Invalid Command");
        return;
      }

      // Attempt to load the snap from the modelMap
      int[][][] snap = modelMap.get(commands[1]);
      if (snap == null) {
        System.out.println("Snap not found for key: " + commands[1]);
        return;
      }

      // Load the snap into the model
      model.loadSnap(new SnapImpl(snap));

      // Default mask is null (no mask)
      int[][][] mask = null;

      // Handle the 'sepia source-image mask-image dest-image' case
      if (counter == 4) {
        mask = modelMap.get(commands[2]);
        if (mask == null) {
          System.out.println("Mask Image not found");
          return;
        }
        // Apply sepia with the mask
        model.toSepia(new SnapImpl(mask));
        lastOutputImage = commands[3]; // Set the output image
      }
      // Handle the 'sepia source-image dest-image split 70' case
      else if (counter == 5 && commands[3].equals("split")) {
        try {
          int percentage = Integer.parseInt(commands[4]);
          if (percentage < 0 || percentage > 100) {
            System.out.println("Invalid Command");
            return;
          }

          // Apply sepia with the mask (or default image dimensions)
          int snapHeight = model.getSnapHeight();
          int snapWidth = model.getSnapWidth();
          model.toSepia(new SnapImpl(new int[snapHeight][snapWidth][3]), percentage);
          lastOutputImage = commands[commands.length - 3]; // Correct destination for split case
        } catch (NumberFormatException e) {
          System.out.println("Invalid Command. split percentage must be a valid integer");
          return;
        }
      }
      // Handle the 'sepia source-image dest-image' case without a mask
      else if (counter == 3) {
        int snapHeight = model.getSnapHeight();
        int snapWidth = model.getSnapWidth();

        model.toSepia(new SnapImpl(new int[snapHeight][snapWidth][3]));
        lastOutputImage = commands[commands.length - 1]; // Set the output image
      } else {
        System.out.println("Invalid Command");
        return;
      }

      // Store the result in modelMap
      modelMap.put(lastOutputImage, model.getSnap());
    }
  }


  /**
   * Command class for applying a sharpen filter to an image.
   */
  private class Sharpen implements Runnable {
    @Override
    public void run() {
      // Ensure there are at least 3 parameters (source-image and dest-image are mandatory)
      if (counter < 3) {
        System.out.println("Invalid Command");
        return;
      }

      // Attempt to load the source snap from the modelMap
      int[][][] sourceSnap = modelMap.get(commands[1]);
      if (sourceSnap == null) {
        System.out.println("Error: source image not found for key: " + commands[1]);
        return;
      }

      // Load the source image into the model
      model.loadSnap(new SnapImpl(sourceSnap));

      Snap mask = null;

      // Case: 'sharpen source-image mask-image dest-image'
      if (counter == 4) {
        int[][][] maskSnap = modelMap.get(commands[2]);
        if (maskSnap == null) {
          System.out.println("Error: mask image not found for key: " + commands[2]);
          return;
        }

        // Load the mask into a Snap object
        mask = new SnapImpl(maskSnap);

        // Apply sharpen transformation with the mask
        model.sharpen(mask);
        lastOutputImage = commands[3]; // Set the destination image
      } else if (counter == 5 && commands[3].equals("split")) {
        try {
          int percentage = Integer.parseInt(commands[4]);
          if (percentage < 0 || percentage > 100) {
            System.out.println("Invalid command.");
            return;
          }

          int snapHeight = model.getSnapHeight();
          int snapWidth = model.getSnapWidth();
          model.sharpen(new SnapImpl(new int[snapHeight][snapWidth][3]), percentage);
          lastOutputImage = commands[commands.length - 3];
        } catch (NumberFormatException e) {
          System.out.println("Error: split percentage must be a valid integer");
          return;
        }
      } else if (counter == 3) {
        model.sharpen(mask);
        lastOutputImage = commands[2]; // Set the destination image
      } else {
        System.out.println("Invalid command");
        return;
      }

      // Store the result in modelMap
      modelMap.put(lastOutputImage, model.getSnap());
    }
  }


  /**
   * Command class for applying a vertical flip to an image.
   */
  private class VerticalFlip implements Runnable {
    @Override
    public void run() {
      if (counter < 3) {
        System.out.println("Invalid command");
        return;
      }
      int[][][] snap = modelMap.get(commands[1]);
      if (snap == null) {
        System.out.println("Snap not found for key: " + commands[1]);
        return;
      }

      // Load the snap into the model
      model.loadSnap(new SnapImpl(snap));
      model.verticalFlip();
      lastOutputImage = commands[2];
      modelMap.put(lastOutputImage, model.getSnap());
    }
  }

  /**
   * Command class for applying a horizontal flip to an image.
   */
  private class HorizontalFlip implements Runnable {
    @Override
    public void run() {
      if (counter < 3) {
        System.out.println("Invalid command");
        return;
      }
      int[][][] snap = modelMap.get(commands[1]);
      if (snap == null) {
        System.out.println("Snap not found for key: " + commands[1]);
        return;
      }

      // Load the snap into the model
      model.loadSnap(new SnapImpl(snap));
      model.horizontalFlip();
      lastOutputImage = commands[2];
      modelMap.put(lastOutputImage, model.getSnap());
    }
  }

  /**
   * Command class for applying a greyscale filter to an image.
   */
  private class Greyscale implements Runnable {
    @Override
    public void run() {
      if (counter < 3) {
        System.out.println("Invalid command");
        return;
      }

      // Attempt to load the snap from the modelMap
      int[][][] snap = modelMap.get(commands[1]);
      if (snap == null) {
        System.out.println("Snap not found for key: " + commands[1]);
        return;
      }

      // Load the snap into the model
      model.loadSnap(new SnapImpl(snap));

      // Default mask is null (no mask)
      int[][][] mask = null;

      // Handle the 'sepia source-image mask-image dest-image' case
      if (counter == 4) {
        mask = modelMap.get(commands[2]);
        if (mask == null) {
          System.out.println("Mask Image not found");
          return;
        }
        // Apply sepia with the mask
        model.toGreyscale(new SnapImpl(mask));
        lastOutputImage = commands[3]; // Set the output image
      }
      // Handle the 'sepia source-image dest-image split 70' case
      else if (counter == 5 && commands[3].equals("split")) {
        try {
          int percentage = Integer.parseInt(commands[4]);
          if (percentage < 0 || percentage > 100) {
            System.out.println("Invalid command");
            return;
          }

          int snapHeight = model.getSnapHeight();
          int snapWidth = model.getSnapWidth();
          model.toGreyscale(new SnapImpl(new int[snapHeight][snapWidth][3]), percentage);
          lastOutputImage = commands[commands.length - 3];
        } catch (NumberFormatException e) {
          System.out.println("Invalid command. split percentage must be a valid integer");
          return;
        }
      } else if (counter == 3) {
        int snapHeight = model.getSnapHeight();
        int snapWidth = model.getSnapWidth();

        model.toGreyscale(new SnapImpl(new int[snapHeight][snapWidth][3]));
        lastOutputImage = commands[commands.length - 1];
      } else {
        System.out.println("Invalid command");
        return;
      }

      // Store the result in modelMap
      modelMap.put(lastOutputImage, model.getSnap());
    }
  }

  /**
   * Command class for splitting an image into RGB channels.
   */
  private class RgbSplit implements Runnable {
    @Override
    public void run() {
      if (commands.length < 5) {
        System.out.println("Invalid command");
        return;
      }

      int[][][] snap = modelMap.get(commands[1]);
      if (snap == null) {
        System.out.println("Snap not found for key: " + commands[1]);
        return;
      }

      // Load the snap into the model
      model.loadSnap(new SnapImpl(snap));
      Snap[] rgbSplitImages = model.applyRGBSplit();

      for (int i = 0; i < rgbSplitImages.length; i++) {
        Snap rgbSplitImage = rgbSplitImages[i];
        modelMap.put(commands[i + 2], rgbSplitImage.getSnap());
      }

      lastOutputImage = commands[1];
    }
  }

  /**
   * Command class for combining RGB channels into a single image.
   */
  private class RgbCombine implements Runnable {
    @Override
    public void run() {
      if (commands.length < 5) {
        System.out.println("Invalid command");
        return;
      }

      String redSplitName = commands[2];
      String greenSplitName = commands[3];
      String blueSplitName = commands[4];
      int[][][] redSnap = modelMap.get(redSplitName);
      int[][][] greenSnap = modelMap.get(greenSplitName);
      int[][][] blueSnap = modelMap.get(blueSplitName);
      if (redSnap == null) {
        System.out.println("Snap not found for key: " + commands[1]);
        return;
      }

      if (greenSnap == null) {
        System.out.println("Snap not found for key: " + commands[1]);
        return;
      }

      if (blueSnap == null) {
        System.out.println("Snap not found for key: " + commands[1]);
        return;
      }

      model.applyRGBCombine(new SnapImpl(redSnap),
              new SnapImpl(greenSnap),
              new SnapImpl(blueSnap));
      lastOutputImage = commands[1];
      modelMap.put(lastOutputImage, model.getSnap());
    }
  }

  /**
   * Command class for retrieving the red component of the current Snap.
   */
  private class RedComponent implements Runnable {
    @Override
    public void run() {
      if (counter < 3) {
        System.out.println("Invalid command");
        return;
      }

      int[][][] sourceSnap = modelMap.get(commands[1]);
      if (sourceSnap == null) {
        System.out.println("Source image not found");
        return;
      }
      model.loadSnap(new SnapImpl(sourceSnap));


      Snap mask = null;

      if (counter == 4) {
        int[][][] maskSnap = modelMap.get(commands[2]);
        if (maskSnap == null) {
          System.out.println("mask image ot found");
          return;
        }
        mask = new SnapImpl(maskSnap);
      }

      // Apply the red-component operation with or without a mask
      model.redComponent(mask);

      lastOutputImage = commands[counter - 1];
      modelMap.put(lastOutputImage, model.getSnap());


      model.redComponent(mask);
      lastOutputImage = commands[2];
      modelMap.put(lastOutputImage, model.getSnap());
    }
  }

  /**
   * Command class for retrieving the green component of the current Snap.
   */
  private class GreenComponent implements Runnable {
    @Override
    public void run() {
      if (counter < 3) {
        System.out.println("Invalid command");
        return;
      }
      int[][][] sourceSnap = modelMap.get(commands[1]);
      if (sourceSnap == null) {
        System.out.println("source image not found");
        return;
      }
      model.loadSnap(new SnapImpl(sourceSnap));
      Snap mask = null;

      if (counter == 4) {
        int[][][] maskSnap = modelMap.get(commands[2]);
        if (maskSnap == null) {
          System.out.println("mask image not found");
          return;
        }
        mask = new SnapImpl(maskSnap);
      }

      model.greenComponent(mask);
      lastOutputImage = commands[counter - 1];
      modelMap.put(lastOutputImage, model.getSnap());
    }
  }

  /**
   * Command class for retrieving the blue component of the current Snap.
   */
  private class BlueComponent implements Runnable {
    @Override
    public void run() {
      if (counter < 3) {
        System.out.println("Invalid command");
        return;
      }

      int[][][] sourceSnap = modelMap.get(commands[1]);
      if (sourceSnap == null) {
        System.out.println("source image not found");
        return;
      }
      model.loadSnap(new SnapImpl(sourceSnap));

      Snap mask = null;
      if (counter == 4) {
        int[][][] maskSnap = modelMap.get(commands[2]);
        if (maskSnap == null) {
          System.out.println("mask image not found");
          return;
        }
        mask = new SnapImpl(maskSnap);
      }
      model.blueComponent(mask);
      lastOutputImage = commands[counter - 1];
      modelMap.put(lastOutputImage, model.getSnap());
    }
  }

  /**
   * Command class for retrieving the luma component of the current Snap.
   */
  private class LumaComponent implements Runnable {
    @Override
    public void run() {
      if (counter < 3) {
        System.out.println("Invalid command");
        return;
      }
      int[][][] sourceSnap = modelMap.get(commands[1]);
      if (sourceSnap == null) {
        System.out.println("Source image not found");
        return;
      }
      model.loadSnap(new SnapImpl(sourceSnap));
      Snap mask = null;
      if (counter == 4) {
        int[][][] maskSnap = modelMap.get(commands[2]);
        if (maskSnap == null) {
          System.out.println("mask image not found");
          return;
        }
        mask = new SnapImpl(maskSnap);
      }
      model.lumaComponent(mask);
      lastOutputImage = commands[counter - 1];

      modelMap.put(lastOutputImage, model.getSnap());
    }
  }

  /**
   * Command class for retrieving the intensity component of the current Snap.
   */
  private class IntensityComponent implements Runnable {
    @Override
    public void run() {
      if (counter < 3) {
        System.out.println("Invalid command");
        return;
      }
      int[][][] sourceSnap = modelMap.get(commands[1]);
      if (sourceSnap == null) {
        System.out.println("source image not found");
        return;
      }
      model.loadSnap(new SnapImpl(sourceSnap));
      Snap mask = null;
      if (counter == 4) {
        int[][][] maskSnap = modelMap.get(commands[2]);
        if (maskSnap == null) {
          System.out.println("mask image not found");
          return;
        }
        mask = new SnapImpl(maskSnap);
      }
      model.intensityComponent(mask);
      lastOutputImage = commands[counter - 1];
      modelMap.put(lastOutputImage, model.getSnap());
    }
  }

  /**
   * Command class for retrieving the value component of the current Snap.
   */
  private class ValueComponent implements Runnable {
    @Override
    public void run() {
      if (counter < 3) {
        System.out.println("Invalid command");
        return;
      }

      int[][][] sourceSnap = modelMap.get(commands[1]);
      if (sourceSnap == null) {
        System.out.println("source image not found");
        return;
      }
      model.loadSnap(new SnapImpl(sourceSnap));
      Snap mask = null;

      if (counter == 4) {
        int[][][] maskSnap = modelMap.get(commands[2]);
        if (maskSnap == null) {
          System.out.println("mask image not found");
          return;
        }
        mask = new SnapImpl(maskSnap);
      }
      model.valueComponent(mask);
      lastOutputImage = commands[counter - 1];
      modelMap.put(lastOutputImage, model.getSnap());
    }
  }

  /**
   * Command class for retrieving the compress component of the current Snap.
   */
  private class CompressComponent implements Runnable {
    @Override
    public void run() {
      if (counter < 4) {
        System.out.println("Invalid command");
        return;
      }

      int[][][] snap = modelMap.get(commands[2]);
      if (snap == null) {
        System.out.println("Snap not found for key: " + commands[2]);
        return;
      }

      // Load the snap into the model
      model.loadSnap(new SnapImpl(snap));

      try {
        int percentage = Integer.parseInt(commands[1]);
        if (percentage < 0 || percentage > 100) {
          System.out.println("Error: Compression percentage "
                  + "must be between 0 and 100.");
          return;
        }

        model.compressionComponent(percentage);

        lastOutputImage = commands[3];
        modelMap.put(lastOutputImage, model.getSnap());
        System.out.println("Image compressed. Use the save command to write it to a file.");
      } catch (NumberFormatException e) {
        System.out.println("Invalid compression percentage");
      }
    }
  }

  /**
   * Command class for retrieving the color-correct component of the current Snap.
   */
  private class ColorCorrectionComponent implements Runnable {
    @Override
    public void run() {
      if (counter < 3) {
        System.out.println("Invalid command");
        return;
      }

      int[][][] snap = modelMap.get(commands[1]);
      if (snap == null) {
        System.out.println("Snap not found for key: " + commands[2]);
        return;
      }

      // Load the snap into the model
      model.loadSnap(new SnapImpl(snap));

      if (counter == 5 && commands[3].equals("split")) {
        try {
          int percentage = Integer.parseInt(commands[4]);
          if (percentage < 0 || percentage > 100) {
            System.out.println("Error: split percentage must be between 0 and 100");
            return;
          }
          model.colorCorrectionComponent(percentage);
        } catch (NumberFormatException e) {
          System.out.println("Error: split percentage must be a valid integer");
          return;
        }
      } else if (counter == 3) {
        model.colorCorrectionComponent();
      } else {
        System.out.println("Error: invalid colour correction command syntax");
        return;
      }
      lastOutputImage = commands[2];
      modelMap.put(lastOutputImage, model.getSnap());
    }
  }

  /**
   * Command class for retrieving the histogram component of the current Snap.
   */
  private class HistogramComponent implements Runnable {
    @Override
    public void run() {
      if (counter < 3) {
        System.out.println("Invalid command");
        return;
      }
      int[][][] snap = modelMap.get(commands[1]);
      if (snap == null) {
        System.out.println("Snap not found for key: " + commands[1]);
        return;
      }

      // Load the snap into the model
      model.loadSnap(new SnapImpl(snap));
      model.histogramComponent();
      System.out.println(commands[2]);
      lastOutputImage = commands[2];
      modelMap.put(lastOutputImage, model.getSnap());
    }
  }

  /**
   * Command class for retrieving the level-adjust component of the current Snap.
   */
  private class LevelAdjustComponent implements Runnable {
    @Override
    public void run() {
      if (counter < 5) {
        System.out.println("Invalid command");
        return;
      }
      int[][][] snap = modelMap.get(commands[4]);
      if (snap == null) {
        System.out.println("Snap not found for key: " + commands[4]);
        return;
      }

      // Load the snap into the model
      model.loadSnap(new SnapImpl(snap));
      int black = Integer.parseInt(commands[1]);
      int middle = Integer.parseInt(commands[2]);
      int white = Integer.parseInt(commands[3]);

      if (black < 0 || black > 255 || middle < 0 || middle > 255 || white < 0 || white > 255) {
        System.out.println("Error: Levels must be between 0 and 255.");
        return;
      }
      if (!(black < middle && middle < white)) {
        System.out.println("Error: Levels must be "
                + "in ascending order (black < mid < white).");
        return;
      }

      if (counter == 8 && commands[6].equals("split")) {
        try {
          int percentage = Integer.parseInt(commands[7]);
          if (percentage < 0 || percentage > 100) {
            System.out.println("Error: split percentage must be between 0 and 100");
            return;
          }
          model.levelAdjustment(black, middle, white, percentage);
        } catch (NumberFormatException e) {
          System.out.println("Error: split percentage must be a valid integer");
          return;
        }
      } else {
        model.levelAdjustment(black, middle, white);
      }

      lastOutputImage = commands[5];
      modelMap.put(lastOutputImage, model.getSnap());
    }
  }

  /**
   * Command class for retrieving the downsize component of the current Snap.
   */
  private class DownsizeComponent implements Runnable {
    @Override
    public void run() {

      if (counter < 5) {
        System.out.println("Invalid command");
        return;
      }

      int[][][] snap = modelMap.get(commands[1]);
      if (snap == null) {
        System.out.println("Snap not found for key: " + commands[1]);
        return;
      }
      // Load the snap into the model
      model.loadSnap(new SnapImpl(snap));
      try {


        int newWidth = Integer.parseInt(commands[3]);
        int newHeight = Integer.parseInt(commands[4]);

        // Validate dimensions
        if (newWidth <= 0 || newHeight <= 0) {
          System.out.println("Error: New dimensions must be positive integers.");
          return;
        }

        // Perform a downsizing operation
        model.downSizeComponent(newWidth, newHeight);

        // Update the model map with the downsized image
        lastOutputImage = commands[2];
        modelMap.put(lastOutputImage, model.getSnap());

      } catch (NumberFormatException e) {
        System.out.println("Error: Invalid dimensions. Width and height must be integers.");
      } catch (UnsupportedOperationException e) {
        System.out.println("Error: Upscaling is not supported in this implementation.");
      }
    }
  }

  /**
   * Command class for retrieving the mask of the current Snap.
   */
  private class CreateMask implements Runnable {
    @Override
    public void run() {
      if (counter < 6) {
        System.out.println("Invalid command.");
        return;
      }
      int[][][] snap = modelMap.get(commands[1]);
      if (snap == null) {
        System.out.println("Snap not found for key: " + commands[1]);
        return;
      }

      model.loadSnap(new SnapImpl(snap));
      try {

        int xStart = Integer.parseInt(commands[3]);
        int yStart = Integer.parseInt(commands[4]);
        int width = Integer.parseInt(commands[5]);
        int height = Integer.parseInt(commands[6]);

        model.createMaskComponent(xStart, yStart, width, height);
      } catch (NumberFormatException e) {
        System.out.println("Error: All dimensions must be valid integers.");
        return;
      }

      lastOutputImage = commands[2];
      modelMap.put(lastOutputImage, model.getSnap());
    }
  }

  /**
   * Command class for exiting the program.
   */
  private class Exit implements Runnable {
    @Override
    public void run() {
      System.out.println("Exiting the program. Goodbye!");
      System.exit(0); // Terminates the program
    }
  }


}

