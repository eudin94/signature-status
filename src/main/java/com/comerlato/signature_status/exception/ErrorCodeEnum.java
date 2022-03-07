package com.comerlato.signature_status.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCodeEnum {

    ERROR_GENERIC_EXCEPTION("error.generic.exception"),
    ERROR_DUPLICATED_FIELD("error.duplicated.field"),
    ERROR_STATUS_ID_NOT_FOUND("error.status.id.not.found"),
    ERROR_STATUS_NAME_NOT_FOUND("error.status.name.not.found"),
    ERROR_SUBSCRIPTION_ALREADY_EXISTS("error.subscription.already.exists");

    private final String messageKey;
}
