package dev.scx.ffi.mapper.struct;

import dev.scx.reflect.ClassInfo;
import dev.scx.reflect.FieldInfo;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;

record StructNode(FieldInfo fieldInfo, Node[] fieldNodes, ClassInfo classInfo) implements Node {

    @Override
    public MemoryLayout createMemoryLayout() {
        var fieldMemoryLayouts = new MemoryLayout[fieldNodes.length];
        for (int i = 0; i < fieldNodes.length; i = i + 1) {
            fieldMemoryLayouts[i] = fieldNodes[i].createMemoryLayout();
        }
        var memoryLayout = MemoryLayout.structLayout(fieldMemoryLayouts);
        if (fieldInfo != null) {
            memoryLayout = memoryLayout.withName(fieldInfo.name());
        }
        return memoryLayout;
    }

    @Override
    public long writeToMemorySegment(MemorySegment memorySegment, long offset, Object targetObject) throws IllegalAccessException {
        long totalBytesWritten = 0;
        for (var fieldNode : fieldNodes) {
            var subObject = targetObject;
            // 如果是 StructNode 我们 使用子对象.
            if (fieldNode instanceof StructNode) {
                // 这里不需要对 fieldInfo 判空, 因为 必定有值.
                subObject = fieldNode.fieldInfo().get(targetObject);
            }
            totalBytesWritten += fieldNode.writeToMemorySegment(memorySegment, offset + totalBytesWritten, subObject);
        }
        return totalBytesWritten;
    }

    @Override
    public long readFromMemorySegment(MemorySegment memorySegment, long offset, Object targetObject) throws IllegalAccessException {
        long totalBytesWritten = 0;
        for (var fieldNode : fieldNodes) {
            var subObject = targetObject;
            // 如果是 StructNode 我们 使用子对象.
            if (fieldNode instanceof StructNode) {
                // 这里不需要对 fieldInfo 判空, 因为 必定有值.
                subObject = fieldNode.fieldInfo().get(targetObject);
            }
            totalBytesWritten += fieldNode.readFromMemorySegment(memorySegment, offset + totalBytesWritten, subObject);
        }
        return totalBytesWritten;
    }

}
