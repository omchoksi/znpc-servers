package io.github.znetworkw.znpcservers.npc;

import io.github.znetworkw.znpcservers.utility.Utils;

public enum NamingType {
    DEFAULT {
        public String resolve(NPC npc) {
            return Utils.randomString(6);
        }
    };

    private static final int FIXED_LENGTH = 6;

    private NamingType() {
    }

    public abstract String resolve(NPC var1);
}
