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

package com.falsepattern.laggoggles.util;

import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import cpw.mods.fml.common.FMLCommonHandler;

public enum Side {
    DEDICATED_SERVER,
    CLIENT_WITHOUT_SERVER,
    CLIENT_WITH_SERVER,
    UNKNOWN;

    public static Side getSide(){
        MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
        if(server == null){
            return CLIENT_WITHOUT_SERVER;
        }else if(server.isDedicatedServer()){
            return DEDICATED_SERVER;
        }else if(Minecraft.getMinecraft().isSingleplayer()){
            return CLIENT_WITH_SERVER;
        }
        return UNKNOWN;
    }

    public boolean isServer(){
        return this == DEDICATED_SERVER;
    }

    public boolean isPlayingOnServer(){
        return this == CLIENT_WITHOUT_SERVER;
    }

    public boolean isClient(){
        switch (this){
            case CLIENT_WITH_SERVER:
            case CLIENT_WITHOUT_SERVER:
                return true;
            default:
                return false;
        }
    }

    public boolean isSinglePlayer(){
        switch (this){
            case DEDICATED_SERVER:
            case CLIENT_WITHOUT_SERVER:
            case UNKNOWN:
                return false;
            case CLIENT_WITH_SERVER:
                return true;
        }
        throw new RuntimeException("Someone forgot to update this piece of code!");
    }
}
