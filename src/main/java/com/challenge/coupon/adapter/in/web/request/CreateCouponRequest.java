package com.challenge.coupon.adapter.in.web.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(description = "Payload para criação de cupom")
public record CreateCouponRequest(
        @NotBlank
        @Schema(example = "AB#C!D1")
        String code,

        @NotBlank
        @Schema(example = "Cupom de desconto 10%")
        String description,

        @NotNull
        @DecimalMin(value = "0.0", inclusive = true)
        @Schema(example = "10.00")
        BigDecimal discountValue,

        @NotNull
        @Schema(example = "2026-12-31")
        LocalDate expirationDate,

        @Schema(example = "false", defaultValue = "false")
        Boolean published
) {
}
