package io.github.znetworkw.znpcservers.configuration;

import io.github.znetworkw.znpcservers.npc.NPC;
import io.github.znetworkw.znpcservers.npc.NPCModel;
import io.github.znetworkw.znpcservers.npc.conversation.Conversation;
import io.github.znetworkw.znpcservers.npc.task.NPCLoadTask;

import java.util.List;

public final class ConfigurationConstants {
    public static final String SPACE_SYMBOL;
    public static final int VIEW_DISTANCE;
    public static final int SAVE_DELAY;
    public static final boolean RGB_ANIMATION;
    public static final List<NPCModel> NPC_LIST;
    public static final List<Conversation> NPC_CONVERSATIONS;

    private ConfigurationConstants() {
    }

    static {
        SPACE_SYMBOL = (String)Configuration.CONFIGURATION.getValue(ConfigurationValue.REPLACE_SYMBOL);
        VIEW_DISTANCE = (Integer)Configuration.CONFIGURATION.getValue(ConfigurationValue.VIEW_DISTANCE);
        SAVE_DELAY = (Integer)Configuration.CONFIGURATION.getValue(ConfigurationValue.SAVE_NPCS_DELAY_SECONDS);
        RGB_ANIMATION = (Boolean)Configuration.CONFIGURATION.getValue(ConfigurationValue.ANIMATION_RGB);
        NPC_LIST = (List)Configuration.DATA.getValue(ConfigurationValue.NPC_LIST);
        NPC_CONVERSATIONS = (List)Configuration.CONVERSATIONS.getValue(ConfigurationValue.CONVERSATION_LIST);
        NPC_LIST.stream().map(NPC::new).forEach(NPCLoadTask::new);
    }
}
