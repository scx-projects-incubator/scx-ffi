package dev.scx.ffi;

import dev.scx.ffi.annotation.SymbolName;
import dev.scx.ffi.mapper.*;
import dev.scx.ffi.type.*;

import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SymbolLookup;
import java.lang.invoke.MethodHandle;
import java.lang.reflect.Method;

import static java.lang.foreign.Linker.nativeLinker;
import static java.lang.foreign.ValueLayout.*;

/// 内部构建辅助类
///
/// @author scx567888
/// @version 0.0.1
final class FFMProxySupport {

    /// 获取 方法 返回值的 内存布局
    public static MemoryLayout getReturnMemoryLayout(Class<?> type) {
        // 1, 先处理可以直接映射的基本类型
        if (type == byte.class) {
            return JAVA_BYTE;
        }
        if (type == short.class) {
            return JAVA_SHORT;
        }
        if (type == int.class) {
            return JAVA_INT;
        }
        if (type == long.class) {
            return JAVA_LONG;
        }
        if (type == float.class) {
            return JAVA_FLOAT;
        }
        if (type == double.class) {
            return JAVA_DOUBLE;
        }
        if (type == boolean.class) {
            return JAVA_BOOLEAN;
        }
        if (type == char.class) {
            return JAVA_CHAR;
        }
        // 2, 内存段
        if (type == MemorySegment.class) {
            return ADDRESS;
        }
        // 其余全不支持 !!!
        throw new IllegalArgumentException("不支持的返回值类型 !!! " + type);
    }

    /// 获取 方法 参数的 内存布局
    public static MemoryLayout getParameterMemoryLayout(Class<?> type) {
        // 1, 先处理可以直接映射的基本类型
        if (type == byte.class) {
            return JAVA_BYTE;
        }
        if (type == short.class) {
            return JAVA_SHORT;
        }
        if (type == int.class) {
            return JAVA_INT;
        }
        if (type == long.class) {
            return JAVA_LONG;
        }
        if (type == float.class) {
            return JAVA_FLOAT;
        }
        if (type == double.class) {
            return JAVA_DOUBLE;
        }
        if (type == boolean.class) {
            return JAVA_BOOLEAN;
        }
        if (type == char.class) {
            return JAVA_CHAR;
        }
        // 2, 处理基本类型的数组类型 这里我们使用 ADDRESS 而不使用 MemoryLayout.sequenceLayout() , 因为我们不知道数组长度
        if (type == byte[].class ||
            type == short[].class ||
            type == int[].class ||
            type == long[].class ||
            type == float[].class ||
            type == double[].class ||
            type == boolean[].class ||
            type == char[].class) {
            return ADDRESS;
        }
        // 3, 内置 基本类型 Ref
        if (type == ByteRef.class ||
            type == ShortRef.class ||
            type == IntRef.class ||
            type == LongRef.class ||
            type == FloatRef.class ||
            type == DoubleRef.class ||
            type == BooleanRef.class ||
            type == CharRef.class) {
            return ADDRESS;
        }
        // 4, 处理字符串
        if (type == String.class || type == StringRef.class) {
            return ADDRESS;
        }
        // 5, 内存段
        if (type == MemorySegment.class) {
            return ADDRESS;
        }
        // 6, 处理 Callback 类型
        if (FFICallback.class.isAssignableFrom(type)) {
            return ADDRESS;
        }
        // 7, 处理 结构体类型 这里我们使用 ADDRESS 而不使用 MemoryLayout.structLayout(), 因为需要在运行时才知道具体结构
        if (FFIStruct.class.isAssignableFrom(type)) {
            return ADDRESS;
        }
        // 8, 处理映射类型
        if (FFMMapper.class.isAssignableFrom(type)) {
            return ADDRESS;
        }
        // 其余全不支持 !!!
        throw new IllegalArgumentException("不支持的参数类型 !!! " + type);
    }

    /// 获取 方法 参数的 内存布局
    public static MemoryLayout[] getParameterMemoryLayouts(Class<?>[] types) {
        var memoryLayouts = new MemoryLayout[types.length];
        for (var i = 0; i < types.length; i = i + 1) {
            memoryLayouts[i] = getParameterMemoryLayout(types[i]);
        }
        return memoryLayouts;
    }

