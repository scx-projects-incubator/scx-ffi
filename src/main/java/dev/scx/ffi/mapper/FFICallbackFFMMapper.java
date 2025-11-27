package dev.scx.ffi.mapper;

import dev.scx.ffi.type.FFICallback;

import java.lang.foreign.Arena;
import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.MemorySegment;
import java.lang.invoke.MethodHandle;

import static dev.scx.ffi.mapper.FFICallbackSupport.createFunctionDescriptor;
import static dev.scx.ffi.mapper.FFICallbackSupport.findFFICallbackMethod;
import static java.lang.foreign.Linker.nativeLinker;
import static java.lang.invoke.MethodHandles.lookup;

/// FFICallbackFFMMapper
///
/// 不建议直接使用, 推荐直接使用 [FFICallback]
///
/// @author scx567888
/// @version 0.0.1
public final class FFICallbackFFMMapper implements FFMMapper {

    private final MethodHandle methodHandle;
    private final FunctionDescriptor functionDescriptor;

    public FFICallbackFFMMapper(FFICallback callback) throws IllegalAccessException {
        var callbackMethod = findFFICallbackMethod(callback);
        this.methodHandle = lookup().unreflect(callbackMethod).bindTo(callback);
        this.functionDescriptor = createFunctionDescriptor(callbackMethod);
    }

    @Override
    public MemorySegment toMemorySegment(Arena arena) {
        return nativeLinker().upcallStub(methodHandle, functionDescriptor, arena);
    }

    @Override
    public void fromMemorySegment(MemorySegment memorySegment) {
        // 只读的 这里忽略
    }

}
