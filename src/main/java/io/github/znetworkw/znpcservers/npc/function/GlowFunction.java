package io.github.znetworkw.znpcservers.npc.function;

import io.github.znetworkw.znpcservers.cache.CacheRegistry;
import io.github.znetworkw.znpcservers.npc.FunctionContext;
import io.github.znetworkw.znpcservers.npc.FunctionFactory;
import io.github.znetworkw.znpcservers.npc.NPC;
import io.github.znetworkw.znpcservers.npc.NPCFunction;

import java.lang.reflect.Method;

public class GlowFunction extends NPCFunction {
    public GlowFunction() {
        super("glow");
    }

    protected NPCFunction.ResultType runFunction(NPC npc, FunctionContext functionContext) {
        if (!(functionContext instanceof FunctionContext.ContextWithValue))
            throw new IllegalStateException("invalid context type, " + functionContext.getClass().getSimpleName() + ", expected ContextWithValue.");
        String glowColorName = ((FunctionContext.ContextWithValue)functionContext).getValue();
        try {
            Object glowColor = ((Method)CacheRegistry.ENUM_CHAT_FORMAT_FIND.load()).invoke(null, (
                    glowColorName == null || glowColorName.length() == 0) ? "WHITE" : glowColorName);
            npc.setGlowColor(glowColor);
            ((Method)CacheRegistry.SET_DATA_WATCHER_METHOD.load()).invoke(((Method)CacheRegistry.GET_DATA_WATCHER_METHOD.load()).invoke(npc.getNmsEntity()), CacheRegistry.DATA_WATCHER_OBJECT_CONSTRUCTOR
                    .load().newInstance(0, CacheRegistry.DATA_WATCHER_REGISTER_FIELD
                    .load()), (byte) (!FunctionFactory.isTrue(npc, this) ? 64 : 0));
            npc.getPackets().getProxyInstance().update(npc.getPackets());
            npc.deleteViewers();
            return NPCFunction.ResultType.SUCCESS;
        } catch (ReflectiveOperationException operationException) {
            return NPCFunction.ResultType.FAIL;
        }
    }

    protected boolean allow(NPC npc) {
        return npc.getPackets().getProxyInstance().allowGlowColor();
    }

    public NPCFunction.ResultType resolve(NPC npc) {
        return runFunction(npc, (FunctionContext)new FunctionContext.ContextWithValue(npc, npc.getNpcPojo().getGlowName()));
    }
}
