package com.comerlato.signature_status.modules.entity;

import com.comerlato.signature_status.enums.EventTypeEnum;
import lombok.*;

import javax.persistence.*;

import java.time.LocalDateTime;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Data
@Builder
@With
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "event_history")
public class EventHistory {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    @Enumerated(STRING)
    private EventTypeEnum type;
    private Long subscriptionId;
    private LocalDateTime createdAt;
}
