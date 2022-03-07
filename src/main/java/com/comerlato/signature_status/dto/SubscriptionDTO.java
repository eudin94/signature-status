package com.comerlato.signature_status.dto;

import com.comerlato.signature_status.modules.entity.Status;
import lombok.Builder;
import lombok.Value;
import lombok.With;
import lombok.extern.jackson.Jacksonized;

import java.time.LocalDateTime;

@Value
@With
@Jacksonized
@Builder
public class SubscriptionDTO {

    String Id;
    Status status;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
