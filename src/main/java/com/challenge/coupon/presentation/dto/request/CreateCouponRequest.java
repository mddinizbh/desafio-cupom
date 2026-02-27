package com.challenge.coupon.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CreateCouponRequest(
    @NotBlank(message = "O código é obrigatório")
    String code,
    
    @NotBlank(message = "A descrição é obrigatória")
    String description,
    
    @NotNull(message = "O valor de desconto é obrigatório")
    BigDecimal discountValue,
    
    @NotNull(message = "A data de expiração é obrigatória")
    LocalDate expirationDate,
    
    boolean published
) {}
