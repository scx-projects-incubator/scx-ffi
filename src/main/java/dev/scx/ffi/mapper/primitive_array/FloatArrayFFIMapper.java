package dev.scx.ffi.mapper.primitive_array;

import dev.scx.ffi.type.FFIMapper;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;

import static java.lang.foreign.ValueLayout.JAVA_FLOAT;

/// FloatArrayFFIMapper
///
/// @author scx567888
/// @version 0.0.1
public final class FloatArrayFFIMapper implements FFIMapper {

    private float[] value;

    public FloatArrayFFIMapper(float[] value) {
        this.value = value;
    }

    public float[] getValue() {
        return value;
    }

    public void setValue(float[] value) {
        this.value = value;
    }

    @Override
    public MemorySegment toMemorySegment(Arena arena) {
        return arena.allocateFrom(JAVA_FLOAT, value);
    }

    @Override
    public void fromMemorySegment(MemorySegment memorySegment) {
        var temp = memorySegment.toArray(JAVA_FLOAT);
        // 原因参考 IntArrayFFIMapper
        System.arraycopy(temp, 0, value, 0, temp.length);
    }

}
