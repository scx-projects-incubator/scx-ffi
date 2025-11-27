package dev.scx.ffi.mapper.struct;

import dev.scx.ffi.mapper.FFMMapper;
import dev.scx.ffi.type.FFIStruct;

import java.lang.foreign.Arena;
import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;

import static dev.scx.ffi.mapper.struct.FFIStructSupport.createNode;

///
/// FFIStructFFMMapper
///
/// 不建议直接使用, 推荐直接使用 [FFIStruct]
///
/// @author scx567888
/// @version 0.0.1
public final class FFIStructFFMMapper implements FFMMapper {

    private final Object ffiStruct;
    private final StructNode node;
    private final MemoryLayout memoryLayout;

    public FFIStructFFMMapper(FFIStruct ffiStruct) {
        this.ffiStruct = ffiStruct;
        // 1, 创建 树形结构 (这里必然是 StructNode)
        this.node = (StructNode) createNode(ffiStruct.getClass());
        // 2, 创建 内存布局
        this.memoryLayout = this.node.createMemoryLayout();
    }

    @Override
    public MemorySegment toMemorySegment(Arena arena) {
        var memorySegment = arena.allocate(memoryLayout);
        try {
            node.writeToMemorySegment(memorySegment, 0, ffiStruct);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("反射发生异常 !!!" + e);
        }
        return memorySegment;
    }

    @Override
    public void fromMemorySegment(MemorySegment memorySegment) {
        try {
            node.readFromMemorySegment(memorySegment, 0, ffiStruct);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("反射发生异常 !!!" + e);
        }
    }

}
