package dev.scx.ffi.mapper.struct;

import dev.scx.reflect.FieldInfo;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;

sealed interface Node permits MemorySegmentNode, PrimitiveNode, StructNode {

    MemoryLayout createMemoryLayout();

    FieldInfo fieldInfo();

    long writeToMemorySegment(MemorySegment memorySegment, long offset, Object value);

}
