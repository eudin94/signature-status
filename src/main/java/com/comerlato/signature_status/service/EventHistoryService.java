package com.comerlato.signature_status.service;

import com.comerlato.signature_status.dto.EventHistoryDTO;
import com.comerlato.signature_status.dto.EventHistoryRequestDTO;
import com.comerlato.signature_status.dto.SubscriptionDTO;
import com.comerlato.signature_status.enums.EventTypeEnum;
import com.comerlato.signature_status.helper.MessageHelper;
import com.comerlato.signature_status.modules.entity.EventHistory;
import com.comerlato.signature_status.modules.repository.EventHistoryRepository;
import com.comerlato.signature_status.modules.repository.spec.EventHistorySpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static com.comerlato.signature_status.exception.ErrorCodeEnum.ERROR_EVENT_NOT_FOUND;
import static com.comerlato.signature_status.util.mapper.MapperConstants.eventHistoryMapper;
import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RequiredArgsConstructor
@Slf4j
@Service
public class EventHistoryService {

    private final EventHistoryRepository repository;
    private final MessageHelper messageHelper;

    public void create(final EventHistoryRequestDTO request) {
        repository.save(
                eventHistoryMapper.buildEventHistory(request)
                        .withSubscriptionId(request.getSubscriptionDTO().getId())
                        .withCreatedAt(now())
        );
    }

    public Page<EventHistoryDTO> findAll(final Optional<EventTypeEnum> type,
                                         final Optional<List<String>> subscriptionsIds,
                                         final Pageable pageable) {
        return repository.findAll(EventHistorySpecification.builder()
                .type(type)
                .subscriptionsIds(subscriptionsIds)
                .build(), pageable).map(eventHistoryMapper::buildEventHistoryDTO);
    }

    public EventHistoryDTO findDTOById(final Long id) {
        return eventHistoryMapper.buildEventHistoryDTO(findById(id));
    }

    public EventHistoryRequestDTO buildRequestDTO(final EventTypeEnum type,
                                                  final SubscriptionDTO subscriptionDTO) {
        return EventHistoryRequestDTO.builder()
                .type(type)
                .subscriptionDTO(subscriptionDTO)
                .build();
    }

    private EventHistory findById(final Long id) {
        return repository.findById(id).orElseThrow(() -> {
            log.error(messageHelper.get(ERROR_EVENT_NOT_FOUND, id.toString()));
            throw new ResponseStatusException(NOT_FOUND, messageHelper.get(ERROR_EVENT_NOT_FOUND, id.toString()));
        });
    }
}
