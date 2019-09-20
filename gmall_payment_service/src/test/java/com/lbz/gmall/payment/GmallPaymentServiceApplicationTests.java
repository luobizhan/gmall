package com.lbz.gmall.payment;

import com.lbz.gmall.service.PaymentService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GmallPaymentServiceApplicationTests {

    @Autowired
    PaymentService paymentService ;
    @Test
    public void contextLoads() {
        paymentService.editPaymentInfo("1001");

    }

}
