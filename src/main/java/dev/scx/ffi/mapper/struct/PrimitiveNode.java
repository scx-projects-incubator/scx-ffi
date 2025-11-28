package dev.scx.ffi.mapper.struct;

import dev.scx.reflect.FieldInfo;
import dev.scx.reflect.PrimitiveTypeInfo;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;

import static dev.scx.ffi.mapper.struct.FFIStructSupport.getPrimitiveValueLayout;

final class PrimitiveNode implements Node {

    private final FieldInfo fieldInfo;
    private final ValueLayout valueLayout;

    public PrimitiveNode(FieldInfo fieldInfo, PrimitiveTypeInfo typeInfo) {
        this.fieldInfo = fieldInfo;
        this.valueLayout = getPrimitiveValueLayout(typeInfo.rawClass());
    }

    @Override
    public MemoryLayout createMemoryLayout() {
        var memoryLayout = valueLayout;
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
