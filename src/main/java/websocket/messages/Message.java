package websocket.messages;

import com.google.gson.JsonObject;

/**
 * alex on 13.11.15.
 */
public interface Message {
    @SuppressWarnings("unused")
    JsonObject getAsJsonObject();
    String getAsString();
}
