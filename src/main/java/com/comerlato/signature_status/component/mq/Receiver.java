package com.comerlato.signature_status.component.mq;

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

import static com.comerlato.signature_status.enums.EventTypeEnum.SUBSCRIPTION_PURCHASED;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Slf4j
@Component
@RequiredArgsConstructor
public class Receiver {

    @Value("${spring.rabbitmq.queue}")
    private String QUEUE_NAME;
    @Value("${spring.rabbitmq.uri}")
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
                String message = new String(delivery.getBody(), UTF_8);
                manageSubscription(message);
            };

            channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> {
            });

        }).onFailure(throwable -> {
            log.error(throwable.getMessage(), throwable);
            throw new ResponseStatusException(INTERNAL_SERVER_ERROR, throwable.getMessage());
        });

    }

    private void manageSubscription(String message) {
        log.info("Message received: [" + message + "]");
        Try.run(() -> {

            final var idAndEventType = message.split(",");
            final var subscriptionRequest = subscriptionService.buildSubscriptionRequestDTO(
                    idAndEventType[0], EventTypeEnum.valueOf(idAndEventType[1])
            );

            if (SUBSCRIPTION_PURCHASED.equals(subscriptionRequest.getEventType())) {
                final var savedSubscription = subscriptionService.create(subscriptionRequest.getId());
                log.info("\nSubscription has been created:\n" + savedSubscription.toString());

            } else {
                final var updatedSubscription = subscriptionService.update(subscriptionRequest);
                log.info("\nSubscription has been updated:\n" + updatedSubscription.toString());
            }

        }).onFailure(throwable -> {
            log.error(throwable.getMessage());
            throw new ResponseStatusException(INTERNAL_SERVER_ERROR, throwable.getMessage());
        });
    }
}
