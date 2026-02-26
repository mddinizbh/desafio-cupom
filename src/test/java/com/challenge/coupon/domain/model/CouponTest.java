package com.challenge.coupon.domain.model;

import com.challenge.coupon.domain.exception.CouponAlreadyDeletedException;
import com.challenge.coupon.domain.exception.InvalidCouponException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class CouponTest {

    @Test
    void deveCriarCupomComDadosValidos() {
        Coupon coupon = Coupon.create("ABC123", "Desconto", new BigDecimal("10.00"), LocalDate.now().plusDays(1), false);

        assertNotNull(coupon.id());
        assertEquals("ABC123", coupon.code());
        assertFalse(coupon.isDeleted());
    }

    @Test
    void deveSanitizarCodigoRemovendoCaracteresEspeciais() {
        Coupon coupon = Coupon.create("AB#12!C3", "Desconto", new BigDecimal("10.00"), LocalDate.now().plusDays(1), false);

        assertEquals("AB12C3", coupon.code());
    }

    @Test
    void deveLancarInvalidCouponExceptionQuandoCodigoAposSanitizacaoMenorQue6() {
        assertThrows(InvalidCouponException.class,
                () -> Coupon.create("AB#1!C2", "Desconto", new BigDecimal("10.00"), LocalDate.now().plusDays(1), false));
    }

    @Test
    void deveLancarInvalidCouponExceptionQuandoDiscountValueMenorQuePontoCinco() {
        assertThrows(InvalidCouponException.class,
                () -> Coupon.create("ABC123", "Desconto", new BigDecimal("0.49"), LocalDate.now().plusDays(1), false));
    }

    @Test
    void deveLancarInvalidCouponExceptionQuandoExpirationDateNoPassado() {
        assertThrows(InvalidCouponException.class,
                () -> Coupon.create("ABC123", "Desconto", new BigDecimal("10.00"), LocalDate.now().minusDays(1), false));
    }

    @Test
    void deveCriarCupomPublicadoQuandoPublishedTrue() {
        Coupon coupon = Coupon.create("ABC123", "Desconto", new BigDecimal("10.00"), LocalDate.now().plusDays(1), true);

        assertTrue(coupon.published());
    }

    @Test
    void deveRealizarSoftDeleteCorretamente() {
        Coupon coupon = Coupon.create("ABC123", "Desconto", new BigDecimal("10.00"), LocalDate.now().plusDays(1), false);

        Coupon deletedCoupon = coupon.delete();

        assertNotNull(deletedCoupon.deletedAt());
    }

    @Test
    void deveLancarCouponAlreadyDeletedExceptionAoTentarDeletarNovamente() {
        Coupon coupon = Coupon.create("ABC123", "Desconto", new BigDecimal("10.00"), LocalDate.now().plusDays(1), false);
        Coupon deletedCoupon = coupon.delete();

        assertThrows(CouponAlreadyDeletedException.class, deletedCoupon::delete);
    }

    @Test
    void isDeletedDeveRetornarFalseParaCupomAtivo() {
        Coupon coupon = Coupon.create("ABC123", "Desconto", new BigDecimal("10.00"), LocalDate.now().plusDays(1), false);

        assertFalse(coupon.isDeleted());
    }

    @Test
    void isDeletedDeveRetornarTrueParaCupomDeletado() {
        Coupon coupon = Coupon.create("ABC123", "Desconto", new BigDecimal("10.00"), LocalDate.now().plusDays(1), false);
        Coupon deletedCoupon = coupon.delete();

        assertTrue(deletedCoupon.isDeleted());
    }
}
