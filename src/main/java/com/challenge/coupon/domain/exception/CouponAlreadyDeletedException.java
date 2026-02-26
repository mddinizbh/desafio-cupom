package com.challenge.coupon.domain.exception;

public class CouponAlreadyDeletedException extends RuntimeException {
    public CouponAlreadyDeletedException(String message) {
        super(message);
    }
}
