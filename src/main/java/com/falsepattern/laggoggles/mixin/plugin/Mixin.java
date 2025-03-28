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

package com.falsepattern.laggoggles.mixin.plugin;

import com.falsepattern.lib.mixin.IMixin;
import com.falsepattern.lib.mixin.ITargetedMod;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.function.Predicate;

import static com.falsepattern.lib.mixin.IMixin.PredicateHelpers.always;
import static com.falsepattern.lib.mixin.IMixin.PredicateHelpers.avoid;
import static com.falsepattern.lib.mixin.IMixin.PredicateHelpers.require;

@RequiredArgsConstructor
public enum Mixin implements IMixin {
    // @formatter:off
    //region vanilla
        //region common
            MixinASMEventHandlerMixin(Side.COMMON, always(), "vanilla.MixinASMEventHandler"),
            MixinEntity(Side.COMMON, always(), "vanilla.MixinEntity"),
            //Loaded in preinit MixinEventBus(Side.COMMON, always(), "vanilla.MixinEventBus"),
            MixinWorld(Side.COMMON, always(), "vanilla.MixinWorld"),
            MixinWorldServer(Side.COMMON, avoid(TargetedMod.DRAGONAPI), "vanilla.MixinWorldServer"),
        //endregion common
        //region client
            MixinRenderManager(Side.CLIENT, always(), "vanilla.MixinRenderManager"),
            MixinTileEntityRendererDispatcher(Side.CLIENT, always(), "vanilla.MixinTileEntityRendererDispatcher"),
        //endregion client
    //endregion vanilla
    //region dragonapi
        //region common
            MixinBlockTickEvent(Side.COMMON, require(TargetedMod.DRAGONAPI), "dragonapi.MixinBlockTickEvent"),
        //endregion common
    //endregion dragonapi
    // @formatter:on
    ;

    @Getter
    public final Side side;
    @Getter
    public final Predicate<List<ITargetedMod>> filter;
    @Getter
    public final String mixin;
}
