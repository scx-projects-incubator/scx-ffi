package dev.scx.ffi.type;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;

import static java.lang.foreign.ValueLayout.JAVA_BYTE;

/// ByteRef
///
/// @author scx567888
/// @version 0.0.1
public final class ByteRef implements FFIMapper {

    private byte value;

    public ByteRef() {
        this.value = 0;
    }

    public ByteRef(byte value) {
        this.value = value;
    }

    public byte getValue() {
        return value;
    }

    public void setValue(byte value) {
        this.value = value;
    }

    @Override
    public MemorySegment toMemorySegment(Arena arena) {
        return arena.allocateFrom(JAVA_BYTE, this.value);
    }

    @Override
    public void fromMemorySegment(MemorySegment memorySegment) {
        this.value = memorySegment.get(JAVA_BYTE, 0);
    }

}
