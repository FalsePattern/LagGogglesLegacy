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

package com.falsepattern.laggoggles.util;

import com.falsepattern.laggoggles.profiler.TimingManager;

import net.minecraft.server.MinecraftServer;
import cpw.mods.fml.common.FMLCommonHandler;

public class ThreadChecker {

    //TODO evil black magic
    public static TimingManager.EventTimings.ThreadType getThreadType(){
        MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
        if(server == null){
            /* No server at all. Multiplayer... probably. */
            if(Thread.currentThread().getName().equals("Client thread")){
                return TimingManager.EventTimings.ThreadType.CLIENT;
            }
        }else{
            if (server.isDedicatedServer()) {
                /* Dedicated server */
                if (Thread.currentThread().getName().equals("Server thread")) {
                    return TimingManager.EventTimings.ThreadType.SERVER;
                }
            } else {
                /* Not a dedicated server, we have both the client and server classes. */
                if (Thread.currentThread().getName().equals("Server thread")) {
                    return TimingManager.EventTimings.ThreadType.SERVER;
                } else if (Thread.currentThread().getName().equals("Client thread")) {
                    return TimingManager.EventTimings.ThreadType.CLIENT;
                }
            }
        }
        return TimingManager.EventTimings.ThreadType.ASYNC;
    }
}
