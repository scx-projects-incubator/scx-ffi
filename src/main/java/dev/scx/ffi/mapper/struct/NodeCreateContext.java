package dev.scx.ffi.mapper.struct;

import dev.scx.reflect.TypeInfo;
import java.util.ArrayList;
import java.util.List;

/// Node 创建上下文
final class NodeCreateContext {

    public List<TypeInfo> chain;

    public NodeCreateContext() {
        this.chain = new ArrayList<>();
    }

    public void startRecursionCheck(TypeInfo type) {
        if (this.chain.contains(type)) {
            throw new IllegalArgumentException("不支持递归嵌套结构体, type: " + type);
        }
        this.chain.addLast(type);
    }

    public void endRecursionCheck() {
        chain.removeLast();
    }

}
