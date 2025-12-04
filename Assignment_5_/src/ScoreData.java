import java.io.Serializable;

/**
 * Represents game score information.
 */
public class ScoreData implements Serializable {
    private static final long serialVersionUID = 1L;

    private int player1Wins;
    private int player2Wins;
    private int draws;
    
    /**
     * Creates score data.
     * @param p1Wins Player 1 wins
     * @param p2Wins Player 2 wins
     * @param draws Draw count
     */
    public ScoreData(int p1Wins, int p2Wins, int draws) {
        this.player1Wins = p1Wins;
        this.player2Wins = p2Wins;
        this.draws = draws;
    }
    
    public int getPlayer1Wins() {
         return player1Wins; 
        }
    public int getPlayer2Wins() {
         return player2Wins; 
        }
    public int getDraws() {
         return draws; 
        }
}