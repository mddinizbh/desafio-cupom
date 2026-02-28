package com.challenge.coupon.infrastructure.persistence.jpa;

import com.challenge.coupon.infrastructure.persistence.jpa.entity.CouponEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CouponJpaRepository extends JpaRepository<CouponEntity, UUID> {
}
