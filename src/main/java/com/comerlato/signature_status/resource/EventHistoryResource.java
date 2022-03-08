package com.comerlato.signature_status.resource;

import com.comerlato.signature_status.dto.EventHistoryDTO;
import com.comerlato.signature_status.enums.EventTypeEnum;
import com.comerlato.signature_status.service.EventHistoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/event-history")
@RequiredArgsConstructor
@Tag(name = "Event Histories")
public class EventHistoryResource {

    private final EventHistoryService service;

    @GetMapping
    @ResponseStatus(OK)
    @Operation(summary = "Find all events", responses = {@ApiResponse(responseCode = "200")})
    public Page<EventHistoryDTO> findAll(@RequestParam(required = false) final Optional<EventTypeEnum> type,
                                         @RequestParam(required = false) final Optional<List<Long>> subscriptionsIds,
                                         @RequestParam(defaultValue = "0") final Integer page,
                                         @RequestParam(defaultValue = "10") final Integer size,
                                         @RequestParam(defaultValue = "id") final String sort,
                                         @RequestParam(defaultValue = "ASC") final Sort.Direction direction) {
        return service.findAll(type, subscriptionsIds, PageRequest.of(page, size, Sort.by(direction, sort)));
    }

    @GetMapping("/{id}")
    @ResponseStatus(OK)
    @Operation(summary = "Find event by id", responses = {@ApiResponse(responseCode = "200")})
    public EventHistoryDTO findDTOById(@PathVariable final Long id) {
        return service.findDTOById(id);
    }
}
