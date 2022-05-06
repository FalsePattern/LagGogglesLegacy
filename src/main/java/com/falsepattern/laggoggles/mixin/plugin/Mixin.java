package com.falsepattern.laggoggles.mixin.plugin;

import com.falsepattern.lib.mixin.IMixin;
import com.falsepattern.lib.mixin.ITargetedMod;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.function.Predicate;

import static com.falsepattern.lib.mixin.IMixin.PredicateHelpers.always;

@RequiredArgsConstructor
public enum Mixin implements IMixin {
    //region vanilla
        //region common
            MixinEntity(Side.COMMON, always(), "vanilla.MixinEntity"),
            MixinEventBus(Side.COMMON, always(), "vanilla.MixinEventBus"),
            MixinWorld(Side.COMMON, always(), "vanilla.MixinWorld"),
            MixinWorldServer(Side.COMMON, always(), "vanilla.MixinWorldServer"),
        //endregion common
        //region client
            MixinRenderManager(Side.CLIENT, always(), "vanilla.MixinRenderManager"),
            MixinTileEntityRendererDispatcher(Side.CLIENT, always(), "vanilla.MixinTileEntityRendererDispatcher"),
        //endregion client
    //endregion vanilla
    ;

    @Getter
    public final Side side;
    @Getter
    public final Predicate<List<ITargetedMod>> filter;
    @Getter
    public final String mixin;
}
