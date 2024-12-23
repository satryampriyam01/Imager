import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import javax.swing.SwingUtilities;

import controller.SnapController;
import controller.SnapControllerImpl;
import model.SnapModel;
import model.SnapModelImpl;
import view.MainView;

/**
 * The Main class for the Imager application.
 * It serves as the entry point to the program and runs the application in either script mode,
 * interactive text mode, or GUI mode based on the command-line arguments provided.
 */
public class Main {

  /**
   * The main method that starts the Imager application.
   *
   * @param args Command-line arguments that determine the mode of operation.
   *
   * @throws IOException if there is an error reading the script file.
   */
  public static void main(String[] args) throws IOException {
    // Create a new instance of the SnapModel
    SnapModel model = new SnapModelImpl();
    SnapController controller;

    // If the command-line argument is "-file" followed by a script file, run in script mode
    if (args.length == 2 && args[0].equals("-file")) {
      try (Reader input = new FileReader(args[1])) {
        controller = new SnapControllerImpl(input, model);
        controller.start();
      } catch (IOException e) {
        throw new IOException("Error reading script file: " + e.getMessage(), e);
      }
    }
    // If the command-line argument is "-text", start interactive text mode
    else if (args.length == 1 && args[0].equals("-text")) {
      System.out.println("Entering interactive mode. Type commands:");
      try (Reader input = new InputStreamReader(System.in)) {
        controller = new SnapControllerImpl(input, model);
        controller.start();
      }
    }
    // If no command-line arguments, start the graphical user interface (GUI) mode
    else if (args.length == 0) {
      SwingUtilities.invokeLater(() -> {
        try {
          MainView view = new MainView(); // Pass the model to the view if needed
          view.setVisible(true); // Display the GUI
        } catch (Exception e) {
          e.printStackTrace();
        }
      });
    }
    // Handle invalid command-line arguments
    else {
      System.out.println("Exiting the program.");
      throw new IllegalArgumentException("Error: Invalid command. "
              + "Use '-file <script_file_path>' for script mode, "
              + "'-text' for interactive text mode, or no arguments for GUI mode.");
    }
  }
}
