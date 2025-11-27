package dev.scx.ffi.mapper.struct;

import dev.scx.reflect.ClassInfo;
import dev.scx.reflect.FieldInfo;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.util.ArrayList;
import java.util.List;

final class StructNode implements Node {

    private final FieldInfo fieldInfo;
    private final ClassInfo classInfo;
    private final List<Node> fieldNodes;

    public StructNode(FieldInfo fieldInfo, ClassInfo classInfo) {
        this.fieldInfo = fieldInfo;
        this.classInfo = classInfo;
        this.fieldNodes = new ArrayList<>();
    }

    public void addFieldNode(Node fieldNode) {
        fieldNodes.add(fieldNode);
    }

    @Override
    public MemoryLayout createMemoryLayout() {
        var arr = new MemoryLayout[fieldNodes.size()];
        for (int i = 0; i < fieldNodes.size(); i = i + 1) {
            arr[i] = fieldNodes.get(i).createMemoryLayout();
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
    public long writeToMemorySegment(MemorySegment memorySegment, long offset, Object value) throws IllegalAccessException {
        long totalBytesWritten = 0;
        for (var fieldNode : fieldNodes) {
            // 这里不需要对 fieldInfo 判空, 因为 必定有值.
            var fieldValue = fieldNode.fieldInfo().get(value);
            totalBytesWritten += fieldNode.writeToMemorySegment(memorySegment, offset + totalBytesWritten, fieldValue);
        }
        return totalBytesWritten;
    }

    @Override
    public long readFromMemorySegment(MemorySegment memorySegment, long offset, Object targetObject) throws IllegalAccessException {
        long totalBytesWritten = 0;
        for (var fieldNode : fieldNodes) {
            // 如果是 StructNode 我们 写入子对象.
            if (fieldNode instanceof StructNode structNode) {
                // 这里不需要对 fieldInfo 判空, 因为 必定有值.
                targetObject = fieldNode.fieldInfo().get(targetObject);
            }
            totalBytesWritten += fieldNode.readFromMemorySegment(memorySegment, offset + totalBytesWritten, targetObject);
        }
        return totalBytesWritten;
    }

}
