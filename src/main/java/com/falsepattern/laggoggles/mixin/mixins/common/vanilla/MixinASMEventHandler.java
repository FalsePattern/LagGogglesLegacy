package com.falsepattern.laggoggles.mixin.mixins.common.vanilla;

import com.falsepattern.laggoggles.util.IMixinASMEventHandler;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.eventhandler.ASMEventHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ASMEventHandler.class)
public abstract class MixinASMEventHandler implements IMixinASMEventHandler {
    @Shadow private ModContainer owner;

    @Override
    public ModContainer getOwner() {
        return owner;
    }
}
