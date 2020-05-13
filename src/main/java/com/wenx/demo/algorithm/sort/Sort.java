package com.wenx.demo.algorithm.sort;

/**
 * 排序接口
 *
 * @author Wenx
 * @date 2020/3/5
 */
public interface Sort<T extends Comparable<T>> {
    void sort(T[] values);
}
