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
package org.spongepowered.vanilla.mixin.server;

import jline.console.ConsoleReader;
import net.minecraft.server.dedicated.DedicatedServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.common.Sponge;
import org.spongepowered.vanilla.console.ConsoleCommandCompleter;
import org.spongepowered.vanilla.console.ConsoleFormatter;
import org.spongepowered.vanilla.launch.console.VanillaConsole;

import java.io.IOException;

@Mixin(targets = "net/minecraft/server/dedicated/DedicatedServer$2")
public abstract class MixinConsoleHandler extends Thread {

    @Shadow(remap = false, aliases = {"field_72428_a", "this$0"})
    private DedicatedServer server;

    @Override
    public void run() {
        final ConsoleReader reader = VanillaConsole.getReader();
        VanillaConsole.setFormatter(ConsoleFormatter.INSTANCE);
        reader.addCompleter(new ConsoleCommandCompleter(this.server));

        String line;
        while (!this.server.isServerStopped() && this.server.isServerRunning()) {
            try {
                line = reader.readLine("> ");

                if (line != null) {
                    line = line.trim();
                    if (!line.isEmpty()) {
                        this.server.addPendingCommand(line, this.server);
                    }
                }
            } catch (IOException e) {
                Sponge.getLogger().error("Exception handling console input", e);
            }
        }
    }

}
