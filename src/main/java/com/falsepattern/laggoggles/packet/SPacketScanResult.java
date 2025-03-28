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

import com.falsepattern.laggoggles.profiler.ScanType;
import com.falsepattern.laggoggles.util.Side;
import io.netty.buffer.ByteBuf;

import cpw.mods.fml.common.network.simpleimpl.IMessage;

import java.util.ArrayList;

public class SPacketScanResult implements IMessage{

    public SPacketScanResult(){}

    public ArrayList<ObjectData> DATA = new ArrayList<>();
    public boolean hasMore = false;
    public long startTime;
    public long endTime;
    public long totalTime;
    public long tickCount;
    public Side side;
    public ScanType type;
    public long totalFrames = 0;

    @Override
    public void fromBytes(ByteBuf buf) {
        tickCount = buf.readLong();
        hasMore = buf.readBoolean();
        endTime = buf.readLong();
        startTime = buf.readLong();
        totalTime = buf.readLong();
        totalFrames = buf.readLong();
        side = Side.values()[buf.readInt()];
        type = ScanType.values()[buf.readInt()];

        int size = buf.readInt();
        for(int i=0; i<size; i++){
            ObjectData data = new ObjectData();
            data.fromBytes(buf);
            DATA.add(data);
        }

    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeLong(tickCount);
        buf.writeBoolean(hasMore);
        buf.writeLong(endTime);
        buf.writeLong(startTime);
        buf.writeLong(totalTime);
        buf.writeLong(totalFrames);
        buf.writeInt(side.ordinal());
        buf.writeInt(type.ordinal());

        buf.writeInt(DATA.size());
        for(ObjectData data : DATA){
            data.toBytes(buf);
        }
    }

}