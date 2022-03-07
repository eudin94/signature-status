package com.comerlato.signature_status.util.mapper;

import com.comerlato.signature_status.dto.SubscriptionDTO;
import com.comerlato.signature_status.modules.entity.Subscription;
import org.mapstruct.Mapper;

@Mapper
public interface SubscriptionMapper {

    SubscriptionDTO buildSubscriptionDTO(Subscription subscription);
}