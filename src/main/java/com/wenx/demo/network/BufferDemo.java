package com.wenx.demo.network;

import java.nio.ByteBuffer;

/**
 * @author Wenx
 * @date 2019/11/19
 */
public class BufferDemo {
    public static void main(String[] args) {
        // 构建一个byte字节缓冲区，容量是4
        ByteBuffer byteBuffer = ByteBuffer.allocate(4);
        //ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4); // 堆外
        // 默认写入模式，查看三个重要的指标
        System.out.println(String.format("初始化：position位置：%s, limit限制：%s capacity容量：%s,", byteBuffer.position(), byteBuffer.limit(), byteBuffer.capacity()));
        // 写入3字节的数据
        byteBuffer.put((byte) 1);
        byteBuffer.put((byte) 2);
        byteBuffer.put((byte) 3);
        // 再看数据
        System.out.println(String.format("写入3字节后，position位置：%s, limit限制：%s capacity容量：%s,", byteBuffer.position(), byteBuffer.limit(), byteBuffer.capacity()));

        // 转换为读取模式(不调用flip方法，也是可以读取数据的，但是position记录读取的位置不对)
        byteBuffer.flip();
        System.out.println(String.format("转换为读取模式后，position位置：%s, limit限制：%s capacity容量：%s,", byteBuffer.position(), byteBuffer.limit(), byteBuffer.capacity()));
        // 读取2字节的数据
        byte a = byteBuffer.get();
        System.out.println(a);
        byte b = byteBuffer.get();
        System.out.println(b);
        System.out.println(String.format("读取2字节数据后，position位置：%s, limit限制：%s capacity容量：%s,", byteBuffer.position(), byteBuffer.limit(), byteBuffer.capacity()));

        // 继续写入3字节，此时读模式下，limit=3，position=2.继续写入只能覆盖写入一条数据
        // clear()方法清除整个缓冲区。compact()方法仅清除已阅读的数据。转为写入模式
        byteBuffer.compact(); // buffer : 1 , 3
        System.out.println(String.format("清除已阅读的数据后，position位置：%s, limit限制：%s capacity容量：%s,", byteBuffer.position(), byteBuffer.limit(), byteBuffer.capacity()));
        // 写入3字节的数据
        byteBuffer.put((byte) 3);
        byteBuffer.put((byte) 4);
        byteBuffer.put((byte) 5);
        System.out.println(String.format("再写入3字节后最终的情况，position位置：%s, limit限制：%s capacity容量：%s,", byteBuffer.position(), byteBuffer.limit(), byteBuffer.capacity()));

        // rewind() 重置position为0
        // mark() 标记position的位置
        // reset() 重置position为上次mark()标记的位置
    }
}
