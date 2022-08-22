package io.github.znetworkw.znpcservers.npc;

import java.util.function.Function;

public enum TypeProperty {
    STRING(String::toString),
    BOOLEAN(Boolean::parseBoolean),
    INT(Integer::parseInt),
    DOUBLE(Double::parseDouble),
    FLOAT(Float::parseFloat),
    SHORT(Short::parseShort),
    LONG(Long::parseLong);

    private final Function<String, ?> function;

    private TypeProperty(Function<String, ?> function) {
        this.function = function;
    }

    public Function<String, ?> getFunction() {
        return this.function;
    }

    public static TypeProperty forType(Class<?> primitiveType) {
        if (primitiveType == String.class) {
            return STRING;
        } else if (primitiveType == Boolean.TYPE) {
            return BOOLEAN;
        } else if (primitiveType == Integer.TYPE) {
            return INT;
        } else if (primitiveType == Double.TYPE) {
            return DOUBLE;
        } else if (primitiveType == Float.TYPE) {
            return FLOAT;
        } else if (primitiveType == Short.TYPE) {
            return SHORT;
        } else {
            return primitiveType == Long.TYPE ? LONG : null;
        }
    }
}
