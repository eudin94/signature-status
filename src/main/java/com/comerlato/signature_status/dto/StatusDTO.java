package com.comerlato.signature_status.dto;

import com.comerlato.signature_status.enums.StatusEnum;
import lombok.Builder;
import lombok.Value;
import lombok.With;
import lombok.extern.jackson.Jacksonized;

@Value
@With
@Jacksonized
@Builder
public class StatusDTO {

    Long id;
    StatusEnum name;

    @Override
    public String toString() {
        return "ID = " + id + " - NAME = " + name;
    }
}
