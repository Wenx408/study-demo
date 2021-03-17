package com.wenx.demo.basis.mybatis.utils;

import java.io.InputStream;

/**
 * 使用ClassLoader加载配置文件
 *
 * @author Wenx
 * @date 2021/3/17
 */
public class Resources {

    /**
     * 获取文件字节输入流
     *
     * @param filePath 配置文件地址
     * @return
     */
    public static InputStream getResourceAsStream(String filePath) {
        return Resources.class.getClassLoader().getResourceAsStream(filePath);
    }
}
