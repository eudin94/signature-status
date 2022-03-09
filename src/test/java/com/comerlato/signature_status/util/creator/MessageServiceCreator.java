package com.comerlato.signature_status.util.creator;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

import static org.mockito.Mockito.mock;

public class MessageServiceCreator {

    public static final String message = "5793cf6b3fd833521db8c420955e6f01,SUBSCRIPTION_PURCHASED";

    public static final ConnectionFactory factory = mock(ConnectionFactory.class);

    public static final Connection connection = mock(Connection.class);

    public static final Channel channel = mock(Channel.class);

    public static final MultipartFile file = mock(MultipartFile.class);

    public static final InputStream inputStream = MessageServiceCreator.class
            .getResourceAsStream("/notifications/notifications.csv");

}
