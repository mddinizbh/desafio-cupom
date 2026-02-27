package com.challenge.coupon.domain.model;

import com.challenge.coupon.domain.exception.CouponAlreadyDeletedException;
import com.challenge.coupon.domain.exception.InvalidCouponException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class CouponTest {

    @Test
    @DisplayName("Deve criar cupom com dados válidos")
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
    @DisplayName("Deve sanitizar código removendo caracteres especiais")
    void shouldSanitizeCodeRemovingSpecialCharacters() {
        String codeWithSpecials = "A#B$C%1&2*3";
        Coupon coupon = Coupon.create(codeWithSpecials, "Desc", new BigDecimal("10.0"), LocalDate.now().plusDays(1), false);
        assertEquals("ABC123", coupon.code());
    }

    @Test
    @DisplayName("Deve lançar InvalidCouponException quando código após sanitização tiver menos de 6 chars")
    void shouldThrowExceptionWhenSanitizedCodeLengthIsInvalid() {
        String shortCode = "A#B$C%1"; // ABC1 -> 4 chars
        assertThrows(InvalidCouponException.class, () -> 
            Coupon.create(shortCode, "Desc", new BigDecimal("10.0"), LocalDate.now().plusDays(1), false));
    }

    @Test
    @DisplayName("Deve lançar InvalidCouponException quando discountValue for menor que 0.5")
    void shouldThrowExceptionWhenDiscountValueIsTooLow() {
        assertThrows(InvalidCouponException.class, () -> 
            Coupon.create("ABC123", "Desc", new BigDecimal("0.4"), LocalDate.now().plusDays(1), false));
    }

    @Test
    @DisplayName("Deve lançar InvalidCouponException quando expirationDate for no passado")
    void shouldThrowExceptionWhenExpirationDateIsInPast() {
        assertThrows(InvalidCouponException.class, () -> 
            Coupon.create("ABC123", "Desc", new BigDecimal("10.0"), LocalDate.now().minusDays(1), false));
    }

    @Test
    @DisplayName("Deve criar cupom já publicado quando published=true")
    void shouldCreatePublishedCoupon() {
        Coupon coupon = Coupon.create("ABC123", "Desc", new BigDecimal("10.0"), LocalDate.now().plusDays(1), true);
        assertTrue(coupon.published());
    }

    @Test
    @DisplayName("Deve realizar soft delete corretamente (retornando nova instância com deletedAt não nulo)")
    void shouldPerformSoftDeleteCorrectly() {
        Coupon coupon = Coupon.create("ABC123", "Desc", new BigDecimal("10.0"), LocalDate.now().plusDays(1), false);
        Coupon deletedCoupon = coupon.delete();
        
        assertNotNull(deletedCoupon.deletedAt());
        assertTrue(deletedCoupon.isDeleted());
        assertNull(coupon.deletedAt());
        assertFalse(coupon.isDeleted());
    }

    @Test
    @DisplayName("Deve lançar CouponAlreadyDeletedException ao tentar deletar cupom já deletado")
    void shouldThrowExceptionWhenDeletingAlreadyDeletedCoupon() {
        Coupon coupon = Coupon.create("ABC123", "Desc", new BigDecimal("10.0"), LocalDate.now().plusDays(1), false);
        Coupon deletedCoupon = coupon.delete();
        assertThrows(CouponAlreadyDeletedException.class, deletedCoupon::delete);
    }

    @Test
    @DisplayName("isDeleted() deve retornar false para cupom ativo")
    void isDeletedShouldReturnFalseForActiveCoupon() {
        Coupon coupon = Coupon.create("ABC123", "Desc", new BigDecimal("10.0"), LocalDate.now().plusDays(1), false);
        assertFalse(coupon.isDeleted());
    }

    @Test
    @DisplayName("isDeleted() deve retornar true para cupom deletado")
    void isDeletedShouldReturnTrueForDeletedCoupon() {
        Coupon coupon = Coupon.create("ABC123", "Desc", new BigDecimal("10.0"), LocalDate.now().plusDays(1), false);
        Coupon deletedCoupon = coupon.delete();
        assertTrue(deletedCoupon.isDeleted());
    }
}
