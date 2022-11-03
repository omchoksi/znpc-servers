package io.github.znetworkw.znpcservers.utility.location;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.util.Vector;

public class ZLocation {
    public static final ZLocationSerializer SERIALIZER = new ZLocationSerializer();
    private final String worldName;
    private final double x;
    private final double y;
    private final double z;
    private final float yaw;
    private final float pitch;
    private Location bukkitLocation;

    public ZLocation(String worldName, double x, double y, double z, float yaw, float pitch) {
        this.worldName = worldName;
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public ZLocation(Location location) {
        this(location.getWorld().getName(), location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
    }

    public String getWorldName() {
        return this.worldName;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public double getZ() {
        return this.z;
    }

    public float getYaw() {
        return this.yaw;
    }

    public float getPitch() {
        return this.pitch;
    }

    public Location bukkitLocation() {
        return this.bukkitLocation != null ? this.bukkitLocation : (this.bukkitLocation = new Location(Bukkit.getWorld(this.worldName), this.x, this.y, this.z, this.yaw, this.pitch));
    }

    public Vector toVector() {
        return this.bukkitLocation().toVector();
    }

    static class ZLocationSerializer implements JsonSerializer<ZLocation>, JsonDeserializer<ZLocation> {
        ZLocationSerializer() {
        }

        public JsonElement serialize(ZLocation src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("world", src.getWorldName());
            jsonObject.addProperty("x", src.getX());
            jsonObject.addProperty("y", src.getY());
            jsonObject.addProperty("z", src.getZ());
            jsonObject.addProperty("yaw", src.getYaw());
            jsonObject.addProperty("pitch", src.getPitch());
            return jsonObject;
        }

        public ZLocation deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();
            return new ZLocation(jsonObject.get("world").getAsString(), jsonObject.get("x").getAsDouble(), jsonObject.get("y").getAsDouble(), jsonObject.get("z").getAsDouble(), jsonObject.get("yaw").getAsFloat(), jsonObject.get("pitch").getAsFloat());
        }
    }
}