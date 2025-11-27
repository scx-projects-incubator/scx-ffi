package dev.scx.ffi.mapper.struct;

import dev.scx.function.Function1Void;
import dev.scx.reflect.FieldInfo;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.util.function.Consumer;

import static java.lang.foreign.ValueLayout.ADDRESS;
import static java.lang.foreign.ValueLayout.JAVA_BYTE;

final class MemorySegmentNode implements Node {

    private final FieldInfo fieldInfo;

    public MemorySegmentNode(FieldInfo fieldInfo) {
        this.fieldInfo = fieldInfo;
    }

    @Override
    public MemoryLayout createMemoryLayout() {
        MemoryLayout memoryLayout = ADDRESS;
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
        memorySegment.set(ADDRESS, offset, (MemorySegment) value);
        return ADDRESS.byteSize();
    }

    @Override
    public long readFromMemorySegment(MemorySegment memorySegment, long offset,  Object targetObject) throws IllegalAccessException {
        fieldInfo.set(targetObject,memorySegment.get(ADDRESS, offset));
        return ADDRESS.byteSize();
    }

}
