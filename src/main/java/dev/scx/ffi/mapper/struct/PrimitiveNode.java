package dev.scx.ffi.mapper.struct;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;

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

    @Override
    public long writeToMemorySegment(MemorySegment memorySegment, int offset, Object value) {
        if (type == byte.class) {
            memorySegment.set(JAVA_BYTE, offset, (Byte) value);
            return JAVA_BYTE.byteSize();
        } else if (type == short.class) {
            memorySegment.set(JAVA_SHORT, offset, (Short) value);
            return JAVA_SHORT.byteSize();
        } else if (type == int.class) {
            memorySegment.set(JAVA_INT, offset, (Integer) value);
            return JAVA_INT.byteSize();
        } else if (type == long.class) {
            memorySegment.set(JAVA_LONG, offset, (Long) value);
            return JAVA_LONG.byteSize();
        } else if (type == float.class) {
            memorySegment.set(JAVA_FLOAT, offset, (Float) value);
            return JAVA_FLOAT.byteSize();
        } else if (type == double.class) {
            memorySegment.set(JAVA_DOUBLE, offset, (Double) value);
            return JAVA_DOUBLE.byteSize();
        } else if (type == boolean.class) {
            memorySegment.set(JAVA_BOOLEAN, offset, (Boolean) value);
            return JAVA_BOOLEAN.byteSize();
        } else if (type == char.class) {
            memorySegment.set(JAVA_CHAR, offset, (Character) value);
            return JAVA_CHAR.byteSize();
        } else {
            throw new IllegalArgumentException("Unsupported primitive type: " + type);
        }
    }

}
