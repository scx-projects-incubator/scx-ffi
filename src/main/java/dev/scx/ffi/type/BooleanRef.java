package dev.scx.ffi.type;

/// BooleanRef
///
/// @author scx567888
/// @version 0.0.1
public final class BooleanRef {

    private boolean value;

    public BooleanRef() {
        this.value = false;
    }

    public BooleanRef(boolean value) {
        this.value = value;
    }

    public boolean value() {
        return value;
    }

    public void value(boolean value) {
        this.value = value;
    }

}
