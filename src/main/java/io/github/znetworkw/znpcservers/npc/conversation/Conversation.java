package io.github.znetworkw.znpcservers.npc.conversation;

import io.github.znetworkw.znpcservers.configuration.ConfigurationConstants;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class Conversation {

    private final String name;
    private final List<ConversationKey> texts;
    private int radius;
    private int delay;

    public Conversation(String name) {
        this(name, (new ArrayList<ConversationKey>()));
    }

    public Conversation(String name, Iterable<String> text) {
        this(name, StreamSupport.stream(text.spliterator(), false).map(ConversationKey::new).collect(Collectors.toList()));
    }

    protected Conversation(String name, List<ConversationKey> text) {
        this.radius = 5;
        this.delay = 10;
        this.name = name;
        this.texts = text;
    }

    public String getName() {
        return this.name;
    }

    public List<ConversationKey> getTexts() {
        return this.texts;
    }

    public int getDelay() {
        return this.delay;
    }

    public int getRadius() {
        return this.radius;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public static Conversation forName(String name) {
        return ConfigurationConstants.NPC_CONVERSATIONS.stream().filter((conversation) -> conversation.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public static boolean exists(String name) {
        return ConfigurationConstants.NPC_CONVERSATIONS.stream().anyMatch((conversation) -> conversation.getName().equalsIgnoreCase(name));
    }
}
