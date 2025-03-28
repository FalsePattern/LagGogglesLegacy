/*
 * Lag Goggles: Legacy
 *
 * Copyright (C) 2022 FalsePattern
 * All Rights Reserved
 *
 * The above copyright notice, this permission notice and the word "SNEED"
 * shall be included in all copies or substantial portions of the Software.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.falsepattern.laggoggles.proxy;

import com.falsepattern.laggoggles.Tags;
import com.falsepattern.laggoggles.client.MessagePacketHandler;
import com.falsepattern.laggoggles.client.ProfileStatusHandler;
import com.falsepattern.laggoggles.client.ScanResultHandler;
import com.falsepattern.laggoggles.client.ServerDataPacketHandler;
import com.falsepattern.laggoggles.command.LagGogglesCommand;
import com.falsepattern.laggoggles.packet.CPacketRequestEntityTeleport;
import com.falsepattern.laggoggles.packet.CPacketRequestResult;
import com.falsepattern.laggoggles.packet.CPacketRequestScan;
import com.falsepattern.laggoggles.packet.CPacketRequestServerData;
import com.falsepattern.laggoggles.packet.CPacketRequestTileEntityTeleport;
import com.falsepattern.laggoggles.packet.SPacketMessage;
import com.falsepattern.laggoggles.packet.SPacketProfileStatus;
import com.falsepattern.laggoggles.packet.SPacketScanResult;
import com.falsepattern.laggoggles.packet.SPacketServerData;
import com.falsepattern.laggoggles.profiler.ProfileResult;
import com.falsepattern.laggoggles.profiler.TickCounter;
import com.falsepattern.laggoggles.server.RequestDataHandler;
import com.falsepattern.laggoggles.server.RequestResultHandler;
import com.falsepattern.laggoggles.server.ScanRequestHandler;
import com.falsepattern.laggoggles.server.TeleportRequestHandler;
import com.falsepattern.laggoggles.server.TeleportToTileEntityRequestHandler;
import com.falsepattern.laggoggles.util.Perms;
import com.falsepattern.laggoggles.util.RunInServerThread;

import net.minecraft.entity.player.EntityPlayerMP;
import cpw.mods.fml.common.FMLCommonHandler;
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

    public static final SimpleNetworkWrapper NETWORK_WRAPPER = NetworkRegistry.INSTANCE.newSimpleChannel(Tags.MOD_ID);

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
        FMLCommonHandler.instance().bus().register(new TickCounter());
        FMLCommonHandler.instance().bus().register(new RequestDataHandler());
    }
}