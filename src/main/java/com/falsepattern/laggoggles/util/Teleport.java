package com.falsepattern.laggoggles.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentStyle;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;
import cpw.mods.fml.common.FMLCommonHandler;

public class Teleport {

    public static void teleportPlayer(EntityPlayerMP player, int dim, double x, double y, double z){
        new RunInServerThread(new Runnable() {
            @Override
            public void run() {
                if(player.dimension != dim) {
                    teleportPlayerToDimension(player, dim, x, y, z);
                }else{
                    player.setPositionAndUpdate(x,y,z);
                }
                player.addChatMessage(new ChatComponentText("Teleported to: ").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GREEN))
                        .appendSibling(new ChatComponentText(" Dim: " + dim).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GRAY)))
                        .appendSibling(new ChatComponentText(", " + (int) x + ", " + (int) y + ", " + (int) z).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.WHITE))));
            }
        });
    }

    /* Shamelessly stolen from the SpongeCommon source, then ported it to forge. For more info:
     * https://github.com/SpongePowered/SpongeCommon/blob/292baf720df84345e02347d75085926b834abfea/src/main/java/org/spongepowered/common/entity/EntityUtil.java
     */
    protected static class LocalTeleporter extends Teleporter {
        protected final WorldServer worldServerInstance;

        public LocalTeleporter(WorldServer world) {
            super(world);
            this.worldServerInstance = world;
        }

        @Override
        public void placeInPortal(Entity entity, double d, double d1, double d2, float d3) {
            int posX = (int) Math.round(entity.posX);
            int posY = (int) Math.round(entity.posY);
            int posZ = (int) Math.round(entity.posZ);

            worldServerInstance.getBlock(posX, posY, posZ); // Used to generate chunk
            posY = worldServerInstance.getHeightValue(posX, posZ) + 1;
            entity.setPosition(entity.posX, posY, entity.posZ);
        }
    }
    private static void teleportPlayerToDimension(EntityPlayerMP playerIn, int suggestedDimensionId, double x, double y, double z) {
        WorldServer toWorld = FMLCommonHandler.instance().getMinecraftServerInstance().worldServerForDimension(suggestedDimensionId);
        playerIn.mcServer.getConfigurationManager().transferPlayerToDimension(playerIn, suggestedDimensionId, new LocalTeleporter(toWorld));
        playerIn.setPositionAndUpdate(x, y, z);
    }
}
