package dev.scx.ffi.type;

/// 回调函数需要继承此接口, 同时需要创建一个名为 callbackMethodName 返回值 的方法 (默认叫做 "callback")
///
/// - 关于回调函数支持的参数和返回值类型, 请参考 [dev.scx.ffi.ScxFFI] 中标注的 类型列表.
///
/// @author scx567888
/// @version 0.0.1
public interface FFICallback {

    default String callbackMethodName() {
        return "callback";
    }

}
