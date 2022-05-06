package com.falsepattern.laggoggles.mixin.mixins.client.vanilla;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

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
            timingManager.addGuiEntityTime(entityIn.getUniqueID(), end - LAGGOGGLES_START);
            LAGGOGGLES_START = null;
        }
    }


}