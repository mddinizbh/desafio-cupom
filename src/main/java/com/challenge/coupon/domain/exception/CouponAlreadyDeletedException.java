package com.challenge.coupon.domain.exception;

public class CouponAlreadyDeletedException extends RuntimeException {
    public CouponAlreadyDeletedException() {
        super("Coupon has already been deleted");
    }
}
