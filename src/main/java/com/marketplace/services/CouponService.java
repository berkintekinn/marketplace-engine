package com.marketplace.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.marketplace.models.Coupon;
import com.marketplace.repository.CouponRepository;

@Service
public class CouponService {

    @Autowired
    private CouponRepository couponRepository;

    public Coupon createCoupon(Coupon coupon) {
        return couponRepository.save(coupon);
    }
}