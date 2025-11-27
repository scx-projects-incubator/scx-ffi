package dev.scx.ffi.test.platform.c;

import dev.scx.ffi.ScxFFI;
import dev.scx.ffi.annotation.SymbolName;

/// 提供一些 C 标准的接口
///
/// @author scx567888
/// @version 0.0.1
public interface C extends C1{

    C C = ScxFFI.createFFI(C.class);

    long strlen(String str);

    // 测试别名
    @SymbolName("abs")
    int javaAbs(int x);

    double sqrt(double x);

    // 测试默认方法
    default int abs(int x) {
        System.out.println("调用了 默认方法 abs !!!");
        return javaAbs(x);
    }

}
