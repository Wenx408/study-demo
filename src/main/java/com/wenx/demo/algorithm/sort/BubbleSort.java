package com.wenx.demo.algorithm.sort;

import java.util.Arrays;

/**
 * 冒泡排序
 *
 * @author Wenx
 * @date 2020/3/5
 */
public class BubbleSort<T extends Comparable<T>> implements Sort<T> {
    @Override
    public void sort(T[] values) {
        int count = 0;
        int size = values.length;
        // 优化外层循环，若本轮比较未发生交换，说明数组已排序完成，取消后续比较
        boolean unfinished = true;
        // 优化内层循环，若本轮比较此位置之后未发生交换，说明数组此位置之后已排序完成，取消此位置之后比较
        int limit = size - 1;
        for (int i = 1, last = 0; i < size; i++, limit = last) {
            if (!unfinished) {
                System.out.println("由于上轮比较未发生交换，说明数组已排序完成，取消后续比较");
                break;
            }
            unfinished = false;
            System.out.println("limit：" + limit);
            for (int j = 0; j < limit; j++) {
                System.out.printf("[比较%02d次][第%02d轮,本轮第%02d次]%s",
                        ++count, i, j + 1, Arrays.toString(values));
                if (values[j].compareTo(values[j + 1]) == 1) {
                    System.out.printf("[(第%d位)%d > %d(第%d位) 进行交换]",
                            j + 1, values[j], j + 2, values[j + 1]);
                    unfinished = true;
                    last = j;
                    T t = values[j];
                    values[j] = values[j + 1];
                    values[j + 1] = t;
                }
                System.out.println();
            }
        }
        System.out.printf("共比较%02d次 ", count);
    }

    public static void main(String[] args) {
        Integer[] values = {1, 5, 4, 3, 2, 6, 7, 8, 9};
        Sort<Integer> sort = new BubbleSort<>();
        long startTime = System.currentTimeMillis();
        sort.sort(values);
        long endTime = System.currentTimeMillis();
        System.out.printf("耗时：%d ms, %s\n", endTime - startTime, Arrays.toString(values));
    }
}
