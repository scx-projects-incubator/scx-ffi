package dev.scx.ffi.mapper;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;

// todo 这个怎么实现?
/// BooleanArrayFFMMapper
///
/// 不建议直接使用, 推荐直接使用 `boolean[]`
///
/// @author scx567888
/// @version 0.0.1
public record BooleanArrayFFMMapper(boolean[] value) implements FFMMapper {

    @Override
    public MemorySegment toMemorySegment(Arena arena) {
        return null;
    }

    @Override
    public void fromMemorySegment(MemorySegment memorySegment) {

    }

}
