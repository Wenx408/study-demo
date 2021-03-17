package com.wenx.demo.basis.mybatis.session;

import java.util.HashMap;
import java.util.Map;

/**
 * 自制xxbatis配置类
 *
 * @author Wenx
 * @date 2021/3/17
 */
public class Configuration {
    /**
     * mysql数据库驱动
     */
    private String driver;
    /**
     * mysql数据库连接字符串
     */
    private String url;
    /**
     * mysql数据库用户名
     */
    private String username;
    /**
     * mysql数据库密码
     */
    private String password;
    /**
     * 映射文件配置信息
     */
    private Map<String, MappedStatement> mappers = new HashMap<String, MappedStatement>();


    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Map<String, MappedStatement> getMappers() {
        return mappers;
    }

    public void setMappers(Map<String, MappedStatement> mappers) {
        // 使用追加的方式
        this.mappers.putAll(mappers);
    }
}
