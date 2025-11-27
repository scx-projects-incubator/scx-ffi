package dev.scx.ffi.mapper.struct;

import java.lang.foreign.MemoryLayout;

import static java.lang.foreign.ValueLayout.ADDRESS;

record MemorySegmentNode(String name) implements Node {

    @Override
    public MemoryLayout createMemoryLayout() {
        return ADDRESS.withName(name);
    }

}
