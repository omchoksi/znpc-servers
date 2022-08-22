package io.github.znetworkw.znpcservers.npc;

public interface FunctionContext {
    NPC getNPC();

    public static class ContextWithValue extends DefaultContext implements WithValue {
        private final String value;

        public ContextWithValue(NPC npc, String value) {
            super(npc);
            this.value = value;
        }

        public String getValue() {
            return this.value;
        }
    }

    public static class DefaultContext implements FunctionContext {
        private final NPC npc;

        public DefaultContext(NPC npc) {
            this.npc = npc;
        }

        public NPC getNPC() {
            return this.npc;
        }
    }

    public interface WithValue extends FunctionContext {
        String getValue();
    }
}
