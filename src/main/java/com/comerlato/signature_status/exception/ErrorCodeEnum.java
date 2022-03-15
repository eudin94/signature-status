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
    ERROR_SUBSCRIPTION_ALREADY_EXISTS("error.subscription.already.exists"),
    ERROR_SUBSCRIPTION_NOT_FOUND("error.subscription.not.found"),
    ERROR_EVENT_NOT_FOUND("error.event.not.found"),
    ERROR_UNCHANGED_STATUS("error.unchanged.status"),
    ERROR_MESSAGE_QUEUE_CONNECTION("error.message.queue.connection"),
    ERROR_MESSAGE_QUEUE_URI("error.message.queue.uri"),
    ERROR_MESSAGE_QUEUE_SEND("error.message.queue.send"),
    ERROR_UPLOADING_FILE("error.uploading.file"),
    ERROR_INVALID_FILE_FORMAT("error.invalid.file.format"),
    ERROR_MANAGING_SUBSCRIPTION("error.managing.subscription");

    private final String messageKey;
}
