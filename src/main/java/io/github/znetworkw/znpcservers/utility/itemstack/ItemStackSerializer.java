package io.github.znetworkw.znpcservers.utility.itemstack;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Base64;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

public class ItemStackSerializer implements JsonSerializer<ItemStack>, JsonDeserializer<ItemStack> {
    private static final ItemStack DEFAULT;

    public ItemStackSerializer() {
    }

    public ItemStack deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        try {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(Base64.getDecoder().decode(json.getAsString()));

            ItemStack var6;
            try {
                BukkitObjectInputStream bukkitObjectOutputStream = new BukkitObjectInputStream(byteArrayInputStream);

                try {
                    var6 = (ItemStack)bukkitObjectOutputStream.readObject();
                } catch (Throwable var10) {
                    try {
                        bukkitObjectOutputStream.close();
                    } catch (Throwable var9) {
                        var10.addSuppressed(var9);
                    }

                    throw var10;
                }

                bukkitObjectOutputStream.close();
            } catch (Throwable var11) {
                try {
                    byteArrayInputStream.close();
                } catch (Throwable var8) {
                    var11.addSuppressed(var8);
                }

                throw var11;
            }

            byteArrayInputStream.close();
            return var6;
        } catch (ClassNotFoundException | IOException var12) {
            return DEFAULT;
        }
    }

    public JsonElement serialize(ItemStack src, Type typeOfSrc, JsonSerializationContext context) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

            JsonPrimitive var6;
            try {
                BukkitObjectOutputStream bukkitObjectOutputStream = new BukkitObjectOutputStream(byteArrayOutputStream);

                try {
                    bukkitObjectOutputStream.writeObject(src);
                    var6 = new JsonPrimitive(Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray()));
                } catch (Throwable var10) {
                    try {
                        bukkitObjectOutputStream.close();
                    } catch (Throwable var9) {
                        var10.addSuppressed(var9);
                    }

                    throw var10;
                }

                bukkitObjectOutputStream.close();
            } catch (Throwable var11) {
                try {
                    byteArrayOutputStream.close();
                } catch (Throwable var8) {
                    var11.addSuppressed(var8);
                }

                throw var11;
            }

            byteArrayOutputStream.close();
            return var6;
        } catch (IOException var12) {
            throw new JsonParseException("Cannot serialize itemstack", var12);
        }
    }

    static {
        DEFAULT = new ItemStack(Material.AIR);
    }
}

