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

import com.falsepattern.laggoggles.profiler.TimingManager;
import com.falsepattern.laggoggles.util.Coder;
import com.falsepattern.laggoggles.util.Graphical;
import com.falsepattern.lib.compat.BlockPos;
import io.netty.buffer.ByteBuf;

import cpw.mods.fml.common.network.simpleimpl.IMessage;

import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

public class ObjectData implements IMessage {

    private TreeMap<Entry, Object> data = new TreeMap<>();
    public Type type;

    ObjectData(){}

    public enum Type{
        ENTITY,
        TILE_ENTITY,
        BLOCK,
        EVENT_BUS_LISTENER,

        GUI_ENTITY,
        GUI_BLOCK
    }

    public enum Entry{
        WORLD_ID(Coder.INTEGER),

        ENTITY_NAME(Coder.STRING),
        ENTITY_UUID(Coder.UUID),
        ENTITY_CLASS_NAME(Coder.STRING),

        BLOCK_NAME(Coder.STRING),
        BLOCK_POS_X(Coder.INTEGER),
        BLOCK_POS_Y(Coder.INTEGER),
        BLOCK_POS_Z(Coder.INTEGER),
        BLOCK_CLASS_NAME(Coder.STRING),

        EVENT_BUS_LISTENER(Coder.STRING),
        EVENT_BUS_EVENT_CLASS_NAME(Coder.STRING),
        EVENT_BUS_THREAD_TYPE(Coder.INTEGER),

        NANOS(Coder.LONG);

        public final Coder coder;

        Entry(Coder d){
            this.coder = d;
        }
    }

    public ObjectData(int worldID, String name, String className, UUID id, long nanos, Type type_){
        type = type_;
        data.put(Entry.WORLD_ID, worldID);
        data.put(Entry.ENTITY_NAME, name);
        data.put(Entry.ENTITY_CLASS_NAME, className);
        data.put(Entry.ENTITY_UUID, id);
        data.put(Entry.NANOS, nanos);
    }

    public ObjectData(int worldID, String name, String className, BlockPos pos, long nanos, Type type_){
        type = type_;
        data.put(Entry.WORLD_ID, worldID);
        data.put(Entry.BLOCK_NAME, name);
        data.put(Entry.BLOCK_CLASS_NAME, className);
        data.put(Entry.BLOCK_POS_X, pos.getX());
        data.put(Entry.BLOCK_POS_Y, pos.getY());
        data.put(Entry.BLOCK_POS_Z, pos.getZ());
        data.put(Entry.NANOS, nanos);
    }

    public ObjectData(TimingManager.EventTimings eventTimings, long nanos){
        type = Type.EVENT_BUS_LISTENER;
        data.put(Entry.EVENT_BUS_EVENT_CLASS_NAME, Graphical.formatClassName(eventTimings.eventClass.toString()));
        data.put(Entry.EVENT_BUS_LISTENER, Graphical.formatClassName(eventTimings.listener));
        data.put(Entry.EVENT_BUS_THREAD_TYPE, eventTimings.threadType.ordinal());
        data.put(Entry.NANOS, nanos);
    }

    @SuppressWarnings("unchecked")
    public <T> T getValue(Entry entry){
        if(data.get(entry) == null){
            throw new IllegalArgumentException("Cant find the entry " + entry + " for " + type);
        }
        return (T) data.get(entry);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(type.ordinal());
        buf.writeInt(data.size());
        for(Map.Entry<Entry, Object> entry : data.entrySet()){
            buf.writeInt(entry.getKey().ordinal());
            entry.getKey().coder.write(entry.getValue(), buf);
        }
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        type = Type.values()[buf.readInt()];
        int size = buf.readInt();
        for(int i=0; i<size; i++){
            Entry entry = Entry.values()[buf.readInt()];
            data.put(entry, entry.coder.read(buf));
        }
    }

    @Override
    public String toString(){
        return type.toString() + ": " + data.toString();
    }
}
