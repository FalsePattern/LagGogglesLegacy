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

import cpw.mods.fml.common.network.simpleimpl.IMessage;

public class CPacketRequestTileEntityTeleport implements IMessage{

    public int dim;
    public int x;
    public int y;
    public int z;

    public CPacketRequestTileEntityTeleport(){}
    public CPacketRequestTileEntityTeleport(ObjectData data){
        dim = data.getValue(ObjectData.Entry.WORLD_ID);
        x =   data.getValue(ObjectData.Entry.BLOCK_POS_X);
        y =   data.getValue(ObjectData.Entry.BLOCK_POS_Y);
        z =   data.getValue(ObjectData.Entry.BLOCK_POS_Z);
    }

    @Override
    public void fromBytes(ByteBuf buf){
        dim = buf.readInt();
        x = buf.readInt();
        y = buf.readInt();
        z = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(dim);
        buf.writeInt(x);
        buf.writeInt(y);
        buf.writeInt(z);
    }
}