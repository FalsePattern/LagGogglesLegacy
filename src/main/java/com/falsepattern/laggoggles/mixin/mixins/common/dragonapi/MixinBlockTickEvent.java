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

package com.falsepattern.laggoggles.mixin.mixins.common.dragonapi;

import Reika.DragonAPI.Instantiable.Event.BlockTickEvent;
import com.falsepattern.laggoggles.util.Helpers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.block.Block;
import net.minecraft.world.World;

import java.util.Random;

@Mixin(value = BlockTickEvent.class,
       remap = false)
public abstract class MixinBlockTickEvent {
    @Redirect(method = "fire(Lnet/minecraft/block/Block;Lnet/minecraft/world/World;IIILjava/util/Random;I)V",
              at = @At(value = "INVOKE",
                       target = "Lnet/minecraft/block/Block;updateTick(Lnet/minecraft/world/World;IIILjava/util/Random;)V",
                       remap = true),
              require = 1)
    private static void measureBlockUpdateTick(Block instance, World world, int x, int y, int z, Random rng) {
        Helpers.measureBlockUpdateTick_server(instance, world, x, y, z, rng);
    }
}
