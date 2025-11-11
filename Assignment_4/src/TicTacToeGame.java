import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

/**
 * Tic Tac Toe game. The class holds all components of the game.
 * The main frame and GUI components, game logic and execution.
 * @author Daniel R. Markvardsen
 */

public class TicTacToeGame {
    
    // Main frame
    private JFrame frame;

    // Game state variables
    private String playerName = "";
    private boolean playerTurn = true;
    private boolean gameActive = false;

    // Scores
    private int playerWins = 0;
    private int computerWins = 0;
    private int draws = 0;

    // GUI Components. Labels, Panels etc.
    private JLabel messageLabel;
    private JLabel namePrompt;
    private JLabel playerWinsLabel;
    private JLabel computerWinsLabel;
    private JLabel drawsLabel;
    private JLabel timerLabel;

    private JPanel playerInputClockPanel;
    private JPanel nameInputPanel;
    private JPanel timerPanel;
    private JPanel scorePanel;
    private JPanel gameBoard;

    private JTextField playerNameField;

    private JButton submitButton;
    private JButton[] gameButtons;

    private JMenuBar menuBar;

    private JMenu controlMenu;
    private JMenu helpMenu;

    private JMenuItem exitItem;
    private JMenuItem instructionItem;

    private Timer clockTimer;
    private Timer computerMoveTimer;

    public TicTacToeGame() {
        initializeGUI();
    }
    
    private void initializeGUI() {
        frame = new JFrame("Tic Tac Toe");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Call set ups for all components in the main frame
        setupMenuBar();
        setupMessageArea();
        setupPlayerInputAndClockPanel();
        setupScorePanel();
        setupGameBoard();


        frame.pack();
        frame.setSize(800, 500);
        frame.setLocationRelativeTo(null); // Center on screen
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
            exitItem.addActionListener(new ExitListener());
            controlMenu.add(exitItem);
            menuBar.add(controlMenu);

            // Help Menu
            helpMenu = new JMenu("Help");
            instructionItem = new JMenuItem("Instructions");
            instructionItem.addActionListener(new InstructionListener());
            helpMenu.add(instructionItem);
            menuBar.add(helpMenu);

            // Implement on frame
            frame.setJMenuBar(menuBar);
    }
    

    //----------------------- Message Area Setup ------------------------------
    /**
     * Sets up the message area either saying "Enter your player name..." or "WELCOME ___" at the top of the frame.
     */
    private void setupMessageArea() {
        messageLabel = new JLabel("Enter your player name...", SwingConstants.CENTER);
        messageLabel.setFont(null);
        frame.add(messageLabel, BorderLayout.NORTH);
    }


    //----------------------- Player Input Panel & Clock Setup -----------------------
    /**
     * Sets up the player input panel at the bottom of the frame.
     * Contains a text field for player name and a submit button.
     */
    private void setupPlayerInputAndClockPanel() {
        // Input Panel
        playerInputClockPanel = new JPanel();
        playerInputClockPanel.setLayout(new BorderLayout());

        nameInputPanel = new JPanel();
        nameInputPanel.setLayout(new FlowLayout());
        namePrompt = new JLabel("Enter your name: ");
        playerNameField = new JTextField(15);
        submitButton = new JButton("Submit");
        submitButton.addActionListener(new SubmitListener());

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


        // Implement on frame and place at the bottom
        frame.add(playerInputClockPanel, BorderLayout.SOUTH);

    }
    


    //----------------------- Score Panel Setup ------------------------------

    /**
     * Sets up the score panel on the right side of the frame.
     * Displays Player Wins, Computer Wins, and Draws.
     */

