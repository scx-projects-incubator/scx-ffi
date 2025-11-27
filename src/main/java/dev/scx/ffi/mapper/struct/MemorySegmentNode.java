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
        if (value instanceof MemorySegment seg) {
            memorySegment.set(ADDRESS, offset, seg);
            return ADDRESS.byteSize();
        } else {
            throw new IllegalArgumentException("Expected MemorySegment value");
        }
    }

}
