
/**
 * Manages a single game session between two players.
 * Handles game state, turn management, and win/draw detection.
 */
public class GameSession {
    private ClientHandler player1;
    private ClientHandler player2;
    private String[][] board;
    private int currentPlayer; // 1 or 2
    private int player1Wins;
    private int player2Wins;
    private int draws;
    private boolean gameInProgress;
    
    /**
     * Creates a new game session with two client handlers.
     * @param p1 First player's client handler
     * @param p2 Second player's client handler
     */
    public GameSession(ClientHandler p1, ClientHandler p2) {
        this.player1 = p1;
        this.player2 = p2;
        this.player1Wins = 0;
        this.player2Wins = 0;
        this.draws = 0;
        
        // Set session reference in handlers
        player1.setSession(this);
        player2.setSession(this);
        
        // Send player numbers to clients
        player1.sendMessage(new Message(Message.MessageType.PLAYER_NUMBER, 1));
        player2.sendMessage(new Message(Message.MessageType.PLAYER_NUMBER, 2));
        
        System.out.println("Game session created. Waiting for players to submit names...");
    }
    
    /**
     * Initializes a new round of the game.
     * Called when both players have submitted names or after restart.
     */
    public void startNewRound() {
        // Initialize empty board
        board = new String[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = "";
            }
        }
        
        currentPlayer = 1; // Player 1 always starts
        gameInProgress = true;
        
        System.out.println("New round started. Player 1's turn.");
        
