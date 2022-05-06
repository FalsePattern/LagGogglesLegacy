package com.falsepattern.laggoggles.util;

import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;

public class RunInServerThread {

    private final Runnable runnable;

    public RunInServerThread(Runnable runnable){
        this.runnable = runnable;
        FMLCommonHandler.instance().bus().register(this);
    }

    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent e){
        FMLCommonHandler.instance().bus().unregister(this);
        runnable.run();
    }
}
