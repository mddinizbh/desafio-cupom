package com.challenge.coupon.domain.model;

import com.challenge.coupon.domain.exception.CouponAlreadyDeletedException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CouponTest {

    @Test
    @DisplayName("Should create coupon with valid data")
    void shouldCreateCouponWithValidData() {
        String code = "ABC123";
        String description = "Test Coupon";
        BigDecimal discountValue = new BigDecimal("10.0");
        LocalDate expirationDate = LocalDate.now().plusDays(10);
        boolean published = true;

        Coupon coupon = Coupon.create(code, description, discountValue, expirationDate, published);

        assertNotNull(coupon);
        assertEquals(code, coupon.code());
        assertEquals(description, coupon.description());
        assertEquals(discountValue, coupon.discountValue());
        assertEquals(expirationDate, coupon.expirationDate());
        assertTrue(coupon.published());
        assertNotNull(coupon.createdAt());
        assertNull(coupon.deletedAt());
    }

    @Test
    @DisplayName("Should apply default discount value of 0.5 when discountValue is less than 0.5")
    void shouldApplyDefaultDiscountValueWhenTooLow() {
        Coupon coupon = Coupon.create("ABC123", "Desc", new BigDecimal("0.4"), LocalDate.now().plusDays(1), false);
        assertEquals(new BigDecimal("0.5"), coupon.discountValue());
    }

    @Test
    @DisplayName("Should allow instantiation with past date (for rehydration from DB)")
    void shouldAllowInstantiationWithPastDate() {
        LocalDate pastDate = LocalDate.now().minusDays(1);
        Coupon coupon = new Coupon(
            UUID.randomUUID(),
            "ABC123",
            "Desc",
            new BigDecimal("10.0"),
            pastDate,
            true,
            LocalDateTime.now().minusDays(1),
            null
        );
        
        assertEquals(pastDate, coupon.expirationDate());
    }

    @Test
    @DisplayName("Should create published coupon when published is true")
    void shouldCreatePublishedCoupon() {
        Coupon coupon = Coupon.create("ABC123", "Desc", new BigDecimal("10.0"), LocalDate.now().plusDays(1), true);
        assertTrue(coupon.published());
    }

    @Test
    @DisplayName("Should perform soft delete correctly (returning new instance with non-null deletedAt)")
    void shouldPerformSoftDeleteCorrectly() {
        Coupon coupon = Coupon.create("ABC123", "Desc", new BigDecimal("10.0"), LocalDate.now().plusDays(1), false);
        Coupon deletedCoupon = coupon.delete();
        
        assertNotNull(deletedCoupon.deletedAt());
        assertTrue(deletedCoupon.isDeleted());
        assertNull(coupon.deletedAt());
        assertFalse(coupon.isDeleted());
    }

    @Test
    @DisplayName("Should throw CouponAlreadyDeletedException when trying to delete an already deleted coupon")
    void shouldThrowExceptionWhenDeletingAlreadyDeletedCoupon() {
        Coupon coupon = Coupon.create("ABC123", "Desc", new BigDecimal("10.0"), LocalDate.now().plusDays(1), false);
        Coupon deletedCoupon = coupon.delete();
        assertThrows(CouponAlreadyDeletedException.class, deletedCoupon::delete);
    }

    @Test
    @DisplayName("isDeleted() should return false for active coupon")
    void isDeletedShouldReturnFalseForActiveCoupon() {
        Coupon coupon = Coupon.create("ABC123", "Desc", new BigDecimal("10.0"), LocalDate.now().plusDays(1), false);
        assertFalse(coupon.isDeleted());
    }

    @Test
    @DisplayName("isDeleted() should return true for deleted coupon")
    void isDeletedShouldReturnTrueForDeletedCoupon() {
        Coupon coupon = Coupon.create("ABC123", "Desc", new BigDecimal("10.0"), LocalDate.now().plusDays(1), false);
        Coupon deletedCoupon = coupon.delete();
        assertTrue(deletedCoupon.isDeleted());
    }
}
