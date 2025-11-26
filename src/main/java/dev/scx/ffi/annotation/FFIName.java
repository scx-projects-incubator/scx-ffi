package dev.scx.ffi.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/// FFIName
///
/// @author scx567888
/// @version 0.0.1
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface FFIName {

    String value();

}
