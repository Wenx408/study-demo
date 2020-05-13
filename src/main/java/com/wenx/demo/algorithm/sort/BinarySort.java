package com.wenx.demo.algorithm.sort;

import java.util.Arrays;

/**
 * 二分插入排序
 *
 * @author Wenx
 * @date 2020/3/7
 */
public class BinarySort<T extends Comparable<T>> implements Sort<T> {
    @Override
    public void sort(T[] values) {
        int size = values.length;
        int l, r, mid;
        T t;
        for (int i = 1; i < size; i++) {
            // 从左向右查找无序的元素（后面比前面小的）
            if (values[i].compareTo(values[i - 1]) == -1) {
                t = values[i];
            } else {
                continue;
            }

            l = 0;
            r = i - 1;
            // 二分查找开始寻坑
            while (l <= r) {
                // 获取中间索引
                mid = (l + r) / 2;
                // 若中间元素比选取元素大，缩小右游标
                if (values[mid].compareTo(t) == 1) {
                    r = mid - 1;
                } else {
                    // 若中间元素比选取元素小，缩小左游标
                    l = mid + 1;
                }
            }

            // 从插入位置逐个后移元素
            for (int j = i; j > l; j--) {
                values[j] = values[j - 1];
            }

            // 将选取元素插入坑中
            values[l] = t;
        }
    }

    public static void main(String[] args) {
        Integer[] values = {1, 5, 4, 3, 2, 6, 7, 8, 9};
        Sort<Integer> sort = new BinarySort<>();
        long startTime = System.nanoTime();
        sort.sort(values);
        long endTime = System.nanoTime();
        System.out.printf("耗时：%d ns, %s\n", endTime - startTime, Arrays.toString(values));
    }
}
