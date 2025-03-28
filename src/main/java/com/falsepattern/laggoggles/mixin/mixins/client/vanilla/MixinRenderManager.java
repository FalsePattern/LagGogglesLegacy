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

package com.falsepattern.laggoggles.mixin.mixins.client.vanilla;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import static com.falsepattern.laggoggles.profiler.ProfileManager.PROFILE_ENABLED;
import static com.falsepattern.laggoggles.profiler.ProfileManager.timingManager;

@Mixin(RenderManager.class)
@SideOnly(Side.CLIENT)
public class MixinRenderManager {
    private Long LAGGOGGLES_START = null;

    @Inject(method = "func_147939_a",
            at = @At("HEAD"),
            require = 1)
    public void beforeRender(Entity entityIn, double x, double y, double z, float yaw, float partialTicks, boolean p_188391_10_, CallbackInfoReturnable<Boolean> cir){
        LAGGOGGLES_START = System.nanoTime();
    }

    @Inject(method = "func_147939_a",
            at = @At("RETURN"),
            require = 1)
    public void afterRender(Entity entityIn, double x, double y, double z, float yaw, float partialTicks, boolean p_188391_10_, CallbackInfoReturnable<Boolean> cir){
        if(PROFILE_ENABLED.get() && LAGGOGGLES_START != null){
            long end = System.nanoTime();
            timingManager.addGuiEntityTime(entityIn.getPersistentID(), end - LAGGOGGLES_START);
            LAGGOGGLES_START = null;
        }
    }


}