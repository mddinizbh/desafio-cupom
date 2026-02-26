package com.challenge.coupon.application.service;

import com.challenge.coupon.domain.exception.InvalidCouponException;
import com.challenge.coupon.domain.model.Coupon;
import com.challenge.coupon.domain.port.out.CouponRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateCouponServiceTest {

    @Mock
    private CouponRepositoryPort couponRepositoryPort;

    @InjectMocks
    private CreateCouponService createCouponService;

    @Test
    void deveSalvarERetornarCupomAoCriarComDadosValidos() {
        when(couponRepositoryPort.save(any(Coupon.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Coupon response = createCouponService.create("ABC123", "Desconto", new BigDecimal("10.00"), LocalDate.now().plusDays(1), false);

        assertEquals("ABC123", response.code());
        verify(couponRepositoryPort).save(any(Coupon.class));
    }

    @Test
    void devePropagarExcecaoQuandoDominioLancarInvalidCouponException() {
        assertThrows(InvalidCouponException.class,
                () -> createCouponService.create("A@1", "Desconto", new BigDecimal("10.00"), LocalDate.now().plusDays(1), false));
    }
}
