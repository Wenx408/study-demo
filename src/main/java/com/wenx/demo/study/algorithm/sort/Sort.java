package com.wenx.demo.study.algorithm.sort;

/**
 * @Author: Wenx
 * @Description: 排序接口
 * @Date: Created in 2020/3/5 17:15
 * @Modified By：
 */
public interface Sort<T extends Comparable<T>> {
    void sort(T[] values);
}
