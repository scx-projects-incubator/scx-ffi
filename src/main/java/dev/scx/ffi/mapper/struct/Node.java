package dev.scx.ffi.mapper.struct;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;

sealed interface Node permits MemorySegmentNode, PrimitiveNode, StructNode {

    MemoryLayout createMemoryLayout();

    long writeToMemorySegment(MemorySegment memorySegment, int offset, Object value);

}
