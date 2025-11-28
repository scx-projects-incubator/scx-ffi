package dev.scx.ffi.mapper.string;

import dev.scx.ffi.mapper.FFMMapper;
import dev.scx.ffi.type.StringRef;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;

// todo 这个类的可用性有问题
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
        stringRef.setValue(memorySegment.getString(0, stringRef.getCharset()));
    }

}
