package dev.scx.ffi.type;

import java.nio.charset.Charset;

import static java.nio.charset.StandardCharsets.UTF_8;

// todo 这玩意合理吗?
/// StringRef
///
/// @author scx567888
/// @version 0.0.1
public final class StringRef {

    private String value;
    private Charset charset;

    public StringRef(String value) {
        this.value = value;
        this.charset = UTF_8;
    }

    public StringRef(String value, Charset charset) {
        this.value = value;
        this.charset = charset;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Charset getCharset() {
        return charset;
    }

    public void setCharset(Charset charset) {
        this.charset = charset;
    }

}
