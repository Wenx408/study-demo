package com.wenx.demo.designpatterns.strategy.impl;

import com.wenx.demo.designpatterns.strategy.GiftService;
import com.wenx.demo.designpatterns.strategy.CustomerConsts;
import org.springframework.stereotype.Component;

/**
 * 过节了给喵星人送点礼物
 *
 * @author Wenx
 * @date 2020/5/13
 */
@Component(CustomerConsts.CAT)
public class CatGiftService implements GiftService {
    @Override
    public void sendGifts() {
        System.out.println("美味的鱼让我吃了，给她点鱼刺好了~");
    }
}
