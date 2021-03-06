package com.falsepattern.laggoggles.packet;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import io.netty.buffer.ByteBuf;

public class SPacketProfileStatus implements IMessage {

    public boolean isProfiling = true;
    public String issuedBy = "Unknown";
    public int length = 0;

    public SPacketProfileStatus(){}
    public SPacketProfileStatus(boolean isProfiling, int length, String issuedBy){
        this.isProfiling = isProfiling;
        this.length = length;
        this.issuedBy = issuedBy;
    }

    @Override
    public void fromBytes(ByteBuf buf){
        isProfiling = buf.readBoolean();
        length = buf.readInt();
        issuedBy = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeBoolean(isProfiling);
        buf.writeInt(length);
        ByteBufUtils.writeUTF8String(buf, issuedBy);
    }
}
