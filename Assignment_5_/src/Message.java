import java.io.Serializable;
/**
 * Represents a message sent between client and server.
 * Uses serialization for network transmission.
 */
public class Message implements Serializable {
    private static final long serialVersionUID = 1L;

    public enum MessageType {
        NAME,           // Player submits name
        MOVE,           // Player makes a move
        BOARD_UPDATE,   // Server updates board state
        TURN_UPDATE,    // Server indicates whose turn it is
        GAME_RESULT,    // Server sends game result
        RESTART_REQUEST,// Server asks if player wants to restart
        RESTART_RESPONSE,// Client responds to restart request
        PLAYER_LEFT,    // Notify that a player left
        PLAYER_NUMBER,  // Server assigns player number
        SCORE_UPDATE    // Server sends updated scores
    }
    
    private MessageType type;
    private Object data;
    
    /**
     * Creates a new message.
     * @param type 
     * @param data 
     */
    public Message(MessageType type, Object data) {
        this.type = type;
        this.data = data;
    }
    
    /**
     * Gets the message type.
     * @return The message type
     */
    public MessageType getType() {
        return type;
    }
    
    /**
     * Gets the message data.
     * @return The message data
     */
    public Object getData() {
        return data;
    }
}
