package com.challenge.coupon.adapter.out.persistence;

import com.challenge.coupon.adapter.out.persistence.mapper.CouponPersistenceMapper;
import com.challenge.coupon.domain.model.Coupon;
import com.challenge.coupon.domain.port.out.CouponRepositoryPort;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class CouponPersistenceAdapter implements CouponRepositoryPort {

    private final CouponJpaRepository couponJpaRepository;
    private final CouponPersistenceMapper couponPersistenceMapper;

    public CouponPersistenceAdapter(CouponJpaRepository couponJpaRepository, CouponPersistenceMapper couponPersistenceMapper) {
        this.couponJpaRepository = couponJpaRepository;
        this.couponPersistenceMapper = couponPersistenceMapper;
    }

    @Override
    public Coupon save(Coupon coupon) {
        return couponPersistenceMapper.toDomain(couponJpaRepository.save(couponPersistenceMapper.toEntity(coupon)));
    }

    @Override
    public Optional<Coupon> findById(UUID id) {
        return couponJpaRepository.findById(id).map(couponPersistenceMapper::toDomain);
    }

    @Override
    public Optional<Coupon> findByIdIncludingDeleted(UUID id) {
        return couponJpaRepository.findByIdIncludingDeleted(id).map(couponPersistenceMapper::toDomain);
    }
}
