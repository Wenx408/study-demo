package com.wenx.demo.optimize;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.concurrent.TimeUnit;

/**
 * @author Wenx
 * @date 2019/11/30
 */
public class ClassLoaderDemo {
    public static void main(String[] args) throws Exception {
        // 查看类的加载器实例
        //classLoaderView();
        // 热加载，指定class 进行加载
        classLoaderTest();
    }

    /**
     * 查看类的加载器实例
     *
     * @throws Exception
     */
    public static void classLoaderView() throws Exception {
        // 加载核心类库的 BootStrap ClassLoader
        System.out.println("核心类库加载器：" + ClassLoaderDemo.class.getClassLoader()
                .loadClass("java.lang.String").getClassLoader());
        // 加载拓展库的 Extension ClassLoader
        System.out.println("拓展类库加载器：" + ClassLoaderDemo.class.getClassLoader()
                .loadClass("com.sun.nio.zipfs.ZipCoder").getClassLoader());
        // 加载应用程序的
        System.out.println("应用程序库加载器：" + ClassLoaderDemo.class.getClassLoader());
        // 双亲委派模型 Parents Delegation Model
        System.out.println("应用程序库加载器的父类：" + ClassLoaderDemo.class.getClassLoader()
                .getParent());
        System.out.println("应用程序库加载器的父类的父类：" + ClassLoaderDemo.class.getClassLoader()
                .getParent().getParent());
    }

    /**
     * 热加载，指定class 进行加载
     *
     * @throws Exception
     */
    public static void classLoaderTest() throws Exception {
        // jvm类放在位置
        URL classUrl = new URL("file:D:\\");
        // 2. 测试类的卸载
        //URLClassLoader loader = new URLClassLoader(new URL[]{classUrl});
        // 3. 测试双亲委派机制
        // 如果使用此加载器作为父加载器，则下面的热更新会失效，因为双亲委派机制，HelloService实际上是被这个类加载器加载的;
        //URLClassLoader parentLoader = new URLClassLoader(new URL[]{classUrl});
        while (true) {
            // 2. 测试类的卸载
            //if (loader == null) {
            //    break;
            //}
            // 3. 测试双亲委派机制
            // 创建一个新的类加载器，它的父加载器为上面的parentLoader
            //URLClassLoader loader = new URLClassLoader(new URL[]{classUrl}, parentLoader);
            // 1. 类不会重复加载，创建一个新的类加载器
            URLClassLoader loader = new URLClassLoader(new URL[]{classUrl});

            Class clazz = loader.loadClass("LoaderTestClass");
            System.out.println("LoaderTestClass所使用的类加载器：" + clazz.getClassLoader());

            Object newInstance = clazz.newInstance();
            Object value = clazz.getMethod("test").invoke(newInstance);
            System.out.println("调用getValue获得的返回值为：" + value);

            // 3秒执行一次
            TimeUnit.SECONDS.sleep(3L);
            System.out.println();

            // 2. 测试类的卸载
            // help gc  -verbose:class
            //newInstance = null;
            //loader = null;
        }

        // 2. 测试类的卸载
        //System.gc();
        //TimeUnit.SECONDS.sleep(3L);
    }
}
