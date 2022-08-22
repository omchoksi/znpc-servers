package io.github.znetworkw.znpcservers.utils;

import java.lang.reflect.Field;

public final class ReflectionUtils {
    private ReflectionUtils() {
    }

    public static Field findFieldForClass(Object instance, Class<?> type) {
        Field[] var2 = instance.getClass().getDeclaredFields();
        int var3 = var2.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            Field field = var2[var4];
            if (field.getType() == type) {
                field.setAccessible(true);
                return field;
            }
        }

        return null;
    }

    public static Field findFieldForClassAndSet(Object instance, Class<?> type, Object value) throws ReflectiveOperationException {
        Field field = findFieldForClass(instance, type);
        if (field == null) {
            return null;
        } else {
            field.set(instance, value);
            return field;
        }
    }
}
