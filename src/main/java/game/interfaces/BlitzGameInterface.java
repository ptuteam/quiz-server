package game.interfaces;

import game.Player;

/**
 * alex on 22.10.15.
 */
public interface BlitzGameInterface {

    void setPlayerAnswer(Player player, String answer);

    void play();

    void gameOver();
}