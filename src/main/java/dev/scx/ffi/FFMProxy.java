package dev.scx.ffi;

import dev.scx.reflect.ClassInfo;
import dev.scx.reflect.ScxReflect;

import java.lang.foreign.Arena;
import java.lang.foreign.SymbolLookup;
import java.lang.invoke.MethodHandle;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;

import static dev.scx.ffi.FFMProxySupport.*;
import static dev.scx.ffi.FFMProxySupport.writeBackParameters;

/// FFMProxy
///
/// @author scx567888
/// @version 0.0.1
final class FFMProxy implements InvocationHandler {

    private final SymbolLookup symbolLookup;
    /// 这里只有读操作 所以 HashMap 即可保证线程安全.
    private final HashMap<Method, MethodHandle> ffmMethodHandleCache;

    public <T> FFMProxy(Class<T> clazz, SymbolLookup symbolLookup) {
        this.symbolLookup = symbolLookup;
        this.ffmMethodHandleCache = initFFMMethodHandleCache(clazz);
    }

    @Override
    public final Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        // Object 方法 直接调用
        if (method.getDeclaringClass() == Object.class) {
            return method.invoke(this, args);
        }

        // 默认方法 直接调用
        if (method.isDefault()) {
            return InvocationHandler.invokeDefault(proxy, method, args);
        }

        // 处理 FFM 的方法调用
        var methodHandle = this.findFFMMethodHandle(method);

        try (var arena = Arena.ofConfined()) {

            // 1, 将 args 转换为只包含 (基本类型 | MemorySegment | FFMMapper) 三种类型的数组
            var wrappedParameters = wrapParameters(args);

            // 2, 将 wrappedParameters 转换为只包含 (基本类型 | MemorySegment) 两种类型的数组
            var nativeParameters = prepareNativeParameters(wrappedParameters, arena);

            // 3, 执行方法
            var result = methodHandle.invokeWithArguments(nativeParameters);

            // 4, FFMMapper 类型的参数 进行 内存段 数据回写.
            writeBackParameters(wrappedParameters, nativeParameters);

            // 5, 返回结果
            return result;
        }

    }

    /// 获取 FFMMethodHandle
    protected abstract MethodHandle findFFMMethodHandle(Method method);

    private HashMap<Method, MethodHandle> initFFMMethodHandleCache(Class<?> clazz) {
        var typeInfo = ScxReflect.typeOf(clazz);

        if (typeInfo instanceof ClassInfo classInfo) {
            var cache = new HashMap<Method, MethodHandle>();
            // 获取接口整个层级的所有方法
            var methodInfos = classInfo.allMethods();
            for (var methodInfo : methodInfos) {
                // 这里只 处理 abstract 方法.
                if (methodInfo.isAbstract()) {
                    cache.put(methodInfo.rawMethod(), createFFMMethodHandle(symbolLookup, methodInfo.rawMethod()));
                }
            }
            return cache;
        }

        // 这种情况几乎不可能发生, 此处仅作防御处理.
        throw new IllegalArgumentException(clazz.getName() + " is not a ClassInfo");
    }

    @Override
    protected MethodHandle findFFMMethodHandle(Method method) {
        return this.ffmMethodHandleCache.get(method);
    }

}
