package dev.scx.ffi.mapper.struct;

import java.lang.foreign.MemoryLayout;

import static java.lang.foreign.ValueLayout.*;
import static java.lang.foreign.ValueLayout.JAVA_BOOLEAN;
import static java.lang.foreign.ValueLayout.JAVA_CHAR;
import static java.lang.foreign.ValueLayout.JAVA_DOUBLE;
import static java.lang.foreign.ValueLayout.JAVA_FLOAT;
import static java.lang.foreign.ValueLayout.JAVA_LONG;

record PrimitiveNode(String name, Class<?> type) implements Node {

    @Override
    public MemoryLayout createMemoryLayout() {
        if (type == byte.class) {
            return JAVA_BYTE.withName(name);
        }
        if (type == short.class) {
            return JAVA_SHORT.withName(name);
        }
        if (type == int.class) {
            return JAVA_INT.withName(name);
        }
        if (type == long.class) {
            return JAVA_LONG.withName(name);
        }
        if (type == float.class) {
            return JAVA_FLOAT.withName(name);
        }
        if (type == double.class) {
            return JAVA_DOUBLE.withName(name);
        }
        if (type == boolean.class) {
            return JAVA_BOOLEAN.withName(name);
        }
        if (type == char.class) {
            return JAVA_CHAR.withName(name);
        }
        // 这里几乎不可能发生
        throw new IllegalArgumentException("type not primitive: " + type);
    }

}
