package com.falsepattern.laggoggles.client;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;

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
