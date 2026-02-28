package com.challenge.coupon.application.usecase;

import com.challenge.coupon.application.port.out.CouponRepositoryPort;
import com.challenge.coupon.domain.exception.CouponNotFoundException;
import com.challenge.coupon.domain.model.Coupon;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetCouponByIdUseCase {

    private final CouponRepositoryPort repositoryPort;

    public Coupon execute(UUID id) {
        return repositoryPort.findById(id)
                .orElseThrow(() -> new CouponNotFoundException(id));
    }
}
