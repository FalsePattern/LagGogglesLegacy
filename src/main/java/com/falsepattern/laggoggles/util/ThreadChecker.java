package com.falsepattern.laggoggles.util;

import com.falsepattern.laggoggles.profiler.TimingManager;
import cpw.mods.fml.client.FMLClientHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import cpw.mods.fml.common.FMLCommonHandler;

public class ThreadChecker {

    //TODO evil black magic
    public static TimingManager.EventTimings.ThreadType getThreadType(){
        MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
        if(server == null){
            /* No server at all. Multiplayer... probably. */
            if(Thread.currentThread().getName().equals("Client thread")){
                return TimingManager.EventTimings.ThreadType.CLIENT;
            }
        }else{
            if (server.isDedicatedServer()) {
                /* Dedicated server */
                if (Thread.currentThread().getName().equals("Server thread")) {
                    return TimingManager.EventTimings.ThreadType.SERVER;
                }
            } else {
                /* Not a dedicated server, we have both the client and server classes. */
                if (Thread.currentThread().getName().equals("Server thread")) {
                    return TimingManager.EventTimings.ThreadType.SERVER;
                } else if (Thread.currentThread().getName().equals("Client thread")) {
                    return TimingManager.EventTimings.ThreadType.CLIENT;
                }
            }
        }
        return TimingManager.EventTimings.ThreadType.ASYNC;
    }
}
