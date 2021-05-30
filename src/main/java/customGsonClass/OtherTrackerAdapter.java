package customGsonClass;

import com.google.gson.*;
import hero.otherTracker.OtherTracker_Base;

import java.lang.reflect.Type;

public class OtherTrackerAdapter implements JsonSerializer<OtherTracker_Base>, JsonDeserializer<OtherTracker_Base> {
    @Override
    public JsonElement serialize(OtherTracker_Base otherTrackerBase, Type type, JsonSerializationContext context) {
        JsonObject result = new JsonObject();
        result.add("type", new JsonPrimitive(otherTrackerBase.getClass().getSimpleName()));
        result.add("properties", context.serialize(otherTrackerBase, otherTrackerBase.getClass()));
        return result;
    }

    @Override
    public OtherTracker_Base deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        String type = jsonObject.get("type").getAsString();
        JsonElement element = jsonObject.get("properties");

        try {
            return context.deserialize(element, Class.forName("hero.otherTracker." + type));
        } catch (ClassNotFoundException cnfe) {
            throw new JsonParseException("Unknown element type: " + type, cnfe);
        }
    }
}
