package dev.scx.ffi;

import java.lang.foreign.SymbolLookup;
import java.lang.invoke.MethodHandle;
import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;

import static dev.scx.ffi.FFMSupport.createFFMMethodHandle;

/// 懒加载 方式
final class FFMProxyLazy extends BaseFFMProxy {

    private final SymbolLookup symbolLookup;
    private final ConcurrentHashMap<Method, MethodHandle> ffmMethodHandleCache;

    public FFMProxyLazy(SymbolLookup symbolLookup) {
        this.symbolLookup = symbolLookup;
        this.ffmMethodHandleCache = new ConcurrentHashMap<>();
    }

    @Override
    protected MethodHandle findFFMMethodHandle(Method method) {
        return this.ffmMethodHandleCache.computeIfAbsent(method, m -> createFFMMethodHandle(symbolLookup, m));
    }

}
