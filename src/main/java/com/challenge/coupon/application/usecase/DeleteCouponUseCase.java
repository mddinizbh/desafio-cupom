package com.challenge.coupon.application.usecase;

import com.challenge.coupon.application.port.out.CouponRepositoryPort;
import com.challenge.coupon.domain.exception.CouponNotFoundException;
import com.challenge.coupon.domain.model.Coupon;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeleteCouponUseCase {

    private final CouponRepositoryPort couponRepositoryPort;

    public void delete(UUID id) {
        Coupon coupon = couponRepositoryPort.findByIdIncludingDeleted(id)
                .orElseThrow(() -> new CouponNotFoundException(id));
        
        Coupon deletedCoupon = coupon.delete();
        couponRepositoryPort.save(deletedCoupon);
    }
}
