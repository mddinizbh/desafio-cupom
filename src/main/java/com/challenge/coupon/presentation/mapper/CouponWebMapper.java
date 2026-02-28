package com.challenge.coupon.presentation.mapper;

import com.challenge.coupon.domain.model.Coupon;
import com.challenge.coupon.presentation.dto.response.CouponResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CouponWebMapper {
    @Mapping(target = "expirationDate", expression = "java(coupon.expirationDate() != null ? coupon.expirationDate().atStartOfDay() : null)")
    CouponResponse toResponse(Coupon coupon);
}
