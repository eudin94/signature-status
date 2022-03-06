package com.comerlato.signature_status.modules.repository.spec;

import com.comerlato.signature_status.modules.entity.Status;
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
public class StatusSpecification implements Specification<Status> {

    @Builder.Default
    private final transient Optional<String> name = empty();


    @Override
    public Predicate toPredicate(Root<Status> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        var predicates = new ArrayList<Predicate>();
        name.ifPresent(s -> predicates.add(builder.like(builder.lower(root.get("name")), "%" + s.toLowerCase() + "%")));
        return builder.and(predicates.toArray(new Predicate[0]));
    }
}
