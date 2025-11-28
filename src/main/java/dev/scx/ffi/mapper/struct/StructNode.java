package dev.scx.ffi.mapper.struct;

import dev.scx.reflect.ClassInfo;
import dev.scx.reflect.FieldInfo;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;

final class StructNode implements Node {

    private final FieldInfo fieldInfo;
    private final Node[] fieldNodes;
    private final ClassInfo classInfo;

    public StructNode(FieldInfo fieldInfo, Node[] fieldNodes, ClassInfo classInfo) {
        this.fieldInfo = fieldInfo;
        this.fieldNodes = fieldNodes;
        this.classInfo = classInfo;
    }

    @Override
    public MemoryLayout createMemoryLayout() {
        var arr = new MemoryLayout[fieldNodes.length];
        for (int i = 0; i < fieldNodes.length; i = i + 1) {
            arr[i] = fieldNodes[i].createMemoryLayout();
        }
        var memoryLayout = MemoryLayout.structLayout(arr);
        if (fieldInfo != null) {
            memoryLayout = memoryLayout.withName(fieldInfo.name());
        }
        return memoryLayout;
    }

    @Override
    public FieldInfo fieldInfo() {
        return fieldInfo;
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
