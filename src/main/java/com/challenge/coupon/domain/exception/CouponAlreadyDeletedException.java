package com.challenge.coupon.domain.exception;

public class CouponAlreadyDeletedException extends RuntimeException {
    public CouponAlreadyDeletedException() {
        super("Cupom já foi deletado");
    }
}
