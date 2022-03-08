package com.comerlato.signature_status.dto;

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

    String id;
    StatusDTO status;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;

    @Override
    public String toString() {
        return "Subscription:\n" +
                "{\n" +
                "   ID = '" + id + "'\n" +
                "   STATUS = '" + status + "'\n" +
                "   CREATED AT = '" + createdAt + "'\n" +
                "   UPDATED AT = '" + updatedAt + "'\n" +
                "}\n";
    }
}
