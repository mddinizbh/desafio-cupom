package com.challenge.coupon.infrastructure.persistence.jpa;

import com.challenge.coupon.infrastructure.persistence.jpa.entity.CouponEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CouponJpaRepository extends JpaRepository<CouponEntity, UUID> {

    @Query(value = "SELECT * FROM coupons WHERE id = :id", nativeQuery = true)
    Optional<CouponEntity> findByIdIncludingDeleted(@Param("id") UUID id);
}
