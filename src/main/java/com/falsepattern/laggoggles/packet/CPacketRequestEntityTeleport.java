package com.falsepattern.laggoggles.packet;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import io.netty.buffer.ByteBuf;

import java.util.UUID;

public class CPacketRequestEntityTeleport implements IMessage {

    public UUID uuid;
    public CPacketRequestEntityTeleport(){}
    public CPacketRequestEntityTeleport(UUID uuid){
        this.uuid = uuid;
    }


    @Override
    public void fromBytes(ByteBuf buf){
        uuid = new UUID(buf.readLong(), buf.readLong());
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeLong(uuid.getMostSignificantBits());
        buf.writeLong(uuid.getLeastSignificantBits());
    }
}
