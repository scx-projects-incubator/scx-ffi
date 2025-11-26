package dev.scx.ffi;

import java.lang.foreign.SymbolLookup;
import java.lang.invoke.MethodHandle;
import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;

/// 懒加载 方式
class FFMProxyLazy extends AbstractFFMProxy {

    private final ConcurrentHashMap<Method, MethodHandle> ffmMethodHandleCache;

    public FFMProxyLazy(SymbolLookup symbolLookup) {
        super(symbolLookup);
        this.ffmMethodHandleCache = new ConcurrentHashMap<>();
    }

    @Override
    protected MethodHandle findFFMMethodHandle(Method method) {
        return this.ffmMethodHandleCache.computeIfAbsent(method, this::createFFMMethodHandle);
    }

}
