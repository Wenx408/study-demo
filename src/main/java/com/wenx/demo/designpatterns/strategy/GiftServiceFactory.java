package com.wenx.demo.designpatterns.strategy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 送礼策略工厂，根据顾客类型选择响应送礼服务
 *
 * @author Wenx
 * @date 2020/5/13
 */
@Component
public class GiftServiceFactory {
    @Autowired
    private Map<String, GiftService> giftServiceMap;

    public GiftService getByCustomerType(String type) {
        return giftServiceMap.get(type);
    }
}
