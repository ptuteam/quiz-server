package game;

/**
 * alex on 22.10.15.
 */
public interface GameField {

    void setPlayerAnswer(Player player, String answer);

    void play();

    void gameOver();
}