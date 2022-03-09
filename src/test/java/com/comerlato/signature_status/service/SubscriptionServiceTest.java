package com.comerlato.signature_status.service;

import com.comerlato.signature_status.helper.MessageHelper;
import com.comerlato.signature_status.modules.repository.SubscriptionRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.support.TransactionTemplate;

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

}
