package dev.scx.ffi.type;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;

import static java.lang.foreign.ValueLayout.JAVA_FLOAT;

/// FloatRef
///
/// @author scx567888
/// @version 0.0.1
public final class FloatRef implements FFIMapper {

    private float value;

    public FloatRef() {
        this.value = 0;
    }

    public FloatRef(float value) {
        this.value = value;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    @Override
    public MemorySegment toMemorySegment(Arena arena) {
        return arena.allocateFrom(JAVA_FLOAT, this.value);
    }

    @Override
    public void fromMemorySegment(MemorySegment memorySegment) {
        this.value = memorySegment.get(JAVA_FLOAT, 0);
    }

}
