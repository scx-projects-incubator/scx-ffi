package dev.scx.ffi.type;

import java.lang.foreign.MemorySegment;

/// 回调函数需要继承此接口, 同时需要创建一个名为 callbackMethodName 返回值 的方法 (默认叫做 "callback")
///
/// ### 回调函数支持的参数类型.
/// - 1. 基本类型: `byte`, `short`, `int`, `long`, `float`, `double`, `boolean`, `char`.
/// - 2. 内存段: [MemorySegment].
///
/// ### 回调函数支持的返回值类型.
/// - 1. 基本类型: `byte`, `short`, `int`, `long`, `float`, `double`, `boolean`, `char`.
/// - 2. 内存段: [MemorySegment].
///
/// @author scx567888
/// @version 0.0.1
public interface FFICallback {

    default String callbackMethodName() {
        return "callback";
    }

}
