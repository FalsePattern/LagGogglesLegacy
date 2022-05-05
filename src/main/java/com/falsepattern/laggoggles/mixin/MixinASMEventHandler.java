package com.falsepattern.laggoggles.mixin;

import com.falsepattern.laggoggles.util.ASMEventHandler;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.eventhandler.ASMEventHandler;
import net.minecraftforge.fml.common.eventhandler.IEventListener;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = ASMEventHandler.class, priority = 1001, remap = false)
public abstract class MixinASMEventHandler implements IEventListener, ASMEventHandler {

    @Shadow
    private ModContainer owner;

    @Override
    public ModContainer getOwner(){
        return owner;
    }

}
