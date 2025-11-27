package dev.scx.ffi;

import dev.scx.ffi.mapper.*;
import dev.scx.ffi.type.*;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;

import static java.lang.foreign.ValueLayout.*;

/// FFMHelper
///
/// @author scx567888
/// @version 0.0.1
public final class FFMHelper {

    public static MemoryLayout getMemoryLayout(Class<?> type) {
        // 1, 先处理可以直接映射的基本类型
        if (type == byte.class || type == Byte.class) {
            return JAVA_BYTE;
        }
        if (type == short.class || type == Short.class) {
            return JAVA_SHORT;
        }
        if (type == int.class || type == Integer.class) {
            return JAVA_INT;
        }
        if (type == long.class || type == Long.class) {
            return JAVA_LONG;
        }
        if (type == float.class || type == Float.class) {
            return JAVA_FLOAT;
        }
        if (type == double.class || type == Double.class) {
            return JAVA_DOUBLE;
        }
        if (type == boolean.class || type == Boolean.class) {
            return JAVA_BOOLEAN;
        }
        if (type == char.class || type == Character.class) {
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

    public static MemoryLayout[] getMemoryLayouts(Class<?>[] types) {
        var memoryLayouts = new MemoryLayout[types.length];
        for (var i = 0; i < types.length; i = i + 1) {
            memoryLayouts[i] = getMemoryLayout(types[i]);
        }
        return memoryLayouts;
    }

    /// 返回值 只允许 (基本类型 | MemorySegment | FFMMapper) 三种
    public static Object convertToParameter(Object o) throws NoSuchMethodException, IllegalAccessException {
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
            case char[] c -> new CharArrayFFMMapper(c);
            // 3, 内置 Ref
            case ByteRef r -> new ByteRefFFMMapper(r);
            case ShortRef r -> new ShortRefFFMMapper(r);
            case IntRef r -> new IntRefFFMMapper(r);
            case LongRef r -> new LongRefFFMMapper(r);
            case FloatRef r -> new FloatRefFFMMapper(r);
            case DoubleRef r -> new DoubleRefFFMMapper(r);
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
            default -> throw new IllegalArgumentException("无法转换的类型 !!! " + o.getClass());
        };
    }

    public static Object[] convertToParameters(Object[] objs) throws NoSuchMethodException, IllegalAccessException {
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

}
