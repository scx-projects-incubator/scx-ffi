package dev.scx.ffi.type;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;

import static java.lang.foreign.ValueLayout.JAVA_INT;

/// IntRef
///
/// @author scx567888
/// @version 0.0.1
public final class IntRef implements FFIMapper {

    private int value;

    public IntRef() {
        this.value = 0;
    }

    public IntRef(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public MemorySegment toMemorySegment(Arena arena) {
        return arena.allocateFrom(JAVA_INT, this.value);
    }

    @Override
    public void fromMemorySegment(MemorySegment memorySegment) {
        this.value = memorySegment.get(JAVA_INT, 0);
    }

}
