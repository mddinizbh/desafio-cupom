package com.challenge.coupon.domain.port.in;

import com.challenge.coupon.domain.model.Coupon;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface CreateCouponUseCase {
    Coupon create(String code, String description, BigDecimal discountValue, LocalDate expirationDate, Boolean published);
}
