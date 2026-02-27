package com.challenge.coupon.presentation.mapper;

import com.challenge.coupon.domain.model.Coupon;
import com.challenge.coupon.presentation.dto.response.CouponResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CouponWebMapper {
    CouponResponse toResponse(Coupon coupon);
}
