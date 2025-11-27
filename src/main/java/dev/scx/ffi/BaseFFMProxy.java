package dev.scx.ffi;

import dev.scx.ffi.mapper.FFMMapper;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.lang.invoke.MethodHandle;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import static dev.scx.ffi.FFMHelper.convertToParameters;

/// BaseFFMProxy
///
/// @author scx567888
/// @version 0.0.1
abstract class BaseFFMProxy implements InvocationHandler {

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

            // 1, 将 args 全部转换为只包含 (基本类型 | MemorySegment | FFMMapper) 三种类型的数组
            var parameters = convertToParameters(args);

            // 2, 将 parameters 转换为只包含 (基本类型 | MemorySegment) 两种类型的 nativeParameters 数组
            var nativeParameters = new Object[args.length];
            for (var i = 0; i < parameters.length; i = i + 1) {
                var parameter = parameters[i];
                if (parameter instanceof FFMMapper ffmMapper) {
                    nativeParameters[i] = ffmMapper.toMemorySegment(arena);
                } else {
                    //这里只剩下 基本类型 | MemorySegment, 能够直接使用.
                    nativeParameters[i] = parameter;
                }
            }

            // 3, 执行方法
            var result = methodHandle.invokeWithArguments(nativeParameters);

            // 4, FFMMapper 类型的参数 进行内存数据回写.
            for (int i = 0; i < parameters.length; i = i + 1) {
                var parameter = parameters[i];
                var nativeParameter = nativeParameters[i];
                if (parameter instanceof FFMMapper ffmMapper && nativeParameter instanceof MemorySegment memorySegment) {
                    ffmMapper.fromMemorySegment(memorySegment);
                }
            }

            // 5, 返回结果
            return result;

        }

    }

    /// 获取 FFMMethodHandle
    protected abstract MethodHandle findFFMMethodHandle(Method method);

}
