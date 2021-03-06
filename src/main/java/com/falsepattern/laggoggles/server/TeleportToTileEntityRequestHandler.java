package com.falsepattern.laggoggles.server;

import com.falsepattern.laggoggles.Main;
import com.falsepattern.laggoggles.packet.CPacketRequestTileEntityTeleport;
import com.falsepattern.laggoggles.packet.SPacketMessage;
import com.falsepattern.laggoggles.util.Perms;
import com.falsepattern.laggoggles.util.Teleport;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import net.minecraft.entity.player.EntityPlayerMP;

public class TeleportToTileEntityRequestHandler implements IMessageHandler<CPacketRequestTileEntityTeleport, IMessage> {

    @Override
    public IMessage onMessage(final CPacketRequestTileEntityTeleport message, MessageContext ctx) {
        EntityPlayerMP player = ctx.getServerHandler().playerEntity;
        if(Perms.hasPermission(player, Perms.Permission.FULL) == false){
            Main.LOGGER.info(player.getDisplayName() + " tried to teleport, but was denied to do so!");
            return new SPacketMessage("No permission");
        }
        Teleport.teleportPlayer(player, message.dim, message.x, message.y, message.z);
        return null;
    }
}