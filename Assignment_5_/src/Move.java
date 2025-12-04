import java.io.Serializable;
/**
 * Represents a move on the board.
 */
public class Move implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int row;
    private int col;
    private String mark; // "X" or "O"
    
    /**
     * Creates a new move.
     * @param row 
     * @param col
     * @param mark
     */
    public Move(int row, int col, String mark) {
        this.row = row;
        this.col = col;
        this.mark = mark;
    }
    
    public int getRow() { 
        return row; 
    }
    public int getCol() { 
        return col; 
    }
    public String getMark() { 
        return mark; 
    }
}