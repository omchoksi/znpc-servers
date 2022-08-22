package io.github.znetworkw.znpcservers.npc;

public abstract class NPCFunction {
    private final String name;

    public NPCFunction(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    protected abstract boolean allow(NPC var1);

    protected abstract ResultType runFunction(NPC var1, FunctionContext var2);

    public void doRunFunction(NPC npc, FunctionContext functionContext) {
        if (this.allow(npc)) {
            ResultType resultType = this.runFunction(npc, functionContext);
            if (resultType == ResultType.SUCCESS) {
                npc.getNpcPojo().getFunctions().put(this.getName(), !this.isTrue(npc));
            }

        }
    }

    protected ResultType resolve(NPC npc) {
        throw new IllegalStateException("resolve is not implemented.");
    }

    public boolean isTrue(NPC npc) {
        return FunctionFactory.isTrue(npc, this);
    }

    public static class WithoutFunctionSelfUpdate extends WithoutFunction {
        public WithoutFunctionSelfUpdate(String name) {
            super(name);
        }

        protected ResultType runFunction(NPC npc, FunctionContext functionContext) {
            npc.deleteViewers();
            return ResultType.SUCCESS;
        }
    }

    public static class WithoutFunction extends NPCFunction {
        public WithoutFunction(String name) {
            super(name);
        }

        protected ResultType runFunction(NPC npc, FunctionContext functionContext) {
            return ResultType.SUCCESS;
        }

        protected boolean allow(NPC npc) {
            return true;
        }

        protected ResultType resolve(NPC npc) {
            return ResultType.SUCCESS;
        }
    }

    public static enum ResultType {
        SUCCESS,
        FAIL;

        private ResultType() {
        }
    }
}
