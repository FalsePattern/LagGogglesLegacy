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

package com.falsepattern.laggoggles.packet;

import io.netty.buffer.ByteBuf;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;

public class SPacketProfileStatus implements IMessage {

    public boolean isProfiling = true;
    public String issuedBy = "Unknown";
    public int length = 0;

    public SPacketProfileStatus(){}
    public SPacketProfileStatus(boolean isProfiling, int length, String issuedBy){
        this.isProfiling = isProfiling;
        this.length = length;
        this.issuedBy = issuedBy;
    }

    @Override
    public void fromBytes(ByteBuf buf){
        isProfiling = buf.readBoolean();
        length = buf.readInt();
        issuedBy = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeBoolean(isProfiling);
        buf.writeInt(length);
        ByteBufUtils.writeUTF8String(buf, issuedBy);
    }
}
