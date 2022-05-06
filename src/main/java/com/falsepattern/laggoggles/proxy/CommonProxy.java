package com.falsepattern.laggoggles.proxy;

import com.falsepattern.laggoggles.Tags;
import com.falsepattern.laggoggles.client.MessagePacketHandler;
import com.falsepattern.laggoggles.client.ProfileStatusHandler;
import com.falsepattern.laggoggles.client.ScanResultHandler;
import com.falsepattern.laggoggles.client.ServerDataPacketHandler;
import com.falsepattern.laggoggles.command.LagGogglesCommand;
import com.falsepattern.laggoggles.packet.*;
import com.falsepattern.laggoggles.packet.*;
import com.falsepattern.laggoggles.profiler.ProfileResult;
import com.falsepattern.laggoggles.profiler.TickCounter;
import com.falsepattern.laggoggles.server.*;
import com.falsepattern.laggoggles.server.*;
import com.falsepattern.laggoggles.util.Perms;
import com.falsepattern.laggoggles.util.RunInServerThread;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;

import java.util.List;

public class CommonProxy {

    public static final SimpleNetworkWrapper NETWORK_WRAPPER = NetworkRegistry.INSTANCE.newSimpleChannel(Tags.MODID);

    private byte PACKET_ID = 0;

    public void preinit(FMLPreInitializationEvent e){

    }

    public void init(FMLInitializationEvent e){}

    public void postinit(FMLPostInitializationEvent e){
        NETWORK_WRAPPER.registerMessage(
                ScanResultHandler.class,
                SPacketScanResult.class, PACKET_ID++, Side.CLIENT);
        NETWORK_WRAPPER.registerMessage(
                ProfileStatusHandler.class,
                SPacketProfileStatus.class, PACKET_ID++, Side.CLIENT);
        NETWORK_WRAPPER.registerMessage(
                ServerDataPacketHandler.class,
                SPacketServerData.class, PACKET_ID++, Side.CLIENT);
        NETWORK_WRAPPER.registerMessage(
                RequestDataHandler.class,
                CPacketRequestServerData.class, PACKET_ID++, Side.SERVER);
        NETWORK_WRAPPER.registerMessage(
                MessagePacketHandler.class,
                SPacketMessage.class, PACKET_ID++, Side.CLIENT);
        NETWORK_WRAPPER.registerMessage(
                ScanRequestHandler.class,
                CPacketRequestScan.class, PACKET_ID++, Side.SERVER);
        NETWORK_WRAPPER.registerMessage(
                TeleportRequestHandler.class,
                CPacketRequestEntityTeleport.class, PACKET_ID++, Side.SERVER);
        NETWORK_WRAPPER.registerMessage(
                TeleportToTileEntityRequestHandler.class,
                CPacketRequestTileEntityTeleport.class, PACKET_ID++, Side.SERVER);
        NETWORK_WRAPPER.registerMessage(
                RequestResultHandler.class,
                CPacketRequestResult.class, PACKET_ID++, Side.SERVER);
    }

    public static void sendTo(IMessage msg, EntityPlayerMP player){
        NETWORK_WRAPPER.sendTo(msg, player);
    }

    public static void sendTo(ProfileResult result, EntityPlayerMP player){
        List<SPacketScanResult> packets = Perms.getResultFor(player, result).createPackets(player);
        new RunInServerThread(new Runnable() {
            @Override
            public void run() {
                for (SPacketScanResult result : packets){
                    sendTo(result, player);
                }
            }
        });
    }

    public void serverStartingEvent(FMLServerStartingEvent e){
        e.registerServerCommand(new LagGogglesCommand());
        MinecraftForge.EVENT_BUS.register(new TickCounter());
        MinecraftForge.EVENT_BUS.register(new RequestDataHandler());
    }
}