/*
 * LagGoggles: Legacy
 *
 * Copyright (C) 2022-2025 FalsePattern
 * All Rights Reserved
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, only version 3 of the License.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package com.falsepattern.laggoggles.mixin.mixins.client.vanilla;

import com.falsepattern.lib.compat.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.tileentity.TileEntity;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import static com.falsepattern.laggoggles.profiler.ProfileManager.PROFILE_ENABLED;
import static com.falsepattern.laggoggles.profiler.ProfileManager.timingManager;

@Mixin(TileEntityRendererDispatcher.class)
@SideOnly(Side.CLIENT)
public class MixinTileEntityRendererDispatcher {

    private Long LAGGOGGLES_START = null;

    @Inject(method = "renderTileEntityAt",
            at = @At("HEAD"),
            require = 1)
    public void beforeRender(TileEntity tileEntityIn, double x, double y, double z, float partialTicks, CallbackInfo info){
        LAGGOGGLES_START = System.nanoTime();
    }

    @Inject(method = "renderTileEntityAt",
            at = @At("HEAD"),
            require = 1)
    public void afterRender(TileEntity tileEntityIn, double x, double y, double z, float partialTicks, CallbackInfo info){
        if(PROFILE_ENABLED.get() && LAGGOGGLES_START != null){
            long end = System.nanoTime();
            timingManager.addGuiBlockTime(new BlockPos(tileEntityIn.xCoord, tileEntityIn.yCoord, tileEntityIn.zCoord), end - LAGGOGGLES_START);
            LAGGOGGLES_START = null;
        }
    }
}