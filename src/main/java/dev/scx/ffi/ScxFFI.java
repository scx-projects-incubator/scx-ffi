package dev.scx.ffi;

import java.lang.foreign.SymbolLookup;
import java.lang.reflect.Proxy;
import java.nio.file.Path;

import static java.lang.foreign.Arena.global;
import static java.lang.foreign.Linker.nativeLinker;
import static java.lang.foreign.SymbolLookup.libraryLookup;

/// ScxFFI
///
/// @author scx567888
/// @version 0.0.1
@SuppressWarnings("unchecked")
public final class ScxFFI {

    public static <T> T createFFI(Class<T> clazz) {
        return createFFI(clazz, nativeLinker().defaultLookup());
    }

    public static <T> T createFFI(Class<T> clazz, String libraryName) {
        return createFFI(clazz, libraryLookup(libraryName, global()));
    }

    public static <T> T createFFI(Class<T> clazz, Path libraryPath) {
        return createFFI(clazz, libraryLookup(libraryPath, global()));
    }

    /// 方法在创建时就会全部绑定
    public static <T> T createFFI(Class<T> clazz, SymbolLookup symbolLookup) {
        // 这里 newProxyInstance 会验证 clazz 是否是接口, 所以我们在 FFMProxy 中无需校验
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, new FFMProxy(clazz, symbolLookup));
    }

    public static <T> T createFFILazy(Class<T> clazz) {
        return createFFILazy(clazz, nativeLinker().defaultLookup());
    }

    public static <T> T createFFILazy(Class<T> clazz, String libraryName) {
        return createFFILazy(clazz, libraryLookup(libraryName, global()));
    }

    public static <T> T createFFILazy(Class<T> clazz, Path libraryPath) {
        return createFFILazy(clazz, libraryLookup(libraryPath, global()));
    }

    /// 方法会在第一次调用时才绑定
    public static <T> T createFFILazy(Class<T> clazz, SymbolLookup symbolLookup) {
        // 这里 newProxyInstance 会验证 clazz 是否是接口, 所以我们在 FFMProxy 中无需校验
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, new FFMProxyLazy(clazz, symbolLookup));
    }

}
