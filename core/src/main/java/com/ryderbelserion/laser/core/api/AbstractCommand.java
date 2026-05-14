package com.ryderbelserion.laser.core.api;

import org.jspecify.annotations.NonNull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class AbstractCommand<CS, S> {

    private final List<Object> branches = new ArrayList<>();

    public final void addBranch(@NonNull final Object branch) {
        this.branches.add(branch);
    }

    public @NonNull final List<Object> getBranches() {
        return Collections.unmodifiableList(this.branches);
    }
}