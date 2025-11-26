package dev.scx.ffi.mapper;

import dev.scx.ffi.type.ShortRef;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;

import static java.lang.foreign.ValueLayout.JAVA_SHORT;

/// ShortRefFFMMapper
///
/// 不建议直接使用, 推荐直接使用 [ShortRef]
///
/// @author scx567888
/// @version 0.0.1
public record ShortRefFFMMapper(ShortRef shortRef) implements FFMMapper {

    @Override
    public MemorySegment toMemorySegment(Arena arena) {
        return arena.allocateFrom(JAVA_SHORT, shortRef.getValue());
    }

    @Override
    public void fromMemorySegment(MemorySegment memorySegment) {
        shortRef.setValue(memorySegment.get(JAVA_SHORT, 0));
    }

}
