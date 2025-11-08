package cool.scx.ffm.test;

import cool.scx.ffm.mapper.IntMapper;
import org.testng.annotations.Test;

import static cool.scx.ffm.test.platform.win32.Kernel32.KERNEL32;

public class Kernel32Test {

    public static void main(String[] args) {
        test1();
    }

    @Test
    public static void test1() {
        //跳过 linux 上的测试
        if (!System.getProperty("os.name").startsWith("Windows")) {
            return;
        }

        var a = KERNEL32.GetStdHandle(-11);

        var i = new IntMapper();

        var b = KERNEL32.GetConsoleMode(a, i);

        var c = KERNEL32.SetConsoleMode(a, i.getValue());
    }

}
