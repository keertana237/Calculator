import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;
import javax.swing.*;
import javax.swing.border.LineBorder;

public class calculator {
    // Define the dimensions and colors for the calculator GUI.
    int boardWidth = 360;
    int boardHeight = 540;

    Color customLightGray = new Color(212, 212, 210);
    Color customDarkGray = new Color(80, 80, 80);
    Color customBlack = new Color(28, 28, 28);
    Color customOrange = new Color(255, 149, 0);

    // Arrays to hold the values for the buttons.
    String[] buttonValues = {
            "AC", "+/-", "%", "/",
            "7", "8", "9", "*",
            "4", "5", "6", "-",
            "1", "2", "3", "+",
            "0", ".", "^", "="
    };
    // Helper arrays for styling.
    String[] rightSymbols = { "/", "*", "-", "+", "=" };
    String[] topSymbols = { "AC", "+/-", "%" }; // Removed "/" from here as it is also in rightSymbols

    // Swing components for the GUI.
    JFrame frame = new JFrame("Calculator");
    JLabel displayLabel = new JLabel();
    JPanel buttonsPanel = new JPanel();

    // Variables to store the numbers and operator for calculations.
    String A = "0"; // First operand
    String operator = null; // The selected operator
    String B = null; // Second operand

    public calculator() {
        // --- Set up the main JFrame (window) ---
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // --- Set up the display label for the calculator screen ---
        displayLabel.setBackground(customBlack);
        displayLabel.setForeground(Color.white);
        displayLabel.setFont(new Font("Arial", Font.PLAIN, 80));
        displayLabel.setHorizontalAlignment(JLabel.RIGHT);
        displayLabel.setText("0");
        displayLabel.setOpaque(true);

        JPanel displayPanel = new JPanel();
        displayPanel.setLayout(new BorderLayout());
        displayPanel.add(displayLabel);
        frame.add(displayPanel, BorderLayout.NORTH);

        // --- Set up the buttons panel using a GridLayout ---
        buttonsPanel.setLayout(new GridLayout(5, 4));
        buttonsPanel.setBackground(customBlack);
        frame.add(buttonsPanel);

        // --- Create and style the buttons dynamically ---
        for (String buttonValue : buttonValues) {
            JButton button = new JButton(buttonValue);
            button.setFont(new Font("Arial", Font.PLAIN, 30));
            button.setFocusable(false);
            button.setBorder(new LineBorder(customBlack));

            // Set button colors based on their type.
            if (Arrays.asList(topSymbols).contains(buttonValue)) {
                button.setBackground(customLightGray);
                button.setForeground(customBlack);
            } else if (Arrays.asList(rightSymbols).contains(buttonValue)) {
                button.setBackground(customOrange);
                button.setForeground(Color.white);
            } else {
                button.setBackground(customDarkGray);
                button.setForeground(Color.white);
            }

            // Add an action listener to each button to handle clicks.
            button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    handleButtonClick(buttonValue);
                }
            });

            buttonsPanel.add(button);
        }

        // Make the frame visible after all components are added.
        frame.setVisible(true);
    }

    // This method contains the main logic for handling button clicks.
    private void handleButtonClick(String buttonValue) {
        if (Arrays.asList(rightSymbols).contains(buttonValue)) {
            // Logic for operator buttons (+, -, *, /, =)
            if (buttonValue.equals("=")) {
                if (operator != null && B == null) {
                    // Perform the calculation when '=' is pressed.
                    B = displayLabel.getText();
                    double numA = Double.parseDouble(A);
                    double numB = Double.parseDouble(B);
                    double result = calculate(numA, numB, operator);
                    displayLabel.setText(removeZeroDecimal(result));
                    A = "0";
                    operator = null;
                    B = null;
                }
            } else {
                // Set the operator and store the first number.
                A = displayLabel.getText();
                operator = buttonValue;
                displayLabel.setText("0"); // Clear the display for the second number.
            }
        } else if (Arrays.asList(topSymbols).contains(buttonValue) || buttonValue.equals("/")) {
            // Logic for top row symbols (AC, +/-, %, /)
            if (buttonValue.equals("AC")) {
                clearAll();
                displayLabel.setText("0");
            } else if (buttonValue.equals("+/-")) {
                double numDisplay = Double.parseDouble(displayLabel.getText());
                numDisplay *= -1;
                displayLabel.setText(removeZeroDecimal(numDisplay));
            } else if (buttonValue.equals("%")) {
                double numDisplay = Double.parseDouble(displayLabel.getText());
                numDisplay /= 100;
                displayLabel.setText(removeZeroDecimal(numDisplay));
            } else if (buttonValue.equals("/")) {
                A = displayLabel.getText();
                operator = "/";
                displayLabel.setText("0");
            }
        } else if (buttonValue.equals(".")) {
            // Logic for the decimal point.
            if (!displayLabel.getText().contains(buttonValue)) {
                displayLabel.setText(displayLabel.getText() + buttonValue);
            }
        } else {
            // Logic for number buttons.
            if (displayLabel.getText().equals("0")) {
                displayLabel.setText(buttonValue);
            } else {
                displayLabel.setText(displayLabel.getText() + buttonValue);
            }
        }
    }

    // Performs the calculation based on the given operator.
    private double calculate(double numA, double numB, String op) {
        switch (op) {
            case "+":
                return numA + numB;
            case "-":
                return numA - numB;
            case "*":
                return numA * numB;
            case "/":
                if (numB == 0) {
                    System.out.println("Error: Cannot divide by zero.");
                    return Double.NaN;
                }
                return numA / numB;
            case "%":
                return numA % numB;
            case "^":
                return Math.pow(numA, numB);
            default:
                return 0;
        }
    }

    // Resets the calculator state.
    void clearAll() {
        A = "0";
        operator = null;
        B = null;
    }

    // Removes the trailing ".0" from a number if it's an integer.
    String removeZeroDecimal(double numDisplay) {
        if (numDisplay % 1 == 0) {
            return Integer.toString((int) numDisplay);
        }
        return Double.toString(numDisplay);
    }
}
