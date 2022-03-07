package com.comerlato.signature_status.modules.repository.spec;

import com.comerlato.signature_status.enums.StatusEnum;
import com.comerlato.signature_status.modules.entity.Status;
import com.comerlato.signature_status.modules.entity.Subscription;
import lombok.Builder;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Optional;

import static java.util.Optional.empty;

@Builder
public class SubscriptionSpecification implements Specification<Subscription> {

    @Builder.Default
    private final transient Optional<StatusEnum> statusEnum = empty();


    @Override
    public Predicate toPredicate(Root<Subscription> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        final var predicates = new ArrayList<Predicate>();
        final var status = query.from(Status.class);
        statusEnum.ifPresent(s -> predicates.add(status.get("name").in(s)));
        return builder.and(predicates.toArray(new Predicate[0]));
    }
}
