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

import com.falsepattern.laggoggles.api.event.LagGogglesEvent;
import com.falsepattern.laggoggles.client.gui.GuiProfile;
import com.falsepattern.laggoggles.client.gui.LagOverlayGui;
import com.falsepattern.laggoggles.packet.ObjectData;
import com.falsepattern.laggoggles.packet.SPacketScanResult;
import com.falsepattern.laggoggles.profiler.ProfileResult;
import com.falsepattern.laggoggles.util.Calculations;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

import java.util.ArrayList;

import static com.falsepattern.laggoggles.profiler.ProfileManager.LAST_PROFILE_RESULT;

public class ScanResultHandler implements IMessageHandler<SPacketScanResult, IMessage> {

    private ArrayList<ObjectData> builder = new ArrayList<>();

    @Override
    public IMessage onMessage(SPacketScanResult message, MessageContext ctx){
        final long tickCount = message.tickCount > 0 ? message.tickCount : 1;
        for(ObjectData objectData : message.DATA){
            if(Calculations.muPerTickCustomTotals(objectData.getValue(ObjectData.Entry.NANOS), tickCount) >= ClientConfig.MINIMUM_AMOUNT_OF_MICROSECONDS_THRESHOLD){
                builder.add(objectData);
            }
        }
        if(message.hasMore == false){
            ProfileResult result = new ProfileResult(message.startTime, message.endTime, tickCount, message.side, message.type);
            result.addAll(builder);
            result.lock();
            builder = new ArrayList<>();
            LAST_PROFILE_RESULT.set(result);
            LagOverlayGui.create(result);
            LagOverlayGui.show();
            GuiProfile.update();
            FMLCommonHandler.instance().bus().post(new LagGogglesEvent.ReceivedFromServer(result));
        }
        return null;
    }

}
