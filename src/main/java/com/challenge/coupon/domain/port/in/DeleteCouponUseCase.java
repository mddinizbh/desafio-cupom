package com.challenge.coupon.domain.port.in;

import java.util.UUID;

public interface DeleteCouponUseCase {
    void delete(UUID id);
}
