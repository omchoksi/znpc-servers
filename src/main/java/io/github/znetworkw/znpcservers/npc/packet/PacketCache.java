package io.github.znetworkw.znpcservers.npc.packet;

import com.google.common.collect.ImmutableMap;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class PacketCache {
    protected static final ImmutableMap<Method, PacketValue> VALUE_LOOKUP_BY_NAME;
    private final Map<String, Object> packetResultCache;
    private final Packet proxyInstance;

    public PacketCache(Packet packet) {
        this.packetResultCache = new ConcurrentHashMap<>();
        this.proxyInstance = this.newProxyInstance(packet);
    }

    public PacketCache() {
        this(PacketFactory.PACKET_FOR_CURRENT_VERSION);
    }

    public Packet getProxyInstance() {
        return this.proxyInstance;
    }

    protected Packet newProxyInstance(Packet packet) {
        return (Packet)Proxy.newProxyInstance(packet.getClass().getClassLoader(), new Class[]{Packet.class}, new PacketHandler(this, packet));
    }

    private Object getOrCache(Packet instance, Method method, Object[] args) {
        if (!VALUE_LOOKUP_BY_NAME.containsKey(method)) {
            throw new IllegalStateException("value not found for method: " + method.getName());
        } else {
            PacketValue packetValue = (PacketValue)VALUE_LOOKUP_BY_NAME.get(method);
            String keyString = packetValue.valueType().resolve(packetValue.keyName(), args);
            return this.packetResultCache.computeIfAbsent(keyString, (o) -> {
                try {
                    return method.invoke(instance, args);
                } catch (IllegalAccessException | InvocationTargetException var5) {
                    throw new AssertionError("can't invoke method: " + method.getName(), var5);
                }
            });
        }
    }

    public void flushCache(String... strings) {
        Set<Map.Entry<String, Object>> set = this.packetResultCache.entrySet();

        for (String string : strings) {
            set.removeIf((entry) -> {
                return ((String) entry.getKey()).startsWith(string);
            });
        }

    }

    public void flushCache() {
        this.flushCache((String[])VALUE_LOOKUP_BY_NAME.values().stream().map(PacketValue::keyName).toArray(String[]::new));
    }

    static {
        ImmutableMap.Builder<Method, PacketValue> methodPacketValueBuilder = ImmutableMap.builder();
        Method[] var1 = Packet.class.getMethods();

        for (Method method : var1) {
            if (method.isAnnotationPresent(PacketValue.class)) {
                methodPacketValueBuilder.put(method, (PacketValue) method.getAnnotation(PacketValue.class));
            }
        }

        VALUE_LOOKUP_BY_NAME = methodPacketValueBuilder.build();
    }

    private static class PacketHandler implements InvocationHandler {
        private final PacketCache packetCache;
        private final Packet packets;

        public PacketHandler(PacketCache packetCache, Packet packets) {
            this.packetCache = packetCache;
            this.packets = packets;
        }

        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            return PacketCache.VALUE_LOOKUP_BY_NAME.containsKey(method) ? this.packetCache.getOrCache(this.packets, method, args) : method.invoke(this.packets, args);
        }
    }
}
