package dev.scx.ffi.mapper.primitive_array;

import dev.scx.ffi.type.FFIMapper;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;

import static java.lang.foreign.ValueLayout.JAVA_DOUBLE;

/// DoubleArrayFFIMapper
///
/// @author scx567888
/// @version 0.0.1
public final class DoubleArrayFFIMapper implements FFIMapper {

    private double[] value;

    public DoubleArrayFFIMapper(double[] value) {
        this.value = value;
    }

    public double[] getValue() {
        return value;
    }

    public void setValue(double[] value) {
        this.value = value;
    }

    @Override
    public MemorySegment toMemorySegment(Arena arena) {
        return arena.allocateFrom(JAVA_DOUBLE, value);
    }

    @Override
    public void fromMemorySegment(MemorySegment memorySegment) {
        var temp = memorySegment.toArray(JAVA_DOUBLE);
        // 原因参考 IntArrayFFIMapper
        System.arraycopy(temp, 0, value, 0, temp.length);
    }

}
