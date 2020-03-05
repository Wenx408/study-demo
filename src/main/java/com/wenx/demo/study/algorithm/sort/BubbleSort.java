package com.wenx.demo.study.algorithm.sort;

import java.util.Arrays;

/**
 * @Author: Wenx
 * @Description: 冒泡排序
 * @Date: Created in 2020/3/5 17:15
 * @Modified By：
 */
public class BubbleSort<T extends Comparable<T>> implements Sort<T> {
    @Override
    public void sort(T[] values) {
        int size = values.length;
        for (int i = 0, num = 1; i < size - 1; i++) {
            for (int j = 0; j < size - i - 1; j++, num++) {
                System.out.printf("第%02d轮：%s [%d > %d ?] ", num, Arrays.toString(values),
                        values[j], values[j + 1]);
                if (values[j].compareTo(values[j + 1]) == 1) {
                    T t = values[j];
                    values[j] = values[j + 1];
                    values[j + 1] = t;
                    System.out.printf("进行交换：%s ", Arrays.toString(values));
                }
                System.out.println();
            }
        }
    }

    public static void main(String[] args) {
        Integer[] values = {2, 5, 4, 3, 1};
        Sort<Integer> sort = new BubbleSort<>();
        long startTime = System.currentTimeMillis();
        sort.sort(values);
        long endTime = System.currentTimeMillis();
        System.out.printf("耗时：%d ms\n", endTime - startTime);
    }
}
