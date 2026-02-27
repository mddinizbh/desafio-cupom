package com.challenge.coupon.infrastructure.persistence.jpa.mapper;

import com.challenge.coupon.domain.model.Coupon;
import com.challenge.coupon.infrastructure.persistence.jpa.entity.CouponEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CouponPersistenceMapper {

    Coupon toDomain(CouponEntity entity);
    CouponEntity toEntity(Coupon domain);
}
