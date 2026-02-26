package com.challenge.coupon.adapter.out.persistence;

import com.challenge.coupon.adapter.out.persistence.entity.CouponEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface CouponJpaRepository extends JpaRepository<CouponEntity, UUID> {

    @Query(value = "SELECT * FROM coupons c WHERE c.id = :id", nativeQuery = true)
    Optional<CouponEntity> findByIdIncludingDeleted(@Param("id") UUID id);
}
