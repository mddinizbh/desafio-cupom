package com.challenge.coupon.presentation.dto.response;

import com.challenge.coupon.domain.model.CouponStatus;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record CouponResponse(
    UUID id,
    String code,
    String description,
    BigDecimal discountValue,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    LocalDateTime expirationDate,
    CouponStatus status,
    boolean published,
    boolean redeemed
) {}
