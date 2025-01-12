package com.alex.coupons.timerTaskCoupon;

import com.alex.coupons.logic.CouponLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.rmi.ServerException;
import java.util.TimerTask;

//:ToDo read about component
@Component
public class DeleteExpiredCoupons {


    private CouponLogic couponsLogic;

    @Autowired
    public DeleteExpiredCoupons(CouponLogic couponsLogic) {
        this.couponsLogic = couponsLogic;
    }
//:ToDo to check https://crontab.guru/#*_*_*_*_*
    @Scheduled(cron = "0  * * * * ?")
    void executeDailyTask() throws ServerException {
        new Thread(() -> {
            try {
                couponsLogic.deleteExpiredCoupons();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}
