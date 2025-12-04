import java.io.*; 
import java.net.*;


public class TicTacToeServer {
    private ServerSocket serverSocket;
    private static final int PORT = 12345;
    private GameSession currentSession;

    public TicTacToeServer() {    
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("Tic-Tac-Toe Server started on port " + PORT);
            System.out.println("Waiting for players to connect...");
            start();
        } catch (IOException e) {
            System.err.println("Error starting server: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public void start() {
        while (true) {
            try {
                // Wait for first player
                System.out.println("Waiting for Player 1...");
                Socket player1Socket = serverSocket.accept();
                System.out.println("Player 1 connected from: " + player1Socket.getInetAddress());
                
                ClientHandler player1 = new ClientHandler(player1Socket, 1);
                
                // Wait for second player
                System.out.println("Waiting for Player 2...");
                Socket player2Socket = serverSocket.accept();
                System.out.println("Player 2 connected from: " + player2Socket.getInetAddress());
                
                ClientHandler player2 = new ClientHandler(player2Socket, 2);
                
                // Create game session with both players
                currentSession = new GameSession(player1, player2);
                System.out.println("Game session started!");
                
                // Start both client handler threads
                player1.start();
                player2.start();
                
                
            } catch (IOException e) {
                System.err.println("Error accepting client connection: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Main method to start the server.
     */
    public static void main(String[] args) {
        new TicTacToeServer();
    }
}
