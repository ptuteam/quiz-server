package websocket.messages;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import game.Player;
import game.Question;
import model.UserProfile;
import utils.serialisers.PlayerSerialiser;
import utils.serialisers.QuestionSerialiser;
import utils.serialisers.UserProfileSerialiser;

import java.util.Map;

/**
 * alex on 11.11.15.
 */
public class SimpleMessage implements Message {

    private final Map<String, Object> parameters;

    public SimpleMessage(Map<String, Object> parameters) {
        this.parameters = parameters;
    }

    @Override
    public JsonObject getAsJsonObject() {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(UserProfile.class, new UserProfileSerialiser());
        builder.registerTypeAdapter(Player.class, new PlayerSerialiser());
        builder.registerTypeAdapter(Question.class, new QuestionSerialiser());
        Gson gson = builder.create();
        return gson.toJsonTree(parameters).getAsJsonObject();
    }

    @Override
    public String getAsString() {
        return getAsJsonObject().toString();
    }
}
