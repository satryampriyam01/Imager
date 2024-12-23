package view;

import java.awt.Image;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Cursor;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JOptionPane;
import javax.swing.JFileChooser;
import javax.swing.JScrollPane;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import javax.swing.SwingConstants;
import javax.swing.BorderFactory;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import controller.SnapControllerImpl;
import model.SnapModel;
import model.SnapModelImpl;

/**
 * The MainView class provides the graphical user interface (GUI) for the Imager application.
 * It allows users to interact with the image processing features, view images,
 * apply transformations, and monitor activities. This class extends JFrame
 * and implements the ImagerView interface.
 * It supports displaying images, showing the output of operations, and managing user commands.
 */
public class MainView extends JFrame implements ImagerView {

  private String imageNameWithoutExtension;
  private SnapModel model;
  private JLabel imageLabel;
  private JLabel alteredImageLabel;
  private final Map<String, Image> tempImageStorage;
  private JPanel activityMonitorPanel;
  private JLabel histogramImageLabel;

  /**
   * Constructs the MainView instance, initializing components and setting up the GUI.
   * Sets up the default values, such as title, layout, and temporary image storage.
   */


  public MainView() {
    tempImageStorage = new HashMap<>();
    initializeView();
  }

  @Override
  public void display() {
    setVisible(true);
  }

