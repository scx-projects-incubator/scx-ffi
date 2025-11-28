package dev.scx.ffi.mapper.struct;

import dev.scx.reflect.FieldInfo;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;

import static java.lang.foreign.ValueLayout.ADDRESS;

final class MemorySegmentNode implements Node {

    private final FieldInfo fieldInfo;

    public MemorySegmentNode(FieldInfo fieldInfo) {
        this.fieldInfo = fieldInfo;
    }

    @Override
    public MemoryLayout createMemoryLayout() {
        var memoryLayout = ADDRESS;
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
    public long writeToMemorySegment(MemorySegment memorySegment, long offset, Object targetObject) throws IllegalAccessException {
        var value = fieldInfo.get(targetObject);
        ADDRESS.varHandle().set(memorySegment, offset, value);
        return ADDRESS.byteSize();
    }

    @Override
    public long readFromMemorySegment(MemorySegment memorySegment, long offset, Object targetObject) throws IllegalAccessException {
        var value = ADDRESS.varHandle().get(memorySegment, offset);
        fieldInfo.set(targetObject, value);
        return ADDRESS.byteSize();
    }

}
