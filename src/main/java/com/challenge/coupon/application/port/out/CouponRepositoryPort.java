package com.challenge.coupon.application.port.out;

import com.challenge.coupon.domain.model.Coupon;

import java.util.Optional;
import java.util.UUID;

public interface CouponRepositoryPort {
    Coupon save(Coupon coupon);
    Optional<Coupon> findById(UUID id);

    Optional<Coupon> findByIdIncludingDeleted(UUID id);
}
