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

package com.falsepattern.laggoggles.packet;

import com.falsepattern.laggoggles.server.ServerConfig;
import com.falsepattern.laggoggles.util.Perms;
import io.netty.buffer.ByteBuf;

import net.minecraft.entity.player.EntityPlayerMP;
import cpw.mods.fml.common.network.simpleimpl.IMessage;

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
