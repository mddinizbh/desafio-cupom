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
    @DisplayName("Deve realizar soft delete quando cupom existir e não estiver deletado")
    void shouldPerformSoftDeleteWhenCouponExistsAndActive() {
        UUID id = UUID.randomUUID();
        Coupon coupon = new Coupon(id, "ABC123", "Desc", new BigDecimal("10.0"), 
                LocalDate.now().plusDays(1), false, LocalDateTime.now(), null);

        when(repositoryPort.findById(id)).thenReturn(Optional.of(coupon));
        when(repositoryPort.save(any(Coupon.class))).thenAnswer(invocation -> invocation.getArgument(0));

        deleteCouponUseCase.delete(id);

        verify(repositoryPort, times(1)).findById(id);
        verify(repositoryPort, times(1)).save(argThat(Coupon::isDeleted));
    }

    @Test
    @DisplayName("Deve lançar CouponNotFoundException quando cupom não existir")
    void shouldThrowExceptionWhenCouponNotFound() {
        UUID id = UUID.randomUUID();
        when(repositoryPort.findById(id)).thenReturn(Optional.empty());

        assertThrows(CouponNotFoundException.class, () -> deleteCouponUseCase.delete(id));
        verify(repositoryPort, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar CouponAlreadyDeletedException quando cupom já estiver deletado")
    void shouldThrowExceptionWhenCouponAlreadyDeleted() {
        UUID id = UUID.randomUUID();
        Coupon coupon = new Coupon(id, "ABC123", "Desc", new BigDecimal("10.0"), 
                LocalDate.now().plusDays(1), false, LocalDateTime.now(), LocalDateTime.now());

        when(repositoryPort.findById(id)).thenReturn(Optional.of(coupon));

        assertThrows(CouponAlreadyDeletedException.class, () -> deleteCouponUseCase.delete(id));
        verify(repositoryPort, never()).save(any());
    }
}
