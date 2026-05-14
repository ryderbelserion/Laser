package com.ryderbelserion.laser.core.meta.types;

import com.ryderbelserion.laser.core.api.annotations.other.Permission;
import com.ryderbelserion.laser.core.api.extensions.SenderExtension;
import com.ryderbelserion.laser.core.enums.PermissionMode;
import net.kyori.adventure.audience.Audience;
import org.jspecify.annotations.NonNull;

public class PermissionMeta<CS, S extends Audience> {

    private final SenderExtension<CS, S> extension;
    private final PermissionMode mode;
    private final String description;
    private final String permission;

    public PermissionMeta(@NonNull final SenderExtension<CS, S> extension, @NonNull final Permission permission) {
        this.description = permission.description();
        this.permission = permission.permission();
        this.mode = permission.mode();
        this.extension = extension;
    }

    public final boolean isPermitted(@NonNull final CS sender) {
        if (this.permission.isBlank()) {
            return true;
        }

        return this.extension.isPermitted(this.extension.mapAudience(sender), this.permission);
    }

    public @NonNull final PermissionMeta<CS, S> init() {
        if (this.permission.isBlank()) {
            return this;
        }

        this.extension.registerPermission(this.permission, this.mode);

        return this;
    }

    public @NonNull final String getDescription() {
        return this.description;
    }

    public @NonNull final String getPermission() {
        return this.permission;
    }
}