# USEME

## Supported Script Commands

### 1. `load <imageFilePath> <imageName>`
- **Description**: Loads an image from the specified file path and assigns it a name.
- **Example**: `load resources/images/sample.ppm sampleImage`
- **Conditions**: None

### 2. `save <imageFilePath> <imageName>`
- **Description**: Saves the image with the specified name to the given file path.
- **Example**: `save resources/images/output.ppm sampleImage`
- **Conditions**: The image must be loaded first.

### 3. `red-component <imageName> <newImageName>`
- **Description**: Creates a new image with only the red component of the specified image.
- **Example**: `red-component sampleImage redImage`
- **Conditions**: The image must be loaded first.

### 4. `red-component <imageName> <maskImageName> <newImageName>`
- **Description**: Creates a new image with only the red component of the specified image, using a mask image.
- **Example**: `red-component sampleImage maskImage redImage`
- **Conditions**: The image and mask image must be loaded first.

### 5. `green-component <imageName> <newImageName>`
- **Description**: Creates a new image with only the green component of the specified image.
- **Example**: `green-component sampleImage greenImage`
- **Conditions**: The image must be loaded first.

### 6. `green-component <imageName> <maskImageName> <newImageName>`
- **Description**: Creates a new image with only the green component of the specified image, using a mask image.
- **Example**: `green-component sampleImage maskImage greenImage`
- **Conditions**: The image and mask image must be loaded first.

### 7. `blue-component <imageName> <newImageName>`
- **Description**: Creates a new image with only the blue component of the specified image.
- **Example**: `blue-component sampleImage blueImage`
- **Conditions**: The image must be loaded first.

### 8. `blue-component <imageName> <maskImageName> <newImageName>`
- **Description**: Creates a new image with only the blue component of the specified image, using a mask image.
- **Example**: `blue-component sampleImage maskImage blueImage`
- **Conditions**: The image and mask image must be loaded first.

### 9. `value-component <imageName> <newImageName>`
- **Description**: Creates a new image with the value component of the specified image.
- **Example**: `value-component sampleImage valueImage`
- **Conditions**: The image must be loaded first.

### 10. `value-component <imageName> <maskImageName> <newImageName>`
- **Description**: Creates a new image with the value component of the specified image, using a mask image.
- **Example**: `value-component sampleImage maskImage valueImage`
- **Conditions**: The image and mask image must be loaded first.

### 11. `luma-component <imageName> <newImageName>`
- **Description**: Creates a new image with the luma component of the specified image.
- **Example**: `luma-component sampleImage lumaImage`
- **Conditions**: The image must be loaded first.

### 12. `luma-component <imageName> <maskImageName> <newImageName>`
- **Description**: Creates a new image with the luma component of the specified image, using a mask image.
- **Example**: `luma-component sampleImage maskImage lumaImage`
- **Conditions**: The image and mask image must be loaded first.

### 13. `intensity-component <imageName> <newImageName>`
- **Description**: Creates a new image with the intensity component of the specified image.
- **Example**: `intensity-component sampleImage intensityImage`
- **Conditions**: The image must be loaded first.

### 14. `intensity-component <imageName> <maskImageName> <newImageName>`
- **Description**: Creates a new image with the intensity component of the specified image, using a mask image.
- **Example**: `intensity-component sampleImage maskImage intensityImage`
- **Conditions**: The image and mask image must be loaded first.

### 15. `blur <imageName> <newImageName>`
- **Description**: Applies a blur effect to the specified image.
- **Example**: `blur sampleImage blurImage`
- **Conditions**: The image must be loaded first.

### 16. `blur <imageName> <maskImageName> <newImageName>`
- **Description**: Applies a blur effect to the specified image, using a mask image.
- **Example**: `blur sampleImage maskImage blurImage`
- **Conditions**: The image and mask image must be loaded first.

### 17. `blur <imageName> <newImageName> split <splitPercentage>`
- **Description**: Applies a blur effect to the specified image. Optionally, generates a split view with the specified percentage.
- **Example**: `blur sampleImage blurImage split 50`
- **Conditions**: The image must be loaded first.

### 18. `blur <imageName> <maskImageName> <newImageName> split <splitPercentage>`
- **Description**: Applies a blur effect to the specified image, using a mask image. Optionally, generates a split view with the specified percentage.
- **Example**: `blur sampleImage maskImage blurImage split 50`
- **Conditions**: The image and mask image must be loaded first.

### 19. `greyscale <imageName> <newImageName>`
- **Description**: Converts the specified image to greyscale.
- **Example**: `greyscale sampleImage greyscaleImage` or `greyscale sampleImage greyscaleImage split 50`
- **Conditions**: The image must be loaded first.

### 20. `greyscale <imageName> <maskImageName> <newImageName>`
- **Description**: Converts the specified image to greyscale using a mask image.
- **Example**: `greyscale sampleImage maskImage greyscaleImage`
- **Conditions**: The image and mask image must be loaded first.

