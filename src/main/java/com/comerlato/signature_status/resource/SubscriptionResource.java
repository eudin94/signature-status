package com.comerlato.signature_status.resource;

import com.comerlato.signature_status.dto.SubscriptionDTO;
import com.comerlato.signature_status.dto.SubscriptionRequestDTO;
import com.comerlato.signature_status.enums.StatusEnum;
import com.comerlato.signature_status.service.SubscriptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Optional;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/subscription")
@RequiredArgsConstructor
@Tag(name = "Subscriptions")
public class SubscriptionResource {

    private final SubscriptionService service;

    @PostMapping
    @ResponseStatus(CREATED)
    @Operation(summary = "Create subscription", responses = {@ApiResponse(responseCode = "201")})
    public SubscriptionDTO create(@Valid @RequestParam final String id) {
        return service.create(id);
    }

    @GetMapping
    @ResponseStatus(OK)
    @Operation(summary = "Find all subscriptions", responses = {@ApiResponse(responseCode = "200")})
    public Page<SubscriptionDTO> findAll(@RequestParam(required = false) final Optional<StatusEnum> status,
                                         @RequestParam(defaultValue = "0") final Integer page,
                                         @RequestParam(defaultValue = "10") final Integer size,
                                         @RequestParam(defaultValue = "id") final String sort,
                                         @RequestParam(defaultValue = "ASC") final Sort.Direction direction) {
        return service.findAll(status, PageRequest.of(page, size, Sort.by(direction, sort)));
    }

    @PutMapping
    @ResponseStatus(OK)
    @Operation(summary = "Update subscription status", responses = {@ApiResponse(responseCode = "200")})
    public SubscriptionDTO update(@Valid @RequestBody final SubscriptionRequestDTO request) {
        return service.update(request);
    }

    @GetMapping("/{id}")
    @ResponseStatus(OK)
    @Operation(summary = "Find subscription by id", responses = {@ApiResponse(responseCode = "200")})
    public SubscriptionDTO findDTOById(@PathVariable final String id) {
        return service.findDTOById(id);
    }
}
