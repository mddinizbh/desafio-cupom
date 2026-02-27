package com.challenge.coupon.presentation.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record CouponResponse(
    UUID id,
    String code,
    String description,
    BigDecimal discountValue,
    LocalDate expirationDate,
    boolean published,
    LocalDateTime createdAt
) {}
