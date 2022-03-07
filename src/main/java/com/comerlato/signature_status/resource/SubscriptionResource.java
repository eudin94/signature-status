package com.comerlato.signature_status.resource;

import com.comerlato.signature_status.dto.SubscriptionCreateRequestDTO;
import com.comerlato.signature_status.dto.SubscriptionDTO;
import com.comerlato.signature_status.dto.SubscriptionUpdateRequestDTO;
import com.comerlato.signature_status.enums.StatusEnum;
import com.comerlato.signature_status.service.SubscriptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/subscription")
@RequiredArgsConstructor
@Tag(name = "Subscriptions")
public class SubscriptionResource {

    private final SubscriptionService service;

    @PostMapping
    @ResponseStatus(CREATED)
    @Operation(summary = "Create subscription", responses = {@ApiResponse(responseCode = "201")})
    public SubscriptionDTO create(@Valid @RequestBody final SubscriptionCreateRequestDTO request) {
        return service.create(request);
    }

    @GetMapping
    @ResponseStatus(OK)
    @Operation(summary = "Find all subscriptions", responses = {@ApiResponse(responseCode = "200")})
    public Page<SubscriptionDTO> findAll(@RequestParam(required = false) final Optional<StatusEnum> status,
                                         @RequestParam(required = false) final Integer page,
                                         @RequestParam(required = false) final Integer size,
                                         @RequestParam(required = false) final String sort,
                                         @RequestParam(required = false) final Sort.Direction direction) {
        return service.findAll(status, PageRequest.of(page, size, Sort.by(direction, sort)));
    }

    @PutMapping
    @ResponseStatus(OK)
    @Operation(summary = "Update subscription status", responses = {@ApiResponse(responseCode = "200")})
    public SubscriptionDTO update(@Valid @RequestBody final SubscriptionUpdateRequestDTO request) {
        return service.update(request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    @Operation(summary = "Delete subscription by id", responses = {@ApiResponse(responseCode = "204")})
    public void delete(@PathVariable final String id) {
        service.delete(id);
    }

    @GetMapping("/{id}")
    @ResponseStatus(OK)
    @Operation(summary = "Find subscription by id", responses = {@ApiResponse(responseCode = "200")})
    public SubscriptionDTO findDTOById(@PathVariable final String id) {
        return service.findDTOById(id);
    }
}