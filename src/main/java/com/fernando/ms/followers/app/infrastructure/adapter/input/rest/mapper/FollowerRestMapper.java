package com.fernando.ms.followers.app.infrastructure.adapter.input.rest.mapper;

import com.fernando.ms.followers.app.domain.models.Follower;
import com.fernando.ms.followers.app.infrastructure.adapter.input.rest.models.response.QuantityFollowerResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FollowerRestMapper {
    @Mapping(target = "quantity",expression = "java(mapQuantity(quantity))")
    QuantityFollowerResponse toQuantityFollowerResponse(Long quantity);

    default Long mapQuantity(Long quantity){
        return  quantity;
    }

    QuantityFollowerResponse toQuantityFollowerResponse(Follower follower);
}
