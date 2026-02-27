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
@Tag(name = "Coupons", description = "API de gerenciamento de cupons de desconto")
public class CouponController {

    private final CreateCouponUseCase createCouponUseCase;
    private final DeleteCouponUseCase deleteCouponUseCase;
    private final CouponWebMapper mapper;

    @PostMapping
    @Operation(summary = "Cria um novo cupom")
    @ApiResponse(responseCode = "201", description = "Cupom criado com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos (Bad Request)")
    @ApiResponse(responseCode = "422", description = "Violação de regra de negócio")
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
    @Operation(summary = "Exclui um cupom (Soft Delete)")
    @ApiResponse(responseCode = "204", description = "Cupom excluído com sucesso")
    @ApiResponse(responseCode = "404", description = "Cupom não encontrado")
    @ApiResponse(responseCode = "409", description = "Cupom já excluído")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        deleteCouponUseCase.delete(id);
        return ResponseEntity.noContent().build();
    }
}
