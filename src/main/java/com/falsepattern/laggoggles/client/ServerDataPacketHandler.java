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

package com.falsepattern.laggoggles.client;

import com.falsepattern.laggoggles.client.gui.GuiProfile;
import com.falsepattern.laggoggles.packet.SPacketServerData;
import com.falsepattern.laggoggles.util.Perms;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class ServerDataPacketHandler implements IMessageHandler<SPacketServerData, IMessage> {

    public static Perms.Permission PERMISSION = Perms.Permission.NONE;
    public static boolean SERVER_HAS_RESULT = false;
    public static int MAX_SECONDS = Integer.MAX_VALUE;
    public static boolean RECEIVED_RESULT = false;
    public static boolean NON_OPS_CAN_SEE_EVENT_SUBSCRIBERS = false;

    @Override
    public IMessage onMessage(SPacketServerData msg, MessageContext messageContext) {
        SERVER_HAS_RESULT = msg.hasResult;
        PERMISSION = msg.permission;
        MAX_SECONDS = PERMISSION == Perms.Permission.FULL ? Integer.MAX_VALUE : msg.maxProfileTime;
        RECEIVED_RESULT = true;
        NON_OPS_CAN_SEE_EVENT_SUBSCRIBERS = msg.canSeeEventSubScribers;
        GuiProfile.update();
        return null;
    }
}
