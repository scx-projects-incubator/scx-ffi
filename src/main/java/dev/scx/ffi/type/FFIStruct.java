package dev.scx.ffi.type;

import dev.scx.ffi.annotation.LayoutOrder;

import java.lang.foreign.MemorySegment;

/// 结构数据需要继承此接口 以便在处理时按照结构数据的方式来处理
///
/// ### 关于内存布局
///
/// - 默认使用 字段声明顺序, 如果需要自定义 请使用 [LayoutOrder] 注解.
///
/// ### 支持的 字段类型
///
/// - 1. 基本类型: `byte`, `short`, `int`, `long`, `float`, `double`, `boolean`, `char`.
/// - 2. 内存段: [MemorySegment].
/// - 3. 结构体: [FFIStruct].
///
/// @author scx567888
/// @version 0.0.1
public interface FFIStruct {

}
