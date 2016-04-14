package joshie.progression.json;

import com.google.gson.JsonObject;

import java.util.UUID;

public class DataGeneric {
    public DataGeneric() {}
    public DataGeneric(UUID uuid, String type, JsonObject data) {
        this.uuid = uuid;
        this.type = type;
        this.data = data;
    }

    public UUID uuid;
    public String type;
    public JsonObject data;
}
