package com.wenx.demo.optimize;

/**
 * 运行命令 javac LoaderTestClass.java
 *
 * @author Wenx
 * @date 2019/11/30
 */
public class LoaderTestClass {
    public static String value = getValue();

    static {
        System.out.println("##########static code");
    }

    private static String getValue() {
        System.out.println("##########static method");
        return "Test";
    }

    public void test() {
        System.out.println("HelloWorld" + value);
    }
}
