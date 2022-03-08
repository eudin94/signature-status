package com.comerlato.signature_status.dto;

import com.comerlato.signature_status.enums.EventTypeEnum;
import lombok.Builder;
import lombok.Value;
import lombok.With;
import lombok.extern.jackson.Jacksonized;

import java.time.LocalDateTime;

@Value
@With
@Jacksonized
@Builder
public class EventHistoryDTO {

    Long id;
    EventTypeEnum type;
    Long subscriptionId;
    LocalDateTime createdAt;
}
