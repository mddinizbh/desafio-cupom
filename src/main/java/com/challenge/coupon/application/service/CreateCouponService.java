package com.challenge.coupon.application.service;

import com.challenge.coupon.domain.model.Coupon;
import com.challenge.coupon.domain.port.in.CreateCouponUseCase;
import com.challenge.coupon.domain.port.out.CouponRepositoryPort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
public class CreateCouponService implements CreateCouponUseCase {

    private final CouponRepositoryPort couponRepositoryPort;

    public CreateCouponService(CouponRepositoryPort couponRepositoryPort) {
        this.couponRepositoryPort = couponRepositoryPort;
    }

    @Override
    public Coupon create(String code, String description, BigDecimal discountValue, LocalDate expirationDate, Boolean published) {
        Coupon coupon = Coupon.create(code, description, discountValue, expirationDate, published);
        return couponRepositoryPort.save(coupon);
    }
}
