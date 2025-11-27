package dev.scx.ffi.mapper.struct;

import java.lang.foreign.MemoryLayout;
import java.util.ArrayList;
import java.util.List;

final class StructNode implements Node {

    private final String name;
    private final Class<?> type;
    private final List<Node> fieldNodes;

    public StructNode(String name, Class<?> type) {
        this.name = name;
        this.type = type;
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
        if (name != null) {
            memoryLayout = memoryLayout.withName(name);
        }
        return memoryLayout;
    }

}
