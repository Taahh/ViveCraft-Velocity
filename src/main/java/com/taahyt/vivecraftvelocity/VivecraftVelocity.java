package com.taahyt.vivecraftvelocity;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PluginMessageEvent;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.ServerConnection;
import com.velocitypowered.api.proxy.messages.ChannelIdentifier;
import com.velocitypowered.api.proxy.messages.LegacyChannelIdentifier;
import org.slf4j.Logger;

import java.util.Arrays;

@Plugin(
        id = "vivecraft-velocity",
        name = "ViveCraft-Velocity",
        version = "1.0-SNAPSHOT",
        description = "Vivecraft for Velocity tho",
        authors = {"ViveCraftDevs", "Taahh"}
)
public class VivecraftVelocity {

    private static final LegacyChannelIdentifier[] CHANNELS
            = new LegacyChannelIdentifier[]{
                    new LegacyChannelIdentifier("vivecraft:data"),
            new LegacyChannelIdentifier("Vivecraft")
    };

    private Logger logger;
    private ProxyServer server;

    @Inject
    public VivecraftVelocity(ProxyServer server, Logger logger)
    {
        this.logger = logger;
        this.server = server;
    }


    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event)
    {
        server.getChannelRegistrar().register(CHANNELS);
        logger.info("Registered ViveCraft Channels");
    }

    @Subscribe
    public void onProxyShutdown(ProxyShutdownEvent event)
    {
        server.getChannelRegistrar().unregister(CHANNELS);
        logger.info("Unregistered ViveCraft Channels");
    }

    @Subscribe
    public void onPluginMSG(PluginMessageEvent event)
    {
        ChannelIdentifier identifier = event.getIdentifier();
        if (identifier instanceof LegacyChannelIdentifier)
        {
            LegacyChannelIdentifier mc = (LegacyChannelIdentifier) event.getIdentifier();

            if (Arrays.stream(CHANNELS).map(LegacyChannelIdentifier::getName).noneMatch(mc.getName()::equalsIgnoreCase) || !mc.getName().equalsIgnoreCase("vivecraft:data")) return;
                if (event.getTarget() instanceof Player)
                {
                    Player player = (Player) event.getTarget();
                    player.sendPluginMessage(event.getIdentifier(), event.getData());
                } else if (event.getTarget() instanceof ServerConnection)
                {
                    ServerConnection con = (ServerConnection) event.getTarget();
                    con.sendPluginMessage(event.getIdentifier(), event.getData());
                }
        }


    }
}
