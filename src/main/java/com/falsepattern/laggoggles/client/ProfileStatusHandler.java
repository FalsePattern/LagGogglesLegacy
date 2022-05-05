package com.falsepattern.laggoggles.client;

import com.falsepattern.laggoggles.client.gui.GuiProfile;
import com.falsepattern.laggoggles.packet.SPacketProfileStatus;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class ProfileStatusHandler implements IMessageHandler<SPacketProfileStatus, IMessage> {

    @Override
    public IMessage onMessage(SPacketProfileStatus message, MessageContext ctx) {
        GuiProfile.PROFILING_PLAYER = message.issuedBy;
        if(message.isProfiling == true) {
            GuiProfile.PROFILE_END_TIME = System.currentTimeMillis() + (message.length * 1000);
        }else{
            GuiProfile.PROFILE_END_TIME = System.currentTimeMillis();
        }
        GuiProfile.update();
        return null;
    }
}
