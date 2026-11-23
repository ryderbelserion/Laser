package com.ryderbelserion.laser.core.objects.keys;

import org.jspecify.annotations.NullMarked;
import java.util.Locale;
import java.util.Objects;

@NullMarked
public record LaserKey(String name) {

    public static LaserKey of(final String name) {
        return new LaserKey(name.toLowerCase(Locale.ROOT));
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) return true;

        if (!(other instanceof LaserKey(String id))) return false;

        return Objects.equals(this.name, id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.name);
    }
}