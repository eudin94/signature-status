package com.comerlato.signature_status.resource;

import com.comerlato.signature_status.service.EventHistoryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/event-history")
@RequiredArgsConstructor
@Tag(name = "Event Histories")
public class EventHistoryResource {

    private final EventHistoryService service;
}
