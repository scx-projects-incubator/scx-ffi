package dev.scx.ffi.type;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;

/// 内存段映射器
///
/// @author scx567888
/// @version 0.0.1
public interface FFIMapper {

    /// 将内部数据转换为 MemorySegment (内存段)
    ///
    /// @param arena 作用域
    /// @return MemorySegment
    MemorySegment toMemorySegment(Arena arena);

    /// 从 MemorySegment (内存段) 设置值
    ///
    /// @param memorySegment a
    void fromMemorySegment(MemorySegment memorySegment);

}
