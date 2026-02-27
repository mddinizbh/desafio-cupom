package com.challenge.coupon.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CreateCouponRequest(
    @NotBlank(message = "Code is mandatory")
    String code,
    
    @NotBlank(message = "Description is mandatory")
    String description,
    
    @NotNull(message = "Discount value is mandatory")
    BigDecimal discountValue,
    
    @NotNull(message = "Expiration date is mandatory")
    LocalDate expirationDate,
    
    boolean published
) {}
