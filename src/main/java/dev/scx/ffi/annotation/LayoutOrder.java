package dev.scx.ffi.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/// LayoutOrder
///
/// 指定字段内存布局顺序, 一般用于 [dev.scx.ffi.type.FFIStruct]
///
/// @author scx567888
/// @version 0.0.1
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface LayoutOrder {

    int value();

}
