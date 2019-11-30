package com.wenx.demo.study.optimize;

/**
 * @Author: Wenx
 * @Description: 运行命令 javac LoaderTestClass.java
 * @Date: Created in 2019/11/30 20:34
 * @Modified By：
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
