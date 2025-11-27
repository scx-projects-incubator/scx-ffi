package dev.scx.ffi.mapper.struct;

import java.lang.foreign.MemoryLayout;

sealed interface Node permits MemorySegmentNode, PrimitiveNode, StructNode {

    MemoryLayout createMemoryLayout();

}
