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
        FMLCommonHandler.instance().bus().register(this);
    }

    public long stop(){
        FMLCommonHandler.instance().bus().unregister(this);
        return frames;
    }
}
