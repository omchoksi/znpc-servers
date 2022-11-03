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

    /**
     * Creates a new parser for a primitive type.
     *
     * @param function The primitive type parse function.
     */
    TypeProperty(Function<String, ?> function) {
        this.function = function;
    }

    public Function<String, ?> getFunction() {
        return function;
    }

    public static TypeProperty forType(Class<?> primitiveType) {
        if (primitiveType == String.class)
            return STRING;
        else if (primitiveType == boolean.class)
            return BOOLEAN;
        else if (primitiveType == int.class)
            return INT;
        else if (primitiveType == double.class)
            return DOUBLE;
        else if (primitiveType == float.class)
            return FLOAT;
        else if (primitiveType == short.class)
            return SHORT;
        else if (primitiveType == long.class)
            return LONG;
        else
            return null;
    }
}
