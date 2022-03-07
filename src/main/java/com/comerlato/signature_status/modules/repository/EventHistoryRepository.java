package com.comerlato.signature_status.modules.repository;

import com.comerlato.signature_status.modules.entity.EventHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface EventHistoryRepository extends JpaRepository<EventHistory, Long>, JpaSpecificationExecutor<EventHistory> {
}
