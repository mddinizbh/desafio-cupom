package com.challenge.coupon.application.service;

import com.challenge.coupon.domain.exception.CouponNotFoundException;
import com.challenge.coupon.domain.model.Coupon;
import com.challenge.coupon.domain.port.in.DeleteCouponUseCase;
import com.challenge.coupon.domain.port.out.CouponRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class DeleteCouponService implements DeleteCouponUseCase {

    private final CouponRepositoryPort couponRepositoryPort;

    public DeleteCouponService(CouponRepositoryPort couponRepositoryPort) {
        this.couponRepositoryPort = couponRepositoryPort;
    }

    @Override
    public void delete(UUID id) {
        Coupon coupon = couponRepositoryPort.findByIdIncludingDeleted(id)
                .orElseThrow(() -> new CouponNotFoundException("Cupom não encontrado com id: " + id));
        couponRepositoryPort.save(coupon.delete());
    }
}
