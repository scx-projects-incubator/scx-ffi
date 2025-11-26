package dev.scx.ffi.mapper;

import dev.scx.ffi.type.FFIMapper;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;

import static java.lang.foreign.ValueLayout.JAVA_SHORT;

/// ShortArrayFFIMapper
///
/// @author scx567888
/// @version 0.0.1
public class ShortArrayFFIMapper implements FFIMapper {

    private short[] value;

    public ShortArrayFFIMapper(short[] value) {
        this.value = value;
    }

    public short[] getValue() {
        return value;
    }

    public void setValue(short[] value) {
        this.value = value;
    }

    @Override
    public MemorySegment toMemorySegment(Arena arena) {
        return arena.allocateFrom(JAVA_SHORT, value);
    }

    @Override
    public void fromMemorySegment(MemorySegment memorySegment) {
        var temp = memorySegment.toArray(JAVA_SHORT);
        // 原因参考 IntArrayMapper
        System.arraycopy(temp, 0, value, 0, temp.length);
    }

}
