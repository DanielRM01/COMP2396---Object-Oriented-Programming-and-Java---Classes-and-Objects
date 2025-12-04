import java.io.*;
import java.net.*;
import javax.swing.*;

/**
 * Main client class that connects to the server and manages the GUI.
 */
public class TicTacToeClient {
    private Socket socket;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private TicTacToeGUI gui;
    private int myPlayerNumber;
    private String myPlayerName;
    private boolean myTurn;
    private boolean connected;
    
    private static final String SERVER_ADDRESS = "127.0.0.1";
    private static final int PORT = 12345;
    
    public TicTacToeClient() {
        connected = false;
        connectToServer();
        
        if (connected) {
            // Create GUI after successful connection
            SwingUtilities.invokeLater(() -> {
                gui = new TicTacToeGUI(this);
            });
            
            // Start listening for server messages
            startListening();
        } else {
            System.err.println("Failed to connect to server. Exiting...");
            System.exit(1);
        }
    }
    
    /**
     * Connects to the server.
     */
    private void connectToServer() {
        try {
            System.out.println("Connecting to server at " + SERVER_ADDRESS + ":" + PORT);
            socket = new Socket(SERVER_ADDRESS, PORT);
            
            // Create output stream first (important for ObjectStreams)
            output = new ObjectOutputStream(socket.getOutputStream());
            output.flush();
            input = new ObjectInputStream(socket.getInputStream());
            
            connected = true;
            System.out.println("Successfully connected to server!");
            
        } catch (IOException e) {
            System.err.println("Error connecting to server: " + e.getMessage());
            e.printStackTrace();
            connected = false;
            
            SwingUtilities.invokeLater(() -> {
                JOptionPane.showMessageDialog(null, 
                    "Could not connect to server at " + SERVER_ADDRESS + ":" + PORT + 
                    "\n\nPlease make sure the server is running.", 
                    "Connection Error", 
                    JOptionPane.ERROR_MESSAGE);
            });
        }
    }
    
    /**
     * Starts listening for messages from server.
     */
    private void startListening() {
        Thread listenerThread = new Thread(() -> {
            try {
                while (connected) {
                    Message message = (Message) input.readObject();
                    
                    if (message == null) {
                        break;
                    }
                    
                    // Process message based on type
                    handleMessage(message);
                }
            } catch (EOFException e) {
                System.out.println("Server closed connection.");
                handleServerDisconnect();
            } catch (IOException e) {
                if (connected) {
                    System.err.println("Error reading from server: " + e.getMessage());
                    handleServerDisconnect();
                }
            } catch (ClassNotFoundException e) {
                System.err.println("Class not found: " + e.getMessage());
                e.printStackTrace();
            }
        });
        
        listenerThread.setDaemon(true);
        listenerThread.start();
    }
    
