package com.comerlato.signature_status.util.mapper;

import com.comerlato.signature_status.dto.StatusDTO;
import com.comerlato.signature_status.modules.entity.Status;
import org.mapstruct.Mapper;

@Mapper
public interface StatusMapper {

    StatusDTO buildStatusDTO(Status status);
}