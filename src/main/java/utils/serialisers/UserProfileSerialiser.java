package utils.serialisers;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import model.UserProfile;

import java.lang.reflect.Type;

/**
 * alex on 13.11.15.
 */
public class UserProfileSerialiser implements JsonSerializer<UserProfile> {

    @Override
    public JsonElement serialize(UserProfile userProfile, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject userObject = new JsonObject();
        userObject.addProperty("first_name", userProfile.getFirstName());
        userObject.addProperty("last_name", userProfile.getLastName());
        userObject.addProperty("email", userProfile.getEmail());
        userObject.addProperty("avatar", userProfile.getAvatarUrl());
        userObject.addProperty("score", userProfile.getScore());

        return userObject;
    }
}
