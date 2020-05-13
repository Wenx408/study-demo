package com.wenx.demo.designpatterns.strategy;

import com.wenx.demo.StudyDemoApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author Wenx
 * @date 2020/5/13
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = StudyDemoApplication.class)
public class GiftServiceFactoryTest {

    @Autowired
    private GiftServiceFactory giftServiceFactory;

    @Test
    public void getByCustomerType() {
        GiftService giftService = giftServiceFactory.getByCustomerType(CustomerConsts.GIRL);
        giftService.sendGifts();
    }
}