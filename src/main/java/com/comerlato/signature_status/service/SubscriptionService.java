package com.comerlato.signature_status.service;

import com.comerlato.signature_status.dto.SubscriptionCreateRequestDTO;
import com.comerlato.signature_status.enums.StatusEnum;
import com.comerlato.signature_status.helper.MessageHelper;
import com.comerlato.signature_status.modules.entity.Subscription;
import com.comerlato.signature_status.modules.repository.SubscriptionRepository;
import com.comerlato.signature_status.modules.repository.spec.SubscriptionSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.comerlato.signature_status.exception.ErrorCodeEnum.ERROR_SUBSCRIPTION_ALREADY_EXISTS;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RequiredArgsConstructor
@Slf4j
@Service
public class SubscriptionService {

    private final SubscriptionRepository repository;
    private final StatusService statusService;
    private final MessageHelper messageHelper;

    public Subscription create(final SubscriptionCreateRequestDTO request) {
        validateId(request.getId());
        final var statusId = statusService.findByName(request.getStatus()).getId();
        return repository.save(buildSubscription(request.getId(), statusId));
    }

    public Page<Subscription> findAll(final Optional<StatusEnum> status, final Pageable pageable) {
        return repository.findAll(SubscriptionSpecification.builder()
                .statusEnum(status)
                .build(), pageable);
    }

    private void validateId(final String id) {
        repository.findById(id).ifPresent(subscription -> {
            log.error(messageHelper.get(ERROR_SUBSCRIPTION_ALREADY_EXISTS, id));
            throw new ResponseStatusException(BAD_REQUEST, messageHelper.get(ERROR_SUBSCRIPTION_ALREADY_EXISTS, id));
        });
    }

    private Subscription buildSubscription(final String id, final Long statusId) {
        return Subscription.builder()
                .id(id)
                .statusId(statusId)
                .createdAt(LocalDateTime.now())
                .build();
    }

}
