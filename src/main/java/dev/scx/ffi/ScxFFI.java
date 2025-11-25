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

    public static <T> T createFFI(Class<T> clazz, SymbolLookup symbolLookup) {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, new FFMProxy(symbolLookup));
    }

}
