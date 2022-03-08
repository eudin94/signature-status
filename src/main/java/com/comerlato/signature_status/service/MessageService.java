package com.comerlato.signature_status.service;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeoutException;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageService {

    @Value("${mq.amqp.queue}")
    private String QUEUE_NAME;
    @Value("${mq.amqp.uri}")
    private String URI;

    public void upload(final MultipartFile file) {
        Try.run(() -> {

            final var inputStream = file.getInputStream();
            final var fileReader = new BufferedReader(new InputStreamReader(inputStream, UTF_8));
            final var csvParser = new CSVParser(fileReader, CSVFormat.DEFAULT);

            csvParser.getRecords().forEach(record -> {
                final var id = record.get(0);
                final var event = record.get(1);
                sendMessage(id + "," + event);
            });

        }).onFailure(throwable -> {
            log.error(throwable.getMessage(), throwable);
            throw new ResponseStatusException(INTERNAL_SERVER_ERROR, throwable.getMessage());
        });
    }

    @Async
    public void sendMessage(final String message) {

        ConnectionFactory factory = new ConnectionFactory();

        Try.run(() -> factory.setUri(URI)).onFailure(throwable -> {
            log.error(throwable.getMessage(), throwable);
            throw new ResponseStatusException(INTERNAL_SERVER_ERROR, throwable.getMessage());
        });

        try (Connection connection = factory.newConnection(); Channel channel = connection.createChannel()) {

            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
            log.info("Message sent: [" + message + "]");

        } catch (IOException | TimeoutException e) {
            log.error(e.getMessage(), e);
            throw new ResponseStatusException(INTERNAL_SERVER_ERROR, e.getMessage());
        }

    }

}