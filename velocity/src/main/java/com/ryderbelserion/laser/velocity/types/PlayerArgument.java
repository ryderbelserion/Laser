package com.ryderbelserion.laser.velocity.types;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;

public record PlayerArgument(String name) {

    public Player resolve(final ProxyServer server) {
        return server.getPlayer(name).orElseThrow();
    }
}