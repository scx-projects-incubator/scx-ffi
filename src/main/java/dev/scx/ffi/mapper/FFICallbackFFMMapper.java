package dev.scx.ffi.mapper;

import dev.scx.ffi.type.FFICallback;

import java.lang.foreign.Arena;
import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.MemorySegment;
import java.lang.invoke.MethodHandle;

import static dev.scx.ffi.FFMHelper.*;
import static java.lang.foreign.Linker.nativeLinker;
import static java.lang.invoke.MethodHandles.lookup;

/// FFICallbackFFMMapper
///
/// 不建议直接使用, 推荐直接使用 [FFICallback]
///
/// @author scx567888
/// @version 0.0.1
public final class FFICallbackFFMMapper implements FFMMapper {

    private final FFICallback callback;
    private final MethodHandle fun;
    private final FunctionDescriptor functionDescriptor;

    public FFICallbackFFMMapper(FFICallback callback) throws IllegalAccessException {
        this.callback = callback;
        // 查找 对应方法
        var method = findFFICallbackMethod(callback);
        // 有时我们会遇到 callback 是一个 lambda 表达式的情况 这时需要 强制设置访问权限
        method.setAccessible(true);
        this.fun = lookup().unreflect(method).bindTo(callback);
        if (method.getReturnType() == void.class) {
            var paramLayouts = getCallbackParameterMemoryLayouts(method.getParameterTypes());
            this.functionDescriptor = FunctionDescriptor.ofVoid(paramLayouts);
        } else {
            var returnLayout = getReturnMemoryLayout(method.getReturnType());
            var paramLayouts = getCallbackParameterMemoryLayouts(method.getParameterTypes());
            this.functionDescriptor = FunctionDescriptor.of(returnLayout, paramLayouts);
        }
    }

    @Override
    public MemorySegment toMemorySegment(Arena arena) {
        return nativeLinker().upcallStub(fun, functionDescriptor, arena);
    }

    @Override
    public void fromMemorySegment(MemorySegment memorySegment) {
        // 只读的 这里忽略
    }

}
