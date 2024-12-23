# Imager: an Image Processing Application

**Authors**:

- Satyam Priyam (priyam.s@northeastern.edu),

- Apurva Saini (saini.ap@northeastern.edu)

**Disclosure**: This is an academic project for Northeastern University’s CS-5010 Program Design
Paradigms course.
---
## Acknowledgements

This program could not have been written without the guidance of the CS-5010 professors and Teaching
Assistants, whose support was invaluable in helping us produce what we believe to be a
well-organized program.
---

## Overview

Imager is an image processing program designed to take in an image or create a new one and perform
pixel-level operations to produce a desired result. This project is structured around the
Model-View-Controller (MVC) design pattern, adhering to solid design principles to ensure
scalability, maintainability, and ease of use. The command-line interface (CLI) and Graphics User
Interface (GUI)
allows users to
load, modify, and save images by sending commands directly to the controller.

---

## Features

- **Load Images**: Load `.ppm` , `.jpeg` ,`.png` and many more formats of images into the program.
- **Save Images**: Save processed images to the desired directory.
- **Image Manipulation**:
    - Brightening or darkening images
    - Flipping images vertically or horizontally
    - Converting images to grayscale
    - Splitting and combining RGB channels to apply color transformations
    - Level Adjustment (black mid white)
    - Blur Images
    - Value Component
    - Red-Component extraction
    - Green-Component extraction
    - Blue-Component extraction
    - Compression of Image
    - Luma-component
    - Intensity-component
    - Color-correction of image
    - Histogram generation
    - Image Downscaling
    - Particular Image Manipulation  (blur, sharpen, greyscale, sepia, component visualizations)

---

## Design Changes

**Design Changes:**

1. **View**: Utilizes Java Swing components to construct the GUI, incorporating menus, labels,
   buttons, and scroll panes. Adopts layout managers like GridBagLayout and BoxLayout for responsive
   UI design.
2. **View Model**: Implements a ViewModel as an adapter to the main model, providing methods
   tailored for the view's requirements.
3. **Model**: Introduces interface for compress, color correction, and levels adjustment operations.
4. **Controller**: Adds new classes (Image Downscaling, Partial Image manipulation) to facilitate
   interaction between controller and model for new operations. Updates existing classes to support
   split view operations .

## How to Run

1. java -jar Program.jar -file path-of-script-file : when invoked in this manner the program should
   open the script file, execute it, and then shut down.

2. java -jar Program.jar -text : when invoked in this manner the program should open in an
   interactive text mode, allowing the user to type the script and execute it one line at a time.

3. java -jar Program.jar : when invoked in this manner the program should open the graphical user
   interface. This is what will happen if you simply double-click on the jar file.



```bash
java Imager -script <script_file>
Here is how the program works:

A FileReader reads the script file.
The controller parses the input commands and interacts with the model, which processes the images based on the instructions.
Example commands you can include in your script file:

# Load a  image and give it the name 'jpeg'
load resources/cat.jpg cat

# Brighten the image by 10 units
brighten 10 cat catload

# Save the cat brighter image
save resources/catBright.jpg catload

# Split the image into RGB components
rgb-split catCombine cat-red cat-green cat-blue

```

## Design Decisions: Model-View-Controller (MVC) Architecture

Model: Represents the core data and business logic. This includes the image data processing.
In our case, SnapModel holds the image data, performs transformations (e.g., blur, grayscale), and
exposes methods to retrieve the transformed images.

The SnapModel interacts with the image data but remains independent of the controller and view,
making it reusable and easily testable.
Operations such as blur(), toGreyscale(), and save() are part of the model.

View: Responsible for presenting the data to the user. The view in this project reads the image
data (3D array format) from the model and saves the output to the file system. It does not perform
any image processing itself.

Controller: The controller manages interactions between the view and the model. It listens for
commands, interacts with the model to manipulate data, and updates the view. For example, it parses
user input or command files, applies the corresponding image transformations via the model, and
updates the output using the view.

Separation of Concerns: By following the MVC pattern, the project allows each layer to evolve
independently. For example, the image processing logic can change in the model without affecting how
users interact with the system via the controller or view.
---

## v1.0 (Initial Release)

- Implemented command-line interface (CLI) for interacting with the program.

- Core image processing features (load, save, brighten, flip, grayscale, etc.) included.

## v2.0(Second Release),

- Compression Feature
- Histogram generation feature
- Level-adjustment added
- Color-correction feature added
- Split view Added for previously implemented features such as sepia, gray-scale, and many more.

We have introduced a new model named Effects and an interface called HistogramGenerator.

Effects Model: This model is solely responsible for the compression of images. It encapsulates all
the logic and operations related to image compression, ensuring that it adheres to the Single
Responsibility Principle (SRP) by focusing only on compression tasks.

HistogramGenerator Interface: This interface is dedicated to generating histograms of images. It
defines the necessary methods for calculating and providing the histogram data of the images.

As part of our design approach, we continue to follow SOLID principles and maintain the MVC (
Model-View-Controller) design pattern.

Additionally, the Command-Line Pattern is still employed for the controller, which allows us to
manage commands efficiently in the application.

## v.3.0(Production Release)

Latest Release version

- Image Downscaling
- Masking of Image (blur, sharpen, greyscale, sepia, component visualizations)

We follow the MVC (Model-View-Controller) design pattern to separate concerns, which makes the application easier to maintain, test, and scale.
By isolating the Model (data and logic), View (UI and presentation), and Controller (user input handling), we ensure that changes in one component don’t affect others. This structure promotes parallel development, improves code reuse, and allows for flexible UI changes without impacting the core logic. Ultimately, MVC enhances maintainability, testability, and scalability, making the system more modular and adaptable to future changes.
---

## Core Classes and Their Responsibilities

SnapModel: Manages image data and transformations. This is where image manipulations like blur,
grayscale, etc., are applied.

SnapController: Acts as an intermediary between user inputs and the model, processing commands and
interacting with the model to apply transformations.

SnapImpl: Represents the actual image as a 3D pixel array. It can be instantiated from either a file
or an array, making it flexible for testing and use cases.

SnapUtil: Provides utility functions to read and write PPM images. By using methods such as
readSnap(), getWidth(), and getHeight(), the system abstracts image I/O into reusable, testable
code.

## Installation

Download the project files to your system.
Compile the .java files using a Java compiler. For example:
bash
Copy code
javac Imager.java
Run the compiled program:
bash
Copy code
java Imager -script <script_file>
For more information on running .java files on your operating system, consult online resources like
Google.

## License and Attribution

The following images are owned by the author and are authorized for use:

- Priyam, Satyam. “cat.jpg”
- Priyam, Satyam. “signStop.ppm”
- Priyam, Satyam. "cat.png"

## Bugs

No known bugs at this time. If you discover one, please report it to the authors using the provided
contact information.

## Contributions / Hacking

We are open to suggestions for improving this program. Feel free to reach out to us for feedback or
ideas on how to enhance this project. We’re always looking for ways to improve, so any guidance or
recommendations are appreciated. Thanks in advance!
