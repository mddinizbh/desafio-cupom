package com.challenge.coupon.application.usecase;

import com.challenge.coupon.application.port.out.CouponRepositoryPort;
import com.challenge.coupon.domain.model.Coupon;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class CreateCouponUseCase {

    private final CouponRepositoryPort couponRepositoryPort;

    public Coupon create(String code, String description, BigDecimal discountValue, LocalDate expirationDate, boolean published) {
        Coupon coupon = Coupon.create(code, description, discountValue, expirationDate, published);
        return couponRepositoryPort.save(coupon);
    }
}
