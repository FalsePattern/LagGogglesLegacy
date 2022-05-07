package com.falsepattern.laggoggles.mixin.mixins.common.vanilla;

import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

import static com.falsepattern.laggoggles.profiler.ProfileManager.PROFILE_ENABLED;
import static com.falsepattern.laggoggles.profiler.ProfileManager.timingManager;

@Mixin(value = Entity.class, priority = 1001)
public abstract class MixinEntity {

    @Shadow
    public int dimension;

    @Shadow public abstract UUID getPersistentID();

    private Long LAGGOGGLES_START = null;

    @Inject(method = "onUpdate",
            at = @At("HEAD"),
            require = 1)
    private void onEntityUpdateHEAD(CallbackInfo info){
        LAGGOGGLES_START = System.nanoTime();
    }

    @Inject(method = "onUpdate",
            at = @At("RETURN"),
            require = 1)
    private void onEntityUpdateRETURN(CallbackInfo info){
        if(PROFILE_ENABLED.get() && LAGGOGGLES_START != null){
            timingManager.addEntityTime(dimension, this.getPersistentID(), System.nanoTime() - LAGGOGGLES_START);
        }
    }





}
