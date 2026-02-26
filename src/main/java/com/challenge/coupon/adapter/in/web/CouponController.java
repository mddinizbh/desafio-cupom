package com.challenge.coupon.adapter.in.web;

import com.challenge.coupon.adapter.in.web.request.CreateCouponRequest;
import com.challenge.coupon.adapter.in.web.response.CouponResponse;
import com.challenge.coupon.adapter.in.web.response.ErrorResponse;
import com.challenge.coupon.domain.model.Coupon;
import com.challenge.coupon.domain.port.in.CreateCouponUseCase;
import com.challenge.coupon.domain.port.in.DeleteCouponUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/coupons")
@Tag(name = "Coupons")
public class CouponController {

    private final CreateCouponUseCase createCouponUseCase;
    private final DeleteCouponUseCase deleteCouponUseCase;

    public CouponController(CreateCouponUseCase createCouponUseCase, DeleteCouponUseCase deleteCouponUseCase) {
        this.createCouponUseCase = createCouponUseCase;
        this.deleteCouponUseCase = deleteCouponUseCase;
    }

    @PostMapping
    @Operation(summary = "Criar cupom")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Cupom criado com sucesso",
                    content = @Content(schema = @Schema(implementation = CouponResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "422", description = "Unprocessable Entity",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = "{\"status\":422,\"error\":\"Unprocessable Entity\",\"message\":\"mensagem descritiva do erro\",\"timestamp\":\"2024-01-15T10:30:00\"}")))
    })
    public ResponseEntity<CouponResponse> create(@Valid @RequestBody CreateCouponRequest request) {
        Coupon coupon = createCouponUseCase.create(
                request.code(),
                request.description(),
                request.discountValue(),
                request.expirationDate(),
                request.published()
        );

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(coupon.id())
                .toUri();

        CouponResponse response = new CouponResponse(
                coupon.id(),
                coupon.code(),
                coupon.description(),
                coupon.discountValue(),
                coupon.expirationDate(),
                coupon.published(),
                coupon.createdAt()
        );

        return ResponseEntity.created(location).body(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar cupom (soft delete)")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Cupom deletado"),
            @ApiResponse(responseCode = "404", description = "Cupom não encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Cupom já deletado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        deleteCouponUseCase.delete(id);
        return ResponseEntity.noContent().build();
    }
}
