package com.falsepattern.laggoggles.mixin.mixins.client.vanilla;

import com.falsepattern.lib.compat.BlockPos;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.tileentity.TileEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.falsepattern.laggoggles.profiler.ProfileManager.PROFILE_ENABLED;
import static com.falsepattern.laggoggles.profiler.ProfileManager.timingManager;

@Mixin(TileEntityRendererDispatcher.class)
@SideOnly(Side.CLIENT)
public class MixinTileEntityRendererDispatcher {

    private Long LAGGOGGLES_START = null;

    @Inject(method = "renderTileEntityAt",
            at = @At("HEAD"),
            require = 1)
    public void beforeRender(TileEntity tileEntityIn, double x, double y, double z, float partialTicks, CallbackInfo info){
        LAGGOGGLES_START = System.nanoTime();
    }

    @Inject(method = "renderTileEntityAt",
            at = @At("HEAD"),
            require = 1)
    public void afterRender(TileEntity tileEntityIn, double x, double y, double z, float partialTicks, CallbackInfo info){
        if(PROFILE_ENABLED.get() && LAGGOGGLES_START != null){
            long end = System.nanoTime();
            timingManager.addGuiBlockTime(new BlockPos(tileEntityIn.xCoord, tileEntityIn.yCoord, tileEntityIn.zCoord), end - LAGGOGGLES_START);
            LAGGOGGLES_START = null;
        }
    }
}