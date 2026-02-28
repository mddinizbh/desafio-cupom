package com.challenge.coupon.application.usecase;

import com.challenge.coupon.application.port.out.CouponRepositoryPort;
import com.challenge.coupon.domain.model.Coupon;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateCouponUseCaseTest {

    @Mock
    private CouponRepositoryPort repositoryPort;

    @InjectMocks
    private CreateCouponUseCase createCouponUseCase;

    @Test
    @DisplayName("Should save and return coupon when creating with valid data")
    void shouldSaveAndReturnCouponWhenDataIsValid() {
        String code = "ABC123";
        String description = "Desc";
        BigDecimal discountValue = new BigDecimal("10.0");
        LocalDate expirationDate = LocalDate.now().plusDays(1);
        boolean published = false;

        when(repositoryPort.save(any(Coupon.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Coupon result = createCouponUseCase.create(code, description, discountValue, expirationDate, published);

        assertNotNull(result);
        assertEquals("ABC123", result.code());
        verify(repositoryPort, times(1)).save(any(Coupon.class));
    }
}
