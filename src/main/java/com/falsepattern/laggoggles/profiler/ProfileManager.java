package com.falsepattern.laggoggles.profiler;

import com.falsepattern.laggoggles.Tags;
import com.falsepattern.laggoggles.proxy.CommonProxy;
import com.falsepattern.laggoggles.Main;
import com.falsepattern.laggoggles.api.event.LagGogglesEvent;
import com.falsepattern.laggoggles.client.FPSCounter;
import com.falsepattern.laggoggles.packet.ObjectData;
import com.falsepattern.laggoggles.packet.SPacketProfileStatus;
import com.falsepattern.laggoggles.util.Perms;
import com.falsepattern.laggoggles.util.RunInClientThread;
import com.falsepattern.laggoggles.util.RunInServerThread;
import com.falsepattern.laggoggles.util.Side;
import com.falsepattern.laggoggles.util.*;
import com.falsepattern.lib.compat.ChunkPos;
import com.falsepattern.lib.text.FormattedText;
import lombok.val;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import com.falsepattern.lib.compat.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

public class ProfileManager {

    public static TimingManager timingManager = new TimingManager();
    public static final AtomicBoolean PROFILE_ENABLED = new AtomicBoolean(false);
    public static final AtomicReference<ProfileResult> LAST_PROFILE_RESULT = new AtomicReference<>();
    private static final Object LOCK = new Object();
    private static final FPSCounter FPS_COUNTER = new FPSCounter();

    public static ProfileResult runProfiler(int seconds, ScanType type, ICommandSender issuer) throws IllegalStateException{
        try {
            if(PROFILE_ENABLED.get()){
                throw new IllegalStateException("Can't start profiler when it's already running!");
            }

            /* Send status to users */
            SPacketProfileStatus status = new SPacketProfileStatus(true, seconds, issuer.getCommandSenderName());

            new RunInServerThread(new Runnable() {
                @Override
                public void run() {
                    for(EntityPlayerMP user : Perms.getLagGogglesUsers()) {
                        CommonProxy.sendTo(status, user);
                    }
                }
            });
            FormattedText.parse(EnumChatFormatting.GRAY + Tags.MODNAME + EnumChatFormatting.WHITE + ": Profiler started for " + seconds + " seconds.").addChatMessage(issuer);
            Main.LOGGER.info(Tags.MODNAME + " profiler started by " + issuer.getCommandSenderName() + " (" + seconds + " seconds)");

            long start = System.nanoTime();
            TickCounter.ticks.set(0L);
            timingManager = new TimingManager();
            if(Side.getSide().isClient()) {
                FPS_COUNTER.start();
            }
            PROFILE_ENABLED.set(true);
            Thread.sleep(seconds * 1000);
            PROFILE_ENABLED.set(false);
            long frames = FPS_COUNTER.stop();

            Runnable task = new Runnable() {
                @Override
                public void run() {
                    try{
                        ArrayList<Entity> ignoredEntities = new ArrayList<>();
                        ArrayList<TileEntity> ignoredTileEntities = new ArrayList<>();
                        ArrayList<BlockPos> ignoredBlocks = new ArrayList<>();

                        Main.LOGGER.info("Processing results synchronously...");
                        ProfileResult result = new ProfileResult(start, System.nanoTime(), TickCounter.ticks.get(), Side.getSide(), type);
                        if(Side.getSide().isClient()) {
                            result.setFrames(frames);
                        }

                        for(Map.Entry<Integer, TimingManager.WorldData> entry : timingManager.getTimings().entrySet()){
                            int worldID = entry.getKey();
                            WorldServer world = DimensionManager.getWorld(worldID);
                            if(world == null){
                                continue;
                            }
                            for(Map.Entry<UUID, Long> entityTimes : entry.getValue().getEntityTimes().entrySet()){
                                Entity e = ((List<Entity>)world.getLoadedEntityList()).stream().filter((ent) -> ent.getUniqueID().equals(entityTimes.getKey())).findFirst().orElse(null);
                                if(e == null){
                                    continue;
                                }
                                try {
                                    result.addData(new ObjectData(
                                            worldID,
                                            e.getCommandSenderName(),
                                            Graphical.formatClassName(e.getClass().toString()),
                                            e.getPersistentID(),
                                            entityTimes.getValue(),
                                            ObjectData.Type.ENTITY)
                                    );
                                }catch (Throwable t){
                                    ignoredEntities.add(e);
                                }
                            }
                            for(Map.Entry<BlockPos, Long> tileEntityTimes : entry.getValue().getBlockTimes().entrySet()){
                                val pos = tileEntityTimes.getKey();
                                val cPos = new ChunkPos(pos);
                                if(world.isRemote ? world.getChunkProvider().provideChunk(cPos.x, cPos.z) == null : !world.getChunkProvider().chunkExists(cPos.x, cPos.z)) {
                                    continue;
                                }
                                TileEntity e = world.getTileEntity(pos.getX(), pos.getY(), pos.getZ());
                                if(e != null) {
                                    try {
                                        String name = e.getClass().getSimpleName();
                                        result.addData(new ObjectData(
                                                worldID,
                                                name,
                                                Graphical.formatClassName(e.getClass().toString()),
                                                new BlockPos(e.xCoord, e.yCoord, e.zCoord),
                                                tileEntityTimes.getValue(),
                                                ObjectData.Type.TILE_ENTITY)
                                        );
                                    }catch (Throwable t){
                                        ignoredTileEntities.add(e);
                                    }
                                }else{
                                    /* The block is not a tile entity, get the actual block. */
                                    try {
                                        val block = world.getBlock(pos.getX(), pos.getY(), pos.getZ());
                                        String name = block.getLocalizedName();
                                        result.addData(new ObjectData(
                                                worldID,
                                                name,
                                                Graphical.formatClassName(block.getClass().toString()),
                                                tileEntityTimes.getKey(),
                                                tileEntityTimes.getValue(),
                                                ObjectData.Type.BLOCK));
                                    }catch (Throwable t){
                                        ignoredBlocks.add(tileEntityTimes.getKey());
                                    }
                                }
                            }
                        }
                        for(Map.Entry<TimingManager.EventTimings, AtomicLong> entry : timingManager.getEventTimings().entrySet()){
                            result.addData(new ObjectData(entry.getKey(), entry.getValue().get()));
                        }
                        if(result.getSide().isClient()) {
                            insertGuiData(result, timingManager);
                        }
                        result.lock();
                        LAST_PROFILE_RESULT.set(result);
                        synchronized (LOCK){
                            LOCK.notifyAll();
                        }
                        if(ignoredBlocks.size() + ignoredEntities.size() + ignoredTileEntities.size() > 0) {
                            Main.LOGGER.info("Ignored some tracked elements:");
                            Main.LOGGER.info("Entities: " + ignoredEntities);
                            Main.LOGGER.info("Tile entities: " + ignoredTileEntities);
                            Main.LOGGER.info("Blocks in locations: " + ignoredBlocks);
                        }
                    } catch (Throwable e) {
                        Main.LOGGER.error("Woa! Something went wrong while processing results! Please contact Terminator_NL and submit the following error in an issue at github!");
                        e.printStackTrace();
                    }
                }
            };
            Side side = Side.getSide();
            if(side.isServer()){
                new RunInServerThread(task);
            }else if(side.isClient()){
                new RunInClientThread(task);
            }else{
                Main.LOGGER.error("LagGoggles did something amazing. I have no clue how this works, but here's a stacktrace, please submit an issue at github with the stacktrace below!");
                Thread.dumpStack();
            }
            synchronized (LOCK) {
                LOCK.wait();
            }
            FMLCommonHandler.instance().bus().post(new LagGogglesEvent.LocalResult(LAST_PROFILE_RESULT.get()));
            Main.LOGGER.info("Profiling complete.");
            FormattedText.parse(EnumChatFormatting.GRAY + Tags.MODNAME + EnumChatFormatting.WHITE + ": Profiling complete.").addChatMessage(issuer);
            return LAST_PROFILE_RESULT.get();
        } catch (Throwable e) {
            Main.LOGGER.error("Woa! Something went wrong while processing results! Please contact Terminator_NL and submit the following error in an issue at github!");
            e.printStackTrace();
            return null;
        }
    }

