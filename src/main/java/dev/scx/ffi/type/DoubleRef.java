package dev.scx.ffi.type;

/// DoubleRef
///
/// @author scx567888
/// @version 0.0.1
public final class DoubleRef {

    private double value;

    public DoubleRef() {
        this.value = 0;
    }

    public DoubleRef(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

}