        // Notify both players
        player1.sendMessage(new Message(Message.MessageType.TURN_UPDATE, 
            "Your opponent has moved, now is your turn."));
        player2.sendMessage(new Message(Message.MessageType.TURN_UPDATE, 
            "Wait for your opponent to move."));
    }
    
    /**
     * Processes a move from a player.
     * @param playerNum 
     * @param row 
     * @param col 
     * @return true if move was valid, false otherwise
     */
    public synchronized boolean processMove(int playerNum, int row, int col) {
        // Validate move
        if (!gameInProgress) {
            System.out.println("Invalid move: Game not in progress");
            return false;
        }
        
        if (playerNum != currentPlayer) {
            System.out.println("Invalid move: Not player " + playerNum + "'s turn");
            return false;
        }
        
        //if (row < 0 || row > 2 || col < 0 || col > 2) {
        //    System.out.println("Invalid move: Out of bounds");
        //    return false;
        //}
        
        if (!board[row][col].isEmpty()) {
            System.out.println("Invalid move: Cell already occupied");
            return false;
        }
        
        // Move is valid - update board
        String mark = (playerNum == 1) ? "X" : "O";
        board[row][col] = mark;
        
        System.out.println("Player " + playerNum + " (" + mark + ") moved to [" + row + ", " + col + "]");
        
        // Broadcast move to both players
        Move move = new Move(row, col, mark);
        player1.sendMessage(new Message(Message.MessageType.BOARD_UPDATE, move));
        player2.sendMessage(new Message(Message.MessageType.BOARD_UPDATE, move));
        
        // Check for win/draw
        int gameStatus = checkGameStatus();
        if (gameStatus != 0) {
            endRound(gameStatus);
            return true;
        }
        
        // Switch turns
        currentPlayer = (currentPlayer == 1) ? 2 : 1;
        
        // Update turn messages
        if (currentPlayer == 1) {
            player1.sendMessage(new Message(Message.MessageType.TURN_UPDATE, 
                "Your opponent has moved, now is your turn."));
            player2.sendMessage(new Message(Message.MessageType.TURN_UPDATE, 
                "Valid move, wait for your opponent."));
        } else {
            player1.sendMessage(new Message(Message.MessageType.TURN_UPDATE, 
                "Valid move, wait for your opponent."));
            player2.sendMessage(new Message(Message.MessageType.TURN_UPDATE, 
                "Your opponent has moved, now is your turn."));
        }
        
        return true;
    }
    
    /**
     * Checks if there is a winner or draw.
     * @return 0 for no winner, 1 for player1, 2 for player2, 3 for draw
     */
    private int checkGameStatus() {
        // Check rows
        for (int i = 0; i < 3; i++) {
            if (!board[i][0].isEmpty() && 
                board[i][0].equals(board[i][1]) && 
                board[i][0].equals(board[i][2])) {
                return board[i][0].equals("X") ? 1 : 2;
            }
        }
        
        // Check columns
        for (int i = 0; i < 3; i++) {
            if (!board[0][i].isEmpty() && 
                board[0][i].equals(board[1][i]) && 
                board[0][i].equals(board[2][i])) {
                return board[0][i].equals("X") ? 1 : 2;
            }
        }
        
        // Check diagonals
        if (!board[0][0].isEmpty() && 
            board[0][0].equals(board[1][1]) && 
            board[0][0].equals(board[2][2])) {
            return board[0][0].equals("X") ? 1 : 2;
        }
        
        if (!board[0][2].isEmpty() && 
            board[0][2].equals(board[1][1]) && 
            board[0][2].equals(board[2][0])) {
            return board[0][2].equals("X") ? 1 : 2;
        }
        
        // Check for draw (board full)
        boolean boardFull = true;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j].isEmpty()) {
                    boardFull = false;
                    break;
                }
            }
            if (!boardFull) break;
        }
        
        if (boardFull) {
            return 3; // Draw
        }
        
        return 0; // Game continues
    }
    
    /**
     * Handles the end of a round.
     * @param result Game result (1=P1 win, 2=P2 win, 3=draw)
     */
    private void endRound(int result) {
        gameInProgress = false;
        
        // Update scores
        if (result == 1) {
            player1Wins++;
            System.out.println("Player 1 wins! Score: P1=" + player1Wins + " P2=" + player2Wins + " Draws=" + draws);
        } else if (result == 2) {
            player2Wins++;
            System.out.println("Player 2 wins! Score: P1=" + player1Wins + " P2=" + player2Wins + " Draws=" + draws);
        } else if (result == 3) {
            draws++;
            System.out.println("Draw! Score: P1=" + player1Wins + " P2=" + player2Wins + " Draws=" + draws);
        }
        
        // Send updated scores to both players
        ScoreData scoreData = new ScoreData(player1Wins, player2Wins, draws);
        player1.sendMessage(new Message(Message.MessageType.SCORE_UPDATE, scoreData));
        player2.sendMessage(new Message(Message.MessageType.SCORE_UPDATE, scoreData));
        
        // Send game result to both players
        String p1Message, p2Message;
        
        if (result == 1) {
            p1Message = "You win!";
            p2Message = "You lose.";
        } else if (result == 2) {
            p1Message = "You lose.";
            p2Message = "You win!";
        } else {
            p1Message = "Draw!";
            p2Message = "Draw!";
        }
        
        player1.sendMessage(new Message(Message.MessageType.GAME_RESULT, p1Message));
        player2.sendMessage(new Message(Message.MessageType.GAME_RESULT, p2Message));
        
        // Server waits for both players' restart responses (handled in ClientHandler)
    }
    
    /**
     * Handles restart responses from both players.
     * @param player1Restart Player 1's restart choice
     * @param player2Restart Player 2's restart choice
     */
    public synchronized void handleRestartResponses(boolean player1Restart, boolean player2Restart) {
        if (player1Restart && player2Restart) {
            System.out.println("Both players chose to restart.");
            startNewRound();
        } else {
            System.out.println("At least one player chose not to restart. Game ending.");
            // Game ends - clients will handle disconnection
        }
    }
    
    /**
     * Handles a player leaving the game.
     * @param playerNum The player who left
     */
    public void handlePlayerLeft(int playerNum) {
        System.out.println("Player " + playerNum + " left the game.");
        gameInProgress = false;
        
        // Notify the other player
        if (playerNum == 1 && player2 != null) {
            player2.sendMessage(new Message(Message.MessageType.PLAYER_LEFT, 
                "Game Ends. One of the players left."));
        } else if (playerNum == 2 && player1 != null) {
            player1.sendMessage(new Message(Message.MessageType.PLAYER_LEFT, 
                "Game Ends. One of the players left."));
        }
    }
    
    /**
     * Gets Player 1's client handler.
     * @return Player 1's client handler
     */
    public ClientHandler getPlayer1() {
        return player1;
    }
    
    /**
     * Gets Player 2's client handler.
     * @return Player 2's client handler
     */
    public ClientHandler getPlayer2() {
        return player2;
    }
}
