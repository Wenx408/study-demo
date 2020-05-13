package com.wenx.demo.designpatterns.strategy.impl;

import com.wenx.demo.designpatterns.strategy.GiftService;
import com.wenx.demo.designpatterns.strategy.CustomerConsts;
import org.springframework.stereotype.Component;

/**
 * 过节了给汪星人送点礼物
 *
 * @author Wenx
 * @date 2020/5/13
 */
@Component(CustomerConsts.DOG)
public class DogGiftService implements GiftService {
    @Override
    public void sendGifts() {
        System.out.println("大棒骨肉真多，忍不住酱了大棒骨吃，给他点骨头就好了~");
    }
}
