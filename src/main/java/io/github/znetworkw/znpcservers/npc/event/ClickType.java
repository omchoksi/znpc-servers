package io.github.znetworkw.znpcservers.npc.event;

public enum ClickType {
    RIGHT,
    LEFT,
    DEFAULT;

    private ClickType() {
    }

    public static ClickType forName(String clickName) {
        if (clickName.startsWith("INTERACT")) {
            return RIGHT;
        } else {
            return clickName.startsWith("ATTACK") ? LEFT : DEFAULT;
        }
    }
}
