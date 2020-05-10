package com.wenx.demo.study.solution;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * @author Wenx
 * @date 2020/5/10
 */
public class GenerateName {
    public static void main(String[] args) {
        // 1.利用Random的Stream方式生成数据集合，内容最好是中文姓名
        // 2.测试Optional返回集合，为null时抛出异常，但为empty时不报错
        List<String> names = Optional.ofNullable(listName(10))
                .orElseThrow(() -> new RuntimeException());
        names.forEach(System.out::println);
    }

    public static List<String> listName(long size) {
        List<String> names = new ArrayList<>();
        if (size > 0) {
            Random random = new Random();
            names = random.ints(2, 4)
                    .limit(size)
                    .mapToObj(GenerateName::getChineseCharacters)
                    .collect(Collectors.toList());
        }
        return names;
    }

    /**
     * 使用GB2312编码生成汉字：
     * <p>
     * GB2312规定对收录的每个字符采用两个字节表示，
     * 第一个字节为“高字节”，对应94个区；第二个字节为“低字节”，对应94个位。
     * 可以理解为一张94行94列的区位表，每一行称为一个“区”，每一列称为一个“位”，
     * 共94个区，每区含有94个位，共8836个码位，这种表示方式称为区位码。
     * <p>
     * 区码范围：
     * 01-09区收录除汉字外的682个特殊字符。
     * 10-15区为空白区，没有使用。
     * 16-55区收录3755个一级汉字，按拼音字母排序。
     * 56-87区收录3008个二级汉字，按部首/笔画排序。
     * 88-94区为空白区，没有使用。
     * <p>
     * 位码范围：区码为55时，位码范围为1-89；其他区码，位码范围为1-94。
     * <p>
     * 区位码范围：0101-9494，区号和位号分别转换成十六进制加上0xA0就是GB2312编码。
     * GB2312编码范围：A1A1-FEFE，其中汉字编码范围：B0A1-F7FE，
     * 汉字编码：第一字节0xB0-0xF7（对应区号：16-87），第二个字节0xA1-0xFE（对应位号：01-94）。
     * 例如最后一个码位是9494，区号和位号分别转换成十六进制是5E5E，0x5E+0xA0=0xFE，所以该码位的GB2312编码是FEFE。
     */
    public static String getChineseCharacters(int size) {
        final int AREA_CODE_55 = 215;
        Random random = new Random();
        String result = "";
        for (int i = 0; i < size; i++) {
            // 第一字节范围：0xB0~0xF7（对应区号：16~87），最常用的一级汉字（16~55），最后加上0xA0（160）
            // Random.nextInt(n)取值为0~(n-1)不包含n，所以16~55为16+nextInt(55-16)+160
            int r = 176 + random.nextInt(39);
            // 第二字节范围：0xA1~0xFE（对应位号：01~94），最后加上0xA0（160）
            // 当区码为55时位码范围为1~89，其他为1~94
            int c = r == AREA_CODE_55 ? (161 + random.nextInt(89))
                    : (161 + random.nextInt(94));
            byte[] bytes = new byte[]{(byte) r, (byte) c};
            try {
                result += new String(bytes, "GB2312");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}
