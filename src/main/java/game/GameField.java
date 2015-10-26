package game;

/**
 * alex on 22.10.15.
 */
public interface GameField {

    void checkPlayerAnswer(Player player, String answer);

    void play();

    void gameOver();
}