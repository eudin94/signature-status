package com.comerlato.signature_status.resource;

import com.comerlato.signature_status.dto.StatusDTO;
import com.comerlato.signature_status.enums.StatusEnum;
import com.comerlato.signature_status.service.StatusService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/status")
@RequiredArgsConstructor
@Tag(name = "Status")
public class StatusResource {

    private final StatusService service;

    @GetMapping
    @ResponseStatus(OK)
    @Operation(summary = "Find all status", responses = {@ApiResponse(responseCode = "200")})
    public Page<StatusDTO> findAll(@RequestParam(required = false) final Optional<StatusEnum> name,
                                   @RequestParam(defaultValue = "0") final Integer page,
                                   @RequestParam(defaultValue = "10") final Integer size,
                                   @RequestParam(defaultValue = "id") final String sort,
                                   @RequestParam(defaultValue = "ASC") final Sort.Direction direction) {
        return service.findAll(name, PageRequest.of(page, size, Sort.by(direction, sort)));
    }

    @GetMapping("/{id}")
    @ResponseStatus(OK)
    @Operation(summary = "Find all status", responses = {@ApiResponse(responseCode = "200")})
    public StatusDTO findDTOById(@PathVariable final Long id) {
        return service.findDTOByID(id);
    }
}
