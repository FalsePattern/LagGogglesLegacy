package com.falsepattern.laggoggles.client;

import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class FPSCounter {

    private long frames;

    @SubscribeEvent
    public void onDraw(RenderWorldLastEvent event) {
        frames++;
    }

    public void start(){
        frames = 0;
        MinecraftForge.EVENT_BUS.register(this);
    }

    public long stop(){
        MinecraftForge.EVENT_BUS.unregister(this);
        return frames;
    }
}