package com.comerlato.signature_status.modules.repository.spec;

import com.comerlato.signature_status.enums.EventTypeEnum;
import com.comerlato.signature_status.modules.entity.EventHistory;
import lombok.Builder;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.Optional.empty;

@Builder
public class EventHistorySpecification implements Specification<EventHistory> {

    @Builder.Default
    private final transient Optional<EventTypeEnum> type = empty();
    @Builder.Default
    private final transient Optional<List<Long>> subscriptionsIds = empty();


    @Override
    public Predicate toPredicate(Root<EventHistory> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        final var predicates = new ArrayList<Predicate>();
        type.ifPresent(s -> predicates.add(root.get("type").in(s)));
        subscriptionsIds.ifPresent(s -> predicates.add(root.get("subscriptionId").in(s)));
        return builder.and(predicates.toArray(new Predicate[0]));
    }
}
