package com.comerlato.signature_status.modules.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Data
@Builder
@With
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "subscription")
public class Subscription {

    @Id
    private String id;
    private Long statusId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
