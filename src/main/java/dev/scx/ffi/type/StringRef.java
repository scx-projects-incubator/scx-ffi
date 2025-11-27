package dev.scx.ffi.type;

/// StringRef
///
/// @author scx567888
/// @version 0.0.1
public final class StringRef {

    private String value;

    public StringRef(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
