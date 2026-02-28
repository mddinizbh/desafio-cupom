package com.challenge.coupon.presentation.rest;

import com.challenge.coupon.infrastructure.persistence.jpa.CouponJpaRepository;
import com.challenge.coupon.infrastructure.persistence.jpa.entity.CouponEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
    private CouponJpaRepository repository;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
    }

    @Test
    @DisplayName("201 when creating valid coupon")
    void shouldCreateValidCoupon() throws Exception {
        Map<String, Object> request = new HashMap<>();
        request.put("code", "ABC123");
        request.put("description", "Desc");
        request.put("discountValue", 10.0);
        request.put("expirationDate", LocalDate.now().plusDays(10).toString());
        request.put("published", false);

        mockMvc.perform(post("/api/v1/coupons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.code", is("ABC123")));
    }

    @Test
    @DisplayName("400 when creating coupon with special characters (no longer supported/sanitized)")
    void shouldReturn400WhenSpecialCharsInCode() throws Exception {
        Map<String, Object> request = new HashMap<>();
        request.put("code", "A#B$C%1&2*3");
        request.put("description", "Desc");
        request.put("discountValue", 10.0);
        request.put("expirationDate", LocalDate.now().plusDays(10).toString());
        request.put("published", false);

        mockMvc.perform(post("/api/v1/coupons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail", is("One or more fields have invalid values")))
                .andExpect(jsonPath("$.errors[0].field", is("code")));
    }

    @Test
    @DisplayName("400 when missing mandatory field")
    void shouldReturn400WhenFieldMissing() throws Exception {
        Map<String, Object> request = new HashMap<>();
        request.put("description", "Desc");

        mockMvc.perform(post("/api/v1/coupons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail", is("One or more fields have invalid values")));
    }

    @Test
    @DisplayName("201 when discountValue is below 0.5 (applies default 0.5)")
    void shouldReturn201WhenDiscountTooLowAndApplyDefault() throws Exception {
        Map<String, Object> request = new HashMap<>();
        request.put("code", "ABC123");
        request.put("description", "Desc");
        request.put("discountValue", 0.4);
        request.put("expirationDate", LocalDate.now().plusDays(10).toString());

        mockMvc.perform(post("/api/v1/coupons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.discountValue", is(0.5)));
    }

    @Test
    @DisplayName("400 when expirationDate is in the past")
    void shouldReturn400WhenExpirationInPast() throws Exception {
        Map<String, Object> request = new HashMap<>();
        request.put("code", "ABC123");
        request.put("description", "Desc");
        request.put("discountValue", 10.0);
        request.put("expirationDate", LocalDate.now().minusDays(1).toString());

        mockMvc.perform(post("/api/v1/coupons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail", is("One or more fields have invalid values")));
    }

    @Test
    @DisplayName("400 when code has invalid format (length or characters)")
    void shouldReturn400WhenCodeInvalidFormat() throws Exception {
        Map<String, Object> request = new HashMap<>();
        request.put("code", "ABC1"); // Too short
        request.put("description", "Desc");
        request.put("discountValue", 10.0);
        request.put("expirationDate", LocalDate.now().plusDays(10).toString());

        mockMvc.perform(post("/api/v1/coupons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail", is("One or more fields have invalid values")));
    }

    @Test
    @DisplayName("200 when getting existing coupon by ID")
    void shouldGetExistingCouponById() throws Exception {
        CouponEntity entity = CouponEntity.builder()
                .code("ABC123")
                .description("Test Description")
                .discountValue(new BigDecimal("10.00"))
                .expirationDate(LocalDate.now().plusDays(5))
                .createdAt(LocalDateTime.now())
                .published(true)
                .build();
        CouponEntity saved = repository.save(entity);

        mockMvc.perform(get("/api/v1/coupons/" + saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(saved.getId().toString())))
                .andExpect(jsonPath("$.code", is("ABC123")))
                .andExpect(jsonPath("$.status", is("ACTIVE")))
                .andExpect(jsonPath("$.published", is(true)))
                .andExpect(jsonPath("$.redeemed", is(false)));
    }

    @Test
    @DisplayName("200 when getting deleted coupon by ID")
    void shouldGetDeletedCouponById() throws Exception {
        CouponEntity entity = CouponEntity.builder()
                .code("DEL123")
                .description("Deleted Description")
                .discountValue(new BigDecimal("15.00"))
                .expirationDate(LocalDate.now().plusDays(5))
                .createdAt(LocalDateTime.now())
                .deletedAt(LocalDateTime.now())
                .published(true)
                .build();
        CouponEntity saved = repository.save(entity);

        mockMvc.perform(get("/api/v1/coupons/" + saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("DELETED")))
                .andExpect(jsonPath("$.redeemed", is(true)));
    }

    @Test
    @DisplayName("404 when getting inexistent coupon")
    void shouldReturn404WhenGettingInexistent() throws Exception {
        mockMvc.perform(get("/api/v1/coupons/" + UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("204 when deleting existing coupon")
    void shouldDeleteExistingCoupon() throws Exception {
        CouponEntity entity = CouponEntity.builder()
                .code("ABC123")
                .description("Desc")
                .discountValue(new BigDecimal("10.0"))
                .expirationDate(LocalDate.now().plusDays(1))
                .createdAt(LocalDateTime.now())
                .published(true)
                .build();
        CouponEntity saved = repository.save(entity);

        mockMvc.perform(delete("/api/v1/coupons/" + saved.getId()))
                .andExpect(status().isNoContent());

        // Verifica soft delete no banco (agora deve estar presente mas com deletedAt preenchido)
        assertTrue(repository.findById(saved.getId()).isPresent());
        assertTrue(repository.findById(saved.getId()).get().getDeletedAt() != null);
    }

    @Test
    @DisplayName("404 when trying to delete inexistent coupon")
    void shouldReturn404WhenDeletingInexistent() throws Exception {
        mockMvc.perform(delete("/api/v1/coupons/" + UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("409 when trying to delete already deleted coupon")
    void shouldReturn409WhenAlreadyDeleted() throws Exception {
        CouponEntity entity = CouponEntity.builder()
                .code("ABC123")
                .description("Desc")
                .discountValue(new BigDecimal("10.0"))
                .expirationDate(LocalDate.now().plusDays(1))
                .createdAt(LocalDateTime.now())
                .deletedAt(LocalDateTime.now()) // Already deleted
                .published(true)
                .build();
        
        CouponEntity saved = repository.save(entity);

        mockMvc.perform(delete("/api/v1/coupons/" + saved.getId()))
                .andExpect(status().isConflict()) // Should be 409
                .andExpect(jsonPath("$.detail", is("Coupon has already been deleted")));
    }

    @Test
    @DisplayName("400 when UUID is malformed in path variable")
    void shouldReturn400WhenUuidIsMalformed() throws Exception {
        mockMvc.perform(delete("/api/v1/coupons/not-a-uuid"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(400)));
    }

    @Test
    @DisplayName("400 when JSON is malformed")
    void shouldReturn400WhenJsonIsMalformed() throws Exception {
        mockMvc.perform(post("/api/v1/coupons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"code\": \"ABC123\", \"invalid\" }"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(400)));
    }
}
