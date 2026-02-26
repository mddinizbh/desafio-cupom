package com.challenge.coupon.adapter.in.web.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "Resposta de cupom")
public record CouponResponse(
        @Schema(example = "8f4569d9-d66c-458e-bb3a-36fcd17ee786")
        UUID id,

        @Schema(example = "ABCD12")
        String code,

        @Schema(example = "Cupom de desconto 10%")
        String description,

        @Schema(example = "10.00")
        BigDecimal discountValue,

        @Schema(example = "2026-12-31")
        LocalDate expirationDate,

        @Schema(example = "false")
        boolean published,

        @Schema(example = "2024-01-15T10:30:00")
        LocalDateTime createdAt
) {
}
