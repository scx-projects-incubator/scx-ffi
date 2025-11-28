package dev.scx.ffi.mapper.struct;

import dev.scx.reflect.FieldInfo;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;

record PrimitiveNode(FieldInfo fieldInfo, ValueLayout valueLayout) implements Node {

    @Override
    public MemoryLayout createMemoryLayout() {
        var memoryLayout = valueLayout;
        if (fieldInfo != null) {
            memoryLayout = memoryLayout.withName(fieldInfo.name());
        }
        return memoryLayout;
    }

    @Override
    public long writeToMemorySegment(MemorySegment memorySegment, long offset, Object targetObject) throws IllegalAccessException {
        var value = fieldInfo.get(targetObject);
        valueLayout.varHandle().set(memorySegment, offset, value);
        return valueLayout.byteSize();
    }

    @Override
    public long readFromMemorySegment(MemorySegment memorySegment, long offset, Object targetObject) throws IllegalAccessException {
        var value = valueLayout.varHandle().get(memorySegment, offset);
        fieldInfo.set(targetObject, value);
        return valueLayout.byteSize();
    }

}
