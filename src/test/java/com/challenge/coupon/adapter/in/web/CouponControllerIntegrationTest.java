package com.challenge.coupon.adapter.in.web;

import com.challenge.coupon.adapter.out.persistence.CouponJpaRepository;
import com.challenge.coupon.adapter.out.persistence.entity.CouponEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CouponControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CouponJpaRepository couponJpaRepository;

    @BeforeEach
    void setup() {
        couponJpaRepository.deleteAll();
    }

    @Test
    void postDeveRetornar201AoCriarCupomValido() throws Exception {
        Map<String, Object> payload = Map.of(
                "code", "ABC123",
                "description", "Cupom de desconto",
                "discountValue", 10.00,
                "expirationDate", LocalDate.now().plusDays(5).toString(),
                "published", false
        );

        mockMvc.perform(post("/api/v1/coupons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.code").value("ABC123"));
    }

    @Test
    void postDeveRetornar201AoCriarCupomComCaracteresEspeciaisNoCodigo() throws Exception {
        Map<String, Object> payload = Map.of(
                "code", "AB#12!C3",
                "description", "Cupom de desconto",
                "discountValue", 10.00,
                "expirationDate", LocalDate.now().plusDays(5).toString(),
                "published", false
        );

        mockMvc.perform(post("/api/v1/coupons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.code").value("AB12C3"));
    }

    @Test
    void postDeveRetornar400AoOmitirCampoObrigatorio() throws Exception {
        Map<String, Object> payload = Map.of(
                "code", "ABC123",
                "discountValue", 10.00,
                "expirationDate", LocalDate.now().plusDays(5).toString()
        );

        mockMvc.perform(post("/api/v1/coupons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void postDeveRetornar422AoInformarDiscountValueAbaixoDePontoCinco() throws Exception {
        Map<String, Object> payload = Map.of(
                "code", "ABC123",
                "description", "Cupom de desconto",
                "discountValue", 0.49,
                "expirationDate", LocalDate.now().plusDays(5).toString(),
                "published", false
        );

        mockMvc.perform(post("/api/v1/coupons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void postDeveRetornar422AoInformarExpirationDateNoPassado() throws Exception {
        Map<String, Object> payload = Map.of(
                "code", "ABC123",
                "description", "Cupom de desconto",
                "discountValue", 10.00,
                "expirationDate", LocalDate.now().minusDays(1).toString(),
                "published", false
        );

        mockMvc.perform(post("/api/v1/coupons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void postDeveRetornar422AoCodigoResultarEmMenosDe6CharsAposSanitizacao() throws Exception {
        Map<String, Object> payload = Map.of(
                "code", "AB#1!C2",
                "description", "Cupom de desconto",
                "discountValue", 10.00,
                "expirationDate", LocalDate.now().plusDays(5).toString(),
                "published", false
        );

        mockMvc.perform(post("/api/v1/coupons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void deleteDeveRetornar204AoDeletarCupomExistente() throws Exception {
        CouponEntity entity = couponJpaRepository.save(CouponEntity.builder()
                .id(UUID.randomUUID())
                .code("ABC123")
                .description("Cupom")
                .discountValue(new BigDecimal("10.00"))
                .expirationDate(LocalDate.now().plusDays(1))
                .published(false)
                .createdAt(LocalDateTime.now())
                .deletedAt(null)
                .build());

        mockMvc.perform(delete("/api/v1/coupons/{id}", entity.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteDeveRetornar404AoTentarDeletarCupomInexistente() throws Exception {
        mockMvc.perform(delete("/api/v1/coupons/{id}", UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteDeveRetornar409AoTentarDeletarCupomJaDeletado() throws Exception {
        UUID id = UUID.randomUUID();
        couponJpaRepository.save(CouponEntity.builder()
                .id(id)
                .code("ABC123")
                .description("Cupom")
                .discountValue(new BigDecimal("10.00"))
                .expirationDate(LocalDate.now().plusDays(1))
                .published(false)
                .createdAt(LocalDateTime.now())
                .deletedAt(LocalDateTime.now())
                .build());

        mockMvc.perform(delete("/api/v1/coupons/{id}", id))
                .andExpect(status().isConflict());
    }
}
