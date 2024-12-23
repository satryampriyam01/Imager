package controller;

import java.io.IOException;

/**
 * This interface defines the controller for managing the Snap application.
 */
public interface SnapController {

  /**
   * Starts the Snap application, allowing the user to interact with the system.
   *
   * @throws IOException if there is an error in reading input or writing output
   */
  void start() throws IOException;
}
