package dev.scx.ffi.mapper;

import dev.scx.ffi.type.StringRef;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;

/// todo 现在是只读的 应该也允许回显吧
/// StringRefFFMMapper
///
/// @author scx567888
/// @version 0.0.1
public record StringRefFFMMapper(StringRef stringRef) implements FFMMapper {

    @Override
    public MemorySegment toMemorySegment(Arena arena) {
        return arena.allocateFrom(stringRef.getValue(), stringRef.getCharset());
    }

    @Override
    public void fromMemorySegment(MemorySegment memorySegment) {
        // todo 这里 怎么做?
    }

}
