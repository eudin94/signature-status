package com.comerlato.signature_status.service;

import com.comerlato.signature_status.dto.SubscriptionCreateRequestDTO;
import com.comerlato.signature_status.dto.SubscriptionDTO;
import com.comerlato.signature_status.dto.SubscriptionUpdateRequestDTO;
import com.comerlato.signature_status.enums.StatusEnum;
import com.comerlato.signature_status.helper.MessageHelper;
import com.comerlato.signature_status.modules.entity.Subscription;
import com.comerlato.signature_status.modules.repository.SubscriptionRepository;
import com.comerlato.signature_status.modules.repository.spec.SubscriptionSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static com.comerlato.signature_status.exception.ErrorCodeEnum.*;
import static com.comerlato.signature_status.util.mapper.MapperConstants.subscriptionMapper;
import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RequiredArgsConstructor
@Slf4j
@Service
public class SubscriptionService {

    private final SubscriptionRepository repository;
    private final StatusService statusService;
    private final MessageHelper messageHelper;

    public SubscriptionDTO create(final SubscriptionCreateRequestDTO request) {
        validateId(request.getId());
        final var statusId = statusService.findByName(request.getStatus()).getId();
        final var savedSubscription = repository.save(buildSubscription(request.getId(), statusId));
        return buildSubscriptionDTO(savedSubscription);
    }

    public SubscriptionDTO update(final SubscriptionUpdateRequestDTO request) {
        final var subscription = findById(request.getId());
        final var status = statusService.findByName(request.getStatus());
        validateStatus(subscription.getStatusId(), status.getId());
        final var updatedSubscription = repository.save(
                subscription.withStatusId(status.getId()).withUpdatedAt(now())
        );
        return buildSubscriptionDTO(updatedSubscription);
    }

    public void delete(final String id) {
        final var subscription = findById(id);
        repository.delete(subscription);
    }

    public Page<SubscriptionDTO> findAll(final Optional<StatusEnum> status,
                                         final Pageable pageable) {
        return repository.findAll(SubscriptionSpecification.builder()
                .statusEnum(status)
                .build(), pageable).map(this::buildSubscriptionDTO);
    }

    public SubscriptionDTO findDTOById(final String id) {
        return buildSubscriptionDTO(findById(id));
    }

    private Subscription findById(final String id) {
        return repository.findById(id).orElseThrow(() -> {
            log.error(messageHelper.get(ERROR_SUBSCRIPTION_NOT_FOUND, id));
            throw new ResponseStatusException(BAD_REQUEST, messageHelper.get(ERROR_SUBSCRIPTION_NOT_FOUND, id));
        });
    }

    private void validateId(final String id) {
        repository.findById(id).ifPresent(subscription -> {
            log.error(messageHelper.get(ERROR_SUBSCRIPTION_ALREADY_EXISTS, id));
            throw new ResponseStatusException(BAD_REQUEST, messageHelper.get(ERROR_SUBSCRIPTION_ALREADY_EXISTS, id));
        });
    }

    private void validateStatus(final Long oldStatusId, final Long newStatusId) {
        if (oldStatusId.equals(newStatusId)) {
            log.error(messageHelper.get(ERROR_UNCHANGED_STATUS));
            throw new ResponseStatusException(BAD_REQUEST, messageHelper.get(ERROR_UNCHANGED_STATUS));
        }
    }

    private Subscription buildSubscription(final String id,
                                           final Long statusId) {
        return Subscription.builder()
                .id(id)
                .statusId(statusId)
                .createdAt(now())
                .build();
    }

    private SubscriptionDTO buildSubscriptionDTO(final Subscription subscription) {
        return subscriptionMapper.buildSubscriptionDTO(subscription)
                .withStatus(statusService.findById(subscription.getStatusId()));
    }

}
