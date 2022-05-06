package com.falsepattern.laggoggles.mixin.mixins.common.vanilla;

import com.falsepattern.lib.compat.BlockPos;
import net.minecraft.block.Block;
import net.minecraft.profiler.Profiler;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldServer;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.storage.ISaveHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Random;

import static com.falsepattern.laggoggles.profiler.ProfileManager.PROFILE_ENABLED;
import static com.falsepattern.laggoggles.profiler.ProfileManager.timingManager;

@Mixin(value = WorldServer.class, priority = 1001)
public abstract class MixinWorldServer extends World {

    public MixinWorldServer(ISaveHandler p_i45368_1_, String p_i45368_2_, WorldProvider p_i45368_3_, WorldSettings p_i45368_4_, Profiler p_i45368_5_) {
        super(p_i45368_1_, p_i45368_2_, p_i45368_3_, p_i45368_4_, p_i45368_5_);
    }

    public MixinWorldServer(ISaveHandler p_i45369_1_, String p_i45369_2_, WorldSettings p_i45369_3_, WorldProvider p_i45369_4_, Profiler p_i45369_5_) {
        super(p_i45369_1_, p_i45369_2_, p_i45369_3_, p_i45369_4_, p_i45369_5_);
    }

    @Redirect(method = "tickUpdates",
              at = @At(value = "INVOKE",
                       target = "Lnet/minecraft/block/Block;updateTick(Lnet/minecraft/world/World;IIILjava/util/Random;)V"),
              require = 1)
    private void measureBlockUpdateTick(Block instance, World world, int x, int y, int z, Random rng) {
        if (PROFILE_ENABLED.get()) {
            long start = System.nanoTime();
            instance.updateTick(world, x, y, z, rng);
            long end = System.nanoTime();
            timingManager.addBlockTime(world.provider.dimensionId, new BlockPos(x, y, z), end - start);
        } else {
            instance.updateTick(world, x, y, z, rng);
        }
    }

    @Redirect(method = "func_147456_g",
              at = @At(value = "INVOKE",
                       target = "Lnet/minecraft/block/Block;updateTick(Lnet/minecraft/world/World;IIILjava/util/Random;)V"),
              require = 1)
    private void measureBlockUpdateTickRandomly(Block instance, World world, int x, int y, int z, Random rng) {
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