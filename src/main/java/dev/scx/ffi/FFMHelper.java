package dev.scx.ffi;

import dev.scx.ffi.mapper.*;
import dev.scx.ffi.mapper.FFICallbackFFMMapper;
import dev.scx.ffi.type.*;
import dev.scx.ffi.mapper.FFIStructFFMMapper;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;

import static java.lang.foreign.ValueLayout.*;

// todo 代码有点问题

/// FFMHelper
///
/// @author scx567888
/// @version 0.0.1
public final class FFMHelper {

    public static MemoryLayout getMemoryLayout(Class<?> type) {
        // 1, 先处理可以直接映射的基本类型
        if (type == Byte.class || type == byte.class) {
            return JAVA_BYTE;
        }
        if (type == Boolean.class || type == boolean.class) {
            return JAVA_BOOLEAN;
        }
        if (type == Character.class || type == char.class) {
            return JAVA_CHAR;
        }
        if (type == Short.class || type == short.class) {
            return JAVA_SHORT;
        }
        if (type == Integer.class || type == int.class) {
            return JAVA_INT;
        }
        if (type == Long.class || type == long.class) {
            return JAVA_LONG;
        }
        if (type == Float.class || type == float.class) {
            return JAVA_FLOAT;
        }
        if (type == Double.class || type == double.class) {
            return JAVA_DOUBLE;
        }
        if (type == MemorySegment.class) {
            return ADDRESS;
        }
        // 3, 处理字符串 todo 这里存疑?
        if (String.class == type) {
            return ADDRESS;
        }
        // 4, 处理映射类型
        if (FFMMapper.class.isAssignableFrom(type)) {
            return ADDRESS;
        }
        // 5, 处理 结构体类型 这里我们使用 ADDRESS 而不使用 MemoryLayout.structLayout(), 因为需要在运行时才知道具体结构
        if (FFIStruct.class.isAssignableFrom(type)) {
            return ADDRESS;
        }
        // 6, 处理 Callback 类型
        if (FFICallback.class.isAssignableFrom(type)) {
            return ADDRESS;
        }
        // 7, 处理基本类型的数组类型 这里我们使用 ADDRESS 而不使用 MemoryLayout.sequenceLayout() , 因为我们不知道数组长度
        if (type.isArray() && type.getComponentType().isPrimitive()) {
            return ADDRESS;
        }
        throw new IllegalArgumentException("不支持的参数类型 !!! " + type);
    }

    public static MemoryLayout[] getMemoryLayouts(Class<?>[] types) {
        var memoryLayouts = new MemoryLayout[types.length];
        for (var i = 0; i < types.length; i = i + 1) {
            memoryLayouts[i] = getMemoryLayout(types[i]);
        }
        return memoryLayouts;
    }

    public static Object convertToParameter(Object o) throws NoSuchMethodException, IllegalAccessException {
        return switch (o) {
            //0, 空值
            case null -> MemorySegment.NULL;
            //1, 基本值/MemorySegment (FFM 能够直接处理, 无需转换)
            case Byte _,
                 Short _,
                 Integer _,
                 Long _,
                 Float _,
                 Double _,
                 Boolean _,
                 Character _,
                 MemorySegment _ -> o;
            //2, 内置 Ref
            case ByteRef r -> new ByteRefFFMMapper(r);
            case CharRef r -> new CharRefFFMMapper(r);
            case ShortRef r -> new ShortRefFFMMapper(r);
            case IntRef r -> new IntRefFFMMapper(r);
            case LongRef r -> new LongRefFFMMapper(r);
            case FloatRef r -> new FloatRefFFMMapper(r);
            case DoubleRef r -> new DoubleRefFFMMapper(r);
            case StringRef r -> new StringRefFFMMapper(r);
            //3, 字符串
            case String s -> new StringFFMMapper(s);
            //4, 映射类型
            case FFMMapper m -> m;
            //5, 结构体
            case FFIStruct c -> new FFIStructFFMMapper(c);
            //6, Callback 类型
            case FFICallback c -> new FFICallbackFFMMapper(c);
            //7, 数组类型
            case byte[] c -> new ByteArrayFFMMapper(c);
            case char[] c -> new CharArrayFFMMapper(c);
            case short[] c -> new ShortArrayFFMMapper(c);
            case int[] c -> new IntArrayFFMMapper(c);
            case long[] c -> new LongArrayFFMMapper(c);
            case float[] c -> new FloatArrayFFMMapper(c);
            case double[] c -> new DoubleArrayFFMMapper(c);
            default -> throw new RuntimeException("无法转换的类型 !!! " + o.getClass());
        };
    }

    public static Object[] convertToParameters(Object[] objs) throws NoSuchMethodException, IllegalAccessException {
        if (objs == null) {
            return new Object[0];
        }
        var result = new Object[objs.length];
        for (var i = 0; i < objs.length; i = i + 1) {
            result[i] = convertToParameter(objs[i]);
        }
        return result;
    }

    public static String toString(char[] buf) {
        int len = buf.length;

        for (int index = 0; index < len; index = index + 1) {
            if (buf[index] == 0) {
                len = index;
                break;
            }
        }

        return len == 0 ? "" : new String(buf, 0, len);
    }

}
