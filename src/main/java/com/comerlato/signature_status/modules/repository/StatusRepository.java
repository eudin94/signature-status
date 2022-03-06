package com.comerlato.signature_status.modules.repository;

import com.comerlato.signature_status.modules.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatusRepository extends JpaRepository<Status, Long> {
}
