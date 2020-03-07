package com.wenx.demo.study.algorithm.sort;

import java.util.Arrays;

/**
 * @Author: Wenx
 * @Description:
 * @Date: Created in 2020/3/7 8:41
 * @Modified By：
 */
public class MergeSort<T extends Comparable<T>> implements Sort<T> {
    @Override
    public void sort(T[] values) {
        int size = values.length;
        int low = 0;
        int high = size - 1;
        sort(values, low, high);
    }

    private void sort(T[] values, int low, int high) {
        if (low < high) {
            // 获取中间索引
            int mid = (low + high) / 2;
            // 拆分左半部分
            sort(values, low, mid);
            // 拆分右半部分
            sort(values, mid + 1, high);
            // 合并两部分
            merge(values, low, mid, high);
        }
    }

    private void merge(Comparable<T>[] values, int low, int mid, int high) {
        // 计算两部分各自元素数量
        int n1 = mid - low + 1;
        int n2 = high - mid;
        Comparable[] a1 = new Comparable[n1];
        Comparable[] a2 = new Comparable[n2];
        // 复制到新数组
        System.arraycopy(values, low, a1, 0, n1);
        System.arraycopy(values, mid + 1, a2, 0, n2);

        int index = low;
        int i = 0, j = 0;
        while (i < n1 && j < n2) {
            // 循环比较两数组元素大小，小的放进源数组
            if (a1[i].compareTo(a2[j]) == -1) {
                values[index++] = a1[i++];
            } else {
                values[index++] = a2[j++];
            }
        }

        // 将剩下的依次放进源数组
        while (i < n1) {
            values[index++] = a1[i++];
        }

        // 将剩下的依次放进源数组
        while (j < n2) {
            values[index++] = a2[j++];
        }
    }

    public static void main(String[] args) {
        Integer[] values = {1, 5, 4, 3, 2, 6, 7, 8, 9};
        Sort<Integer> sort = new MergeSort<>();
        long startTime = System.nanoTime();
        sort.sort(values);
        long endTime = System.nanoTime();
        System.out.printf("耗时：%d ns, %s\n", endTime - startTime, Arrays.toString(values));
    }
}
