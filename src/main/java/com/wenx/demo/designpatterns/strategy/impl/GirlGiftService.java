package com.wenx.demo.designpatterns.strategy.impl;

import com.wenx.demo.designpatterns.strategy.GiftService;
import com.wenx.demo.designpatterns.strategy.CustomerConsts;
import org.springframework.stereotype.Component;

/**
 * 过节了给小姐姐送点礼物
 *
 * @author Wenx
 * @date 2020/5/13
 */
@Component(CustomerConsts.GIRL)
public class GirlGiftService implements GiftService {
    @Override
    public void sendGifts() {
        System.out.println("也不知道小姐姐喜欢啥，把我自己送给她吧~");
    }
}
