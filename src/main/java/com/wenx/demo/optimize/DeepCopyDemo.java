package com.wenx.demo.optimize;

import java.io.*;
import java.util.*;
import java.util.function.Function;

/**
 * @author Wenx
 * @date 2020/3/5
 */
public class DeepCopyDemo {
    public static void main(String[] args) throws Exception {
        System.out.println("===================== 基础数据类型，值传递 =====================");
        List<Integer> list1 = Arrays.asList(1, 2, 3, 4, 5);
        System.out.println("======================== Shallow Copy ========================");
        List<Integer> list2 = new ArrayList<>(list1);
        displayDiff(list1, list2);
        System.out.println("========== Clone 集合元素改变，不会影响 Source 集合元素 ==========");
        list2.set(0, 5);
        displayDiff(list1, list2);
        System.out.println("========================== Deep Copy ==========================");
        List<Integer> list3 = deepCopy1(e -> new Integer(e), list1);
        displayDiff(list1, list3);
        System.out.println("========== Clone 集合元素改变，不会影响 Source 集合元素 ==========");
        list3.set(0, 5);
        displayDiff(list1, list3);

        System.out.println("=================== 引用类型，引用（地址）传递 ===================");
        // DeepCopyDemo::newUser 等价于 id -> new User(id)
        List<User> list11 = of(DeepCopyDemo::newUser,
                1, 2, 3, 4, 5);
        System.out.println("======================== Shallow Copy ========================");
        List<User> list12 = new ArrayList<>(list11);
        displayDiff(list11, list12);
        System.out.println("========== Clone 集合元素改变，将会影响 Source 集合元素 ==========");
        list12.get(0).setId(5);
        displayDiff(list11, list12);
        System.out.println("========================== Deep Copy ==========================");
        List<User> list13 = deepCopy2(list11);
        displayDiff(list11, list13);
        System.out.println("========== Clone 集合元素改变，不会影响 Source 集合元素 ==========");
        list13.get(0).setId(10);
        displayDiff(list11, list13);
    }

    /**
     * 展示集合元素之间的差异
     *
     * @param list1 第一个要比较的集合
     * @param list2 第二个要比较的集合
     * @param <T>   集合元素类型
     */
    private static <T> void displayDiff(List<T> list1, List<T> list2) {
        for (int i = 0; i < list1.size(); i++) {
            System.out.printf("value1 : %s, value2 : %s, Objects.equals : %s, Object == : %s\n",
                    list1.get(i), list2.get(i),
                    Objects.equals(list1.get(i), list2.get(i)),
                    list1.get(i) == list2.get(i));
        }
    }

    /**
     * 深度复制方法1-new Object()
     *
     * @param newFunc 元素实例化方法
     * @param source  数据源集合
     * @param <T>     集合元素类型
     * @return 深度复制集合
     */
    private static <T> List<T> deepCopy1(Function<T, T> newFunc, Iterable<T> source) {
        List<T> clone = new ArrayList<>();
        source.forEach(e -> clone.add(newFunc.apply(e)));
        return clone;
    }

    /**
     * 深度复制方法2-序列化，反序列化
     *
     * @param source 数据源集合
     * @param <T>    集合元素类型
     * @return 深度复制集合
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private static <T> List<T> deepCopy2(Collection<T> source) throws IOException, ClassNotFoundException {
        List<T> list = new ArrayList<>(source);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        // 序列化
        objectOutputStream.writeObject(list);

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
        // 反序列化
        List<T> clone = (List<T>) objectInputStream.readObject();

        objectOutputStream.close();
        outputStream.close();
        objectInputStream.close();
        inputStream.close();

        return clone;
    }

    /**
     * 基础类型集合包装成引用类型集合
     *
     * @param func   包装方法
     * @param values 基础类型集合
     * @param <T>    基础元素类型
     * @param <R>    引用包装类型
     * @return 引用类型集合
     */
    private static <T, R> List<R> of(Function<T, R> func, T... values) {
        List<R> list = new ArrayList<>(values.length);
        for (T element : values) {
            list.add(func.apply(element));
        }
        return list;
    }

    public static User newUser(Integer id) {
        return new User(id);
    }

    private static class User implements Serializable {
        private static final long serialVersionUID = 2962108650734878038L;

        private Integer id;

        public User(Integer id) {
            this.id = id;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof User) {
                User user = (User) obj;
                return id.equals(user.getId());
            }
            return false;
        }

        @Override
        public int hashCode() {
            return id.hashCode();
        }

        @Override
        public String toString() {
            return "[User: " + this.hashCode() + ",id:" + id + "]";
        }
    }
}
