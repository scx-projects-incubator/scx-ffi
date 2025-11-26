package dev.scx.ffi;

import dev.scx.ffi.mapper.Mapper;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.lang.invoke.MethodHandle;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import static dev.scx.ffi.FFMHelper.convertToParameters;

// todo
abstract class AbstractFFMProxy implements InvocationHandler {

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        // Object 的方法这里直接跳过, 我们只处理接口上的方法
        if (method.getDeclaringClass() == Object.class) {
            return method.invoke(this, args);
        }

        var methodHandle = this.findMethodHandle(method);

        try (var arena = Arena.ofConfined()) {

            //1, 将参数全部转换为 基本类型 | MemorySegment | Mapper
            var parameters = convertToParameters(args);

            //2, 将 parameters 转换为 nativeParameters 基本类型 | MemorySegment
            var nativeParameters = new Object[args.length];
            for (var i = 0; i < parameters.length; i = i + 1) {
                var parameter = parameters[i];
                if (parameter instanceof Mapper mapper) {
                    nativeParameters[i] = mapper.toMemorySegment(arena);
                } else {
                    //能够直接使用的 保留原始值
                    nativeParameters[i] = parameter;
                }
            }

            //3, 执行方法
            var result = methodHandle.invokeWithArguments(nativeParameters);

            //4, Mapper 类型的参数 数据回写
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

    protected abstract MethodHandle findMethodHandle(Method method);

}
