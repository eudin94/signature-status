package com.comerlato.signature_status.dto;

import com.comerlato.signature_status.enums.EventTypeEnum;
import lombok.Builder;
import lombok.Value;
import lombok.With;
import lombok.extern.jackson.Jacksonized;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Value
@With
@Jacksonized
@Builder
public class EventHistoryCreateRequestDTO {

    @NotNull
    EventTypeEnum type;
    @NotBlank
    String subscriptionId;
}
