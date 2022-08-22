package io.github.znetworkw.znpcservers.configuration;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.internal.$Gson$Types;
import io.github.znetworkw.znpcservers.ServersNPC;
import io.github.znetworkw.znpcservers.utils.Utils;
import org.bukkit.command.CommandSender;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Collectors;

public class Configuration {
    static final String CONFIG_FORMAT = ".json";
    private static final JsonParser JSON_PARSER = new JsonParser();
    private static final Charset CHARSET;
    private final String name;
    private final Path path;
    private final Map<ConfigurationValue, Object> configurationValues;
    public static final Configuration CONFIGURATION;
    public static final Configuration MESSAGES;
    public static final Configuration CONVERSATIONS;
    public static final Configuration DATA;
    public static final ImmutableList<Configuration> SAVE_CONFIGURATIONS;

    protected Configuration(String name) {
        this(name, ServersNPC.PLUGIN_FOLDER.toPath().resolve(name + ".json"));
    }

    private Configuration(String name, Path path) {
        if (!path.getFileName().toString().endsWith(".json")) {
            throw new IllegalStateException("invalid configuration format for: " + path.getFileName());
        } else {
            this.name = name;
            this.path = path;
            this.configurationValues = (Map<ConfigurationValue, Object>)((ImmutableSet)ConfigurationValue.VALUES_BY_NAME.get(name)).stream().collect(Collectors.toMap((c) -> {
                return c;
            }, ConfigurationValue::getValue));
            this.onLoad();
        }
    }

    protected void onLoad() {
        synchronized(this.path) {
            try {
                BufferedReader reader = Files.newBufferedReader(this.path, CHARSET);

                label209: {
                    try {
                        JsonElement data = JSON_PARSER.parse(reader);
                        if (data != null) {
                            Iterator<ConfigurationValue> var4 = this.configurationValues.keySet().iterator();

                            while(true) {
                                if (!var4.hasNext()) {
                                    break label209;
                                }

                                ConfigurationValue configValue = var4.next();
                                boolean single = this.configurationValues.size() == 1;
                                JsonElement jsonElement = single ? data : (data.isJsonObject() ? data.getAsJsonObject().get(configValue.name()) : null);
                                if (jsonElement != null && !jsonElement.isJsonNull()) {
                                    if (!single && configValue.getPrimitiveType().isEnum()) {
                                        this.configurationValues.put(configValue, ServersNPC.GSON.fromJson(jsonElement, configValue.getPrimitiveType()));
                                    } else {
                                        this.configurationValues.put(configValue, ServersNPC.GSON.fromJson(jsonElement, $Gson$Types.newParameterizedTypeWithOwner((Type)null, configValue.getValue().getClass(), new Type[]{configValue.getPrimitiveType()})));
                                    }
                                }
                            }
                        }
                    } catch (Throwable var17) {
                        if (reader != null) {
                            try {
                                reader.close();
                            } catch (Throwable var16) {
                                var17.addSuppressed(var16);
                            }
                        }

                        throw var17;
                    }

                    if (reader != null) {
                        reader.close();
                    }

                    return;
                }

                if (reader != null) {
                    reader.close();
                }

            } catch (NoSuchFileException ignored) {
            } catch (IOException var19) {
                throw new IllegalStateException("Failed to read config: " + this.name);
            } finally {
                this.save();
            }
        }
    }

    public void save() {
        synchronized(this.path) {
            try {
                BufferedWriter writer = Files.newBufferedWriter(this.path, CHARSET);

                try {
                    ServersNPC.GSON.toJson(this.configurationValues.size() == 1 ? this.configurationValues.values().iterator().next() : this.configurationValues, writer);
                } catch (Throwable var7) {
                    if (writer != null) {
                        try {
                            writer.close();
                        } catch (Throwable var6) {
                            var7.addSuppressed(var6);
                        }
                    }

                    throw var7;
                }

                if (writer != null) {
                    writer.close();
                }
            } catch (IOException var8) {
                throw new IllegalStateException("Failed to save config: " + this.name);
            }

        }
    }

    public <T> T getValue(ConfigurationValue configValue) {
        synchronized(this.path) {
            return (T) configurationValues.get(configValue);
        }
    }

    public void sendMessage(CommandSender sender, ConfigurationValue configValue, Object... replaces) {
        sender.sendMessage(Utils.toColor(String.format((String)this.getValue(configValue), replaces)));
    }

    static {
        CHARSET = StandardCharsets.UTF_8;
        CONFIGURATION = new Configuration("config");
        MESSAGES = new Configuration("messages");
        CONVERSATIONS = new Configuration("conversations");
        DATA = new Configuration("data");
        SAVE_CONFIGURATIONS = ImmutableList.of(CONVERSATIONS, DATA);
    }
}
