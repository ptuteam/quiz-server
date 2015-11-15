package utils.serialisers;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import game.Player;
import model.UserProfile;

import java.lang.reflect.Type;

/**
 * alex on 13.11.15.
 */
public class PlayerSerialiser implements JsonSerializer<Player> {

    @Override
    public JsonElement serialize(Player player, Type type, JsonSerializationContext jsonSerializationContext) {
        UserProfile userProfile = player.getUserProfile();

        JsonObject playerObject = new JsonObject();
        playerObject.addProperty("first_name", userProfile.getFirstName());
        playerObject.addProperty("last_name", userProfile.getLastName());
        playerObject.addProperty("email", userProfile.getEmail());
        playerObject.addProperty("avatar", userProfile.getAvatarUrl());
        playerObject.addProperty("score", player.getScore());

        return playerObject;
    }
}