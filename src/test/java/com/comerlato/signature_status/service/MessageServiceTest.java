package com.comerlato.signature_status.service;

import com.comerlato.signature_status.helper.MessageHelper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import io.vavr.control.Try;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

import static com.comerlato.signature_status.util.creator.MessageServiceCreator.channel;
import static com.comerlato.signature_status.util.creator.MessageServiceCreator.connection;
import static com.comerlato.signature_status.util.creator.MessageServiceCreator.file;
import static com.comerlato.signature_status.util.creator.MessageServiceCreator.inputStream;
import static com.comerlato.signature_status.util.creator.MessageServiceCreator.message;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@ExtendWith(SpringExtension.class)
public class MessageServiceTest {

    @InjectMocks
    private MessageService service;
    @Mock
    private MessageHelper messageHelper;

    private static final String queueName = "TEST_QUEUE";
    private static final String uri = "amqp://guest:guest@localhost";

    @BeforeEach
    void setUp() {
    }

    @Test
    void upload_receivesCSVFile_sendsMessage_whenSuccessful() {
        ReflectionTestUtils.setField(service, "QUEUE_NAME", queueName, String.class);
        ReflectionTestUtils.setField(service, "URI", uri, String.class);

        var connection = mock(Connection.class);
        var channel = mock(Channel.class);

        var construction = mockConstruction(ConnectionFactory.class,
                (factory, context) -> when(factory.newConnection()).thenReturn(connection));

        Try.run(() -> {
            when(file.getInputStream()).thenReturn(inputStream);
            when(file.getContentType()).thenReturn("text/csv");
            when(connection.createChannel()).thenReturn(channel);

        });

        assertDoesNotThrow(() -> service.upload(file));
        construction.close();
    }

    @Test
    void upload_returns500_whenFileIsInvalid() {
        final var status = assertThrows(ResponseStatusException.class,
                () -> service.upload(file)).getStatus();

        assertEquals(INTERNAL_SERVER_ERROR, status);
    }

    @Test
    void sendMessage_returns500_whenUriIsInvalid() {
        ReflectionTestUtils.setField(service, "URI", uri, String.class);
        var construction = mockConstruction(ConnectionFactory.class,
                (factory, context) -> doThrow(ResponseStatusException.class).when(factory).setUri(uri));

        final var status = assertThrows(ResponseStatusException.class,
                () -> service.sendMessage(message)).getStatus();

        assertEquals(INTERNAL_SERVER_ERROR, status);
        construction.close();
    }

    @Test
    void sendMessage_returns500_whenMessageQueueFails() {
        var construction = mockConstruction(ConnectionFactory.class,
                (factory, context) -> {
                    doNothing().when(factory).setUri(uri);
                    when(factory.newConnection()).thenReturn(connection);
                });

        ReflectionTestUtils.setField(service, "QUEUE_NAME", queueName, String.class);
        ReflectionTestUtils.setField(service, "URI", uri, String.class);

        Try.run(() -> {
            when(connection.createChannel()).thenReturn(channel);
            when(channel.queueDeclare(queueName, false, false, false, null)).thenThrow(new IOException());
        });

        final var status = assertThrows(ResponseStatusException.class,
                () -> service.sendMessage(message)).getStatus();

        assertEquals(INTERNAL_SERVER_ERROR, status);
        construction.close();
    }
}
