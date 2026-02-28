package com.challenge.coupon.domain.model;

import com.challenge.coupon.domain.exception.CouponAlreadyDeletedException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record Coupon(
    UUID id,
    String code,
    String description,
    BigDecimal discountValue,
    LocalDate expirationDate,
    boolean published,
    LocalDateTime createdAt,
    LocalDateTime deletedAt
) {
    public Coupon {
        if (code != null) {
            code = code.toUpperCase();
        }
        if (discountValue != null && discountValue.compareTo(new BigDecimal("0.5")) < 0) {
            discountValue = new BigDecimal("0.5");
        }
    }

    public static Coupon create(String code, String description,
                                BigDecimal discountValue, LocalDate expirationDate,
                                boolean published) {
        return new Coupon(
            null,
            code,
            description,
            discountValue,
            expirationDate,
            published,
            LocalDateTime.now(),
            null
        );
    }

    public Coupon delete() {
        if (isDeleted()) {
            throw new CouponAlreadyDeletedException();
        }
        return new Coupon(
            id,
            code,
            description,
            discountValue,
            expirationDate,
            published,
            createdAt,
            LocalDateTime.now()
        );
    }

    public boolean isDeleted() {
        return deletedAt != null;
    }

    public CouponStatus getStatus() {
        if (isDeleted()) {
            return CouponStatus.DELETED;
        }
        return published ? CouponStatus.ACTIVE : CouponStatus.INACTIVE;
    }

    public boolean isRedeemed() {
        return isDeleted() && published;
    }
}
