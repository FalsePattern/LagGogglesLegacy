package com.falsepattern.laggoggles.packet;

import com.falsepattern.laggoggles.server.ServerConfig;
import com.falsepattern.laggoggles.util.Perms;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;

public class SPacketServerData implements IMessage {

    public boolean hasResult = false;
    public Perms.Permission permission;
    public int maxProfileTime = ServerConfig.NON_OPS_MAX_PROFILE_TIME;
    public boolean canSeeEventSubScribers = ServerConfig.ALLOW_NON_OPS_TO_SEE_EVENT_SUBSCRIBERS;

    public SPacketServerData(){}
    public SPacketServerData(EntityPlayerMP player){
        hasResult = true;
        permission = Perms.getPermission(player);
    }

    @Override
    public void fromBytes(ByteBuf byteBuf) {
        hasResult = byteBuf.readBoolean();
        permission = Perms.Permission.values()[byteBuf.readInt()];
        maxProfileTime = byteBuf.readInt();
        canSeeEventSubScribers = byteBuf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf byteBuf) {
        byteBuf.writeBoolean(hasResult);
        byteBuf.writeInt(permission.ordinal());
        byteBuf.writeInt(maxProfileTime);
        byteBuf.writeBoolean(canSeeEventSubScribers);
    }
}
