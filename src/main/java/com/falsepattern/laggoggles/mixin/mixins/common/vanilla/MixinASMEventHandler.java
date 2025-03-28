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

package com.falsepattern.laggoggles.mixin.mixins.common.vanilla;

import com.falsepattern.laggoggles.util.IMixinASMEventHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.eventhandler.ASMEventHandler;

@Mixin(ASMEventHandler.class)
public abstract class MixinASMEventHandler implements IMixinASMEventHandler {
    @Shadow private ModContainer owner;

    @Override
    public ModContainer getOwner() {
        return owner;
    }
}
