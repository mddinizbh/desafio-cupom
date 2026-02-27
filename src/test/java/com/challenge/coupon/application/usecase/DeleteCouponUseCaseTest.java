package com.challenge.coupon.application.usecase;

import com.challenge.coupon.application.port.out.CouponRepositoryPort;
import com.challenge.coupon.domain.exception.CouponAlreadyDeletedException;
import com.challenge.coupon.domain.exception.CouponNotFoundException;
import com.challenge.coupon.domain.model.Coupon;
import org.junit.jupiter.api.DisplayName;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteCouponUseCaseTest {

    @Mock
    private CouponRepositoryPort repositoryPort;

    @InjectMocks
    private DeleteCouponUseCase deleteCouponUseCase;

    @Test
    @DisplayName("Should perform soft delete when coupon exists and is not deleted")
    void shouldPerformSoftDeleteWhenCouponExistsAndActive() {
        UUID id = UUID.randomUUID();
        Coupon coupon = new Coupon(id, "ABC123", "Desc", new BigDecimal("10.0"), 
                LocalDate.now().plusDays(1), false, LocalDateTime.now(), null);

        when(repositoryPort.findByIdIncludingDeleted(id)).thenReturn(Optional.of(coupon));
        when(repositoryPort.save(any(Coupon.class))).thenAnswer(invocation -> invocation.getArgument(0));

        deleteCouponUseCase.delete(id);

        verify(repositoryPort, times(1)).findByIdIncludingDeleted(id);
        verify(repositoryPort, times(1)).save(argThat(Coupon::isDeleted));
    }

    @Test
    @DisplayName("Should throw CouponNotFoundException when coupon does not exist")
    void shouldThrowExceptionWhenCouponNotFound() {
        UUID id = UUID.randomUUID();
        when(repositoryPort.findByIdIncludingDeleted(id)).thenReturn(Optional.empty());

        assertThrows(CouponNotFoundException.class, () -> deleteCouponUseCase.delete(id));
        verify(repositoryPort, never()).save(any());
    }

    @Test
    @DisplayName("Should throw CouponAlreadyDeletedException when coupon is already deleted")
    void shouldThrowExceptionWhenCouponAlreadyDeleted() {
        UUID id = UUID.randomUUID();
        Coupon coupon = new Coupon(id, "ABC123", "Desc", new BigDecimal("10.0"), 
                LocalDate.now().plusDays(1), false, LocalDateTime.now(), LocalDateTime.now());

        when(repositoryPort.findByIdIncludingDeleted(id)).thenReturn(Optional.of(coupon));

        assertThrows(CouponAlreadyDeletedException.class, () -> deleteCouponUseCase.delete(id));
        verify(repositoryPort, never()).save(any());
    }
}
