package com.challenge.coupon.domain.model;

import com.challenge.coupon.domain.exception.CouponAlreadyDeletedException;
import com.challenge.coupon.domain.exception.InvalidCouponException;

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
        validateMandatoryFields(code, description, discountValue, expirationDate);
        code = sanitizeCode(code);
        if (code.length() != 6) {
            throw new InvalidCouponException("O código do cupom deve ter exatamente 6 caracteres após a sanitização.");
        }

        if (discountValue.compareTo(new BigDecimal("0.5")) < 0) {
            discountValue = new BigDecimal("0.5");
        }

        if (expirationDate.isBefore(LocalDate.now())) {
            throw new InvalidCouponException("A data de expiração não pode estar no passado.");
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

    private static String sanitizeCode(String code) {
        if (code == null) return "";
        return code.replaceAll("[^a-zA-Z0-9]", "");
    }

    private static void validateMandatoryFields(String code, String description, BigDecimal discountValue, LocalDate expirationDate) {
        if (code == null || code.isBlank() ||
            description == null || description.isBlank() ||
            discountValue == null || expirationDate == null) {
            throw new InvalidCouponException("Campos obrigatórios ausentes.");
        }
    }
}
