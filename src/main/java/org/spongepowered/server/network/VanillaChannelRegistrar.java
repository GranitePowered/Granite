/*
 * This file is part of Sponge, licensed under the MIT License (MIT).
 *
 * Copyright (c) SpongePowered <https://www.spongepowered.org>
 * Copyright (c) contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.spongepowered.server.network;

import static com.google.common.base.Preconditions.checkState;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.google.inject.Singleton;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.ServerConfigurationManager;
import org.spongepowered.api.Platform;
import org.spongepowered.api.network.ChannelBinding;
import org.spongepowered.api.network.ChannelRegistrationException;
import org.spongepowered.api.network.RemoteConnection;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.common.network.SpongeNetworkManager;

import java.util.Map;
import java.util.Set;

@Singleton
public class VanillaChannelRegistrar extends SpongeNetworkManager {

    public static final String INTERNAL_PREFIX = "MC|";

    public static final String REGISTER_CHANNEL = "REGISTER";
    public static final String UNREGISTER_CHANNEL = "UNREGISTER";

    public static final char CHANNEL_SEPARATOR = '\0';

    private final Map<String, VanillaChannelBinding> channels = Maps.newHashMap();

    private static boolean isReservedChannel(String name) {
        return name.startsWith(INTERNAL_PREFIX) || name.equals(REGISTER_CHANNEL) || name.equals(UNREGISTER_CHANNEL);
    }

    private void validateChannel(String name) {
        if (isReservedChannel(name)) {
            throw new ChannelRegistrationException("Reserved channels cannot be registered by plugins");
        }

        ChannelBinding current = channels.get(name);
        if (current != null) {
            throw new ChannelRegistrationException("Channel '" + name + "' is already registered by " + current.getOwner());
        }
    }

    private void registerChannel(VanillaChannelBinding channel) {
        final String name = channel.getName();
        this.channels.put(name, channel);

        ServerConfigurationManager manager = MinecraftServer.getServer().getConfigurationManager();
        if (manager != null) {
            manager.sendPacketToAllPlayers(getRegPacket(name));
        }
    }

    @Override
    public ChannelBinding.IndexedMessageChannel createChannel(Object plugin, String name) throws ChannelRegistrationException {
        PluginContainer container = checkCreateChannelArgs(plugin, name);

        validateChannel(name);
        VanillaIndexedMessageChannel channel = new VanillaIndexedMessageChannel(this, name, container);
        registerChannel(channel);
        return new VanillaIndexedMessageChannel(this, name, container);
    }

    @Override
    public ChannelBinding.RawDataChannel createRawChannel(Object plugin, String name) throws ChannelRegistrationException {
        PluginContainer container = checkCreateChannelArgs(plugin, name);

        validateChannel(name);
        VanillaRawDataChannel channel = new VanillaRawDataChannel(this, name, container);
        registerChannel(channel);
        return channel;
    }

    @Override
    public void unbindChannel(ChannelBinding channel) {
        final String name = channel.getName();

        VanillaChannelBinding binding = this.channels.remove(name);
        checkState(binding != null, "Channel is already unbound");
        binding.invalidate();

        ServerConfigurationManager manager = MinecraftServer.getServer().getConfigurationManager();
        if (manager != null) {
            manager.sendPacketToAllPlayers(getUnregPacket(name));
        }
    }

    @Override
    public Set<String> getRegisteredChannels(Platform.Type side) {
        if (side == Platform.Type.SERVER) {
            return ImmutableSet.copyOf(channels.keySet());
        } else {
            return ImmutableSet.of();
        }
    }

    @Override
    public boolean isChannelAvailable(String name) {
        return !isReservedChannel(name) && !this.channels.containsKey(name);
    }

    public void post(RemoteConnection connection, C17PacketCustomPayload packet) {
        VanillaChannelBinding binding = this.channels.get(packet.getChannelName());
        if (binding != null) {
            binding.post(connection, packet.getBufferData());
        }
    }

}
