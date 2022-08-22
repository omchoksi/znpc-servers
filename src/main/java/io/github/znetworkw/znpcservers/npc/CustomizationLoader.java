package io.github.znetworkw.znpcservers.npc;

import com.google.common.collect.Iterables;
import io.github.znetworkw.znpcservers.cache.CachePackage;
import io.github.znetworkw.znpcservers.cache.TypeCache.CacheBuilder;
import io.github.znetworkw.znpcservers.cache.TypeCache.BaseCache.EnumLoader;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

public class CustomizationLoader {
    private final Class<? extends Entity> entityClass;
    private final Map<String, Method> methods;

    public CustomizationLoader(EntityType entityType, Iterable<String> methodsName) {
        this(entityType.getEntityClass(), methodsName);
    }

    protected CustomizationLoader(Class<? extends Entity> entityClass, Iterable<String> methodsName) {
        this.entityClass = entityClass;
        this.methods = this.loadMethods(methodsName);
    }

    protected Map<String, Method> loadMethods(Iterable<String> iterable) {
        Map<String, Method> builder = new HashMap();
        Method[] var3 = this.entityClass.getMethods();
        int var4 = var3.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            Method method = var3[var5];
            if (!builder.containsKey(method.getName()) && Iterables.contains(iterable, method.getName())) {
                Class[] var7 = method.getParameterTypes();
                int var8 = var7.length;

                for(int var9 = 0; var9 < var8; ++var9) {
                    Class<?> parameter = var7[var9];
                    TypeProperty typeProperty = TypeProperty.forType(parameter);
                    if (typeProperty == null && parameter.isEnum()) {
                        (new EnumLoader((new CacheBuilder(CachePackage.DEFAULT)).withClassName(parameter.getTypeName()))).load();
                    }
                }

                builder.put(method.getName(), method);
            }
        }

        return builder;
    }

    public boolean contains(String name) {
        return this.methods.containsKey(name);
    }

    public Map<String, Method> getMethods() {
        return this.methods;
    }
}
