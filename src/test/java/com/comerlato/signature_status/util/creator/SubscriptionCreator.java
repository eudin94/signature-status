package com.comerlato.signature_status.util.creator;

import com.comerlato.signature_status.dto.SubscriptionDTO;
import com.comerlato.signature_status.dto.SubscriptionRequestDTO;
import com.comerlato.signature_status.modules.entity.Subscription;

import static com.comerlato.signature_status.util.creator.StatusCreator.statusActive;
import static com.comerlato.signature_status.util.creator.StatusCreator.statusActiveDTO;
import static com.comerlato.signature_status.util.factory.PodamFactory.podam;
import static com.comerlato.signature_status.util.mapper.MapperConstants.subscriptionMapper;

public class SubscriptionCreator {

    public static final Subscription subscription = podam.manufacturePojo(Subscription.class)
            .withStatusId(statusActive.getId());

    public static final SubscriptionDTO subscriptionDTO = subscriptionMapper.buildSubscriptionDTO(subscription)
            .withStatus(statusActiveDTO);

    public static final SubscriptionRequestDTO subscriptionRequestDTO = podam.manufacturePojo(
            SubscriptionRequestDTO.class
    ).withId(subscription.getId());
}
