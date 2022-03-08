package com.comerlato.signature_status.util.mapper;

import com.comerlato.signature_status.dto.EventHistoryDTO;
import com.comerlato.signature_status.dto.EventHistoryRequestDTO;
import com.comerlato.signature_status.modules.entity.EventHistory;
import org.mapstruct.Mapper;

@Mapper
public interface EventHistoryMapper {

    EventHistory buildEventHistory(EventHistoryRequestDTO requestDTO);

    EventHistoryDTO buildEventHistoryDTO(EventHistory eventHistory);
}