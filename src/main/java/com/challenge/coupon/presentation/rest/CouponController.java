package com.challenge.coupon.presentation.rest;

import com.challenge.coupon.application.usecase.CreateCouponUseCase;
import com.challenge.coupon.application.usecase.DeleteCouponUseCase;
import com.challenge.coupon.domain.model.Coupon;
import com.challenge.coupon.presentation.dto.request.CreateCouponRequest;
import com.challenge.coupon.presentation.dto.response.CouponResponse;
import com.challenge.coupon.presentation.mapper.CouponWebMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/coupons")
@RequiredArgsConstructor
@Tag(name = "Coupons", description = "Coupon Management API")
public class CouponController {

    private final CreateCouponUseCase createCouponUseCase;
    private final DeleteCouponUseCase deleteCouponUseCase;
    private final CouponWebMapper mapper;

    @PostMapping
    @Operation(summary = "Creates a new coupon")
    @ApiResponse(responseCode = "201", description = "Coupon created successfully")
    @ApiResponse(responseCode = "400", description = "Invalid data (Bad Request)")
    @ApiResponse(responseCode = "422", description = "Business rule violation")
    public ResponseEntity<CouponResponse> create(@Valid @RequestBody CreateCouponRequest request) {
        Coupon coupon = createCouponUseCase.create(
                request.code(),
                request.description(),
                request.discountValue(),
                request.expirationDate(),
                request.published()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(coupon));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletes a coupon (Soft Delete)")
    @ApiResponse(responseCode = "204", description = "Coupon deleted successfully")
    @ApiResponse(responseCode = "404", description = "Coupon not found")
    @ApiResponse(responseCode = "409", description = "Coupon already deleted")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        deleteCouponUseCase.delete(id);
        return ResponseEntity.noContent().build();
    }
}
