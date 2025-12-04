import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

/**
 * GUI class for the Tic-Tac-Toe game client.
 * Contains all visual components and handles user interactions.
 * @author Daniel R. Markvardsen
 */
public class TicTacToeGUI {

    private JFrame frame;
    private TicTacToeClient client;
    
    // GUI Components
    private JLabel messageLabel;
    private JLabel player1WinsLabel;
    private JLabel player2WinsLabel;
    private JLabel drawsLabel;
    private JLabel timerLabel;

    private JPanel playerInputClockPanel;
    private JPanel timerPanel;

    private JTextField playerNameField;
    private JButton submitButton;
    private JButton[][] gameButtons;

    private Timer clockTimer;
    
    // Menu components
    private JMenuBar menuBar;
    private JMenu controlMenu;
    private JMenu helpMenu;
    private JMenuItem exitItem;
    private JMenuItem instructionItem;
    
    // Game state
    private boolean nameSubmitted;
    private int player1Wins;
    private int player2Wins;
    private int draws;
    
    /**
     * Creates the GUI for the client.
     * @param client The client instance to communicate with
     */
    public TicTacToeGUI(TicTacToeClient client) {
        this.client = client;
        this.nameSubmitted = false;
        this.player1Wins = 0;
        this.player2Wins = 0;
        this.draws = 0;
        initializeGUI();
    }
    