    /**
     * Handles incoming messages from the server.
     * @param message The message received from server
     */
    private void handleMessage(Message message) {
        switch (message.getType()) {
            case PLAYER_NUMBER:
                myPlayerNumber = (Integer) message.getData();
                System.out.println("I am Player " + myPlayerNumber);
                break;
                
            case BOARD_UPDATE:
                Move move = (Move) message.getData();
                gui.updateBoard(move.getRow(), move.getCol(), move.getMark());
                System.out.println("Board updated: [" + move.getRow() + ", " + move.getCol() + "] = " + move.getMark());
                break;
                
            case TURN_UPDATE:
                String turnMessage = (String) message.getData();
                gui.updateMessage(turnMessage);
                
                // Enable/disable board based on turn message
                if (turnMessage.contains("now is your turn")) {
                    myTurn = true;
                    gui.setBoardEnabled(true);
                } else {
                    myTurn = false;
                    gui.setBoardEnabled(false);
                }
                System.out.println("Turn update: " + turnMessage);
                break;
                
            case GAME_RESULT:
                String resultMessage = (String) message.getData();
                System.out.println("Game result: " + resultMessage);
                
                // Disable board
                gui.setBoardEnabled(false);
                
                // Show dialog and get restart choice
                SwingUtilities.invokeLater(() -> {
                    boolean restart = gui.showGameResultDialog(resultMessage);
                    sendRestartResponse(restart);
                    
                    if (!restart) {
                        // Player chose not to restart
                        disconnect();
                    }
                });
                break;
                
            case SCORE_UPDATE:
                ScoreData scoreData = (ScoreData) message.getData();
                gui.updateScores(scoreData.getPlayer1Wins(), 
                               scoreData.getPlayer2Wins(), 
                               scoreData.getDraws());
                System.out.println("Scores updated: P1=" + scoreData.getPlayer1Wins() + 
                                 " P2=" + scoreData.getPlayer2Wins() + 
                                 " Draws=" + scoreData.getDraws());
                break;
                
            case PLAYER_LEFT:
                String leftMessage = (String) message.getData();
                System.out.println("Player left: " + leftMessage);
                gui.setBoardEnabled(false);
                gui.showPlayerLeftDialog();
                
                // Disconnect after showing dialog
                SwingUtilities.invokeLater(() -> {
                    try {
                        Thread.sleep(2000); // Give user time to see dialog
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    disconnect();
                });
                break;
                
            default:
                System.out.println("Unknown message type: " + message.getType());
        }
    }
    
    /**
     * Sends player name to server.
     * @param name The player's name
     */
    public void sendPlayerName(String name) {
        if (!connected) return;
        
        myPlayerName = name;
        Message message = new Message(Message.MessageType.NAME, name);
        sendMessage(message);
        System.out.println("Sent player name: " + name);
    }
    
    /**
     * Sends a move to the server.
     * @param row Row position (0-2)
     * @param col Column position (0-2)
     */
    public void sendMove(int row, int col) {
        if (!connected) return;
        
        String mark = (myPlayerNumber == 1) ? "X" : "O";
        Move move = new Move(row, col, mark);
        Message message = new Message(Message.MessageType.MOVE, move);
        sendMessage(message);
        System.out.println("Sent move: [" + row + ", " + col + "]");
    }
    
    /**
     * Sends restart response to server.
     * @param restart true to restart, false to exit
     */
    public void sendRestartResponse(boolean restart) {
        if (!connected) return;
        
        Message message = new Message(Message.MessageType.RESTART_RESPONSE, restart);
        sendMessage(message);
        System.out.println("Sent restart response: " + restart);
        
        if (restart) {
            // Reset board for new round
            gui.resetBoard();
            gui.updateMessage("Waiting for new round to start...");
        }
    }
    
    /**
     * Sends a message to the server.
     * @param message The message to send
     */
    private void sendMessage(Message message) {
        try {
            output.writeObject(message);
            output.flush();
        } catch (IOException e) {
            System.err.println("Error sending message to server: " + e.getMessage());
            e.printStackTrace();
            handleServerDisconnect();
        }
    }
    
    /**
     * Handles disconnection from server.
     */
    public void disconnect() {
        if (!connected) return;
        
        connected = false;
        
        try {
            // Notify server that we're leaving
            Message message = new Message(Message.MessageType.PLAYER_LEFT, null);
            output.writeObject(message);
            output.flush();
        } catch (IOException e) {
            // Server might already be disconnected, ignore
        }
        
        cleanup();
        System.out.println("Disconnected from server.");
    }
    
    /**
     * Handles unexpected server disconnection.
     */
    private void handleServerDisconnect() {
        if (!connected) return;
        
        connected = false;
        cleanup();
        
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(gui != null ? gui.getFrame() : null,
                "Lost connection to server.",
                "Connection Lost",
                JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        });
    }
    
    /**
     * Cleans up resources.
     */
    private void cleanup() {
        try {
            if (input != null) input.close();
            if (output != null) output.close();
            if (socket != null) socket.close();
            System.out.println("Resources cleaned up.");
        } catch (IOException e) {
            System.err.println("Error cleaning up resources: " + e.getMessage());
        }
    }
    
    /**
     * Main method to start the client.
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        // Set system look and feel for better appearance
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // Use default look and feel if system L&F fails
        }
        
        new TicTacToeClient();
    }
}