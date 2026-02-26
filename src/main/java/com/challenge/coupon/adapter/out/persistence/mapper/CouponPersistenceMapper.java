package com.challenge.coupon.adapter.out.persistence.mapper;

import com.challenge.coupon.adapter.out.persistence.entity.CouponEntity;
import com.challenge.coupon.domain.model.Coupon;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CouponPersistenceMapper {
    CouponEntity toEntity(Coupon coupon);
    Coupon toDomain(CouponEntity entity);
}
