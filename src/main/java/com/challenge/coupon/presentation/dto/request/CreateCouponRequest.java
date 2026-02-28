package com.challenge.coupon.presentation.dto.request;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CreateCouponRequest(
    @NotBlank(message = "Code is mandatory")
    @Pattern(regexp = "^[a-zA-Z0-9]{6}$", message = "Code must have exactly 6 alphanumeric characters")
    String code,
    
    @NotBlank(message = "Description is mandatory")
    String description,
    
    @NotNull(message = "Discount value is mandatory")
    BigDecimal discountValue,
    
    @NotNull(message = "Expiration date is mandatory")
    @FutureOrPresent(message = "Expiration date cannot be in the past")
    LocalDate expirationDate,
    
    boolean published
) {}
