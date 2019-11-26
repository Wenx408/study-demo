package com.wenx.demo.study.network;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.Unpooled;

import java.util.Arrays;

/**
 * @Author: Wenx
 * @Description:
 * @Date: Created in 2019/11/25 22:31
 * @Modified By：
 */
public class ByteBufDemo {
    public static void main(String[] args) {
        apiTest();
        compositeTest();
        wrapTest();
        sliceTest();
    }

    public static void apiTest() {
        //  +-------------------+------------------+------------------+
        //  | discardable bytes |  readable bytes  |  writable bytes  |
        //  |                   |     (CONTENT)    |                  |
        //  +-------------------+------------------+------------------+
        //  |                   |                  |                  |
        //  0      <=       readerIndex   <=   writerIndex    <=    capacity

        // 1.创建一个非池化的ByteBuf，大小为10个字节
        ByteBuf buf = Unpooled.buffer(10);
        //ByteBuf buf = Unpooled.directBuffer(10);
        println("1.原始ByteBuf为", buf);

        // 2.写入一段内容
        byte[] bytes = {1, 2, 3, 4, 5};
        buf.writeBytes(bytes);
        print("2.写入的bytes为", bytes);
        println("写入内容后ByteBuf为", buf);

        // 3.读取一段内容
        byte b1 = buf.readByte();
        byte b2 = buf.readByte();
        print("3.读取的bytes为", new byte[]{b1, b2});
        println("读取内容后ByteBuf为", buf);

        // 4.将读取的内容丢弃
        buf.discardReadBytes();
        println("4.将读取的内容丢弃后ByteBuf为", buf);

        // 5.清空读写指针
        buf.clear();
        println("5.清空读写指针后ByteBuf为", buf);

        // 6.再次写入一段内容，比第一段内容少
        byte[] bytes2 = {1, 2, 3};
        buf.writeBytes(bytes2);
        print("6.写入的bytes为", bytes2);
        println("写入内容后ByteBuf为", buf);

        // 7.将ByteBuf清零
        buf.setZero(0, buf.capacity());
        println("7.将内容清零后ByteBuf为", buf);

        // 8.再次写入一段超过容量的内容
        byte[] bytes3 = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11};
        buf.writeBytes(bytes3);
        print("8.写入的bytes为", bytes3);
        println("写入内容后ByteBuf为", buf);

        // 随机访问索引 getByte
        // 顺序读 read*
        // 顺序写 write*
        // 清除已读内容 discardReadBytes
        // 清除缓冲区 clear
        // 搜索操作
        // 标记和重置
        // 完整代码示例：参考
        // 搜索操作 读取指定位置 buf.getByte(1);
    }

    public static void compositeTest() {
        ByteBuf buffer1 = Unpooled.buffer(3);
        buffer1.writeByte(1);
        ByteBuf buffer2 = Unpooled.buffer(3);
        buffer2.writeByte(4);
        print("buffer1为", buffer1);
        print("buffer2为", buffer2);

        CompositeByteBuf compositeByteBuf = Unpooled.compositeBuffer();
        ByteBuf newBuffer = compositeByteBuf.addComponents(true, buffer1, buffer2);
        println("CompositeByteBuf为", newBuffer);
    }

    public static void wrapTest() {
        byte[] arr = {1, 2, 3, 4, 5};
        ByteBuf newBuffer = Unpooled.wrappedBuffer(arr);
        print("byte[]为", arr);
        print("wrappedBuffer为", newBuffer);

        print("newBuffer.getByte(4)为", newBuffer.getByte(4));
        arr[4] = 6;
        println("byte[4] = 6; 后newBuffer.getByte(4)为", newBuffer.getByte(4));
    }

    public static void sliceTest() {
        ByteBuf oldBuffer = Unpooled.wrappedBuffer("hello".getBytes());
        ByteBuf newBuffer = oldBuffer.slice(1, 2);
        print("oldBuffer为", oldBuffer);
        print("oldBuffer.slice(1, 2); 为", newBuffer);
        print("newBuffer.getByte(0)为", newBuffer.getByte(0));
        print("newBuffer.getByte(1)为", newBuffer.getByte(1));

        // 新buf中原buf的引用
        ByteBuf buf = newBuffer.unwrap();
        print("newBuffer.unwrap()为", buf);
        print("buf.getByte(0)为", buf.getByte(0));
        print("buf.getByte(1)为", buf.getByte(1));
        print("buf.getByte(2)为", buf.getByte(2));
        print("buf.getByte(3)为", buf.getByte(3));
        print("buf.getByte(4)为", buf.getByte(4));
    }


    private static void print(String str, byte b) {
        System.out.println(String.format("%s==========>%s", str, b));
    }

    private static void print(String str, byte[] bytes) {
        System.out.println(String.format("%s==========>%s", str, Arrays.toString(bytes)));
    }

    private static void print(String str, ByteBuf buf) {
        print(str, buf, "");
    }

    private static void print(String before, ByteBuf buf, String after) {
        byte[] bytes;

        if (buf.hasArray()) {
            bytes = buf.array();
        } else {
            int capacity = buf.capacity();
            bytes = new byte[capacity];
            for (int i = 0; i < buf.capacity(); i++) {
                bytes[i] = buf.getByte(i);
            }
        }

        System.out.println(String.format("%s==========>%s(ridx:%s, widx: %s, cap: %s)%s", before, Arrays.toString(bytes), buf.readerIndex(), buf.writerIndex(), buf.capacity(), after));
    }

    private static void println(String str, byte b) {
        System.out.println(String.format("%s==========>%s\n", str, b));
    }

    private static void println(String str, ByteBuf buf) {
        print(str, buf, "\n");
    }
}
