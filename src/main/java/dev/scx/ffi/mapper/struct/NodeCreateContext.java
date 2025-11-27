package dev.scx.ffi.mapper.struct;

import java.util.ArrayList;
import java.util.List;

/// Node 创建上下文
final class NodeCreateContext {

    public List<Class<?>> chain;

    public NodeCreateContext() {
        this.chain = new ArrayList<>();
    }

    public void startRecursionCheck(Class<?> type) {
        if (this.chain.contains(type)) {
            throw new IllegalArgumentException("不支持递归嵌套结构体, type: " + type);
        }
        this.chain.addLast(type);
    }

    public void endRecursionCheck() {
        chain.removeLast();
    }

}
