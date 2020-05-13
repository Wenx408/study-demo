package com.wenx.demo.designpatterns.strategy.impl;

import com.wenx.demo.designpatterns.strategy.GiftService;
import com.wenx.demo.designpatterns.strategy.CustomerConsts;
import org.springframework.stereotype.Component;

/**
 * 过节了给大兄弟送点礼物
 *
 * @author Wenx
 * @date 2020/5/13
 */
@Component(CustomerConsts.BOY)
public class BoyGiftService implements GiftService {
    @Override
    public void sendGifts() {
        System.out.println("我跟大兄弟这关系不送都行，可我还是得送，送他一脚吧~");
    }
}
