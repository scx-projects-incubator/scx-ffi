package dev.scx.ffi.type.primitive_array;

import dev.scx.ffi.type.FFIMapper;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;

import static java.lang.foreign.ValueLayout.JAVA_CHAR;

/// CharArrayFFIMapper
///
/// @author scx567888
/// @version 0.0.1
public final class CharArrayFFIMapper implements FFIMapper {

    private char[] value;

    public CharArrayFFIMapper(char[] value) {
        this.value = value;
    }

    public char[] getValue() {
        return value;
    }

    public void setValue(char[] value) {
        this.value = value;
    }

    @Override
    public MemorySegment toMemorySegment(Arena arena) {
        return arena.allocateFrom(JAVA_CHAR, value);
    }

    @Override
    public void fromMemorySegment(MemorySegment memorySegment) {
        var temp = memorySegment.toArray(JAVA_CHAR);
        // 原因参考 IntArrayFFIMapper
        System.arraycopy(temp, 0, value, 0, temp.length);
    }

}
