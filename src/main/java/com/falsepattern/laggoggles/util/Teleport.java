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

package com.falsepattern.laggoggles.util;

import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;

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
