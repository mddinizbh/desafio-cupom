package com.challenge.coupon.application.service;

import com.challenge.coupon.domain.exception.CouponAlreadyDeletedException;
import com.challenge.coupon.domain.exception.CouponNotFoundException;
import com.challenge.coupon.domain.model.Coupon;
import com.challenge.coupon.domain.port.out.CouponRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeleteCouponServiceTest {

    @Mock
    private CouponRepositoryPort couponRepositoryPort;

    @InjectMocks
    private DeleteCouponService deleteCouponService;

    @Test
    void deveRealizarSoftDeleteQuandoCupomExistirENaoEstiverDeletado() {
        UUID id = UUID.randomUUID();
        Coupon coupon = new Coupon(id, "ABC123", "Desconto", new BigDecimal("10.00"), LocalDate.now().plusDays(1), false, LocalDateTime.now(), null);
        when(couponRepositoryPort.findByIdIncludingDeleted(id)).thenReturn(Optional.of(coupon));
        when(couponRepositoryPort.save(any(Coupon.class))).thenAnswer(invocation -> invocation.getArgument(0));

        deleteCouponService.delete(id);

        verify(couponRepositoryPort).save(any(Coupon.class));
    }

    @Test
    void deveLancarCouponNotFoundExceptionQuandoCupomNaoExistir() {
        UUID id = UUID.randomUUID();
        when(couponRepositoryPort.findByIdIncludingDeleted(id)).thenReturn(Optional.empty());

        assertThrows(CouponNotFoundException.class, () -> deleteCouponService.delete(id));
    }

    @Test
    void deveLancarCouponAlreadyDeletedExceptionQuandoCupomJaEstiverDeletado() {
        UUID id = UUID.randomUUID();
        Coupon coupon = new Coupon(id, "ABC123", "Desconto", new BigDecimal("10.00"), LocalDate.now().plusDays(1), false, LocalDateTime.now(), LocalDateTime.now());
        when(couponRepositoryPort.findByIdIncludingDeleted(id)).thenReturn(Optional.of(coupon));

        assertThrows(CouponAlreadyDeletedException.class, () -> deleteCouponService.delete(id));
    }
}
