import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChessDashboard extends JFrame {

    private JPanel dashboardPanel;
    private ChessSinglePlayerPanel singlePlayerPanel;
    private ChessMultiplayerPanel multiplayerPanel;

    public ChessDashboard() {
        // Set up the frame
        setTitle("Chess Application Dashboard");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the frame
        setLayout(new BorderLayout()); // Set the layout for the frame

        // Initialize panels
        dashboardPanel = createDashboardPanel();
        singlePlayerPanel = new ChessSinglePlayerPanel();
        multiplayerPanel = new ChessMultiplayerPanel(); // Initialize multiplayer panel

        // Show the dashboard panel by default
        add(dashboardPanel);
    }

    private JPanel createDashboardPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Logo at the top
        JLabel logoLabel = new JLabel("Chess Game", JLabel.CENTER);
        logoLabel.setFont(new Font("Arial", Font.BOLD, 24));
        logoLabel.setIcon(new ImageIcon(getScaledImage("logo.png", 100, 100))); // Adjust path & size
        panel.add(logoLabel, BorderLayout.NORTH);

        // Panel for buttons
        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 10, 10));

        // Local Player button
        JButton localPlayerButton = new JButton("Local Player");
        localPlayerButton.setFont(new Font("Arial", Font.PLAIN, 18));
        localPlayerButton.addActionListener(e -> switchToSinglePlayerPanel());

        // Multiplayer button
        JButton multiplayerButton = new JButton("Multiplayer");
        multiplayerButton.setFont(new Font("Arial", Font.PLAIN, 18));
        multiplayerButton.addActionListener(e -> switchToMultiplayerPanel()); // Switch to multiplayer panel

        // Add buttons to button panel
        buttonPanel.add(localPlayerButton);
        buttonPanel.add(multiplayerButton);

        // Add button panel to dashboard
        panel.add(buttonPanel, BorderLayout.CENTER);

        return panel;
    }

    private void switchToSinglePlayerPanel() {
        // Remove the dashboard panel and add the single-player panel
        getContentPane().remove(dashboardPanel);
        getContentPane().add(singlePlayerPanel);
        revalidate();
        repaint();
    }

    private void switchToMultiplayerPanel() {
        // Remove the dashboard panel and add the multiplayer panel
        getContentPane().remove(dashboardPanel);
        getContentPane().add(multiplayerPanel);
        revalidate();
        repaint();
    }

    private void switchToDashboardPanel() {
        // Remove the single-player or multiplayer panel and add the dashboard panel
        if (getContentPane().getComponent(0) instanceof ChessSinglePlayerPanel) {
            getContentPane().remove(singlePlayerPanel);
        } else if (getContentPane().getComponent(0) instanceof ChessMultiplayerPanel) {
            getContentPane().remove(multiplayerPanel);
        }
        getContentPane().add(dashboardPanel);
        revalidate();
        repaint();
    }

    // Method to load and scale the logo image
    private Image getScaledImage(String imagePath, int width, int height) {
        ImageIcon icon = new ImageIcon(imagePath);
        Image originalImage = icon.getImage();
        return originalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ChessDashboard dashboard = new ChessDashboard();
            dashboard.setVisible(true);
        });
    }

    // Inner class for ChessSinglePlayerPanel with Back button functionality
    class ChessSinglePlayerPanel extends JPanel {
        public ChessSinglePlayerPanel() {
            super(new BorderLayout());

            // Create a sub-panel with GridBagLayout for precise positioning
            JPanel subPanel = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();

            // Back button at the top left
            JButton backButton = new JButton("Back");
            backButton.setBackground(Color.BLUE);
            backButton.addActionListener(e -> switchToDashboardPanel()); // Switch back to Dashboard panel

            // Set constraints for the back button
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.insets = new Insets(5, 5, 5, 5); // Margin around the button
            gbc.anchor = GridBagConstraints.WEST; // Align to the left
            subPanel.add(backButton, gbc);

            // Spacer label for empty space (if needed)
            gbc.gridx = 1;
            gbc.gridy = 0;
            gbc.weightx = 1.0; // Take up extra horizontal space
            gbc.fill = GridBagConstraints.HORIZONTAL;
            subPanel.add(new JLabel(" "), gbc);

            // Start button in the center
            JButton startButton = new JButton("Start");
            startButton.addActionListener(e -> {
                new Thread(new ChessGameServer(12345)).start(); // Start the server thread
                new ChessGame("localhost", 12345);
                System.out.println("Single player game started"); // Placeholder action for starting the game
            });

            // Set constraints for the start button
            gbc.gridx = 0;
            gbc.gridy = 1;
            gbc.gridwidth = 2; // Span across two columns
            gbc.gridheight = 2;
            gbc.weightx = 0;
            gbc.weighty = 1.0;
            gbc.anchor = GridBagConstraints.CENTER; // Center alignment
            subPanel.add(startButton, gbc);

            // Add the sub-panel to the center of the main panel
            add(subPanel, BorderLayout.CENTER);
        }
    }

    // Inner class for ChessMultiplayerPanel with Port Number input
    class ChessMultiplayerPanel extends JPanel {
        public ChessMultiplayerPanel() {
            super(new BorderLayout());

            // Create a sub-panel with GridBagLayout for precise positioning
            JPanel subPanel = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();

            // Back button at the top left
            JButton backButton = new JButton("Back");
            backButton.addActionListener(e -> switchToDashboardPanel()); // Switch back to Dashboard panel

            // Set constraints for the back button
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.insets = new Insets(5, 5, 5, 5); // Margin around the button
            gbc.anchor = GridBagConstraints.WEST; // Align to the left
            subPanel.add(backButton, gbc);

            // Spacer label for empty space (if needed)
            gbc.gridx = 1;
            gbc.gridy = 0;
            gbc.weightx = 1.0; // Take up extra horizontal space
            gbc.fill = GridBagConstraints.HORIZONTAL;
            subPanel.add(new JLabel(" "), gbc);

            // Port number label and text field
            JLabel portLabel = new JLabel("Port:");
            gbc.gridx = 0;
            gbc.gridy = 1;
            gbc.anchor = GridBagConstraints.WEST; // Align to the left
            subPanel.add(portLabel, gbc);

            JTextField portField = new JTextField(10);
            gbc.gridx = 1;
            gbc.gridy = 1;
            subPanel.add(portField, gbc);

            // Join Room button in the center
            JButton joinButton = new JButton("Join Room");
            joinButton.addActionListener(e -> {
				int port = Integer.parseInt(portField.getText().trim());
                new Thread(new ChessGameServer(port)).start(); // Start the server thread
				new ChessGame("localhost", port);
                System.out.println("Joining room on port: " + port); // Placeholder action for joining the room
            });

            // Set constraints for the Join Room button
            gbc.gridx = 0;
            gbc.gridy = 2;
            gbc.gridwidth = 2; // Span across two columns
            gbc.weighty = 1.0;
            gbc.anchor = GridBagConstraints.CENTER; // Center alignment
            subPanel.add(joinButton, gbc);

            // Add the sub-panel to the center of the main panel
            add(subPanel, BorderLayout.CENTER);
        }
    }
}
