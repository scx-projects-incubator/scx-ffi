package dev.scx.ffi.mapper;

import dev.scx.ffi.type.FFICallback;
import dev.scx.reflect.ClassInfo;
import dev.scx.reflect.ScxReflect;

import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.reflect.Method;
import java.util.ArrayList;

import static java.lang.foreign.ValueLayout.*;

/// 内部构建辅助类
///
/// @author scx567888
/// @version 0.0.1
final class FFICallbackSupport {

    /// 寻找 callback Method
    public static Method findFFICallbackMethod(FFICallback ffiCallback) {
        var typeInfo = ScxReflect.typeOf(ffiCallback.getClass());
        // 只处理 ClassInfo 类型
        if (typeInfo instanceof ClassInfo classInfo) {
            var callbackMethodName = ffiCallback.callbackMethodName();
            var list = new ArrayList<Method>();

            // 查找 指定名称 方法
            for (var methodInfo : classInfo.allMethods()) {
                if (callbackMethodName.equals(methodInfo.name())) {
                    list.add(methodInfo.rawMethod());
                }
            }
            if (list.isEmpty()) {
                throw new IllegalArgumentException("Not found any FFICallback method, name : " + callbackMethodName);
            }
            if (list.size() > 1) {
                throw new IllegalArgumentException("found more than one FFICallback method, name : " + callbackMethodName);
            }

            var callbackMethod = list.get(0);

            // 有时我们会遇到 callback 是一个 lambda 表达式的情况 这时需要 强制设置访问权限
            callbackMethod.setAccessible(true);

            return callbackMethod;
        }

        throw new IllegalArgumentException(ffiCallback.getClass().getName() + " is not a ClassInfo");
    }

    /// 获取 Callback 参数的 内存布局 (规则和 getReturnMemoryLayout 是一致的)
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
        // 2, 内存段
        if (type == MemorySegment.class) {
            return ADDRESS;
        }
        // 其余全不支持 !!!
        throw new IllegalArgumentException("不支持的 callback 参数类型 !!! " + type);
    }

    /// 获取 Callback 返回值参数的 内存布局 (规则和 getParameterMemoryLayout 是一致的)
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
        throw new IllegalArgumentException("不支持的 callback 返回值类型 !!! " + type);
    }

    public static MemoryLayout[] getParameterMemoryLayouts(Class<?>[] types) {
        var memoryLayouts = new MemoryLayout[types.length];
        for (var i = 0; i < types.length; i = i + 1) {
            memoryLayouts[i] = getParameterMemoryLayout(types[i]);
        }
        return memoryLayouts;
    }

    /// 生成方法描述
    public static FunctionDescriptor createFunctionDescriptor(Method callbackMethod) {
        if (callbackMethod.getReturnType() == void.class) {
            var paramLayouts = getParameterMemoryLayouts(callbackMethod.getParameterTypes());
            return FunctionDescriptor.ofVoid(paramLayouts);
        } else {
            var returnLayout = getReturnMemoryLayout(callbackMethod.getReturnType());
            var paramLayouts = getParameterMemoryLayouts(callbackMethod.getParameterTypes());
            return FunctionDescriptor.of(returnLayout, paramLayouts);
        }
    }

}
