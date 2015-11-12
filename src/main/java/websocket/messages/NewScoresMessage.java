package websocket.messages;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.Map;

/**
 * alex on 11.11.15.
 */
public class NewScoresMessage implements Message {

    private final Map<String, Object> parameters;
    private final Map<String, Integer> scoresMap;

    public NewScoresMessage(Map<String, Object> parameters, Map<String, Integer> scoresMap) {
        this.parameters = parameters;
        this.scoresMap = scoresMap;
    }

    @Override
    public JsonObject getAsJsonObject() {
        Gson gson = new Gson();
        JsonObject message = gson.toJsonTree(parameters).getAsJsonObject();

        JsonArray playersArray = new JsonArray();
        message.add("players", playersArray);

        for (Map.Entry<String, Integer> entry : scoresMap.entrySet()) {
            JsonObject player = new JsonObject();
            player.addProperty("email", entry.getKey());
            player.addProperty("score", entry.getValue());
            playersArray.add(player);
        }

        return message;
    }

    @Override
    public String getAsString() {
        return getAsJsonObject().toString();
    }
}
