package dev.scx.ffi;

import dev.scx.reflect.ClassInfo;
import dev.scx.reflect.ScxReflect;

import java.lang.foreign.SymbolLookup;
import java.lang.invoke.MethodHandle;
import java.lang.reflect.Method;
import java.util.HashMap;

/// FFMProxy
///
/// @author scx567888
/// @version 0.0.1
final class FFMProxy extends AbstractFFMProxy {

    /// 这里只会读取 所以 HashMap 即可保证线程安全.
    private final HashMap<Method, MethodHandle> ffmMethodHandleCache;

    public <T> FFMProxy(Class<T> clazz, SymbolLookup symbolLookup) {
        super(symbolLookup);
        this.ffmMethodHandleCache = initFFMMethodHandleCache(clazz);
    }

    private HashMap<Method, MethodHandle> initFFMMethodHandleCache(Class<?> clazz) {
        var typeInfo = ScxReflect.typeOf(clazz);
        if (typeInfo instanceof ClassInfo classInfo) {
            var map = new HashMap<Method, MethodHandle>();
            // 获取接口整个层级的所有方法
            var methodInfos = classInfo.allMethods();
            for (var methodInfo : methodInfos) {
                // 这里只 bind abstract 方法.
                if (!methodInfo.isAbstract()) {
                    continue;
                }
                map.put(methodInfo.rawMethod(), createFFMMethodHandle(methodInfo.rawMethod()));
            }
            return map;
        }

        // 这种情况几乎不可能发生, 此处仅作防御处理.
        throw new IllegalArgumentException(clazz.getName() + " is not a ClassInfo");
    }

    @Override
    protected MethodHandle findFFMMethodHandle(Method method) {
        return this.ffmMethodHandleCache.get(method);
    }

}
