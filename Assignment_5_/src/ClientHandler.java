import java.io.*; 
import java.net.*;

/**
 * Handles communication with a single client.
 * Runs in its own thread to handle asynchronous communication.
 */
public class ClientHandler extends Thread {
    private Socket socket;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private GameSession session;
    private int playerNumber;
    private String playerName;
    private boolean restartResponse;
    private boolean restartResponseReceived;
    
    /**
     * Creates a client handler for the given socket.
     * @param socket
     * @param playerNum
     */
    public ClientHandler(Socket socket, int playerNum) {
        this.socket = socket;
        this.playerNumber = playerNum;
        this.restartResponseReceived = false;
        
        try {
            output = new ObjectOutputStream(socket.getOutputStream());
            output.flush();
            input = new ObjectInputStream(socket.getInputStream());
            System.out.println("Streams initialized for Player " + playerNum);
        } catch (IOException e) {
            System.err.println("Error creating streams for Player " + playerNum + ": " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Sets the game session for this client.
     * @param session The game session
     */
    public void setSession(GameSession session) {
        this.session = session;
    }
    
    /**
     * Gets the player number.
     * @return The player number (1 or 2)
     */
    public int getPlayerNumber() {
        return playerNumber;
    }
    
    /**
     * Gets the player name.
     * @return The player's name
     */
    public String getPlayerName() {
        return playerName;
    }
    
    @Override
    public void run() {
        try {
            boolean namesReceived = false;
            
            while (true) {
                // Read message from client
                Message message = (Message) input.readObject();
                
                if (message == null) {
                    break;
                }
                
                // Process different message types
                switch (message.getType()) {
                    case NAME:
                        playerName = (String) message.getData();
                        System.out.println("Player " + playerNumber + " name: " + playerName);
                        
                        // Check if both players have submitted names
                        if (!namesReceived && session != null) {
                            ClientHandler otherPlayer = (playerNumber == 1) ? 
                                session.getPlayer2() : session.getPlayer1();
                            
                            if (otherPlayer != null && otherPlayer.getPlayerName() != null) {
                                // Both players have names, start the game
                                namesReceived = true;
                                System.out.println("Both players ready. Starting game...");
                                session.startNewRound();
                            }
                        }
                        break;
                        
                    case MOVE:
                        Move move = (Move) message.getData();
                        session.processMove(playerNumber, move.getRow(), move.getCol());
                        break;
                        
                    case RESTART_RESPONSE:
                        boolean wantsRestart = (Boolean) message.getData();
                        handleRestartResponse(wantsRestart);
                        break;
                        
                    case PLAYER_LEFT:
                        // Client is notifying of disconnect
                        session.handlePlayerLeft(playerNumber);
                        return;
                        
                    default:
                        System.out.println("Unknown message type: " + message.getType());
                }
            }
            
        } catch (EOFException e) {
            // Client disconnected
            System.out.println("Player " + playerNumber + " disconnected (EOF)");
            if (session != null) {
                session.handlePlayerLeft(playerNumber);
            }
        } catch (IOException e) {
            System.err.println("IO Error with Player " + playerNumber + ": " + e.getMessage());
            if (session != null) {
                session.handlePlayerLeft(playerNumber);
            }
        } catch (ClassNotFoundException e) {
            System.err.println("Class not found error with Player " + playerNumber + ": " + e.getMessage());
            e.printStackTrace();
        } finally {
            cleanup();
        }
    }
    
    /**
     * Handles restart response from this player.
     * @param wantsRestart true if player wants to restart
     */
    private synchronized void handleRestartResponse(boolean wantsRestart) {
        this.restartResponse = wantsRestart;
        this.restartResponseReceived = true;
        
        System.out.println("Player " + playerNumber + " restart response: " + wantsRestart);
        
        // Check if both players have responded
        ClientHandler otherPlayer = (playerNumber == 1) ? 
            session.getPlayer2() : session.getPlayer1();
        
        if (otherPlayer != null && otherPlayer.hasRestartResponse()) {
            // Both responded
            boolean player1Restart = (playerNumber == 1) ? 
                this.restartResponse : otherPlayer.getRestartResponse();
            boolean player2Restart = (playerNumber == 2) ? 
                this.restartResponse : otherPlayer.getRestartResponse();
            
            // Reset flags for next round
            this.restartResponseReceived = false;
            otherPlayer.resetRestartResponse();
            
            session.handleRestartResponses(player1Restart, player2Restart);
        }
    }
    
    /**
     * Checks if restart response has been received.
     * @return true if response received
     */
    public synchronized boolean hasRestartResponse() {
        return restartResponseReceived;
    }
    
    /**
     * Gets the restart response.
     * @return true if player wants to restart
     */
    public synchronized boolean getRestartResponse() {
        return restartResponse;
    }
    
    /**
     * Resets the restart response flag.
     */
    public synchronized void resetRestartResponse() {
        this.restartResponseReceived = false;
    }
    
    /**
     * Sends a message to the client.
     * @param message The message to send
     */
    public void sendMessage(Message message) {
        try {
            output.writeObject(message);
            output.flush();
        } catch (IOException e) {
            System.err.println("Error sending message to Player " + playerNumber + ": " + e.getMessage());
        }
    }
    
    /**
     * Cleans up resources when client disconnects.
     */
    private void cleanup() {
        try {
            if (input != null) input.close();
            if (output != null) output.close();
            if (socket != null) socket.close();
            System.out.println("Cleaned up resources for Player " + playerNumber);
        } catch (IOException e) {
            System.err.println("Error cleaning up Player " + playerNumber + ": " + e.getMessage());
        }
    }
}
