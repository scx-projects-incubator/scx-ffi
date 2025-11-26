package dev.scx.ffi.mapper.primitive_array;

import dev.scx.ffi.type.FFIMapper;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;

import static java.lang.foreign.ValueLayout.JAVA_LONG;

/// LongArrayFFIMapper
///
/// @author scx567888
/// @version 0.0.1
public final class LongArrayFFIMapper implements FFIMapper {

    private long[] value;

    public LongArrayFFIMapper(long[] value) {
        this.value = value;
    }

    public long[] getValue() {
        return value;
    }

    public void setValue(long[] value) {
        this.value = value;
    }

    @Override
    public MemorySegment toMemorySegment(Arena arena) {
        return arena.allocateFrom(JAVA_LONG, value);
    }

    @Override
    public void fromMemorySegment(MemorySegment memorySegment) {
        var temp = memorySegment.toArray(JAVA_LONG);
        // 原因参考 IntArrayFFIMapper
        System.arraycopy(temp, 0, value, 0, temp.length);
    }

}
