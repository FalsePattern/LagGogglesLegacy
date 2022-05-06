package com.falsepattern.laggoggles.server;

import com.falsepattern.laggoggles.packet.CPacketRequestServerData;
import com.falsepattern.laggoggles.packet.SPacketServerData;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

import java.util.ArrayList;
import java.util.UUID;

public class RequestDataHandler implements IMessageHandler<CPacketRequestServerData, SPacketServerData>{

    public static final ArrayList<UUID> playersWithLagGoggles = new ArrayList<>();

    @SubscribeEvent
    public void onLogout(PlayerEvent.PlayerLoggedOutEvent e){
        playersWithLagGoggles.remove(e.player.getGameProfile().getId());
    }

    @Override
    public SPacketServerData onMessage(CPacketRequestServerData cPacketRequestServerData, MessageContext ctx){
        if(!playersWithLagGoggles.contains(ctx.getServerHandler().playerEntity.getGameProfile().getId())) {
            playersWithLagGoggles.add(ctx.getServerHandler().playerEntity.getGameProfile().getId());
        }
        return new SPacketServerData(ctx.getServerHandler().playerEntity);
    }
}
