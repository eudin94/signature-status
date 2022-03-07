package com.comerlato.signature_status.service;

import com.comerlato.signature_status.dto.EventHistoryCreateRequestDTO;
import com.comerlato.signature_status.dto.EventHistoryDTO;
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

import java.util.List;
import java.util.Optional;

import static com.comerlato.signature_status.util.mapper.MapperConstants.eventHistoryMapper;
import static java.time.LocalDateTime.now;

@RequiredArgsConstructor
@Slf4j
@Service
public class EventHistoryService {

    private final EventHistoryRepository repository;
    private final SubscriptionService subscriptionService;
    private final MessageHelper messageHelper;

    public EventHistoryDTO create(final EventHistoryCreateRequestDTO request) {
        final var subscription = subscriptionService.findDTOById(request.getSubscriptionId());
        final var savedEvent = repository.save(eventHistoryMapper.buildEventHistory(request)
                .withCreatedAt(now()));
        // TODO IMPLEMENTAR MENSAGERIA
        return eventHistoryMapper.buildEventHistoryDTO(savedEvent)
                .withSubscription(subscription);
    }

    public Page<EventHistoryDTO> findAll(final Optional<EventTypeEnum> type,
                                         final Optional<List<Long>> subscriptionsIds,
                                         final Pageable pageable) {
        return repository.findAll(EventHistorySpecification.builder()
                .type(type)
                .subscriptionsIds(subscriptionsIds)
                .build(), pageable).map(this::buildEventHistoryDTO);
    }

    private EventHistoryDTO buildEventHistoryDTO(final EventHistory eventHistory) {
        final var subscription = subscriptionService.findDTOById(eventHistory.getSubscriptionId());
        return eventHistoryMapper.buildEventHistoryDTO(eventHistory)
                .withSubscription(subscription);
    }

}