     private void setupScorePanel() {
        scorePanel = new JPanel();
        scorePanel.setLayout(new BoxLayout(scorePanel, BoxLayout.Y_AXIS));
        scorePanel.setBorder(BorderFactory.createTitledBorder("Score"));
        
        playerWinsLabel = new JLabel("Player Wins: " + Integer.toString(playerWins));
        computerWinsLabel = new JLabel("Computer Wins: " + Integer.toString(computerWins));
        drawsLabel = new JLabel("Draws: " + Integer.toString(draws));

        playerWinsLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        computerWinsLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        drawsLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        scorePanel.add(playerWinsLabel);
        scorePanel.add(computerWinsLabel);
        scorePanel.add(drawsLabel);

        // Implement on frame and place on the right side
        frame.add(scorePanel, BorderLayout.EAST);

     }

    //----------------------- Game Board Setup ------------------------------

    /**
    * Sets up the game board in the center area
    */
    private void setupGameBoard() {
        gameBoard = new JPanel();
        gameBoard.setLayout(new GridLayout(3, 3));
        gameBoard.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        gameButtons = new JButton[9];

        for (int i = 0; i < 9; i++){
            gameButtons[i] = new JButton();
            gameButtons[i].addActionListener(new GameButtonListener());
            gameButtons[i].setEnabled(false);
            gameBoard.add(gameButtons[i]);
        }

        computerMoveTimer = new Timer(2000, new ComputerMoveListener());
        computerMoveTimer.setRepeats(false);

        frame.add(gameBoard, BorderLayout.CENTER);

    }

    //------------------------ Listeners --------------------------------