    /**
     * Initializes all GUI components.
     */
    private void initializeGUI() {
        frame = new JFrame("Tic Tac Toe");
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                handleWindowClose();
            }
        });
        frame.setLayout(new BorderLayout());
        
        // Setup all components
        setupMenuBar();
        setupMessageArea();
        setupPlayerInputAndClockPanel();
        setupGameBoard();
        setupScorePanel();
        
        frame.pack();
        frame.setSize(800, 500);
        frame.setLocationRelativeTo(null);
        frame.setResizable(true);
        frame.setVisible(true);
    }
    // ----------------------- Menu Bar Setup --------------------------------
    /**
     * Sets up the menu bar with Control and Help menus.
     */
    private void setupMenuBar() {
        menuBar = new JMenuBar();
        
        // Control Menu
        controlMenu = new JMenu("Control");
        exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> handleExit());
        controlMenu.add(exitItem);
        menuBar.add(controlMenu);
        
        // Help Menu
        helpMenu = new JMenu("Help");
        instructionItem = new JMenuItem("Instructions");
        instructionItem.addActionListener(e -> showInstructions());
        helpMenu.add(instructionItem);
        menuBar.add(helpMenu);
        
        frame.setJMenuBar(menuBar);
    }
    
    //----------------------- Message Area Setup ------------------------------
    /**
     * Sets up the message area at the top of the frame.
     */
    private void setupMessageArea() {
        messageLabel = new JLabel("Enter your player name...", SwingConstants.CENTER);
        messageLabel.setFont(new Font("Arial", Font.BOLD, 16));
       //messageLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        frame.add(messageLabel, BorderLayout.NORTH);
    }

    //----------------------- Player Input Panel & Clock Setup -----------------------
    /**
     * Sets up the name input panel at the bottom of the frame.
     */
    private void setupPlayerInputAndClockPanel() {
        playerInputClockPanel = new JPanel();
        playerInputClockPanel.setLayout(new BorderLayout());

        JPanel nameInputPanel = new JPanel();
        nameInputPanel.setLayout(new FlowLayout());
        
        JLabel namePrompt = new JLabel("Enter your name: ");
        playerNameField = new JTextField(15);
        submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> handleNameSubmission());
        
        // Allow Enter key to submit
        playerNameField.addActionListener(e -> handleNameSubmission());
        
        nameInputPanel.add(namePrompt);
        nameInputPanel.add(playerNameField);
        nameInputPanel.add(submitButton);

        // Clock Label
        timerLabel = new JLabel();
        clockTimer = new Timer(1000, new TimerListener());
        clockTimer.start();

        // Set up timer panel to center
        timerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        timerPanel.add(timerLabel);

        // Set up the timer under the panel
        playerInputClockPanel.add(nameInputPanel, BorderLayout.NORTH);
        playerInputClockPanel.add(timerPanel, BorderLayout.SOUTH);

        
        frame.add(playerInputClockPanel, BorderLayout.SOUTH);
    }

    /**
     * Listener for the Timer.
     */
    private class TimerListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
            String currentTime = timeFormat.format(new Date());
            timerLabel.setText("Current Time: " + currentTime);
        }
    }

    //----------------------- Score Panel Setup ------------------------------

    /**
     * Sets up the score panel on the right side of the frame.
     */
    private void setupScorePanel() {
        JPanel scorePanel = new JPanel();
        scorePanel.setLayout(new BoxLayout(scorePanel, BoxLayout.Y_AXIS));
        scorePanel.setBorder(BorderFactory.createTitledBorder("Score"));
        
        player1WinsLabel = new JLabel("Player 1 Wins: " + Integer.toString(player1Wins));
        player2WinsLabel = new JLabel("Player 2 Wins: " + Integer.toString(player2Wins));
        drawsLabel = new JLabel("Draws: " + Integer.toString(draws));
        
        player1WinsLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        player2WinsLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        drawsLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        scorePanel.add(player1WinsLabel);
        scorePanel.add(player2WinsLabel);
        scorePanel.add(drawsLabel);
        
        frame.add(scorePanel, BorderLayout.EAST);
    }

    /**
     * Sets up the game board (3x3 grid of buttons).
     */
    private void setupGameBoard() {
        JPanel boardPanel = new JPanel();
        boardPanel.setLayout(new GridLayout(3, 3, 5, 5));
        boardPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        gameButtons = new JButton[3][3];
        
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                final int r = row;
                final int c = col;
                
                gameButtons[row][col] = new JButton("");
                gameButtons[row][col].setFont(new Font("Arial", Font.BOLD, 48));
                gameButtons[row][col].setFocusPainted(false);
                gameButtons[row][col].setEnabled(false);
                gameButtons[row][col].addActionListener(e -> handleBoardClick(r, c));
                
                boardPanel.add(gameButtons[row][col]);
            }
        }
        
        frame.add(boardPanel, BorderLayout.CENTER);
    }
    
    //------------------------ Handlers --------------------------------


    /**
     * Handles name submission.
     */
    private void handleNameSubmission() {
        String name = playerNameField.getText().trim();
        if (!name.isEmpty() && !nameSubmitted) {
            nameSubmitted = true;
            
            // Update GUI
            playerNameField.setEnabled(false);
            submitButton.setEnabled(false);
            
            // Update title and message
            frame.setTitle("Tic Tac Toe - Player: " + name);
            messageLabel.setText("WELCOME " + name +  "!");
            
            // Send name to server via client
            client.sendPlayerName(name);
        } else if (name.isEmpty()) {
            JOptionPane.showMessageDialog(
                frame,
                "Please enter a valid name.",
                "Invalid Name",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }
    
    /**
     * Handles a button click on the game board.
     * @param row Row of the button
     * @param col Column of the button
     */
    private void handleBoardClick(int row, int col) {
        // Check if name has been submitted
        if (!nameSubmitted) {
            JOptionPane.showMessageDialog(
                frame,
                "Please enter your name first.",
                "Name Required",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }
        
        // Send move to server (server will validate if it's player's turn)
        client.sendMove(row, col);
    }
    
    /**
     * Updates the board display with a move.
     * @param row Row position
     * @param col Column position
     * @param mark The mark to display ("X" or "O")
     */
    public void updateBoard(int row, int col, String mark) {
        SwingUtilities.invokeLater(() -> {
            gameButtons[row][col].setText(mark);
            gameButtons[row][col].setEnabled(false);
        });
    }
    
    /**
     * Updates the message label.
     * @param message The message to display
     */
    public void updateMessage(String message) {
        SwingUtilities.invokeLater(() -> {
            messageLabel.setText(message);
        });
    }
    
    /**
     * Updates the frame title with player name.
     * @param name The player's name
     */
    public void updateTitle(String name) {
        SwingUtilities.invokeLater(() -> {
            frame.setTitle("Tic Tac Toe - Player: " + name);
        });
    }
    
    /**
     * Enables or disables the game board.
     * @param enabled true to enable, false to disable
     */
    public void setBoardEnabled(boolean enabled) {
        SwingUtilities.invokeLater(() -> {
            for (int row = 0; row < 3; row++) {
                for (int col = 0; col < 3; col++) {
                    // Only enable if the button is empty (no text)
                    if (enabled && gameButtons[row][col].getText().isEmpty()) {
                        gameButtons[row][col].setEnabled(true);
                    } else if (!enabled) {
                        gameButtons[row][col].setEnabled(false);
                    }
                }
            }
        });
    }
    
    /**
     * Shows game result dialog and asks for restart.
     * @param message The result message
     * @return true if player wants to restart, false otherwise
     */
    public boolean showGameResultDialog(String message) {
        int choice = JOptionPane.showConfirmDialog(
            frame,
            message + "\nDo you want to play again?",
            "Game Over",
            JOptionPane.YES_NO_OPTION
        );
        return choice == JOptionPane.YES_OPTION;
    }
    
    /**
     * Shows player left dialog.
     */
    public void showPlayerLeftDialog() {
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(
                frame,
                "Game Ends. One of the players left.",
                "Game Over",
                JOptionPane.INFORMATION_MESSAGE
            );
        });
    }
    
    /**
     * Updates the score display.
     * @param p1Wins Player 1 wins
     * @param p2Wins Player 2 wins
     * @param draws Draw count
     */
    public void updateScores(int p1Wins, int p2Wins, int draws) {
        this.player1Wins = p1Wins;
        this.player2Wins = p2Wins;
        this.draws = draws;
        
        SwingUtilities.invokeLater(() -> {
            player1WinsLabel.setText("Player 1 Wins: " + p1Wins);
            player2WinsLabel.setText("Player 2 Wins: " + p2Wins);
            drawsLabel.setText("Draws: " + draws);
        });
    }
    
    /**
     * Resets the board for a new round.
     */
    public void resetBoard() {
        SwingUtilities.invokeLater(() -> {
            for (int row = 0; row < 3; row++) {
                for (int col = 0; col < 3; col++) {
                    gameButtons[row][col].setText("");
                    gameButtons[row][col].setEnabled(false);
                }
            }
        });
    }
    
    /**
     * Handles Exit menu item click.
     */
    public void handleExit() {
        handleWindowClose();
    }
    
    /**
     * Handles window close button click.
     */
    public void handleWindowClose() {
        // Disconnect from server and notify opponent
        client.disconnect();
        
        // Exit application
        System.exit(0);
    }
    
    /**
     * Shows the instructions dialog.
     */
    private void showInstructions() {
        String instructions = 
            "Some information about the game:\n\n" +
            "- The move occupied by any mark.\n" +
            "- The move is made in the player's turn.\n" +
            "- The move is made within 3 x 3 board.\n" +
            "The game would continue and switch among the opposite player until it reaches either one of the following conditions:\n\n" +
            "- Player 1 wins.\n" +
            "- Player 2 wins.\n" +
            "- Draw.\n" +
            "- One of the players leaves the game.";

        JOptionPane.showMessageDialog(
            frame,
            instructions,
            "Instructions",
            JOptionPane.INFORMATION_MESSAGE
        );
    }
    
    /**
     * Gets the main frame.
     * @return The JFrame instance
     */
    public JFrame getFrame() {
        return frame;
    }
}