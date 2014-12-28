/*
 * License (MIT)
 *
 * Copyright (c) 2014 Granite Team
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this
 * software and associated documentation files (the "Software"), to deal in the
 * Software without restriction, including without limitation the rights to use, copy,
 * modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,
 * and to permit persons to whom the Software is furnished to do so, subject to the
 * following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package org.granitepowered.granite.impl.event.player;

import org.apache.commons.lang3.NotImplementedException;
import org.granitepowered.granite.impl.event.block.GraniteBlockEvent;
import org.spongepowered.api.block.BlockLoc;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.event.player.PlayerBreakBlockEvent;

public class GranitePlayerBreakBlockEvent extends GraniteBlockEvent implements PlayerBreakBlockEvent {
    Player player;
    BlockSnapshot nextBlock;

    public GranitePlayerBreakBlockEvent(BlockLoc loc, Player player, BlockSnapshot nextBlock) {
        super(loc);
        this.player = player;
        this.nextBlock = nextBlock;
    }

    @Override
    public BlockSnapshot getReplacementBlock() {
        return nextBlock;
    }

    @Override
    public int getExp() {
        throw new NotImplementedException("");
    }

    @Override
    public void setExp(int exp) {
        throw new NotImplementedException("");
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    @Override
    public Entity getEntity() {
        return player;
    }
}
