package com.comerlato.signature_status.mq;

import com.comerlato.signature_status.dto.SubscriptionUpdateRequestDTO;
import com.comerlato.signature_status.enums.EventTypeEnum;
import com.comerlato.signature_status.service.SubscriptionService;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.PostConstruct;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Slf4j
@Component
@RequiredArgsConstructor
public class Receiver {

    @Value("${mq.amqp.queue}")
    private String QUEUE_NAME;
    @Value("${mq.amqp.uri}")
    private String URI;

    private final SubscriptionService subscriptionService;

    @PostConstruct
    public void receive() {
        Try.run(() -> {

            ConnectionFactory factory = new ConnectionFactory();
            factory.setUri(URI);
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();

            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            log.info("The message queue service is waiting for new messages");

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), "UTF-8");
                updateSubscription(message);
            };

            channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> {
            });

        }).onFailure(throwable -> {
            log.error(throwable.getMessage(), throwable);
            throw new ResponseStatusException(INTERNAL_SERVER_ERROR, throwable.getMessage());
        });

    }

    private void updateSubscription(String message) {
        log.info("Message received: [" + message + "]");
        Try.run(() -> {
            final var updatedSubscription = subscriptionService.update(buildRequest(message));
            log.info("\nSubscription has been updated:\n" + updatedSubscription.toString());
        }).onFailure(throwable -> {
            log.error(throwable.getMessage());
            throw new ResponseStatusException(INTERNAL_SERVER_ERROR, throwable.getMessage());
        });
    }

    private SubscriptionUpdateRequestDTO buildRequest(String message) {
        final var idAndEventType = message.split(",");
        return SubscriptionUpdateRequestDTO.builder()
                .id(idAndEventType[0])
                .eventType(EventTypeEnum.valueOf(idAndEventType[1]))
                .build();
    }
}
