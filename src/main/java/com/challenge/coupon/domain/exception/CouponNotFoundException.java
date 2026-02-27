package com.challenge.coupon.domain.exception;

import java.util.UUID;

public class CouponNotFoundException extends RuntimeException {
    public CouponNotFoundException(UUID id) {
        super("Cupom não encontrado com id: " + id);
    }
}
