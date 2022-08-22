package io.github.znetworkw.znpcservers.npc;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import io.github.znetworkw.znpcservers.npc.NPCFunction.WithoutFunction;
import io.github.znetworkw.znpcservers.npc.NPCFunction.WithoutFunctionSelfUpdate;
import io.github.znetworkw.znpcservers.npc.function.GlowFunction;
import io.github.znetworkw.znpcservers.utils.GuavaCollectors;

public final class FunctionFactory {
    public static ImmutableList<NPCFunction> WITHOUT_FUNCTION = ImmutableList.of(new WithoutFunction("look"), new WithoutFunctionSelfUpdate("holo"), new WithoutFunctionSelfUpdate("mirror"));
    public static ImmutableList<NPCFunction> WITH_FUNCTION = ImmutableList.of(new GlowFunction());
    public static ImmutableList<NPCFunction> ALL;
    public static ImmutableMap<String, NPCFunction> BY_NAME;

    public static NPCFunction findFunctionForName(String name) {
        return BY_NAME.get(name);
    }

    public static ImmutableList<NPCFunction> findFunctionsForNpc(NPC npc) {
        return ALL.stream().filter((function) -> isTrue(npc, function)).collect(GuavaCollectors.toImmutableList());
    }

    public static boolean isTrue(NPC npc, NPCFunction function) {
        return npc.getNpcPojo().getFunctions().getOrDefault(function.getName(), false);
    }

    public static boolean isTrue(NPC npc, String function) {
        return isTrue(npc, findFunctionForName(function));
    }

    private FunctionFactory() {
    }

    static {
        ALL = ImmutableList.<NPCFunction>builder().addAll(WITHOUT_FUNCTION).addAll(WITH_FUNCTION).build();
        BY_NAME = ALL.stream().collect(GuavaCollectors.toImmutableMap(NPCFunction::getName, (function) -> {
            return function;
        }));
    }
}
