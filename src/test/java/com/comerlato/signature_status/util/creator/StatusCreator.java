package com.comerlato.signature_status.util.creator;

import com.comerlato.signature_status.dto.StatusDTO;
import com.comerlato.signature_status.modules.entity.Status;

import static com.comerlato.signature_status.util.factory.PodamFactory.podam;
import static com.comerlato.signature_status.util.mapper.MapperConstants.statusMapper;

public class StatusCreator {

    public static final Status status = podam.manufacturePojo(Status.class);

    public static final StatusDTO statusDTO = statusMapper.buildStatusDTO(status);
}
