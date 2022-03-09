package com.comerlato.signature_status.service;

import com.comerlato.signature_status.helper.MessageHelper;
import com.comerlato.signature_status.modules.repository.EventHistoryRepository;
import com.comerlato.signature_status.modules.repository.spec.EventHistorySpecification;
import io.vavr.control.Try;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.comerlato.signature_status.util.creator.EventHistoryCreator.eventHistory;
import static com.comerlato.signature_status.util.creator.EventHistoryCreator.eventHistoryDTO;
import static com.comerlato.signature_status.util.creator.EventHistoryCreator.eventHistoryRequestDTO;
import static com.comerlato.signature_status.util.creator.SubscriptionCreator.subscriptionDTO;
import static com.comerlato.signature_status.util.mapper.MapperConstants.eventHistoryMapper;
import static java.time.LocalDateTime.now;
import static java.util.Optional.empty;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ExtendWith(SpringExtension.class)
public class EventHistoryServiceTest {

    @InjectMocks
    private EventHistoryService service;
    @Mock
    private EventHistoryRepository repository;
    @Mock
    private MessageHelper messageHelper;

    private static MockedStatic<LocalDateTime> localDateTime;
    private static LocalDateTime now = now();

    @BeforeAll
    public static void setUp() {
        Try.run(() -> {
            localDateTime = mockStatic(LocalDateTime.class);
            localDateTime.when(LocalDateTime::now).thenReturn(now);
        });
    }

    @Test
    void create_savesEntityToDatabase_whenSuccessful() {
        final var unsavedEvent = eventHistory
                .withId(null)
                .withCreatedAt(now);

        service.create(eventHistoryRequestDTO);

        verify(repository, times(1)).save(unsavedEvent);
    }

    @Test
    void findAll_returnsPageOfEventDTOs_whenSuccessful() {
        final var list = List.of(eventHistory);
        final var page = new PageImpl<>(list);
        final var pageDTO = page.map(eventHistoryMapper::buildEventHistoryDTO);
        final var pageable = PageRequest.of(0, 1, Sort.by(ASC, "id"));

        when(repository.findAll(any(EventHistorySpecification.class), any(Pageable.class))).thenReturn(page);
        assertEquals(pageDTO, service.findAll(empty(), empty(), pageable));
    }

    @Test
    void findDTOById_returnsEventHistoryDTO_whenSuccessful() {
        when(repository.findById(eventHistory.getId())).thenReturn(Optional.of(eventHistory));
        assertEquals(eventHistoryDTO, service.findDTOById(eventHistory.getId()));
    }

    @Test
    void findDTOById_returns404_whenEventIsNotFound() {
        when(repository.findById(eventHistory.getId())).thenReturn(empty());
        final var status = assertThrows(ResponseStatusException.class,
                () -> service.findDTOById(eventHistory.getId())).getStatus();

        assertEquals(NOT_FOUND, status);
    }

    @Test
    void buildRequestDTO_returnsRequestDTO_whenSuccessful() {
        assertEquals(eventHistoryRequestDTO, service.buildRequestDTO(eventHistory.getType(), subscriptionDTO));
    }

}