  private void initializeView() {
    setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    setSize(1000, 800);
    setLayout(new BorderLayout());
    setBackground(Color.DARK_GRAY);

    model = new SnapModelImpl();

    JPanel mainPanel = new JPanel(new BorderLayout());
    mainPanel.setBackground(Color.LIGHT_GRAY);
    mainPanel.add(createControlPanel(), BorderLayout.NORTH);
    mainPanel.add(createImagePanel(), BorderLayout.CENTER);
    mainPanel.add(createAlteredImagePanel(), BorderLayout.SOUTH);
    mainPanel.add(createActivityMonitorPanel(), BorderLayout.EAST);
    mainPanel.add(createHistogramPanel(), BorderLayout.WEST);

    add(mainPanel, BorderLayout.CENTER);

    addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent e) {
        int option = JOptionPane.showConfirmDialog(
                null,
                "Are you sure you want to exit?",
                "Exit Confirmation",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        // If 'Yes' is selected, exit the application
        if (option == JOptionPane.YES_OPTION) {
          System.exit(0);  // Exit the application
        }
      }
    });
  }

  private JScrollPane createHistogramPanel() {
    histogramImageLabel = new JLabel();
    JPanel histogramPanel;
    histogramPanel = new JPanel();
    histogramPanel.setBorder(new TitledBorder("Histogram"));
    JScrollPane histogramScrollPane = new JScrollPane(histogramPanel);
    histogramPanel.add(histogramImageLabel);
    histogramScrollPane.setPreferredSize(new Dimension(450, 400));
    return histogramScrollPane;
  }

  private JScrollPane createActivityMonitorPanel() {
    JScrollPane activityScrollPane;
    activityMonitorPanel = new JPanel();
    activityMonitorPanel.setLayout(new BoxLayout(activityMonitorPanel, BoxLayout.Y_AXIS));
    activityMonitorPanel.setBackground(Color.WHITE);
    activityMonitorPanel.setBorder(new TitledBorder("Activity Monitor"));

    activityScrollPane = new JScrollPane(activityMonitorPanel);
    activityScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    activityScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    activityScrollPane.setPreferredSize(new Dimension(150, 400));
    return activityScrollPane;
  }

  private JPanel createImagePanel() {
    JScrollPane loadedImage;
    JPanel imagePanel = new JPanel();
    imagePanel.setBackground(Color.WHITE);
    imagePanel.setBorder(new TitledBorder("Loaded Image"));
    imagePanel.setLayout(new BoxLayout(imagePanel, BoxLayout.Y_AXIS));
    imageLabel = new JLabel();

    loadedImage = new JScrollPane(imageLabel);
    loadedImage.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    loadedImage.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    loadedImage.setPreferredSize(new Dimension(200, 400));

    imagePanel.add(loadedImage);
    return imagePanel;
  }

  private JPanel createAlteredImagePanel() {
    JPanel imagePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
    imagePanel.setBackground(Color.WHITE);
    imagePanel.setBorder(new TitledBorder("Altered Image"));
    imagePanel.setLayout(new BoxLayout(imagePanel, BoxLayout.Y_AXIS));

    alteredImageLabel = new JLabel();
    JScrollPane alteredImage = new JScrollPane(alteredImageLabel);
    alteredImage.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    alteredImage.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    alteredImage.setPreferredSize(new Dimension(200, 300));

    imagePanel.add(alteredImage);
    return imagePanel;
  }

  private JPanel createControlPanel() {
    JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
    controlPanel.setBackground(new Color(230, 230, 250));
    controlPanel.setBorder(new TitledBorder("Controls"));

    controlPanel.add(createLoadButton());
    controlPanel.add(createActionDropdownPanel());
    controlPanel.add(createSaveButton());
    return controlPanel;
  }

  private JPanel createActionDropdownPanel() {
    JPanel dropdownPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    dropdownPanel.setBackground(new Color(230, 230, 250));

    JComboBox<String> actionDropdown = createActionDropdown();
    JButton applyButton = createApplyButton(actionDropdown);
    JButton cancelButton = createCancelButton(actionDropdown);

    dropdownPanel.add(actionDropdown);
    dropdownPanel.add(applyButton);
    dropdownPanel.add(cancelButton);

    return dropdownPanel;
  }

  private JButton createLoadButton() {
    JButton loadButton = new JButton("Load", new ImageIcon("icons/load.png"));
    loadButton.setHorizontalTextPosition(SwingConstants.RIGHT); // Text next to the icon
    loadButton.setPreferredSize(new Dimension(120, 40)); // Set a fixed size
    loadButton.setToolTipText("Load an image file.");
    loadButton.addActionListener(e -> handleLoadAction());
    styleButton(loadButton);
    return loadButton;
  }

  private JButton createSaveButton() {
    JButton saveButton = new JButton("Save", new ImageIcon("icons/save.png"));
    saveButton.setHorizontalTextPosition(SwingConstants.RIGHT); // Text next to the icon
    saveButton.setPreferredSize(new Dimension(120, 40)); // Set a fixed size
    saveButton.setToolTipText("Save the modified image.");
    saveButton.addActionListener(e -> handleSaveAction());
    styleButton(saveButton);
    return saveButton;
  }

  private JComboBox<String> createActionDropdown() {
    String[] transformations = {"blur", "blur-split", "brighten", "vertical-flip",
      "horizontal-flip", "value-component", "greyscale", "greyscale-split",
      "sepia", "sepia-split", "sharpen", "sharpen-split", "red-component",
      "green-component", "blue-component", "luma-component", "intensity-component",
      "compress", "color-correct", "color-correct-split", "histogram",
      "level-adjust", "level-adjust-split", "downsize"};

    JComboBox<String> actionDropdown = new JComboBox<>(transformations);
    actionDropdown.setToolTipText("Choose a transformation to apply.");
    styleComboBox(actionDropdown);
    return actionDropdown;
  }

  private JButton createApplyButton(JComboBox<String> actionDropdown) {
    JButton applyButton = new JButton("Apply", new ImageIcon("icons/apply.png"));
    applyButton.setToolTipText("Apply the selected transformation.");
    applyButton.setPreferredSize(new Dimension(100, 30));

    applyButton.addActionListener(e -> {
      String action = (String) actionDropdown.getSelectedItem();
      if (imageNameWithoutExtension != null && action != null) {
        try {
          int saveOption = JOptionPane.showConfirmDialog(this,
                  "Would you like to save the file before applying the filter ?",
                  "Save File",
                  JOptionPane.YES_NO_OPTION,
                  JOptionPane.QUESTION_MESSAGE);

          if (saveOption == JOptionPane.YES_OPTION) {
            handleSaveAction();
          }
          handleActionSelection(action, imageNameWithoutExtension);
        } catch (Exception ex) {
          JOptionPane.showMessageDialog(this, "Error applying action: "
                  + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
      } else {
        JOptionPane.showMessageDialog(this,
                "Please load an image and select a valid action.",
                "Error", JOptionPane.ERROR_MESSAGE);
      }
    });

    styleButton(applyButton);
    return applyButton;
  }

  private JButton createCancelButton(JComboBox<String> actionDropdown) {
    JButton cancelButton = new JButton("Cancel", new ImageIcon("icons/cancel.png"));
    cancelButton.setToolTipText("Cancel the selected transformation.");
    cancelButton.setPreferredSize(new Dimension(100, 30));

    cancelButton.addActionListener(e -> {
      actionDropdown.setSelectedIndex(0);
      JOptionPane.showMessageDialog(this,
              "Action canceled.", "Info", JOptionPane.INFORMATION_MESSAGE);
    });

    styleButton(cancelButton);
    return cancelButton;
  }

  private void styleButton(JButton button) {
    button.setFocusPainted(false);
    button.setFont(new Font("Arial", Font.BOLD, 14));
    button.setBackground(new Color(173, 216, 230));
    button.setBorder(BorderFactory.createRaisedBevelBorder());
    button.setMargin(new Insets(5, 10, 5, 10));
  }

  private void styleComboBox(JComboBox<?> comboBox) {
    comboBox.setFont(new Font("Arial", Font.PLAIN, 14));
    comboBox.setBackground(new Color(245, 245, 245));
    comboBox.setBorder(BorderFactory.createEtchedBorder());
  }

  private void handleLoadAction() {
    JFileChooser fileChooser = new JFileChooser();
    if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
      String filePath = fileChooser.getSelectedFile().getAbsolutePath();

      clearScriptFile();
      String inputName = getInputName("Please input the name:");
      if (inputName == null) {
        return;  // If Cancel was clicked, exit the method without doing anything
      } else {
        imageNameWithoutExtension = inputName;
      }

      // Ensure the input name is not blank
      while (inputName.trim().isEmpty()) {
        // Prompt the user again until a valid name is entered
        inputName = getInputName("Name cannot be blank. Please input the name:");

        // If the user clicked Cancel, exit the method
        if (inputName == null) {
          return;  // If Cancel was clicked, exit the method
        }
      }
      addCommandToScript("load", filePath, imageNameWithoutExtension);

      // Display the loaded image
      try {
        ImageIcon imageIcon = new ImageIcon(filePath);
        Image image =
                imageIcon.getImage().getScaledInstance(400, 300, Image.SCALE_SMOOTH);
        imageLabel.setIcon(new ImageIcon(image));
        processAndDisplayImage(inputName);
      } catch (Exception e) {
        showError("Error while applying action: " + e.getMessage());
      }
    }
  }

  private void handleSaveAction() {
    if (imageNameWithoutExtension == null) {
      JOptionPane.showMessageDialog(this, "No image loaded!");
      return;
    }

    // Open a save dialog to choose the directory and specify the file name
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setDialogTitle("Save Image As...");
    fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

    // Show save dialog
    if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
      File selectedFile = fileChooser.getSelectedFile();
      String filePath = selectedFile.getAbsolutePath();

      // Ensure a valid extension is provided
      String extension = getFileExtension(filePath);
      if (extension == null || !isValidImageExtension(extension)) {
        JOptionPane.showMessageDialog(this,
                "Invalid or missing file extension. "
                        + "Please use a supported format: jpg, jpeg, png, ppm.",
                "Error", JOptionPane.ERROR_MESSAGE);
        return;
      }

      // Add save command to script with the correct extension
      addCommandToScript("save", filePath, imageNameWithoutExtension);
      executeCommands();

      JOptionPane.showMessageDialog(this,
              "Image saved successfully as: " + filePath);
    }
  }

  private String getFileExtension(String filePath) {
    int dotIndex = filePath.lastIndexOf('.');
    if (dotIndex > 0 && dotIndex < filePath.length() - 1) {
      return filePath.substring(dotIndex + 1).toLowerCase();
    }
    return null; // No extension found
  }

  private boolean isValidImageExtension(String extension) {
    String[] validExtensions = {"jpg", "jpeg", "png", "ppm"};
    for (String validExtension : validExtensions) {
      if (validExtension.equalsIgnoreCase(extension)) {
        return true;
      }
    }
    return false;
  }

  private void handleActionSelection(String action, String previousImageName) {
    try {
      String inputName = getInputName("Please input the name:");
      if (inputName == null) {
        return;
      } else {
        imageNameWithoutExtension = inputName;
      }

      while (inputName.trim().isEmpty()) {
        inputName = getInputName("Name cannot be blank. Please input the name:");

        if (inputName == null) {
          return;
        }
      }

      String baseCommand = action.toLowerCase().replace("-split", "");
      if (action.toLowerCase().contains("-split")) {
        handleSplitAction(action, baseCommand, previousImageName, inputName);
      } else {
        switch (action.toLowerCase()) {
          case "brighten":
            handleBrightenAction(baseCommand, previousImageName, inputName);
            break;
          case "compress":
            handleCompressAction(baseCommand, previousImageName, inputName);
            break;
          case "level-adjust":
          case "level-adjust-split":
            handleLevelAdjustAction(baseCommand, previousImageName, inputName);
            break;
          case "downsize":
            handleDownsizeAction(baseCommand, previousImageName, inputName);
            break;
          default:
            addCommandToScript(baseCommand, previousImageName, inputName);
            showSuccessMessage(action);
            break;
        }
      }

      processAndDisplayImage(inputName);

    } catch (Exception e) {
      showError("Error while applying action: " + e.getMessage());
    }
  }

  private void handleSplitAction(String action, String baseCommand,
                                 String previousImageName, String inputName) {
    int splitPercentage = getInputValue("Enter split percentage (0-100) or cancel:",
            0, 100);
    if (splitPercentage > 0) {
      addCommandToScript(baseCommand, previousImageName,
              inputName, "split " + splitPercentage);
    } else {
      addCommandToScript(baseCommand, previousImageName, inputName);
    }
    showSuccessMessage(action);
  }

  private void handleBrightenAction(String baseCommand,
                                    String previousImageName, String inputName) {
    int value = getInputValue("Enter value to brighten or darken (positive or negative):",
            -100, 100);
    if (value != -1) {
      addCommandToScript(baseCommand, value + "", previousImageName, inputName);
      showSuccessMessage("Brighten");
    }
  }

  private void handleCompressAction(String baseCommand,
                                    String previousImageName, String inputName) {
    int compressValue = getInputValue("Enter compression value (1-100):",
            1, 100);
    if (compressValue != -1) {
      addCommandToScript(baseCommand, compressValue + "", previousImageName, inputName);
      showSuccessMessage("Compress");
    }
  }

  private void handleLevelAdjustAction(String baseCommand,
                                       String previousImageName, String inputName) {
    int black = getLevelAdjustValue("Enter black value (0-255):", 0, 255);
    int mid = getLevelAdjustValue("Enter mid value (0-255):", 0, 255);
    int white = getLevelAdjustValue("Enter white value (0-255):", 0, 255);
    if (black != -1 && mid != -1 && white != -1) {
      addCommandToScript(baseCommand,
              black + " " + mid + " " + white, previousImageName, inputName);
      showSuccessMessage("Level Adjust");
    }
  }

  private void handleDownsizeAction(String baseCommand,
                                    String previousImageName, String inputName) {
    int height = getInputValue("Enter the height value (must not be less than 0):",
            0, Integer.MAX_VALUE);
    int width = getInputValue("Enter the width value (must not be less than 0):",
            0, Integer.MAX_VALUE);
    if (height >= 0 && width >= 0) {
      addCommandToScript(baseCommand, previousImageName, inputName, width + " " + height);
      showSuccessMessage("Downsize");
    }
  }

  private void processAndDisplayImage(String inputName) throws IOException {
    File tempFile = createTempFile("altered_image_", ".jpg");
    File tempFileHist = createTempFile("histogram_", ".jpg");

    addCommandToScript("save", tempFile.getAbsolutePath(), inputName);
    addCommandToScript("histogram", inputName, "catHistogram");
    addCommandToScript("save", tempFileHist.getAbsolutePath(), "catHistogram");
    executeCommands();

    displayImages(inputName, tempFile, tempFileHist);
  }

  private File createTempFile(String prefix, String suffix) throws IOException {
    File tempFile = File.createTempFile(prefix, suffix);
    tempFile.deleteOnExit(); // Ensure the file is deleted when the program exits
    return tempFile;
  }

  private void displayImages(String inputName, File tempFile, File tempFileHist) {
    Image scaledImage =
            scaleImage(new ImageIcon(tempFile.getAbsolutePath()).getImage(), 400, 300);
    Image scaledHistImage =
            scaleImage(new ImageIcon(tempFileHist.getAbsolutePath()).getImage(),
                    400, 300);

    tempImageStorage.put(inputName, scaledImage);
    updateActivityMonitor();

    alteredImageLabel.setIcon(new ImageIcon(scaledImage));
    histogramImageLabel.setIcon(new ImageIcon(scaledHistImage));
  }

  private Image scaleImage(Image image, int width, int height) {
    return image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
  }

  private void showSuccessMessage(String action) {
    JOptionPane.showMessageDialog(this, action + " applied successfully!");
  }

  private void showError(String message) {
    JOptionPane.showMessageDialog(this, message,
            "Error", JOptionPane.ERROR_MESSAGE);
  }

  private int getLevelAdjustValue(String message, int min, int max) {
    while (true) {
      String input = JOptionPane.showInputDialog(this, message);
      if (input == null) {
        return -1; // User canceled input
      }
      try {
        int value = Integer.parseInt(input);
        if (value >= min && value <= max) {
          return value; // Valid value
        } else {
          JOptionPane.showMessageDialog(this,
                  "Please enter a value between "
                          + min + " and " + max + ".",
                  "Invalid Input", JOptionPane.ERROR_MESSAGE);
        }
      } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this,
                "Invalid number. Please try again.",
                "Error", JOptionPane.ERROR_MESSAGE);
      }
    }
  }

  private int getInputValue(String message, int min, int max) {
    String input = JOptionPane.showInputDialog(this, message);
    try {
      if (input == null || input.isBlank()) {
        return -1; // User canceled or skipped
      }
      int value = Integer.parseInt(input.trim());
      if (value < min || value > max) {
        throw new NumberFormatException();
      }
      return value;
    } catch (NumberFormatException e) {
      JOptionPane.showMessageDialog(this,
              "Invalid input. Please enter a value between " + min + " and " + max + ".",
              "Error", JOptionPane.ERROR_MESSAGE);
      return -1;
    }
  }

  private String getInputName(String message) {
    // Create a JOptionPane to prompt the user for input
    JTextField textField = new JTextField(20);
    Object[] messageBox = {message, textField};

    int option = JOptionPane.showConfirmDialog(
            this,
            messageBox,
            "Input Required",
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.QUESTION_MESSAGE
    );

    // If user clicked Cancel, return null (indicating no action was taken)
    if (option == JOptionPane.CANCEL_OPTION) {
      return null;  // Simply return null to indicate that the user canceled
    }

    // Check if input is empty
    String input = textField.getText().trim();
    if (input.isEmpty()) {
      JOptionPane.showMessageDialog(this,
              "Input cannot be blank!", "Error", JOptionPane.ERROR_MESSAGE);
      return getInputName("Please input a valid name:");  // Recursive call to retry
    }

    return input;
  }

  @Override
  public void addCommandToScript(String command, String... args) {
    try (BufferedWriter writer =
                 new BufferedWriter(new FileWriter("scriptGUI.txt", true))) {
      StringBuilder scriptLine = new StringBuilder(command);
      for (String arg : args) {
        scriptLine.append(" ").append(arg);
      }
      scriptLine.append("\n");
      writer.write(scriptLine.toString());
    } catch (IOException e) {
      JOptionPane.showMessageDialog(this,
              "Error writing to script: " + e.getMessage(),
              "Error", JOptionPane.ERROR_MESSAGE);
    }
  }

  @Override
  public void executeCommands() {
    try (FileReader inputFileReader = new FileReader("scriptGUI.txt")) {
      SnapControllerImpl controller = new SnapControllerImpl(inputFileReader, model);
      controller.start();
    } catch (IOException ex) {
      JOptionPane.showMessageDialog(this,
              "Error occurred while executing commands: " + ex.getMessage(),
              "Error",
              JOptionPane.ERROR_MESSAGE);
    }
  }

  private void clearScriptFile() {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter("scriptGUI.txt"))) {
      writer.write("");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void updateActivityMonitor() {
    activityMonitorPanel.removeAll();

    for (String key : tempImageStorage.keySet()) {
      // Create a JLabel for the key name
      JLabel keyLabel = new JLabel(key);
      keyLabel.setFont(new Font("Arial", Font.PLAIN, 14));
      keyLabel.setForeground(Color.BLUE);
      keyLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
      keyLabel.setToolTipText("Click to view this image in the Altered Image Panel.");

      // Add mouse listener to handle clicks
      keyLabel.addMouseListener(new java.awt.event.MouseAdapter() {
        @Override
        public void mouseClicked(java.awt.event.MouseEvent evt) {
          displayImageInAlteredPanel(tempImageStorage.get(key)); // Show corresponding image
        }
      });

      // Add some spacing and styling
      keyLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
      activityMonitorPanel.add(keyLabel);
    }

    activityMonitorPanel.revalidate(); // Refresh the panel
    activityMonitorPanel.repaint();
  }

  private void displayImageInAlteredPanel(Image image) {
    Image scaledImage = image.getScaledInstance(400, 300, Image.SCALE_SMOOTH);
    alteredImageLabel.setIcon(new ImageIcon(scaledImage));
  }
}
