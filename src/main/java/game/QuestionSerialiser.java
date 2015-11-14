package game;

import com.google.gson.*;

import java.lang.reflect.Type;

/**
 * alex on 14.11.15.
 */
public class QuestionSerialiser implements JsonSerializer<Question> {

    @Override
    public JsonElement serialize(Question question, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject questionObject = new JsonObject();
        questionObject.addProperty("type", question.getType());
        questionObject.addProperty("title", question.getTitle());

        Gson gson = new Gson();
        questionObject.add("answers", gson.toJsonTree(question.getAnswers()));

        return questionObject;
    }
}