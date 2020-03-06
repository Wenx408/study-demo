package com.wenx.demo.study.algorithm.sort;

import java.util.Arrays;

/**
 * @Author: Wenx
 * @Description: 快速排序
 * @Date: Created in 2020/3/6 17:35
 * @Modified By：
 */
public class QuickSort<T extends Comparable<T>> implements Sort<T> {
    @Override
    public void sort(T[] values) {
        int size = values.length;
        int low = 0;
        int high = size - 1;
        sort(values, low, high);
    }

    private void sort(T[] values, int low, int high) {
        if (low < high) {
            int pivotIdx = partition(values, low, high);
            sort(values, low, pivotIdx - 1);
            sort(values, pivotIdx + 1, high);
        }
    }

    private int partition(T[] values, int low, int high) {
        int l = low;
        int r = high;
        // 挖坑1：保存基准的值
        T pivot = values[low];
        while (l < r) {
            // 挖坑2：从后向前找到比基准小的元素，插入到基准位置坑1中
            while (l < r && values[r].compareTo(pivot) > -1) {
                r--;
            }
            values[l] = values[r];
            // 挖坑3：从前往后找到比基准大的元素，放到刚才挖的坑2中
            while (l < r && values[l].compareTo(pivot) < 1) {
                l++;
            }
            values[r] = values[l];
        }
        // 基准值填补到坑3中，准备分治递归快排
        values[l] = pivot;
        return l;
    }

    public static void main(String[] args) {
        Integer[] values = {1, 5, 4, 3, 2, 6, 7, 8, 9};
        Sort<Integer> sort = new QuickSort<>();
        long startTime = System.nanoTime();
        sort.sort(values);
        long endTime = System.nanoTime();
        System.out.printf("耗时：%d ns, %s\n", endTime - startTime, Arrays.toString(values));
    }
}
