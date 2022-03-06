package com.comerlato.signature_status.enums;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Getter;

@JsonSerialize(using = EnumSerializer.class)
@AllArgsConstructor
@Getter
public enum StatusEnum implements EnumDescription {

    ACTIVE("Assinatura ativa"),
    INACTIVE("Assinatura inativa");

    private final String description;
}
