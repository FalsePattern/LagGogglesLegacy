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

package com.falsepattern.laggoggles.server;

import com.falsepattern.laggoggles.packet.CPacketRequestServerData;
import com.falsepattern.laggoggles.packet.SPacketServerData;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

import java.util.ArrayList;
import java.util.UUID;

public class RequestDataHandler implements IMessageHandler<CPacketRequestServerData, SPacketServerData>{

    public static final ArrayList<UUID> playersWithLagGoggles = new ArrayList<>();

    @SubscribeEvent
    public void onLogout(PlayerEvent.PlayerLoggedOutEvent e){
        playersWithLagGoggles.remove(e.player.getGameProfile().getId());
    }

    @Override
    public SPacketServerData onMessage(CPacketRequestServerData cPacketRequestServerData, MessageContext ctx){
        if(!playersWithLagGoggles.contains(ctx.getServerHandler().playerEntity.getGameProfile().getId())) {
            playersWithLagGoggles.add(ctx.getServerHandler().playerEntity.getGameProfile().getId());
        }
        return new SPacketServerData(ctx.getServerHandler().playerEntity);
    }
}