    /// 转换成 (基本类型 | MemorySegment | FFMMapper) 三种
    public static Object convertToParameter(Object o) throws IllegalAccessException {
        return switch (o) {
            // 1, 基本值 (FFM 能够直接处理, 无需转换)
            case Byte _,
                 Short _,
                 Integer _,
                 Long _,
                 Float _,
                 Double _,
                 Boolean _,
                 Character _ -> o;
            // 2, 数组类型
            case byte[] c -> new ByteArrayFFMMapper(c);
            case short[] c -> new ShortArrayFFMMapper(c);
            case int[] c -> new IntArrayFFMMapper(c);
            case long[] c -> new LongArrayFFMMapper(c);
            case float[] c -> new FloatArrayFFMMapper(c);
            case double[] c -> new DoubleArrayFFMMapper(c);
            case boolean[] c -> new BooleanArrayFFMMapper(c);
            case char[] c -> new CharArrayFFMMapper(c);
            // 3, 内置 Ref
            case ByteRef r -> new ByteRefFFMMapper(r);
            case ShortRef r -> new ShortRefFFMMapper(r);
            case IntRef r -> new IntRefFFMMapper(r);
            case LongRef r -> new LongRefFFMMapper(r);
            case FloatRef r -> new FloatRefFFMMapper(r);
            case DoubleRef r -> new DoubleRefFFMMapper(r);
            case BooleanRef r -> new BooleanRefFFMMapper(r);
            case CharRef r -> new CharRefFFMMapper(r);
            // 4, 字符串
            case String s -> new StringFFMMapper(s);
            case StringRef r -> new StringRefFFMMapper(r);
            // 5, 内存段 (FFM 能够直接处理, 无需转换)
            case MemorySegment _ -> o;
            // 6, Callback 类型
            case FFICallback c -> new FFICallbackFFMMapper(c);
            // 7, 结构体
            case FFIStruct c -> new FFIStructFFMMapper(c);
            // 8, 映射类型
            case FFMMapper m -> m;
            // 9, 空值
            case null -> MemorySegment.NULL;
            // 其余抛异常
            default -> throw new IllegalArgumentException("无法转换的类型 !!! " + o.getClass());
        };
    }

    /// 转换成 (基本类型 | MemorySegment | FFMMapper) 三种
    public static Object[] convertToParameters(Object[] objs) throws IllegalAccessException {
        // 针对 null 做防御处理
        if (objs == null) {
            return new Object[0];
        }
        var result = new Object[objs.length];
        for (var i = 0; i < objs.length; i = i + 1) {
            result[i] = convertToParameter(objs[i]);
        }
        return result;
    }

    /// 创建 FFMMethodHandle
    public static MethodHandle createFFMMethodHandle(SymbolLookup symbolLookup, Method method) {
        // 0, 获取方法名
        var symbolName = method.getAnnotation(SymbolName.class);
        var name = symbolName == null ? method.getName() : symbolName.value();

        // 1, 根据方法名查找对应的方法
        var fun = symbolLookup.find(name).orElse(null);
        if (fun == null) {
            throw new IllegalArgumentException("未找到对应外部方法 : " + method.getName());
        }

        // 2, 创建方法的描述, 包括 返回值类型 参数类型列表
        FunctionDescriptor functionDescriptor;

        if (method.getReturnType() == void.class) {
            var paramLayouts = getParameterMemoryLayouts(method.getParameterTypes());
            functionDescriptor = FunctionDescriptor.ofVoid(paramLayouts);
        } else {
            var returnLayout = getReturnMemoryLayout(method.getReturnType());
            var paramLayouts = getParameterMemoryLayouts(method.getParameterTypes());
            functionDescriptor = FunctionDescriptor.of(returnLayout, paramLayouts);
        }

        // 3, 根据方法和描述, 获取可以调用本机方法的方法句柄
        return nativeLinker().downcallHandle(fun, functionDescriptor);
    }

}
