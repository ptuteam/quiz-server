package websocket.messages;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import game.Player;

import java.util.Collection;
import java.util.Map;

/**
 * alex on 11.11.15.
 */
public class NewScoresMessage implements Message {

    private final Player player;
    private final Collection<Player> players;
    private final Map<String, Object> parameters;

    public NewScoresMessage(Map<String, Object> parameters, Player player, Collection<Player> players) {
        this.parameters = parameters;
        this.player = player;
        this.players = players;
    }

    @Override
    public JsonObject getAsJsonObject() {
        Gson gson = new Gson();
        JsonObject message = gson.toJsonTree(parameters).getAsJsonObject();

        JsonObject playerObject = new JsonObject();
        message.add("player", playerObject);
        playerObject.addProperty("email", player.getUserEmail());
        playerObject.addProperty("score", player.getScore());

        JsonArray opponentsArray = new JsonArray();
        message.add("opponents", opponentsArray);

        for (Player p : players) {
            JsonObject opponentObject = new JsonObject();
            opponentObject.addProperty("email", p.getUserEmail());
            opponentObject.addProperty("score", p.getScore());
            opponentsArray.add(opponentObject);
        }

        return message;
    }

    @Override
    public String getAsString() {
        return getAsJsonObject().toString();
    }
}
