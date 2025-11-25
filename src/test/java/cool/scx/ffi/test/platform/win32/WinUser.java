package cool.scx.ffi.test.platform.win32;

import cool.scx.ffi.type.Callback;
import cool.scx.ffi.type.Struct;

import java.lang.foreign.MemorySegment;

public final class WinUser {

    /// EnumWindows 回调接口
    public interface WNDENUMPROC extends Callback {

        boolean callback(MemorySegment hwnd, long lParam);

    }

    public static class POINT implements Struct {
        public int x;
        public int y;
    }

}
