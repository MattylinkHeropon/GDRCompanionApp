package customGsonClass;

import com.google.gson.*;
import hero.magic.casterClass.Caster_Class_Base;

import java.lang.reflect.Type;

public class CasterClassAdapter implements JsonSerializer<Caster_Class_Base>, JsonDeserializer<Caster_Class_Base> {
    @Override
    public JsonElement serialize(Caster_Class_Base casterClassBase, Type type, JsonSerializationContext context) {
        JsonObject result = new JsonObject();
        result.add("type", new JsonPrimitive(casterClassBase.getClass().getSimpleName()));
        result.add("properties", context.serialize(casterClassBase, casterClassBase.getClass()));
        return result;
    }

    @Override
    public Caster_Class_Base deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        String type = jsonObject.get("type").getAsString();
        JsonElement element = jsonObject.get("properties");

        try {
            return context.deserialize(element, Class.forName("hero.magic.casterClass." + type));
        } catch (ClassNotFoundException cnfe) {
            throw new JsonParseException("Unknown element type: " + type, cnfe);
        }
    }


}
