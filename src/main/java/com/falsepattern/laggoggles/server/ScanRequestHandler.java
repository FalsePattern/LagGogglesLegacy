package com.falsepattern.laggoggles.server;

import com.falsepattern.laggoggles.Main;
import com.falsepattern.laggoggles.api.Profiler;
import com.falsepattern.laggoggles.packet.CPacketRequestScan;
import com.falsepattern.laggoggles.packet.SPacketMessage;
import com.falsepattern.laggoggles.packet.SPacketProfileStatus;
import com.falsepattern.laggoggles.packet.SPacketServerData;
import com.falsepattern.laggoggles.profiler.ProfileManager;
import com.falsepattern.laggoggles.profiler.ScanType;
import com.falsepattern.laggoggles.proxy.CommonProxy;
import com.falsepattern.laggoggles.util.Perms;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import net.minecraft.entity.player.EntityPlayerMP;

import java.util.HashMap;
import java.util.UUID;

public class ScanRequestHandler implements IMessageHandler<CPacketRequestScan, IMessage> {

    private static HashMap<UUID, Long> COOLDOWN = new HashMap<>();

    @Override
    public IMessage onMessage(CPacketRequestScan request, MessageContext ctx) {
        final EntityPlayerMP requestee = ctx.getServerHandler().playerEntity;
        Perms.Permission requesteePerms = Perms.getPermission(requestee);

        if(requesteePerms.ordinal() < Perms.Permission.START.ordinal()){
            Main.LOGGER.info(requestee.getDisplayName() + " Tried to start the profiler, but was denied to do so!");
            return new SPacketMessage("No permission");
        }

        if(requesteePerms != Perms.Permission.FULL && request.length > ServerConfig.NON_OPS_MAX_PROFILE_TIME){
            return new SPacketMessage("Profile time is too long! You can profile up to " + ServerConfig.NON_OPS_MAX_PROFILE_TIME + " seconds.");
        }

        if(ProfileManager.PROFILE_ENABLED.get() == true){
            return new SPacketMessage("Profiler is already running");
        }

        /*
        long secondsLeft = (COOLDOWN.getOrDefault(requestee.getGameProfile().getId(),0L) - System.currentTimeMillis())/1000;
        if(secondsLeft > 0 && requesteePerms != Perms.Permission.FULL){
            return new SPacketMessage("Please wait " + secondsLeft + " seconds.");
        }
        COOLDOWN.put(requestee.getGameProfile().getId(), System.currentTimeMillis() + (1000 * NON_OPS_PROFILE_COOL_DOWN_SECONDS));
*/

        long secondsLeft = secondsLeft(requestee.getGameProfile().getId());
        if(secondsLeft > 0 && requesteePerms != Perms.Permission.FULL){
            return new SPacketMessage("Please wait " + secondsLeft + " seconds.");
        }

        /* Start profiler */
        new Thread(new Runnable() {
            @Override
            public void run() {
                Profiler.runProfiler(request.length, ScanType.WORLD, requestee);

                /* Send status to users */
                SPacketProfileStatus status2 = new SPacketProfileStatus(false, request.length, requestee.getDisplayName());
                for(EntityPlayerMP user : Perms.getLagGogglesUsers()) {
                    CommonProxy.sendTo(status2, user);
                }

                CommonProxy.sendTo(ProfileManager.LAST_PROFILE_RESULT.get(), requestee);
                for(EntityPlayerMP user : Perms.getLagGogglesUsers()) {
                    CommonProxy.sendTo(new SPacketServerData(user), user);
                }
            }
        }).start();
        return null;
    }


    public static long secondsLeft(UUID uuid){
        long lastRequest = COOLDOWN.getOrDefault(uuid, 0L);
        long secondsLeft = ServerConfig.NON_OPS_PROFILE_COOL_DOWN_SECONDS - ((System.currentTimeMillis() - lastRequest)/1000);
        if(secondsLeft <= 0){
            COOLDOWN.put(uuid, System.currentTimeMillis());
        }
        return secondsLeft;
    }
}