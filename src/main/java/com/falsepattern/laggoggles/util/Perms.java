/*
 * LagGoggles: Legacy
 *
 * Copyright (C) 2022-2025 FalsePattern
 * All Rights Reserved
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, only version 3 of the License.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package com.falsepattern.laggoggles.util;

import com.falsepattern.laggoggles.packet.ObjectData;
import com.falsepattern.laggoggles.profiler.ProfileResult;
import com.falsepattern.laggoggles.server.RequestDataHandler;
import com.falsepattern.laggoggles.server.ServerConfig;
import lombok.val;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;
import cpw.mods.fml.common.FMLCommonHandler;

import java.util.ArrayList;
import java.util.List;

public class Perms {

    public static final double MAX_RANGE_FOR_PLAYERS_HORIZONTAL_SQ = ServerConfig.NON_OPS_MAX_HORIZONTAL_RANGE * ServerConfig.NON_OPS_MAX_HORIZONTAL_RANGE;
    public static final double MAX_RANGE_FOR_PLAYERS_VERTICAL_SQ = ServerConfig.NON_OPS_MAX_VERTICAL_RANGE * ServerConfig.NON_OPS_MAX_HORIZONTAL_RANGE;

    public enum Permission{
        NONE,
        GET,
        START,
        FULL
    }

    public static Permission getPermission(EntityPlayerMP p){
        val profile = p.getGameProfile();
        val manager = MinecraftServer.getServer().getConfigurationManager();
        if (manager.func_152596_g(profile)) {
            return Permission.FULL;
        } else {
            return ServerConfig.NON_OP_PERMISSION_LEVEL;
        }
    }

    public static boolean hasPermission(EntityPlayerMP player, Permission permission){
        return getPermission(player).ordinal() >= permission.ordinal();
    }

    @SuppressWarnings("unchecked")
    public static ArrayList<EntityPlayerMP> getLagGogglesUsers(){
        ArrayList<EntityPlayerMP> list = new ArrayList<>();
        MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
        if(server == null){
            return list;
        }
        ((List<EntityPlayerMP>) MinecraftServer.getServer().getConfigurationManager().playerEntityList)
                .stream()
                .filter((ent) -> RequestDataHandler.playersWithLagGoggles.contains(ent.getPersistentID()))
                .forEach(list::add);
        return list;
    }

    public static ProfileResult getResultFor(EntityPlayerMP player, ProfileResult result){
        Permission permission = getPermission(player);
        if(permission == Permission.NONE){
            return ProfileResult.EMPTY_RESULT;
        }
        if(permission == Permission.FULL){
            return result;
        }
        ProfileResult trimmedResult = result.copyStatsOnly();
        for(ObjectData data : result.getData()){
            if(isInRange(data, player)){
                trimmedResult.addData(data);
            }
        }
        return trimmedResult;
    }

    @SuppressWarnings("unchecked")
    public static boolean isInRange(ObjectData data, EntityPlayerMP player){
        if(data.type == ObjectData.Type.EVENT_BUS_LISTENER){
            return ServerConfig.ALLOW_NON_OPS_TO_SEE_EVENT_SUBSCRIBERS;
        }
        if(data.<Integer>getValue(ObjectData.Entry.WORLD_ID) != player.dimension){
            return false;
        }
        switch(data.type){
            case ENTITY:
                WorldServer world = DimensionManager.getWorld(data.getValue(ObjectData.Entry.WORLD_ID));
                Entity e;
                val uuid = data.getValue(ObjectData.Entry.ENTITY_UUID);
                if(world != null && (e = ((List<Entity>)world.loadedEntityList).stream().filter((ent) -> ent.getPersistentID().equals(uuid))
                                                                                    .findFirst()
                                                                                    .orElse(null)) != null){
                    return checkRange(player, e.posX, e.posY, e.posZ);
                }
                return false;
            case BLOCK:
            case TILE_ENTITY:
                return checkRange(player, data.getValue(ObjectData.Entry.BLOCK_POS_X), data.getValue(ObjectData.Entry.BLOCK_POS_Y), data.getValue(ObjectData.Entry.BLOCK_POS_Z));
            default:
                return false;
        }
    }

    public static boolean checkRange(EntityPlayerMP player, Integer x, Integer y, Integer z){
        return checkRange(player, x.doubleValue(), y.doubleValue(), z.doubleValue());
    }

    public static boolean checkRange(EntityPlayerMP player, double x, double y, double z){
        double xD = x - player.posX;
        double zD = z - player.posZ;

        /* Check horizontal range */
        if(xD*xD + zD*zD > MAX_RANGE_FOR_PLAYERS_HORIZONTAL_SQ){
            return false;
        }

        /* If it's within range, we check if the Y level is whitelisted */
        if(y > ServerConfig.NON_OPS_WHITELIST_HEIGHT_ABOVE){
            return true;
        }

        /* If it's underground, we restrict the results, so you can't abuse it to find spawners, chests, minecarts.. etc. */
        double yD = y - player.posY;
        return !(yD * yD > MAX_RANGE_FOR_PLAYERS_VERTICAL_SQ);
    }
}
