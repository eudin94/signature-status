package com.comerlato.signature_status.service;

import com.comerlato.signature_status.enums.StatusEnum;
import com.comerlato.signature_status.helper.MessageHelper;
import com.comerlato.signature_status.modules.entity.Status;
import com.comerlato.signature_status.modules.repository.StatusRepository;
import com.comerlato.signature_status.modules.repository.spec.StatusSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static com.comerlato.signature_status.exception.ErrorCodeEnum.ERROR_STATUS_NOT_FOUND;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RequiredArgsConstructor
@Slf4j
@Service
public class StatusService {

    private final StatusRepository repository;
    private final MessageHelper messageHelper;

    public Page<Status> findAll(final Optional<String> name, final Pageable pageable) {
        return repository.findAll(StatusSpecification.builder()
                .name(name)
                .build(), pageable);
    }

    public Status findById(final Long id) {
        return repository.findById(id).orElseThrow(() -> {
            log.error(messageHelper.get(ERROR_STATUS_NOT_FOUND, id.toString()));
            throw new ResponseStatusException(NOT_FOUND, messageHelper.get(ERROR_STATUS_NOT_FOUND, id.toString()));
        });
    }
}
