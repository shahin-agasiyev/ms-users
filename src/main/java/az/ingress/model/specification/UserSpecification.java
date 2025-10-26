package az.ingress.model.specification;

import az.ingress.dao.entity.UserEntity;
import az.ingress.model.criteria.UserCriteria;
import az.ingress.util.PredicateUtil;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import static az.ingress.model.constants.CriteriaConstants.AGE;
import static az.ingress.model.constants.CriteriaConstants.NAME;
import static az.ingress.util.PredicateUtil.applyLikePattern;

@AllArgsConstructor
public class UserSpecification implements Specification<UserEntity> {

    private UserCriteria userCriteria;

    @Override
    public Predicate toPredicate(@NonNull Root<UserEntity> root,
                                 @NonNull CriteriaQuery<?> query,
                                 CriteriaBuilder cb) {
        var predicates = PredicateUtil.builder()
                .addNullSafety(userCriteria.getName(),
                        name -> cb.like(root.get(NAME), applyLikePattern(name))
                )
                .addNullSafety(userCriteria.getAgeFrom(), ageFrom -> cb.greaterThanOrEqualTo(root.get(AGE), ageFrom))
                .addNullSafety(userCriteria.getAgeTo(), ageTo -> cb.lessThanOrEqualTo(root.get(AGE), ageTo))
                .build();
        return cb.and(predicates);
    }
}
