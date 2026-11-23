package com.ryderbelserion.laser.core.objects.requirements;

import org.jspecify.annotations.NullMarked;
import java.util.function.Predicate;

@FunctionalInterface
@NullMarked
public interface RequirementResolver<CS> {

    Predicate<CS> get();

}