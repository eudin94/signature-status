package com.comerlato.signature_status.enums;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Getter;

@JsonSerialize(using = EnumSerializer.class)
@AllArgsConstructor
@Getter
public enum EventTypeEnum implements EnumDescription {

    SUBSCRIPTION_PURCHASED("A Compra foi realizada e a assinatura deve estar com status ativa"),
    SUBSCRIPTION_CANCELED("A Compra foi cancelada e a assinatura deve estar com status cancelada"),
    SUBSCRIPTION_RESTARTED("A Compra foi recuperada e a assinatura deve estar com status ativa");

    private final String description;
}
