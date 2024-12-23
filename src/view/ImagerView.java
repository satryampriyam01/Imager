package view;

/**
 * Represents the view interface for an image processing application. This interface defines
 * methods to display the application, manage script commands, and execute them.
 */
public interface ImagerView {

  /**
   * Displays the main application window.
   */
  void display();

  /**
   * Adds a command to the script file.
   *
   * @param command   the command to be added (e.g., load, save, transform).
   */
  void addCommandToScript(String command, String ...args);

  /**
   * Executes all the commands present in the script file.
   */
  void executeCommands();
}
