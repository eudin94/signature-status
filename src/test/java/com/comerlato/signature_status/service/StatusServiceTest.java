package com.comerlato.signature_status.service;

import com.comerlato.signature_status.helper.MessageHelper;
import com.comerlato.signature_status.modules.repository.StatusRepository;
import com.comerlato.signature_status.modules.repository.spec.StatusSpecification;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static com.comerlato.signature_status.enums.EventTypeEnum.SUBSCRIPTION_CANCELED;
import static com.comerlato.signature_status.enums.EventTypeEnum.SUBSCRIPTION_PURCHASED;
import static com.comerlato.signature_status.enums.StatusEnum.ACTIVE;
import static com.comerlato.signature_status.enums.StatusEnum.INACTIVE;
import static com.comerlato.signature_status.util.creator.StatusCreator.statusActive;
import static com.comerlato.signature_status.util.creator.StatusCreator.statusActiveDTO;
import static com.comerlato.signature_status.util.mapper.MapperConstants.statusMapper;
import static java.util.Optional.empty;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ExtendWith(SpringExtension.class)
public class StatusServiceTest {

    @InjectMocks
    private StatusService service;
    @Mock
    private StatusRepository repository;
    @Mock
    private MessageHelper messageHelper;

    @Test
    void findAll_returnsPageOfStatusDTOs_whenSuccessful() {
        final var list = List.of(statusActive);
        final var page = new PageImpl<>(list);
        final var pageDTO = page.map(statusMapper::buildStatusDTO);
        final var pageable = PageRequest.of(0, 1, Sort.by(ASC, "id"));

        when(repository.findAll(any(StatusSpecification.class), any(Pageable.class))).thenReturn(page);
        assertEquals(pageDTO, service.findAll(empty(), pageable));
    }

    @Test
    void findDTOById_returnsStatusDTO_whenSuccessful() {
        when(repository.findById(statusActive.getId())).thenReturn(Optional.of(statusActive));
        assertEquals(statusActiveDTO, service.findDTOById(statusActive.getId()));
    }

    @Test
    void findDTOById_returns404_whenStatusIsNotFound() {
        when(repository.findById(statusActive.getId())).thenReturn(empty());
        final var status = assertThrows(ResponseStatusException.class,
                () -> service.findDTOById(statusActive.getId())).getStatus();

        assertEquals(NOT_FOUND, status);
    }

    @Test
    void findByEventType_returnsStatus_whenPURCHASED() {
        when(repository.findByName(ACTIVE)).thenReturn(Optional.of(statusActive));
        assertEquals(statusActive, service.findByEventType(SUBSCRIPTION_PURCHASED));
    }

    @Test
    void findByEventType_returns404_whenCANCELED() {
        when(repository.findByName(INACTIVE)).thenReturn(empty());
        final var status = assertThrows(ResponseStatusException.class,
                () -> service.findByEventType(SUBSCRIPTION_CANCELED)).getStatus();

        assertEquals(NOT_FOUND, status);
    }

}