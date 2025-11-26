package dev.scx.ffi.mapper;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;

/// todo 现在是只读的 应该也允许回显吧
/// StringFFMMapper
///
/// @author scx567888
/// @version 0.0.1
public record StringFFMMapper(String value) implements FFMMapper {

    @Override
    public MemorySegment toMemorySegment(Arena arena) {
        return arena.allocateFrom(value);
    }

    @Override
    public void fromMemorySegment(MemorySegment memorySegment) {
        // todo 这里 怎么做?
    }

}
