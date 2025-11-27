package dev.scx.ffi.mapper.struct;

import dev.scx.reflect.FieldInfo;
import dev.scx.reflect.PrimitiveTypeInfo;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;

import static java.lang.foreign.ValueLayout.*;

final class PrimitiveNode implements Node {

    private final FieldInfo fieldInfo;
    private final PrimitiveTypeInfo typeInfo;

    public PrimitiveNode(FieldInfo fieldInfo, PrimitiveTypeInfo typeInfo) {
        this.fieldInfo = fieldInfo;
        this.typeInfo = typeInfo;
    }

    @Override
    public MemoryLayout createMemoryLayout() {
        var type = typeInfo.rawClass();
        MemoryLayout memoryLayout;
        if (type == byte.class) {
            memoryLayout = JAVA_BYTE;
        } else if (type == short.class) {
            memoryLayout = JAVA_SHORT;
        } else if (type == int.class) {
            memoryLayout = JAVA_INT;
        } else if (type == long.class) {
            memoryLayout = JAVA_LONG;
        } else if (type == float.class) {
            memoryLayout = JAVA_FLOAT;
        } else if (type == double.class) {
            memoryLayout = JAVA_DOUBLE;
        } else if (type == boolean.class) {
            memoryLayout = JAVA_BOOLEAN;
        } else if (type == char.class) {
            memoryLayout = JAVA_CHAR;
        } else {
            // 这里几乎不可能发生
            throw new IllegalArgumentException("type not primitive: " + type);
        }
        if (fieldInfo != null) {
            memoryLayout = memoryLayout.withName(fieldInfo.name());
        }
        return memoryLayout;
    }

    @Override
    public FieldInfo fieldInfo() {
        return fieldInfo;
    }

    @Override
    public long writeToMemorySegment(MemorySegment memorySegment, long offset, Object value) {
        var type = typeInfo.rawClass();
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
