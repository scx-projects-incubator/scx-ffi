package dev.scx.ffi.type;

/// 回调函数需要继承此接口, 同时需要创建一个名为 callbackMethodName 返回值 的方法 (默认叫做 "callback")
///
/// @author scx567888
/// @version 0.0.1
public interface FFICallback {

    default String callbackMethodName() {
        return "callback";
    }

}
