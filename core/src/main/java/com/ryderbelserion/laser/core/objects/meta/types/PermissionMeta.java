package com.ryderbelserion.laser.core.objects.meta.types;

import com.ryderbelserion.laser.core.api.annotations.other.Permission;
import com.ryderbelserion.laser.core.api.extensions.SenderExtension;
import com.ryderbelserion.laser.core.enums.PermissionMode;
import net.kyori.adventure.audience.Audience;
import org.jspecify.annotations.NonNull;
import java.util.Arrays;
import java.util.List;

public class PermissionMeta<CS, S extends Audience> {

    private final SenderExtension<CS, S> extension;
    private final List<String> permissions;
    private final PermissionMode mode;
    private final String description;

    public PermissionMeta(@NonNull final SenderExtension<CS, S> extension, @NonNull final Permission permission) {
        this.description = permission.description();
        this.permissions = Arrays.asList(permission.permission());
        this.mode = permission.mode();
        this.extension = extension;
    }

    public final boolean isPermitted(@NonNull final CS sender) {
        if (this.permissions.isEmpty()) {
            return true;
        }

        final S mapper = this.extension.mapAudience(sender);

        return switch (this.mode) {
            case ALL_OF -> this.permissions.stream().allMatch(index -> this.extension.isPermitted(mapper, index));

            case ANY_OF -> {
                boolean isPermitted = false;

                for (final String permission : this.permissions) {
                    if (this.extension.isPermitted(mapper, permission)) { // break if it matches
                        isPermitted = true;

                        break;
                    }
                }

                yield isPermitted;
            }

            default -> this.extension.isPermitted(mapper, this.permissions.getFirst());
        };
    }

    public @NonNull final PermissionMeta<CS, S> init() {
        if (this.permissions.isEmpty()) {
            return this;
        }

        for (final String permission : this.permissions) {
            if (permission.isBlank()) {
                continue;
            }

            PermissionMode index = this.mode;

            switch (index) {
                case ALL_OF, ANY_OF -> index = PermissionMode.OP; // default to op for bukkit servers
            }

            this.extension.registerPermission(permission, index);
        }

        return this;
    }

    public @NonNull final List<String> getPermissions() {
        return this.permissions;
    }

    public @NonNull final String getDescription() {
        return this.description;
    }
}