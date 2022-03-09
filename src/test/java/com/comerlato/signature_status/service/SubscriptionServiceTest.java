package com.comerlato.signature_status.service;

import com.comerlato.signature_status.helper.MessageHelper;
import com.comerlato.signature_status.modules.repository.SubscriptionRepository;
import com.comerlato.signature_status.modules.repository.spec.SubscriptionSpecification;
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
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.comerlato.signature_status.enums.StatusEnum.ACTIVE;
import static com.comerlato.signature_status.util.creator.StatusCreator.statusActive;
import static com.comerlato.signature_status.util.creator.StatusCreator.statusActiveDTO;
import static com.comerlato.signature_status.util.creator.StatusCreator.statusInactive;
import static com.comerlato.signature_status.util.creator.SubscriptionCreator.subscription;
import static com.comerlato.signature_status.util.creator.SubscriptionCreator.subscriptionDTO;
import static com.comerlato.signature_status.util.creator.SubscriptionCreator.subscriptionRequestDTO;
import static com.comerlato.signature_status.util.mapper.MapperConstants.subscriptionMapper;
import static java.time.LocalDateTime.now;
import static java.util.Optional.empty;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;
import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ExtendWith(SpringExtension.class)
public class SubscriptionServiceTest {

    @InjectMocks
    private SubscriptionService service;
    @Mock
    private SubscriptionRepository repository;
    @Mock
    private StatusService statusService;
    @Mock
    private EventHistoryService eventHistoryService;
    @Mock
    private TransactionTemplate transaction;
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
        final var unsavedSubscription = subscription.withCreatedAt(now).withUpdatedAt(null);

        when(transaction.execute(any())).thenAnswer(invocationOnMock -> invocationOnMock
                .<TransactionCallback<Boolean>>getArgument(0).doInTransaction(mock(TransactionStatus.class)));
        when(repository.findById(subscription.getId())).thenReturn(empty());
        when(repository.save(unsavedSubscription)).thenReturn(subscription);
        when(statusService.findByStatusEnum(ACTIVE)).thenReturn(statusActive);
        when(statusService.findDTOById(statusActive.getId())).thenReturn(statusActiveDTO);

        assertEquals(subscriptionDTO, service.create(subscription.getId()));
    }

    @Test
    void create_returns400_whenIdValidationFails() {
        when(transaction.execute(any())).thenAnswer(invocationOnMock -> invocationOnMock
                .<TransactionCallback<Boolean>>getArgument(0).doInTransaction(mock(TransactionStatus.class)));
        when(repository.findById(subscription.getId())).thenReturn(Optional.of(subscription));

        final var status = assertThrows(ResponseStatusException.class,
                () -> service.create(subscription.getId())).getStatus();

        assertEquals(BAD_REQUEST, status);
    }

    @Test
    void update_editsEntityInDatabase_whenSuccessful() {
        final var uneditedSubscription = subscription.withUpdatedAt(now).withStatusId(statusInactive.getId());

        when(transaction.execute(any())).thenAnswer(invocationOnMock -> invocationOnMock
                .<TransactionCallback<Boolean>>getArgument(0).doInTransaction(mock(TransactionStatus.class)));
        when(repository.findById(subscriptionRequestDTO.getId())).thenReturn(Optional.of(uneditedSubscription));
        when(repository.save(uneditedSubscription.withStatusId(statusActive.getId()))).thenReturn(subscription);
        when(statusService.findByEventType(subscriptionRequestDTO.getEventType())).thenReturn(statusActive);
        when(statusService.findDTOById(subscription.getStatusId())).thenReturn(statusActiveDTO);

        assertEquals(subscriptionDTO, service.update(subscriptionRequestDTO));
    }

    @Test
    void update_returns400_whenStatusIsInaltered() {
        when(transaction.execute(any())).thenAnswer(invocationOnMock -> invocationOnMock
                .<TransactionCallback<Boolean>>getArgument(0).doInTransaction(mock(TransactionStatus.class)));
        when(repository.findById(subscriptionRequestDTO.getId())).thenReturn(Optional.of(subscription));
        when(statusService.findByEventType(subscriptionRequestDTO.getEventType())).thenReturn(statusActive);

        final var status = assertThrows(ResponseStatusException.class,
                () -> service.update(subscriptionRequestDTO)).getStatus();

        assertEquals(BAD_REQUEST, status);
    }

    @Test
    void findAll_returnsPageOfSubscriptionDTOs_whenSuccessful() {
        final var list = List.of(subscription);
        final var page = new PageImpl<>(list);
        final var pageDTO = page.map(
                sub -> subscriptionMapper.buildSubscriptionDTO(sub).withStatus(statusActiveDTO)
        );
        final var pageable = PageRequest.of(0, 1, Sort.by(ASC, "id"));

        when(repository.findAll(any(SubscriptionSpecification.class), any(Pageable.class))).thenReturn(page);
        when(statusService.findDTOById(subscription.getStatusId())).thenReturn(statusActiveDTO);

        assertEquals(pageDTO, service.findAll(empty(), pageable));
    }

    @Test
    void findDTOById_returns404_whenSubscriptionIsNotFound() {
        when(repository.findById(subscription.getId())).thenReturn(empty());
        final var status = assertThrows(ResponseStatusException.class,
                () -> service.findDTOById(subscription.getId())).getStatus();

        assertEquals(NOT_FOUND, status);
    }

    @Test
    void buildSubscriptionRequestDTO_returnsRequestDTO_whenSuccessful() {
        assertEquals(subscriptionRequestDTO, service.buildSubscriptionRequestDTO(
                subscription.getId(), subscriptionRequestDTO.getEventType())
        );
    }
}
