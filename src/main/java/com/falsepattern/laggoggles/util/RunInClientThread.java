package com.falsepattern.laggoggles.util;

import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;

public class RunInClientThread {

    private final Runnable runnable;

    public RunInClientThread(Runnable runnable){
        this.runnable = runnable;
        FMLCommonHandler.instance().bus().register(this);
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent e){
        FMLCommonHandler.instance().bus().unregister(this);
        runnable.run();
    }
}
