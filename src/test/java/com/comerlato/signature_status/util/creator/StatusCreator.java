package com.comerlato.signature_status.util.creator;

import com.comerlato.signature_status.dto.StatusDTO;
import com.comerlato.signature_status.modules.entity.Status;

import static com.comerlato.signature_status.enums.StatusEnum.ACTIVE;
import static com.comerlato.signature_status.enums.StatusEnum.INACTIVE;
import static com.comerlato.signature_status.util.factory.PodamFactory.podam;
import static com.comerlato.signature_status.util.mapper.MapperConstants.statusMapper;

public class StatusCreator {

    public static final Status statusActive = podam.manufacturePojo(Status.class).withName(ACTIVE);

    public static final Status statusInactive = podam.manufacturePojo(Status.class).withName(INACTIVE);

    public static final StatusDTO statusActiveDTO = statusMapper.buildStatusDTO(statusActive);
}