    public static void insertGuiData(ProfileResult result, TimingManager timings) {
        TreeMap<UUID, Long> entityTimes = timings.getGuiEntityTimings();
        for (Entity e : (List<Entity>)Minecraft.getMinecraft().theWorld.loadedEntityList) {
            Long time = entityTimes.get(e.getUniqueID());
            if (time == null) {
                continue;
            }
            result.addData(new ObjectData(
                    e.worldObj.provider.dimensionId,
                    e.getClass().getSimpleName(),
                    Graphical.formatClassName(e.getClass().toString()),
                    e.getPersistentID(),
                    time,
                    ObjectData.Type.GUI_ENTITY)
            );
        }

        TreeMap<BlockPos, Long> blockTimes = timings.getGuiBlockTimings();
        WorldClient world = Minecraft.getMinecraft().theWorld;
        for (Map.Entry<BlockPos, Long> e: blockTimes.entrySet()) {
            val pos = e.getKey();
            Long time = e.getValue();
            TileEntity entity = world.getTileEntity(pos.getX(), pos.getY(), pos.getZ());
            if(entity != null) {
                String name = entity.getClass().getSimpleName();
                result.addData(new ObjectData(
                        entity.getWorldObj().provider.dimensionId,
                        name,
                        Graphical.formatClassName(entity.getClass().toString()),
                        new BlockPos(entity.xCoord, entity.yCoord, entity.zCoord),
                        time,
                        ObjectData.Type.GUI_BLOCK)
                );
            }else{
                /* The block is not a tile entity, get the actual block. */
                val block = world.getBlock(pos.getX(), pos.getY(), pos.getZ());
                result.addData(new ObjectData(
                        world.provider.dimensionId,
                        block.getLocalizedName(),
                        Graphical.formatClassName(block.getClass().toString()),
                        e.getKey(),
                        time,
                        ObjectData.Type.GUI_BLOCK));
            }
        }
    }

}
