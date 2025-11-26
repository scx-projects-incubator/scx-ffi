package dev.scx.ffi;

import dev.scx.ffi.annotation.FFIName;
import dev.scx.ffi.mapper.Mapper;

import java.lang.foreign.Arena;
import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SymbolLookup;
import java.lang.invoke.MethodHandle;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import static dev.scx.ffi.FFMHelper.*;
import static java.lang.foreign.Linker.nativeLinker;

/// AbstractFFMProxy
///
/// @author scx567888
/// @version 0.0.1
abstract class BaseFFMProxy implements InvocationHandler {

    private final SymbolLookup symbolLookup;

    protected BaseFFMProxy(SymbolLookup symbolLookup) {
        this.symbolLookup = symbolLookup;
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

            // 1, 将 args 全部转换为只包含 (基本类型 | MemorySegment | Mapper) 三种类型的数组
            var parameters = convertToParameters(args);

            // 2, 将 parameters 转换为只包含 (基本类型 | MemorySegment) 两种类型的 nativeParameters 数组
            var nativeParameters = new Object[args.length];
            for (var i = 0; i < parameters.length; i = i + 1) {
                var parameter = parameters[i];
                if (parameter instanceof Mapper mapper) {
                    nativeParameters[i] = mapper.toMemorySegment(arena);
                } else {
                    //这里只剩下 基本类型 | MemorySegment, 能够直接使用.
                    nativeParameters[i] = parameter;
                }
            }

            // 3, 执行方法
            var result = methodHandle.invokeWithArguments(nativeParameters);

            // 4, Mapper 类型的参数 进行内存数据回写.
            for (int i = 0; i < parameters.length; i = i + 1) {
                var parameter = parameters[i];
                var nativeParameter = nativeParameters[i];
                if (parameter instanceof Mapper mapper && nativeParameter instanceof MemorySegment memorySegment) {
                    mapper.fromMemorySegment(memorySegment);
                }
            }

            //5, 返回结果
            return result;

        }

    }

    /// 创建 FFMMethodHandle (用于子类使用)
    protected final MethodHandle createFFMMethodHandle(Method method) {
        // 0, 获取方法名
        var ffiName = method.getAnnotation(FFIName.class);
        var name = ffiName == null ? method.getName() : ffiName.value();

        // 1, 根据方法名查找对应的方法
        var fun = symbolLookup.find(name).orElse(null);
        if (fun == null) {
            throw new IllegalArgumentException("未找到对应外部方法 : " + method.getName());
        }

        // 2, 创建方法的描述, 包括 返回值类型 参数类型列表
        var returnLayout = getMemoryLayout(method.getReturnType());
        var paramLayouts = getMemoryLayouts(method.getParameterTypes());
        var functionDescriptor = FunctionDescriptor.of(returnLayout, paramLayouts);

        // 3, 根据方法和描述, 获取可以调用本机方法的方法句柄
        return nativeLinker().downcallHandle(fun, functionDescriptor);
    }

    /// 获取 FFMMethodHandle
    protected abstract MethodHandle findFFMMethodHandle(Method method);

}
