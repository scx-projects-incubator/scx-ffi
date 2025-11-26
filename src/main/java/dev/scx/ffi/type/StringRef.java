package dev.scx.ffi.type;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.nio.charset.Charset;

import static java.nio.charset.StandardCharsets.UTF_8;

///  todo 现在是只读的 应该也允许回显吧
/// StringRef
///
/// @author scx567888
/// @version 0.0.1
public final class StringRef implements FFIMapper {

    private final String value;
    private final Charset charset;

    public StringRef(String value) {
        this.value = value;
        this.charset = UTF_8;
    }

    public StringRef(String value, Charset charset) {
        this.value = value;
        this.charset = charset;
    }

    @Override
    public MemorySegment toMemorySegment(Arena arena) {
        return arena.allocateFrom(value, charset);
    }

    @Override
    public void fromMemorySegment(MemorySegment memorySegment) {

    }

}
