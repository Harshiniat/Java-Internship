import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;

public class PasswordChecker {
    private JLabel strengthLabel;
    private JPasswordField passwordField;
    private JCheckBox showPasswordCheckbox;
    private JPanel strengthPanel;

    public PasswordChecker() {
        initializeUI();
    }

    private void initializeUI() {
        JFrame frame = new JFrame("Password Strength Indicator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 200);

        JPanel panel = new JPanel();
        JLabel labelStrength = new JLabel("Strength: ");
        strengthLabel = new JLabel();
        strengthPanel = new JPanel();
        passwordField = new JPasswordField(20);
        showPasswordCheckbox = new JCheckBox("Show Password");
        JButton enterButton = new JButton("Enter");

        panel.setLayout(new GridLayout(6, 1));
        panel.add(new JLabel("Enter Password: "));
        panel.add(passwordField);
        panel.add(labelStrength);
        panel.add(showPasswordCheckbox);
        panel.add(strengthPanel);
        panel.add(enterButton);
        panel.add(strengthLabel);

        frame.add(panel);
        frame.setVisible(true);

        passwordField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateStrength();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateStrength();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateStrength();
            }

            private void updateStrength() {
                char[] password = passwordField.getPassword();
                String passwordStr = new String(password);
                int strength = calculatePasswordStrength(passwordStr);
                strengthLabel.setText("Strength: " + getStrengthLabel(strength));
                updateStrengthPanels(strength);
                enterButton.setEnabled(strength == 3);
            }
        });

        showPasswordCheckbox.addActionListener(e -> {
            JCheckBox checkBox = (JCheckBox) e.getSource();
            passwordField.setEchoChar(checkBox.isSelected() ? '\0' : '*');
        });

        enterButton.addActionListener(e -> {
            char[] password = passwordField.getPassword();
            String passwordStr = new String(password);
            if (passwordStr.isEmpty()) {
                strengthLabel.setText("Please enter a password.");
            } else {
                JOptionPane.showMessageDialog(null, "Good Password.", "Password Strength",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });

        enterButton.setEnabled(false);
    }

    private int calculatePasswordStrength(String password) {
        int length = password.length();
        boolean hasUppercase = !password.equals(password.toLowerCase());
        boolean hasLowercase = !password.equals(password.toUpperCase());
        boolean hasDigit = password.matches(".\\d.");
        boolean hasSpecialChar = password.matches(".[!@#\\$%^&()].*");

        if (length >= 8 && hasUppercase && hasLowercase && hasDigit && hasSpecialChar) {
            return 3; // Strong
        } else if (length >= 6 && (hasUppercase || hasLowercase || hasDigit || hasSpecialChar)) {
            return 2; // Medium
        } else if (length > 0) {
            return 1; // Weak
        } else {
            return 0; // Unknown
        }
    }

    private String getStrengthLabel(int strength) {
        switch (strength) {
            case 1:
                return "Weak";
            case 2:
                return "Medium";
            case 3:
                return "Strong";
            default:
                return "Unknown";
        }
    }

    private void updateStrengthPanels(int strength) {
        strengthPanel.removeAll();
        Color[] colors = { Color.RED, Color.ORANGE, Color.GREEN }; // Colors for Weak, Medium, Strong
        for (int i = 0; i < 3; i++) {
            JPanel levelPanel = new JPanel();
            levelPanel.setPreferredSize(new Dimension(20, 20));
            levelPanel.setOpaque(true);
            levelPanel.setBackground(i == strength - 1 ? colors[i] : Color.LIGHT_GRAY);
            strengthPanel.add(levelPanel);
        }
        strengthPanel.revalidate();
        strengthPanel.repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(PasswordChecker::new);
    }
}
