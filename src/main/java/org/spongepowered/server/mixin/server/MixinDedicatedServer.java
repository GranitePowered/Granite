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
package org.spongepowered.server.mixin.server;

import static org.spongepowered.common.SpongeVersion.MINECRAFT_VERSION;

import net.minecraft.network.ServerStatusResponse;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.util.ChatComponentText;
import org.spongepowered.api.GameState;
import org.spongepowered.api.event.game.state.GameAboutToStartServerEvent;
import org.spongepowered.api.event.game.state.GameStartingServerEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.common.scheduler.SpongeScheduler;
import org.spongepowered.server.SpongeVanilla;

import java.io.File;
import java.net.Proxy;

@Mixin(DedicatedServer.class)
public abstract class MixinDedicatedServer extends MinecraftServer {

    protected MixinDedicatedServer(File workDir, Proxy proxy, File profileCacheDir) {
        super(workDir, proxy, profileCacheDir);
    }

    @Inject(method = "startServer", at = @At(value = "INVOKE_STRING", target = "Lorg/apache/logging/log4j/Logger;info(Ljava/lang/String;)V",
            args = "ldc=Loading properties", remap = false))
    public void onServerLoad(CallbackInfoReturnable<Boolean> ci) {
        SpongeVanilla.INSTANCE.preInitialize();
    }

    @Inject(method = "startServer", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/dedicated/DedicatedServer;setConfigManager"
            + "(Lnet/minecraft/server/management/ServerConfigurationManager;)V", shift = At.Shift.BY, by = -7))
    public void onServerInitialize(CallbackInfoReturnable<Boolean> ci) {
        SpongeVanilla.INSTANCE.initialize();

        ServerStatusResponse statusResponse = getServerStatusResponse();
        statusResponse.setServerDescription(new ChatComponentText(getMOTD()));
        statusResponse.setProtocolVersionInfo(
                new ServerStatusResponse.MinecraftProtocolVersionIdentifier(MINECRAFT_VERSION.getName(), MINECRAFT_VERSION.getProtocol()));
        addFaviconToStatusResponse(statusResponse);
    }

    @Inject(method = "startServer", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/dedicated/DedicatedServer;loadAllWorlds"
            + "(Ljava/lang/String;Ljava/lang/String;JLnet/minecraft/world/WorldType;Ljava/lang/String;)V", shift = At.Shift.BY, by = -24))
    public void callServerAboutToStart(CallbackInfoReturnable<Boolean> ci) {
        SpongeVanilla.INSTANCE.postState(GameAboutToStartServerEvent.class, GameState.SERVER_ABOUT_TO_START);
    }

    @Inject(method = "startServer", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/dedicated/DedicatedServer;loadAllWorlds"
            + "(Ljava/lang/String;Ljava/lang/String;JLnet/minecraft/world/WorldType;Ljava/lang/String;)V", shift = At.Shift.AFTER))
    public void callServerStarting(CallbackInfoReturnable<Boolean> ci) {
        SpongeVanilla.INSTANCE.postState(GameStartingServerEvent.class, GameState.SERVER_STARTING);
    }

    @Inject(method = "updateTimeLightAndEntities", at = @At("RETURN"))
    public void onTick(CallbackInfo ci) {
        SpongeScheduler.getInstance().tickSyncScheduler();
    }

}
