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

package org.granitepowered.granite.impl.block;

import com.flowpowered.math.vector.Vector3i;
import com.google.common.base.Optional;
import org.apache.commons.lang3.NotImplementedException;
import org.granitepowered.granite.impl.item.inventory.GraniteItemStack;
import org.granitepowered.granite.impl.world.GraniteWorld;
import org.granitepowered.granite.mappings.Mappings;
import org.granitepowered.granite.mc.*;
import org.spongepowered.api.block.BlockLoc;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.util.Direction;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.extent.Extent;

import javax.annotation.Nullable;
import java.util.Collection;

import static org.granitepowered.granite.utils.MinecraftUtils.*;

public class GraniteBlockLoc implements BlockLoc {
    Location location;

    public GraniteBlockLoc(Location location) {
        this.location = location;
    }

    @Override
    public Extent getExtent() {
        return location.getExtent();
    }

    @Override
    public Vector3i getPosition() {
        return location.getPosition().toInt();
    }

    @Override
    public Location getLocation() {
        return location;
    }

    @Override
    public int getX() {
        return location.getPosition().getFloorX();
    }

    @Override
    public int getY() {
        return location.getPosition().getFloorY();
    }

    @Override
    public int getZ() {
        return location.getPosition().getFloorZ();
    }

    @Override
    public BlockType getType() {
        return getState().getType();
    }

    @Override
    public BlockState getState() {
        return wrap(getWorld().obj.getBlockState(getBlockPos()));
    }

    @Override
    public BlockSnapshot getSnapshot() {
        return new GraniteBlockSnapshot((GraniteBlockState) getState());
    }

    @Override
    public byte getLuminanceFromGround() {
        return (byte) getWorld().obj.getLightFor((Enum) Mappings.getClass("EnumSkyBlock").getEnumConstants()[1], getBlockPos());
    }

    @Override
    public boolean isPowered() {
        // TODO: Get clarification on what indirectly means
        throw new NotImplementedException("");
    }

    @Override
    public boolean isIndirectlyPowered() {
        // TODO: Get clarification on what indirectly means
        throw new NotImplementedException("");
    }

    @Override
    public boolean isFacePowered(Direction direction) {
        // TODO: Get clarification on what indirectly means
        throw new NotImplementedException("");
    }

    @Override
    public boolean isFaceIndirectlyPowered(Direction direction) {
        // TODO: Get clarification on what indirectly means
        throw new NotImplementedException("");
    }

    @Override
    public Collection<Direction> getPoweredFaces() {
        // TODO: Get clarification on what indirectly means
        throw new NotImplementedException("");
    }

    @Override
    public Collection<Direction> getIndirectlyPoweredFaces() {
        // TODO: Get clarification on what indirectly means
        throw new NotImplementedException("");
    }

    @Override
    public boolean isPassable() {
        throw new NotImplementedException("");
    }

    @Override
    public boolean isFaceFlammable(Direction direction) {
        throw new NotImplementedException("");
    }

    @Override
    public byte getLuminanceFromSky() {
        return (byte) getWorld().obj.getLightFor((Enum) Mappings.getClass("EnumSkyBlock").getEnumConstants()[1], getBlockPos());
    }

    @Override
    public boolean dig() {
        // TODO: Figure out if this is possible to simulate (see ItemStack.onBlockDestroyed() or Block.harvestBlock()?)
        throw new NotImplementedException("");
    }

    @Override
    public boolean digWith(ItemStack itemStack) {
        // TODO: Figure out if this is possible to simulate (see ItemStack.onBlockDestroyed() or Block.harvestBlock()?)
        throw new NotImplementedException("");
    }

    @Override
    public int getDigTime() {
        return getDigTimeWith(null);
    }

    @Override
    public int getDigTimeWith(@Nullable ItemStack itemStack) {
        // This is mostly copied from Minecraft's source, I have no idea how most of it works

        float hardness = getMCType().fieldGet$blockHardness();

        MCMaterial material = getMCType().fieldGet$blockMaterial();

        boolean canHarvest = material.fieldGet$requiresNoTool() || itemStack != null && ((MCItemStack) unwrap(itemStack)).canHarvestBlock(getMCType());

        float strength = 1.0F;
        if (itemStack != null) {
            MCItem item = unwrap(itemStack.getItem());
            strength *= item.getStrVsBlock((MCItemStack) unwrap(itemStack), (MCBlock) unwrap(getType()));
        }

        if (strength > 2.0F) {
            int efficiencyModifier = (int) Mappings.invokeStatic("EnchantmentHelper", "getEnchantmentLevel", 32, ((GraniteItemStack) itemStack).obj);

            if (efficiencyModifier > 0) {
                strength += efficiencyModifier * efficiencyModifier + 1;
            }
        }

        int thumps = (int) (hardness < 0.0F ? 0.0F : (!canHarvest ? strength / hardness / 100.0F : strength / hardness / 30.0F));

        // One thump every 4 ticks (or 5 times per second)
        return thumps * 4;
    }

    @Override
    public byte getLuminance() {
        return (byte) getMCType().fieldGet$lightValue();
    }

    @Override
    public void replaceWith(BlockState state) {
        getWorld().obj.setBlockState(getBlockPos(), (MCBlockState) unwrap(state));
    }

    @Override
    public void replaceWith(BlockType type) {
        replaceWith(type.getDefaultState());
    }

    @Override
    public void replaceWith(BlockSnapshot snapshot) {
        // TODO: The other snapshot fields when they come
        replaceWith(snapshot.getState());
    }

    @Override
    public void interact() {
        // TODO: Figure out if this is possible to simulate
        throw new NotImplementedException("");
    }

    @Override
    public void interactWith(ItemStack itemStack) {
        // TODO: Figure out if this is possible to simulate
        throw new NotImplementedException("");
    }

    @Override
    public <T> Optional<T> getData(Class<T> dataClass) {
        // TODO: Data API
        throw new NotImplementedException("");
    }

    public GraniteWorld getWorld() {
        return ((GraniteWorld) getExtent());
    }

    public MCBlockPos getBlockPos() {
        return (MCBlockPos) graniteToMinecraftBlockPos(getPosition());
    }

    public MCBlock getMCType() {
        return unwrap(getType());
    }
}
