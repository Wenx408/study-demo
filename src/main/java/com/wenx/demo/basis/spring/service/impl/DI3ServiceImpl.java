package com.wenx.demo.basis.spring.service.impl;

import com.wenx.demo.basis.spring.service.ISpringService;

import java.util.*;

/**
 * @author Wenx
 * @date 2021/3/31
 */
public class DI3ServiceImpl implements ISpringService {
    private String[] strings;
    private List<String> lists;
    private Set<String> sets;
    private Map<String, String> maps;
    private Properties props;

    public void setStrings(String[] strings) {
        this.strings = strings;
    }

    public void setLists(List<String> lists) {
        this.lists = lists;
    }

    public void setSets(Set<String> sets) {
        this.sets = sets;
    }

    public void setMaps(Map<String, String> maps) {
        this.maps = maps;
    }

    public void setProps(Properties props) {
        this.props = props;
    }

    @Override
    public void save() {
        System.out.println("DI3ServiceImpl的save方法执行了……");
        System.out.println("strings：" + Arrays.toString(strings));
        System.out.println("lists：" + lists);
        System.out.println("sets：" + sets);
        System.out.println("maps：" + maps);
        System.out.println("props：" + props);
    }
}
