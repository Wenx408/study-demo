package com.wenx.demo.algorithm.sort;

import java.util.Arrays;

/**
 * 插入排序
 *
 * @author Wenx
 * @date 2020/3/6
 */
public class InsertionSort<T extends Comparable<T>> implements Sort<T> {
    @Override
    public void sort(T[] values) {
        int size = values.length;
        for (int i = 1; i < size; i++) {
            // 向后选取当前元素
            // 1, 5, 4, 3, 2, 6, 7, 8, 9
            //    |
            T t = values[i];
            // 当前选取元素向前逐个比较，若前面元素比选取元素大，则进行后移操作
            // 1, 5, 4, 3, 2, 6, 7, 8, 9
            // <- | (选取5)
            // 1, 5, 4, 3, 2, 6, 7, 8, 9
            //    <- | (选取4, 5 > 4 进行后移操作)
            for (int j = i - 1; j >= 0 && values[j].compareTo(t) == 1; j--) {
                // 首先逐个后移元素
                // 1, 5, 4, 3, 2, 6, 7, 8, 9
                //    | -> (将5向后移)
                // 1, 5, 5, 3, 2, 6, 7, 8, 9
                values[j + 1] = values[j];
                // 直到首元素或前方元素小于等于当前选取元素，则进行插入操作
                // 1, 5, 5, 3, 2, 6, 7, 8, 9
                //    ^ (当前位置前方1 <= 4, 此位置插入4)
                // 1, 4, 5, 3, 2, 6, 7, 8, 9
                if (j == 0 || values[j - 1].compareTo(t) < 1) {
                    // 插入当前选取元素，跳出进行下一轮比较
                    values[j] = t;
                    break;
                }
            }
        }
    }

    public static void main(String[] args) {
        Integer[] values = {1, 5, 4, 3, 2, 6, 7, 8, 9};
        Sort<Integer> sort = new InsertionSort<>();
        long startTime = System.nanoTime();
        sort.sort(values);
        long endTime = System.nanoTime();
        System.out.printf("耗时：%d ns, %s\n", endTime - startTime, Arrays.toString(values));
    }
}
