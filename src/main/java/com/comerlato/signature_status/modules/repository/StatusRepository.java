package com.comerlato.signature_status.modules.repository;

import com.comerlato.signature_status.enums.StatusEnum;
import com.comerlato.signature_status.modules.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StatusRepository extends JpaRepository<Status, Long>, JpaSpecificationExecutor<Status> {

    Optional<Status> findByName(final StatusEnum name);
}
