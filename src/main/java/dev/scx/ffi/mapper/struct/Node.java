package dev.scx.ffi.mapper.struct;

import dev.scx.reflect.FieldInfo;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;

sealed interface Node permits PrimitiveNode, MemorySegmentNode, StructNode {

    /// 创建一个内存布局
    MemoryLayout createMemoryLayout();

    /// fieldInfo
    FieldInfo fieldInfo();

    /// 将 targetObject 写入到 memorySegment 中, 注意 不是直接读取 targetObject 本体, 而是读取 targetObject 的字段.
    long writeToMemorySegment(MemorySegment memorySegment, long offset, Object targetObject) throws IllegalAccessException;

    /// 将 memorySegment 读取到 targetObject 中, 注意 不是直接写入 targetObject 本体, 而是写入 targetObject 的字段.
    long readFromMemorySegment(MemorySegment memorySegment, long offset, Object targetObject) throws IllegalAccessException;

}
