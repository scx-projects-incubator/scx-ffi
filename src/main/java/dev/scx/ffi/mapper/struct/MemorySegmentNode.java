package dev.scx.ffi.mapper.struct;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;

import static java.lang.foreign.ValueLayout.ADDRESS;

record MemorySegmentNode(String name) implements Node {

    @Override
    public MemoryLayout createMemoryLayout() {
        return ADDRESS.withName(name);
    }

    @Override
    public long writeToMemorySegment(MemorySegment memorySegment, long offset, Object value) {
        if (value instanceof MemorySegment seg) {
            memorySegment.set(ADDRESS, offset, seg);
            return ADDRESS.byteSize(); // 返回写入的字节长度
        }
        throw new IllegalArgumentException("Expected MemorySegment value");
    }

}