### 21 `greyscale <imageName> <newImageName> split <splitPercentage>`
- **Description**: Converts the specified image to greyscale. Optionally, generates a split view with the specified percentage.
- **Example**: `greyscale sampleImage greyscaleImage split 50`
- **Conditions**: The image must be loaded first.

### 22. `greyscale <imageName> <maskImageName> <newImageName> split <splitPercentage>`
- **Description**: Converts the specified image to greyscale using a mask image. Optionally, generates a split view with the specified percentage.
- **Example**: `greyscale sampleImage maskImage greyscaleImage split 50`
- **Conditions**: The image and mask image must be loaded first.

### 23. `sharpen <imageName> <newImageName>`
- **Description**: Applies a sharpen effect to the specified image.
- **Example**: `sharpen sampleImage sharpenImage`
- **Conditions**: The image must be loaded first.

### 24. `sharpen <imageName> <maskImageName> <newImageName>`
- **Description**: Applies a sharpen effect to the specified image using a mask image.
- **Example**: `sharpen sampleImage maskImage sharpenImage`
- **Conditions**: The image and mask image must be loaded first.

### 25. `sharpen <imageName> <newImageName> split <splitPercentage>`
- **Description**: Applies a sharpen effect to the specified image. Optionally, generates a split view with the specified percentage.
- **Example**: `sharpen sampleImage sharpenImage split 50`
- **Conditions**: The image must be loaded first.

### 26. `sharpen <imageName> <maskImageName> <newImageName> split <splitPercentage>`
- **Description**: Applies a sharpen effect to the specified image using a mask image. Optionally, generates a split view with the specified percentage.
- **Example**: `sharpen sampleImage maskImage sharpenImage split 50`
- **Conditions**: The image and mask image must be loaded first.

### 27. `sepia <imageName> <newImageName>`
- **Description**: Applies a sepia effect to the specified image.
- **Example**: `sepia sampleImage sepiaImage`
- **Conditions**: The image must be loaded first.

### 28. `sepia <imageName> <maskImageName> <newImageName>`
- **Description**: Applies a sepia effect to the specified image using a mask image.
- **Example**: `sepia sampleImage maskImage sepiaImage`
- **Conditions**: The image and mask image must be loaded first.

### 29. `sepia <imageName> <newImageName> split <splitPercentage>`
- **Description**: Applies a sepia effect to the specified image. Optionally, generates a split view with the specified percentage.
- **Example**: `sepia sampleImage sepiaImage split 50`
- **Conditions**: The image must be loaded first.

### 30. `sepia <imageName> <maskImageName> <newImageName> split <splitPercentage>`
- **Description**: Applies a sepia effect to the specified image using a mask image. Optionally, generates a split view with the specified percentage.
- **Example**: `sepia sampleImage maskImage sepiaImage split 50`
- **Conditions**: The image and mask image must be loaded first.

### 31. `horizontal-flip <imageName> <newImageName>`
- **Description**: Flips the specified image horizontally.
- **Example**: `horizontal-flip sampleImage hFlipImage`
- **Conditions**: The image must be loaded first.

### 32. `vertical-flip <imageName> <newImageName>`
- **Description**: Flips the specified image vertically.
- **Example**: `vertical-flip sampleImage vFlipImage`
- **Conditions**: The image must be loaded first.

### 33. `brighten <amount> <imageName> <newImageName>`
- **Description**: Adjusts the brightness of the specified image by the given amount.
- **Example**: `brighten 50 sampleImage brightImage`
- **Conditions**: The image must be loaded first.

### 34. `rgb-split <imageName> <newRedImageName> <newGreenImageName> <newBlueImageName>`
- **Description**: Splits the specified image into its RGB components.
- **Example**: `rgb-split sampleImage redImage greenImage blueImage`
- **Conditions**: The image must be loaded first.

### 35. `rgb-combine <newImageName> <redImageName> <greenImageName> <blueImageName>`
- **Description**: Combines the specified RGB component images into a single image.
- **Example**: `rgb-combine combinedImage redImage greenImage blueImage`
- **Conditions**: The RGB component images must be created first.

### 36. `run <scriptFilePath>`
- **Description**: Executes a script file containing a series of commands.
- **Example**: `run scripts/commands.txt`
- **Conditions**: The script file must exist and be readable.

### 37. `exit`
- **Description**: Exits the application.
- **Example**: `exit`
- **Conditions**: None

### 38. `histogram <imageName> <newImageName>`
- **Description**: Generates a histogram for the specified image.
- **Example**: `histogram sampleImage histogramImage`
- **Conditions**: The image must be loaded first.

### 39. `color-correct <imageName> <newImageName> [split p]`
- **Description**: Applies color correction to the specified image. Optionally, generates a split view with the specified percentage.
- **Example**: `color-correct sampleImage correctedImage` or `color-correct sampleImage correctedImage split 50`
- **Conditions**: The image must be loaded first.

