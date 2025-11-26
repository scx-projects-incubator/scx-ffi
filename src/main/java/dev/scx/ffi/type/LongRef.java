package dev.scx.ffi.type;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;

import static java.lang.foreign.ValueLayout.JAVA_LONG;

/// LongRef
///
/// @author scx567888
/// @version 0.0.1
public final class LongRef implements FFIMapper {

    private long value;

    public LongRef() {
        this.value = 0;
    }

    public LongRef(long value) {
        this.value = value;
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }

    @Override
    public MemorySegment toMemorySegment(Arena arena) {
        return arena.allocateFrom(JAVA_LONG, this.value);
    }

    @Override
    public void fromMemorySegment(MemorySegment memorySegment) {
        this.value = memorySegment.get(JAVA_LONG, 0);
    }

}
