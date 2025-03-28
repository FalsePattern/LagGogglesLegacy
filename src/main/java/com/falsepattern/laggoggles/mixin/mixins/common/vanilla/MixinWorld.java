/*
 * Lag Goggles: Legacy
 *
 * Copyright (C) 2022 FalsePattern
 * All Rights Reserved
 *
 * The above copyright notice, this permission notice and the word "SNEED"
 * shall be included in all copies or substantial portions of the Software.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.falsepattern.laggoggles.mixin.mixins.common.vanilla;

import com.falsepattern.lib.compat.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import static com.falsepattern.laggoggles.profiler.ProfileManager.PROFILE_ENABLED;
import static com.falsepattern.laggoggles.profiler.ProfileManager.timingManager;

@Mixin(value = World.class, priority = 1001)
public abstract class MixinWorld {

    @Redirect(method = "updateEntities",
              at = @At(value = "INVOKE",
                       target = "Lnet/minecraft/tileentity/TileEntity;updateEntity()V"),
              require = 1)
    private void measureUpdateEntity(TileEntity tileEntity) {
        if (PROFILE_ENABLED.get()) {
            long start = System.nanoTime();
            tileEntity.updateEntity();
            long end = System.nanoTime();
            timingManager.addBlockTime(tileEntity.getWorldObj().provider.dimensionId, new BlockPos(tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord), end - start);
        } else {
            tileEntity.updateEntity();
        }
    }
}