### 40. `levels-adjust <blackPoint> <midPoint> <whitePoint> <imageName> <newImageName> [split p]`
- **Description**: Adjusts the levels of the specified image. Optionally, generates a split view with the specified percentage.
- **Example**: `levels-adjust 0 128 255 sampleImage adjustedImage` or `levels-adjust 0 128 255 sampleImage adjustedImage split 50`
- **Conditions**: The image must be loaded first.

### 41. `compress <percentage> <imageName> <newImageName>`
- **Description**: Compresses the specified image by the given percentage.
- **Example**: `compress 30 sampleImage compressedImage`
- **Conditions**: The image must be loaded first.

### 42. `downscale <imageName> <newImageName> <newWidth> <newHeight>`
- **Description**: Downscales the specified image to the given width and height.
- **Example**: `downscale sampleImage downscaledImage 100 100`
- **Conditions**: The image must be loaded first.

### 43. `mask-image <imageName> <width> <height> <maskImageName>`
- **Description**: Creates a mask image with the specified width and height.
- **Example**: `mask-image sampleImage 100 100 maskImage`
- **Conditions**: The image must be loaded first.

## Command Line Argument

### `-file <scriptFilePath>`
- **Description**: Accepts a script file as a command-line option. If a valid file is provided, the program runs the script and exits. If the program is run without any command line options, it allows interactive entry of script commands.
- **Example**: `java -jar ImageProcessor.jar -file scripts/commands.txt`
- **Conditions**: The script file must exist and be readable.

## Using the Graphical User Interface (GUI)

The application supports a graphical user interface (GUI) for users who prefer a visual interaction with the image processing features. Below are the steps to use each operation supported by the program through the GUI.

### 1. Load an Image
- **Action**: Click on the "Load Image" button.
- **Description**: Loads an image from the specified file path and assigns it a name.

### 2. Save an Image
- **Action**: Click on the "Save Image" button.
- **Description**: Saves the image with the specified name to the given file path.

### 3. See Red Component
- **Action**: Click on the "See Red Component" button.
- **Description**: Creates a new image with only the red component of the specified image.

### 4. See Green Component
- **Action**: Click on the "See Green Component" button.
- **Description**: Creates a new image with only the green component of the specified image.

### 5. See Blue Component
- **Action**: Click on the "See Blue Component" button.
- **Description**: Creates a new image with only the blue component of the specified image.

### 7. Greyscale
- **Action**: Click on the "Greyscale" button.
- **Description**: Converts the specified image to greyscale.

### 8. Flip Horizontal
- **Action**: Click on the "Flip Vertical" button.
- **Description**: Flips the specified image horizontally.

### 9. Flip Vertical
- **Action**: Click on the "Flip Vertical" button.
- **Description**: Flips the specified image vertically.

### 10. Adjust Brightenss
- **Action**: Click on the "Adjust Brightenss" button and enter the amount.
- **Description**: Adjusts the brightness of the specified image by the given amount.

### 11. Split RGB
- **Action**: Click on the "Split RGB" button.
- **Description**: Splits the specified image into its RGB components.

### 12. Combine RGB
- **Action**: Click on the "Combine RGB" button.
- **Description**: Combines the specified RGB component images into a single image.

### 13. Blur
- **Action**: Click on the "Blur" button.
- **Description**: Applies a blur effect to the specified image.

### 14. Sharpen
- **Action**: Click on the "Sharpen" button.
- **Description**: Applies a sharpen effect to the specified image.

### 15. Sepia
- **Action**: Click on the "Sepia" button.
- **Description**: Applies a sepia effect to the specified image.

### 16. Histogram
- **Action**: Click on the "Histogram" button to save.
- **Description**: Generates a histogram for the specified image and save it.

### 17. Color Correct
- **Action**: Click on the "Color Correct" button.
- **Description**: Applies color correction to the specified image.

### 18. Levels Adjust
- **Action**: Click on the "Levels Adjust" button and enter the black, mid, and white points.
- **Description**: Adjusts the levels of the specified image.

### 19. Compress
- **Action**: Click on the "Compress" button and enter the percentage.
- **Description**: Compresses the specified image by the given percentage.

### 20. Downscale
- **Action**: Click on the "Downscale" button and enter the new width and height.
- **Description**: Downscales the specified image to the given width and height.

## Command Line Argument

### `-file <scriptFilePath>`
- **Description**: Accepts a script file as a command-line option. If a valid file is provided, the program runs the script and exits. If the program is run without any command line options, it allows interactive entry of script commands.
- **Example**: `java -jar ImageProcessor.jar -file scripts/commands.txt`
- **Conditions**: The script file must exist and be readable.

## Launching the GUI

To launch the GUI, simply run the application without any command-line arguments:

```sh
java -jar Program.jar
java -jar Program.jar -file path-of-script-file 
java -jar Program.jar -text 