    /**
     * Listener for the Exit menu item.
     */
    private class ExitListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.exit(0);
        }
    }

    /**
     * Listener for the Submit button.
     */
    private class SubmitListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            handleNameSubmission();
        }
    }

    /**
     * Listener for the Submit button.
     */
    private class TimerListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
            String currentTime = timeFormat.format(new Date());
            timerLabel.setText("Current Time: " + currentTime);
        }
    }

    /**
     * Listener for the Submit button.
     */
    private class ComputerMoveListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (gameActive){
                ArrayList<JButton> enabledButtons = new ArrayList<>();
                for (JButton button : gameButtons) {
                    if (button.isEnabled()) {
                        enabledButtons.add(button);
                    }
                }
                Random random = new Random();
                enabledButtons.get(random.nextInt(enabledButtons.size())).doClick();
            }
            else{
                return;
            }        
        }
    }

    /**
     * Listener for the Instruction menu item. Calls showInstructions method.
     */
    private class InstructionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            showInstructions();
        }
    }

    /**
     * Listener for the game buttons.
     */
    private class GameButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton button = (JButton) e.getSource();
            if (playerTurn) {
                button.setText("X");
                messageLabel.setText("Valid move, waiting for your opponent.");
                computerMoveTimer.start();
            } 
            else {
                button.setText("O");
                messageLabel.setText("Your opponent has moved, now is your turn.");
            }
            button.setEnabled(false);

            playerTurn = !playerTurn;
            checkForWinner();
        }
    }

    //-----------------------------------------------------------------------------------------------------
    /**
     * Handles the submission of the player's name.
    */
    private void handleNameSubmission() {
        playerName = playerNameField.getText().trim();
        if (!playerName.isEmpty()) {

            // Update title of the main frame to "Tic Tac Toe - Player: ____"
            frame.setTitle("Tic Tac Toe - Player: " + playerName);
            // Update message area to "WELCOME ____"
            messageLabel.setText("WELCOME " + playerName + "!");

            // Disable input panel to prevent further submissions
            playerNameField.setEnabled(false);
            submitButton.setEnabled(false);

            // Set game as active
            ActivateGame();
        }
        else {
            JOptionPane.showMessageDialog(frame, 
                "Please enter a valid name.", 
                "Invalid Name", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Shows the instructions dialog for the game. Used with the Help menu.
     */
    private void showInstructions() {
        String instructions = 
            "Some information about the game:\n\n" +
            "- The move occupied by any mark.\n" +
            "- The move is made in the player's turn.\n" +
            "- The move is made within 3 x 3 board.\n" +
            "The game would continue and switch among the player and the computer intil it reaches either one of the following conditions:\n\n" +
            "- Player wins.\n" +
            "- Computer wins.\n" +
            "- Draw.";

        JOptionPane.showMessageDialog(frame, 
            instructions, 
            "Instructions", 
            JOptionPane.INFORMATION_MESSAGE);
    }

    //-------------------------------------- Game Logic ---------------------------------------------------
    /**
     * The main function for handling the game logic. 
     */
    public void checkForWinner() {
        // Check rows
        for (int i = 0; i < 9; i += 3) {
            if (gameButtons[i].getText().equals(gameButtons[i+1].getText()) && gameButtons[i].getText().equals(gameButtons[i+2].getText()) && !gameButtons[i].isEnabled()) {
                if (gameButtons[i].getText().equals("X")){
                    incrementPlayerWins();
                    updateScores();
                    gameActive = false;
                    JOptionPane.showMessageDialog(frame, "Player wins!");
                } 
                else{
                    computerWins(); 
                }
                resetGame();
            }
        }

        // Check columns
        for (int i = 0; i < 3; i++) {
            if (gameButtons[i].getText().equals(gameButtons[i+3].getText()) && gameButtons[i].getText().equals(gameButtons[i+6].getText()) && !gameButtons[i].isEnabled()) {
                if (gameButtons[i].getText().equals("X")){
                    playerWins();
                } 
                else{
                    computerWins();
                    
                }
                resetGame();
            }
        }

        // Check diagonals
        if (gameButtons[0].getText().equals(gameButtons[4].getText()) && gameButtons[0].getText().equals(gameButtons[8].getText()) && !gameButtons[0].isEnabled()) {
            if (gameButtons[0].getText().equals("X")){
                playerWins();
            } 
            else{
                computerWins();
            }
            resetGame();
        }
        if (gameButtons[2].getText().equals(gameButtons[4].getText()) && gameButtons[2].getText().equals(gameButtons[6].getText()) && !gameButtons[2].isEnabled()) {
            if (gameButtons[2].getText().equals("X")){
                    playerWins();
                    
                } 
                else{
                    computerWins();
                }
            resetGame();
        }

        // Check for tie
        boolean tie = true;
        for (int i = 0; i < 9; i++) {
            if (gameButtons[i].isEnabled()) {
                tie = false;
                break;
            }
        }
        if (tie) {
            incrementDraws();
            updateScores();
            gameActive = false;
            JOptionPane.showMessageDialog(frame, "It's a draw!");
            resetGame();
        }
    }

    
    /**
     * Operations to be done when player wins.
     */
    private void playerWins() {
        incrementPlayerWins();
        updateScores();
        gameActive = false;
        JOptionPane.showMessageDialog(frame, "Player wins!");
    }

    /**
     * Operations to be done when computer wins.
     */
    private void computerWins() {
        incrementComputerWins();
        updateScores();
        gameActive = false;
        JOptionPane.showMessageDialog(frame, "Computer wins!");
    }

    /**
     * Resetter
     */
    private void resetGame() {
        computerMoveTimer.stop();
        ActivateGame();
        return;
    }

    //Increment methods for scores

     /**
     * Increments the player wins count.
     */
    private void incrementPlayerWins() {
        playerWins++;
        updateScores();
    }
    
    /**
     * Increments the computer wins count.
     */
    private void incrementComputerWins() {
        computerWins++;
        updateScores();
    }
    
    /**
     * Increments the draws count.
     */
    private void incrementDraws() {
        draws++;
        updateScores();
    }

    /**
     * Updates the score display with current values.
     */
    private void updateScores() {
        playerWinsLabel.setText("Player Wins: " + playerWins);
        computerWinsLabel.setText("Computer Wins: " + computerWins);
        drawsLabel.setText("Draws: " + draws);
    }

    /**
     * Activate the game.
     */
    private void ActivateGame() {
        for (int i = 0; i < 9; i++){
            gameButtons[i].setText("");
            gameButtons[i].setEnabled(true);
        }
        gameActive = true;
        playerTurn = true;
    }
}
