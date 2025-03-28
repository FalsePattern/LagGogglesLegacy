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

package com.falsepattern.laggoggles.client;

import com.falsepattern.laggoggles.Main;
import com.falsepattern.laggoggles.client.gui.GuiProfile;
import com.falsepattern.laggoggles.packet.CPacketRequestServerData;
import com.falsepattern.laggoggles.packet.SPacketMessage;
import com.falsepattern.lib.text.FormattedText;
import lombok.val;

import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class MessagePacketHandler implements IMessageHandler<SPacketMessage, CPacketRequestServerData> {

    @Override
    public CPacketRequestServerData onMessage(SPacketMessage msg, MessageContext messageContext) {
        GuiProfile.MESSAGE = msg;
        GuiProfile.MESSAGE_END_TIME = System.currentTimeMillis() + (msg.seconds * 1000);
        GuiProfile.update();
        Main.LOGGER.info("message received from server: " + msg.message);
        val txt = FormattedText.parse(EnumChatFormatting.RED + msg.message).toChatText();
        val gui = Minecraft.getMinecraft().ingameGUI.getChatGUI();
        for (val line: txt) {
            gui.printChatMessage(line);
        }
        return new CPacketRequestServerData();
    }
}
