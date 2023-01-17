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

package com.falsepattern.laggoggles.util;

import com.falsepattern.lib.compat.BlockPos;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import net.minecraft.block.Block;
import net.minecraft.world.World;

import java.util.Random;

import static com.falsepattern.laggoggles.profiler.ProfileManager.PROFILE_ENABLED;
import static com.falsepattern.laggoggles.profiler.ProfileManager.timingManager;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Helpers {
    public static void measureBlockUpdateTick_server(Block instance, World world, int x, int y, int z, Random rng) {
        if (PROFILE_ENABLED.get()) {
            long start = System.nanoTime();
            instance.updateTick(world, x, y, z, rng);
            long end = System.nanoTime();
            timingManager.addBlockTime(world.provider.dimensionId, new BlockPos(x, y, z), end - start);
        } else {
            instance.updateTick(world, x, y, z, rng);
        }
    }
}
