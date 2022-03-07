package com.comerlato.signature_status.resource;

import com.comerlato.signature_status.service.StatusService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/status")
@RequiredArgsConstructor
@Tag(name = "Status")
public class StatusResource {

    private final StatusService service;

}
