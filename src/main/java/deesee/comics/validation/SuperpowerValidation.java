package deesee.comics.validation;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.CollectionUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Set;

public class SuperpowerValidation implements ConstraintValidator<SuperpowerConstraint, Set<String>> {
    @Value("${deeseecomics.allowed-superpowers}")
    private Set<String> allowedSuperpowers;

    @Override
    public boolean isValid(Set<String> superpowers, ConstraintValidatorContext cxt) {
        return CollectionUtils.isEmpty(superpowers) || superpowers.stream()
                .anyMatch(superpower -> !allowedSuperpowers.contains(superpower));
    }
}
