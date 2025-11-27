package dev.scx.ffi.mapper.primitive_ref;

import dev.scx.ffi.mapper.FFMMapper;
import dev.scx.ffi.type.BooleanRef;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;

import static java.lang.foreign.ValueLayout.JAVA_BOOLEAN;
import static java.lang.foreign.ValueLayout.JAVA_BYTE;

// todo 这个类有必要存在吗? 写法正确吗?
/// BooleanRefFFMMapper
///
/// 不建议直接使用, 推荐直接使用 [BooleanRef]
///
/// @author scx567888
/// @version 0.0.1
public record BooleanRefFFMMapper(BooleanRef booleanRef) implements FFMMapper {

    @Override
    public MemorySegment toMemorySegment(Arena arena) {
        return arena.allocateFrom(JAVA_BYTE, (byte) (booleanRef.getValue() ? 1 : 0));
    }

    @Override
    public void fromMemorySegment(MemorySegment memorySegment) {
        booleanRef.setValue(memorySegment.get(JAVA_BOOLEAN, 0));
    }

}
