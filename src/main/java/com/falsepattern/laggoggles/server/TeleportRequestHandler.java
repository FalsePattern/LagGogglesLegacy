package com.falsepattern.laggoggles.server;

import com.falsepattern.laggoggles.Main;
import com.falsepattern.laggoggles.packet.CPacketRequestEntityTeleport;
import com.falsepattern.laggoggles.packet.SPacketMessage;
import com.falsepattern.laggoggles.util.Perms;
import com.falsepattern.laggoggles.util.RunInServerThread;
import com.falsepattern.laggoggles.util.Teleport;
import com.falsepattern.lib.text.FormattedText;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.EnumChatFormatting;

import java.util.Arrays;
import java.util.List;

public class TeleportRequestHandler implements IMessageHandler<CPacketRequestEntityTeleport, IMessage> {

    @Override
    public IMessage onMessage(CPacketRequestEntityTeleport message, MessageContext ctx) {
        EntityPlayerMP player = ctx.getServerHandler().playerEntity;
        if(!Perms.hasPermission(player, Perms.Permission.FULL)){
            Main.LOGGER.info(player.getDisplayName() + " tried to teleport, but was denied to do so!");
            return new SPacketMessage("No permission");
        }
        new RunInServerThread(() -> {
            Entity e = Arrays.stream(FMLCommonHandler.instance().getMinecraftServerInstance().worldServers)
                             .flatMap((world) -> ((List<Entity>)world.getLoadedEntityList()).stream())
                             .filter((entity) -> entity.getPersistentID().equals(message.uuid))
                             .findFirst()
                             .orElse(null);
            if(e == null){
                FormattedText.parse(EnumChatFormatting.RED + "Woops! This entity no longer exists!").addChatMessage(player);
                return;
            }
            Teleport.teleportPlayer(player, e.dimension, e.posX, e.posY, e.posZ);
        });
        return null;
    }
}
