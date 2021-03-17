package com.wenx.demo.basis.mybatis.entity;

import java.io.Serializable;

/**
 * Test1实体类
 *
 * @author Wenx
 * @date 2021/3/16
 */
public class Test1 implements Serializable {
    private Integer id;
    private String name;
    private String content;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Test1{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
