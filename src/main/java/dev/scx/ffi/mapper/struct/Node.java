package dev.scx.ffi.mapper.struct;

import dev.scx.reflect.FieldInfo;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;

///
sealed interface Node permits MemorySegmentNode, PrimitiveNode, StructNode {

    /// 创建一个内存布局
    MemoryLayout createMemoryLayout();

    /// fieldInfo
    FieldInfo fieldInfo();

    /// 将 value 写入 MemorySegment
    long writeToMemorySegment(MemorySegment memorySegment, long offset, Object value) throws IllegalAccessException;

    /// 将 memorySegment 反射读取到 targetObject 中, 注意 不是直接修改 targetObject 本体而是修改 targetObject 的字段
    long readFromMemorySegment(MemorySegment memorySegment, long offset, Object targetObject) throws IllegalAccessException;

}
