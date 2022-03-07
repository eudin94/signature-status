package com.comerlato.signature_status.util.mapper;

import org.mapstruct.factory.Mappers;

public class MapperConstants {

    private MapperConstants() {
    }

    public static final StatusMapper statusMapper = Mappers.getMapper(StatusMapper.class);
    public static final SubscriptionMapper subscriptionMapper = Mappers.getMapper(SubscriptionMapper.class);
    public static final EventHistoryMapper eventHistoryMapper= Mappers.getMapper(EventHistoryMapper.class);
}
