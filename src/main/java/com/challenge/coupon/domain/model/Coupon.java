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
        code = sanitizeCode(code);
        validate(code, description, discountValue, expirationDate);
    }

    public static Coupon create(String code,
                                String description,
                                BigDecimal discountValue,
                                LocalDate expirationDate,
                                Boolean published) {
        return new Coupon(
                UUID.randomUUID(),
                code,
                description,
                discountValue,
                expirationDate,
                published != null && published,
                LocalDateTime.now(),
                null
        );
    }

    public Coupon delete() {
        if (isDeleted()) {
            throw new CouponAlreadyDeletedException("Cupom já foi deletado");
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

    private static void validate(String code,
                                 String description,
                                 BigDecimal discountValue,
                                 LocalDate expirationDate) {
        if (isBlank(code) || isBlank(description) || discountValue == null || expirationDate == null) {
            throw new InvalidCouponException("Campos obrigatórios: code, description, discountValue, expirationDate");
        }

        if (code.length() != 6 || !code.matches("^[a-zA-Z0-9]{6}$")) {
            throw new InvalidCouponException("Código do cupom deve ser alfanumérico e possuir exatamente 6 caracteres após sanitização");
        }

        if (discountValue.compareTo(new BigDecimal("0.5")) < 0) {
            throw new InvalidCouponException("discountValue deve ser maior ou igual a 0.5");
        }

        if (expirationDate.isBefore(LocalDate.now())) {
            throw new InvalidCouponException("expirationDate não pode ser uma data no passado");
        }
    }

    private static String sanitizeCode(String code) {
        if (code == null) {
            return null;
        }
        return code.replaceAll("[^a-zA-Z0-9]", "");
    }

    private static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
