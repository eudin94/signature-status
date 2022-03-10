package com.comerlato.signature_status.resource;

import com.comerlato.signature_status.service.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@RestController
@RequestMapping("/message")
@RequiredArgsConstructor
@Tag(name = "Messages")
public class MessageResource {

    private final MessageService service;

    @PostMapping(consumes = MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(CREATED)
    @Operation(
            summary = "Create subscriptions from csv file using message queue",
            responses = {@ApiResponse(responseCode = "201")}
    )
    public void upload(@Valid @RequestBody final MultipartFile file) {
        service.upload(file);
    }

}
