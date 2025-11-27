package dev.scx.ffi.mapper.primitive_ref;

import dev.scx.ffi.mapper.FFMMapper;
import dev.scx.ffi.type.LongRef;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;

import static java.lang.foreign.ValueLayout.JAVA_LONG;

/// LongRefFFMMapper
///
/// 不建议直接使用, 推荐直接使用 [LongRef]
///
/// @author scx567888
/// @version 0.0.1
public record LongRefFFMMapper(LongRef longRef) implements FFMMapper {

    @Override
    public MemorySegment toMemorySegment(Arena arena) {
        return arena.allocateFrom(JAVA_LONG, longRef.getValue());
    }

    @Override
    public void fromMemorySegment(MemorySegment memorySegment) {
        longRef.setValue(memorySegment.get(JAVA_LONG, 0));
    }

}
