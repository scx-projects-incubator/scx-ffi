package dev.scx.ffi.mapper;

import dev.scx.ffi.type.FFICallback;
import dev.scx.reflect.ClassInfo;
import dev.scx.reflect.ScxReflect;

import java.lang.foreign.Arena;
import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.MemorySegment;
import java.lang.invoke.MethodHandle;
import java.util.Arrays;

import static dev.scx.ffi.FFMHelper.getMemoryLayout;
import static dev.scx.ffi.FFMHelper.getMemoryLayouts;
import static java.lang.foreign.Linker.nativeLinker;
import static java.lang.invoke.MethodHandles.lookup;

// todo 有bug
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

    public FFICallbackFFMMapper(FFICallback callback) throws NoSuchMethodException, IllegalAccessException {
        this.callback = callback;
        var classInfo = (ClassInfo) ScxReflect.typeOf(callback.getClass());
        var callbackMethodName = callback.callbackMethodName();
        var methodInfo = Arrays.stream(classInfo.methods())
            .filter(m -> m.name().equals(callbackMethodName))
            .findFirst().orElseThrow(() -> new NoSuchMethodException(callbackMethodName));
        methodInfo.setAccessible(true);// 有时我们会遇到 callback 是一个 lambda 表达式的情况 这时需要 强制设置访问权限
        var method = methodInfo.rawMethod();
        this.fun = lookup().unreflect(method).bindTo(callback);
        var r = getMemoryLayout(method.getReturnType());
        var p = getMemoryLayouts(method.getParameterTypes());
        this.functionDescriptor = FunctionDescriptor.of(r, p);
    }

    @Override
    public MemorySegment toMemorySegment(Arena arena) {
        return nativeLinker().upcallStub(fun, functionDescriptor, arena);
    }

    @Override
    public void fromMemorySegment(MemorySegment memorySegment) {

    }

}
