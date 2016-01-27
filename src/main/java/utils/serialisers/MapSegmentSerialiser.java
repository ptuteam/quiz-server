package utils.serialisers;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import game.MapSegment;

import java.lang.reflect.Type;

/**
 * alex on 26.01.16.
 */
public class MapSegmentSerialiser implements JsonSerializer<MapSegment> {

    @Override
    public JsonElement serialize(MapSegment segment, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject segmentObject = new JsonObject();
        segmentObject.addProperty("id", segment.getId());
        segmentObject.addProperty("value", segment.getValue());
        segmentObject.addProperty("user", segment.getUser());

        return segmentObject;
    }
}
