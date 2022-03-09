package com.comerlato.signature_status.util.creator;

import com.comerlato.signature_status.dto.EventHistoryDTO;
import com.comerlato.signature_status.dto.EventHistoryRequestDTO;
import com.comerlato.signature_status.modules.entity.EventHistory;

import static com.comerlato.signature_status.util.creator.SubscriptionCreator.subscription;
import static com.comerlato.signature_status.util.creator.SubscriptionCreator.subscriptionDTO;
import static com.comerlato.signature_status.util.factory.PodamFactory.podam;
import static com.comerlato.signature_status.util.mapper.MapperConstants.eventHistoryMapper;

public class EventHistoryCreator {

    public static final EventHistory eventHistory = podam.manufacturePojo(EventHistory.class)
            .withSubscriptionId(subscription.getId());

    public static final EventHistoryDTO eventHistoryDTO = eventHistoryMapper.buildEventHistoryDTO(eventHistory);

    public static final EventHistoryRequestDTO eventHistoryRequestDTO = EventHistoryRequestDTO.builder()
            .type(eventHistory.getType())
            .subscriptionDTO(subscriptionDTO)
            .build();
}
