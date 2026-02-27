package com.challenge.coupon.infrastructure.persistence.jpa;

import com.challenge.coupon.application.port.out.CouponRepositoryPort;
import com.challenge.coupon.domain.model.Coupon;
import com.challenge.coupon.infrastructure.persistence.jpa.entity.CouponEntity;
import com.challenge.coupon.infrastructure.persistence.jpa.mapper.CouponPersistenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CouponPersistenceAdapter implements CouponRepositoryPort {

    private final CouponJpaRepository jpaRepository;
    private final CouponPersistenceMapper mapper;

    @Override
    public Coupon save(Coupon coupon) {
        CouponEntity entity = mapper.toEntity(coupon);
        CouponEntity savedEntity = jpaRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Coupon> findById(UUID id) {
        return jpaRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public Optional<Coupon> findByIdIncludingDeleted(UUID id) {
        return jpaRepository.findByIdIncludingDeleted(id)
                .map(mapper::toDomain);
    }
}
