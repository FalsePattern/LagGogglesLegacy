package com.falsepattern.laggoggles.mixin.mixins.common.vanilla;

import com.falsepattern.lib.compat.BlockPos;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import static com.falsepattern.laggoggles.profiler.ProfileManager.PROFILE_ENABLED;
import static com.falsepattern.laggoggles.profiler.ProfileManager.timingManager;

@Mixin(value = World.class, priority = 1001)
public abstract class MixinWorld {

    @Redirect(method = "updateEntities",
              at = @At(value = "INVOKE",
                       target = "Lnet/minecraft/tileentity/TileEntity;updateEntity()V"),
              require = 1)
    private void measureUpdateEntity(TileEntity tileEntity) {
        if (PROFILE_ENABLED.get()) {
            long start = System.nanoTime();
            tileEntity.updateEntity();
            long end = System.nanoTime();
            timingManager.addBlockTime(tileEntity.getWorldObj().provider.dimensionId, new BlockPos(tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord), end - start);
        } else {
            tileEntity.updateEntity();
        }
    }
}
